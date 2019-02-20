package pers.liujunyi.cloud.core.datasource;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.interceptor.*;
import org.springframework.validation.BindException;
import pers.liujunyi.common.exception.DescribeException;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/***
 * 文件名称: TransactionAdviceConfig.java
 * 文件描述: 全局事物配置
 * 公 司:
 * 内容摘要:   @description 通过AOP切面设置全局事务，拦截service包下面所有方法
 * 其他说明:   AOP术语：通知（Advice）、连接点（Joinpoint）、切入点（Pointcut)、切面（Aspect）、目标(Target)、代理(Proxy)、织入（Weaving）
 * 完成日期:2019年01月17日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
@Slf4j
@Aspect
@Component
public class TransactionAdviceConfig {

    /**
     * 事物超时时间  (秒)
     */
    @Value("${spring.datasource.druid.master.transaction-threshold-millis}")
    private Integer transactionTimeOut;

    /**
     * 定义切点变量：拦截pers.liujunyi.cloud.core包下所有类的所有方法,返回值类型任意的方法
     */
    private static final String AOP_POINTCUT_EXPRESSION = "execution (* pers.liujunyi.cloud.core.service.*.*(..))";

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Bean
    public TransactionInterceptor txAdvice() {
        /* 事务管理规则，声明具备事务管理的方法名 */
        NameMatchTransactionAttributeSource source = new NameMatchTransactionAttributeSource();
        /* 只读事物、不做更新删除等 */
        /* 当前存在事务就用当前的事务，当前不存在事务就创建一个新的事务 */
        RuleBasedTransactionAttribute readOnlyRule = new RuleBasedTransactionAttribute();
        /* 设置当前事务是否为只读事务，true为只读 */
        readOnlyRule.setReadOnly(true);
        /* transactiondefinition 定义事务的隔离级别；
         * PROPAGATION_NOT_SUPPORTED事务传播级别5，以非事务运行，如果当前存在事务，则把当前事务挂起 */
        readOnlyRule.setPropagationBehavior(TransactionDefinition.PROPAGATION_NOT_SUPPORTED);
        RuleBasedTransactionAttribute requireRule = new RuleBasedTransactionAttribute();
        /* 抛出异常后执行切点回滚 */
        List<RollbackRuleAttribute> rollbackRules = new LinkedList<>();
        rollbackRules.add(new RollbackRuleAttribute(RuntimeException.class));
        rollbackRules.add(new RollbackRuleAttribute(Exception.class));
        rollbackRules.add(new RollbackRuleAttribute(DescribeException.class));
        rollbackRules.add(new RollbackRuleAttribute(BindException.class));
        rollbackRules.add(new RollbackRuleAttribute(IOException.class));
        requireRule.setRollbackRules(rollbackRules);
        /* PROPAGATION_REQUIRED:事务隔离性为1，若当前存在事务，则加入该事务；如果当前没有事务，则创建一个新的事务。这是默认值。 */
        requireRule.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        /* 设置事务失效时间，如果超过设定时间(秒)，则回滚事务 */
        requireRule.setTimeout(transactionTimeOut);
        Map<String, TransactionAttribute> txMap = new HashMap<>();
        txMap.put("add*", requireRule);
        txMap.put("save*", requireRule);
        txMap.put("insert*", requireRule);
        txMap.put("update*", requireRule);
        txMap.put("delete*", requireRule);
        txMap.put("remove*", requireRule);
        txMap.put("get*", readOnlyRule);
        txMap.put("query*", readOnlyRule);
        txMap.put("find*", readOnlyRule);
        txMap.put("select*", readOnlyRule);
        txMap.put("count*", readOnlyRule);
        txMap.put("exists*", readOnlyRule);
        txMap.put("read*", readOnlyRule);
        source.setNameMap(txMap);
        TransactionInterceptor txAdvice = new TransactionInterceptor(transactionManager, source);
        return txAdvice;
    }

    /**
     * 声明切点的面
     *    * 切面（Aspect）：切面就是通知和切入点的结合。通知和切入点共同定义了关于切面的全部内容——它的功能、在何时和何地完成其功能。
     *    *
     */
    @Bean
    public Advisor txAdviceAdvisor() {
        log.info("开启全局声明式事物管理,serviceImpl中方法命名规则：｛add*, save*, insert*, update*, delete*, remove*, query*, find*, select*, count*, exists*, read*｝");
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        /* 声明和设置需要拦截的方法,用切点语言描写 */
        pointcut.setExpression(AOP_POINTCUT_EXPRESSION);
        /* 设置切面=切点pointcut+通知TxAdvice */
        return new DefaultPointcutAdvisor(pointcut, txAdvice());
    }


}
