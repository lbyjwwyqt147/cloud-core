package pers.liujunyi.cloud.core.datasource;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/***
 * 文件名称: DataSourceConfig.java
 * 文件描述: 配置数据源
 * 公 司:
 * 内容摘要:
 * 其他说明:
 * 完成日期:2019年01月17日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
@Configuration
public class DataSourceConfig {

    /**
     * 　声明主库数据源 Bean实例
     *
     * @return
     */
    @Bean(value = "masterDataSource", destroyMethod = "close")
    @Qualifier(value = "masterDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.druid.master")
    public DataSource masterDataSource() {
        return DruidDataSourceBuilder.create().build();
    }

    /**
     * 　声明从库数据源 Bean实例
     *
     * @return
     */
    @Bean(value = "slaveDataSource", destroyMethod = "close")
    @Qualifier(value = "slaveDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.druid.slave")
    public DataSource slaveDataSource() {
        return DruidDataSourceBuilder.create().build();
    }

    /**
     * 设置　AbstractRoutingDataSource　包装多个数据源
     *
     * @return
     */
    @Bean(name = "dataSource")
    @Qualifier(value = "dataSource")
    @Primary
    public MultipleDataSourceRouting dataSource() throws SQLException {
        //按照目标数据源名称和目标数据源对象的映射存放在Map中
        Map<String, DataSource> targetDataSources = new ConcurrentHashMap<>();
        // 主库数据源
        targetDataSources.put(DataSourceType.MASTER, masterDataSource());
        // 从库数据源
        targetDataSources.put(DataSourceType.SLAVE, slaveDataSource());
        //采用是想AbstractRoutingDataSource的对象包装多数据源
        return new MultipleDataSourceRouting(masterDataSource(), targetDataSources);
    }


}
