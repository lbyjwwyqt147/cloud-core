package pers.liujunyi.cloud.core.entity.tenement;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Proxy;
import org.hibernate.annotations.Table;
import pers.liujunyi.cloud.common.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;

/***
 * 文件名称: CientDataSourceConfigure.java
 * 文件描述: 租户客户端数据源配置
 * 公 司:
 * 内容摘要:
 * 其他说明:
 * 完成日期:2019年01月17日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
@Data
@Entity
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@DynamicInsert
@DynamicUpdate
@Proxy(lazy = false)
@Table(appliesTo = "cient_data_source_configure", comment = "租户客户端数据源配置表")
public class CientDataSourceConfigure extends BaseEntity {

    @Column(columnDefinition="bigint(20) NOT NULL COMMENT '租户ID'")
    private Long tenementId;

    /** 数据源类型 1：MySql  2：redis 3: MongoDb 4: Elasticsearch */
    @Column(columnDefinition="tinyint(4) NOT NULL COMMENT '状态：1：MySql  2：redis 3: MongoDb 4: Elasticsearch '")
    private Byte category;

    /** applicationName 对应每个运营程序 application.yml 中的 application.name */
    @Column(columnDefinition="varchar(20) NOT NULL COMMENT 'applicationName 对应每个运营程序 application.yml 中的 application.name '")
    private String applicationName;

    /** 数据源类型 默认 com.alibaba.druid.pool.DruidDataSource */
    @Column(length = 65, columnDefinition="varchar(65) DEFAULT NULL COMMENT '数据源类型'")
    private String dataSourceType;

    /** JDBC驱动 默认 com.mysql.cj.jdbc.Driver */
    @Column(length = 45, columnDefinition="varchar(45) DEFAULT NULL COMMENT 'JDBC驱动'")
    private String driverClassName;

    /** 数据库地址 */
    @Column(columnDefinition="varchar(255) NOT NULL COMMENT '数据库地址'")
    private String dataSourceUrl;

    /** 数据库登录名 */
    @Column(columnDefinition="varchar(32) NOT NULL COMMENT '数据库登录名'")
    private String dataSourceUserName;

    /** 数据库登录密码 */
    @Column(columnDefinition="varchar(65) DEFAULT NULL COMMENT '数据库登录密码'")
    private String dataSourcePassword;

    /** 数据库名称 */
    @Column(columnDefinition="varchar(20) DEFAULT NULL COMMENT '数据库名称'")
    private String database;

    /** 端口 */
    @Column(columnDefinition="int(11) DEFAULT NULL COMMENT '端口'")
    private Integer port;

    /** 0: 启动 1：禁用  */
    @Column(columnDefinition="tinyint(4) DEFAULT '0' COMMENT '状态：0：正常  1：禁用 '")
    private Byte status;

}
