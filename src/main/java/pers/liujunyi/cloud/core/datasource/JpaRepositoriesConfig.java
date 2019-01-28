package pers.liujunyi.cloud.core.datasource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.sql.SQLException;


/***
 * 文件名称: JpaRepositoriesConfig.java
 * 文件描述: JPA 事物配置
 * 公 司:
 * 内容摘要:
 * 其他说明:  @EnableTransactionManagement  开启注解事物   @EnableJpaRepositories 开启JPA存储库扫描
 *            EnableJpaRepositories 和 EnableElasticsearchRepositories 不能在同一包下面
 * 完成日期:2019年01月17日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
@Configuration
@EnableJpaRepositories(basePackages = "pers.liujunyi.cloud.core.repository.jpa")
@EnableElasticsearchRepositories(basePackages = "pers.liujunyi.cloud.core.repository.elasticsearch")
@EnableTransactionManagement(proxyTargetClass = true)
public class JpaRepositoriesConfig {

    @Autowired
    @Qualifier("dataSource")
    private DataSource dataSource;


    /**
     * 　DataSource　事物
     *
     * @param dataSource
     * @return
     */
    @Bean
    public PlatformTransactionManager txManager(DataSource dataSource) throws SQLException {
        return new DataSourceTransactionManager(dataSource);
    }

    /**
     * jpa 事物
     *
     * @param entityManagerFactory
     * @return
     */
    @Bean
    @Primary
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) throws SQLException {
        return new JpaTransactionManager(entityManagerFactory);
    }


}
