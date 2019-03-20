package pers.liujunyi.cloud.core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import pers.liujunyi.cloud.common.configuration.MySQLUpperCaseStrategy;


/***
 *
 * 开启增强代理 @EnableAspectJAutoProxy
 * @author
 */
@EnableJpaAuditing
@EnableAspectJAutoProxy(proxyTargetClass = true)
@ComponentScan(basePackages = {"pers.liujunyi.cloud"}, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {MySQLUpperCaseStrategy.class}))
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class,  RedisAutoConfiguration.class, RedisRepositoriesAutoConfiguration.class})
public class CloudCoreApplication {

    public static void main(String[] args) {
        // 解决 Factory method 'elasticsearchClient' threw exception; nested exception is java.lang.IllegalStateException: availableProcessors is already set to [4], rejecting [4]
        //原因：程序的其他地方使用了Netty，这里指redis。这影响在实例化传输客户端之前初始化处理器的数量。 实例化传输客户端时，我们尝试初始化处理器的数量。 由于在其他地方使用Netty，因此已经初始化并且Netty会对此进行防范，因此首次实例化会因看到的非法状态异常而失败。
        System.setProperty("es.set.netty.runtime.available.processors", "false");
        SpringApplication.run(CloudCoreApplication.class, args);
    }

}

