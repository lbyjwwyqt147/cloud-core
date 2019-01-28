package pers.liujunyi.cloud.core.datasource;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/***
 * 文件名称: DataSourceAspect.java
 * 文件描述: AOP拦截特定的注解去动态的切换数据源
 * 公 司:
 * 内容摘要:
 * 其他说明:
 * 完成日期:2019年01月17日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
@Aspect
@Component
@Order(-1) //保证该AOP在@Transactional之前执行
@Slf4j
public class DataSourceAspect {

    /**
     * 切换放在dao接口的方法上，所以这里要配置AOP切面的切入点
     * 增加api 控制器切入点，是因为动态数据源切换需要在事务开启前执行，故需要在service前切换
     *
     * @within 在类上设置
     * @annotation 在方法上进行设置
     */
    @Pointcut("@within(TargetDataSource) || @annotation(TargetDataSource)")
    public void dataSourcePointCut() {
    }

    /**
     * @param joinPoint
     */
    @Before("dataSourcePointCut()")
    public void doBefore(JoinPoint joinPoint) {
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        //获取方法上的注解
        TargetDataSource annotationClass = method.getAnnotation(TargetDataSource.class);
        if (annotationClass == null) {
            // 获取类上面的注解
            annotationClass = joinPoint.getTarget().getClass().getAnnotation(TargetDataSource.class);
            if (annotationClass == null) {
                return;
            }
        }
        //获取注解上的数据源的值的信息
        String dataSourceKey = annotationClass.dataSource();
        String dataSourceMsg = null;
        if (dataSourceKey != null && dataSourceKey.trim().equals(DataSourceType.SLAVE)) {
            //　设置从库
            DataSourceContextHolder.read();
            dataSourceMsg = DataSourceType.SLAVE;
        } else {
            //　设置 主库
            DataSourceContextHolder.write();
            dataSourceMsg = DataSourceType.MASTER;
        }
        log.info("AOP动态切换数据源，className:" + joinPoint.getTarget().getClass().getName() + "." + method.getName() + ";使用数据源:" + dataSourceMsg);
    }

    /**
     * 执行完切面后，将线程共享中的数据源名称清空
     *
     * @param point
     */
    @After("dataSourcePointCut()")
    public void after(JoinPoint point) {
        //清理掉当前设置的数据源，让默认的数据源不受影响
        DataSourceContextHolder.clear();
    }


}
