package pers.liujunyi.cloud.core;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import pers.liujunyi.cloud.core.datasource.MultipleDataSourceRouting;
import pers.liujunyi.common.configuration.MySQLUpperCaseStrategy;


/***
 * @Import({MultipleDataSourceRouting.class}) 解决 DataSourceConfig.class 中出现循环依赖问题
 * 开启增强代理 @EnableAspectJAutoProxy
 * @author
 */
@EnableJpaAuditing
@EnableAspectJAutoProxy(proxyTargetClass = true)
@Import({MultipleDataSourceRouting.class})
@SpringBootApplication(exclude = {DruidDataSourceAutoConfigure.class, DataSourceAutoConfiguration.class})
@ComponentScan(basePackages = {"pers.liujunyi.common", "pers.liujunyi.cloud.core"}, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {MySQLUpperCaseStrategy.class}))
public class CloudCoreApplication {

    public static void main(String[] args) {
        // 解决 Factory method 'elasticsearchClient' threw exception; nested exception is java.lang.IllegalStateException: availableProcessors is already set to [4], rejecting [4]
        //原因：程序的其他地方使用了Netty，这里指redis。这影响在实例化传输客户端之前初始化处理器的数量。 实例化传输客户端时，我们尝试初始化处理器的数量。 由于在其他地方使用Netty，因此已经初始化并且Netty会对此进行防范，因此首次实例化会因看到的非法状态异常而失败。
        System.setProperty("es.set.netty.runtime.available.processors", "false");
        SpringApplication.run(CloudCoreApplication.class, args);
    }

}

