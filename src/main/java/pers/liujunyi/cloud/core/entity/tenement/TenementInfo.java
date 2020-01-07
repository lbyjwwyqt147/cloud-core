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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

/***
 * 文件名称: TenementInfo.java
 * 文件描述: 租户信息
 * 公 司:
 * 内容摘要:
 * 其他说明:   @Proxy(lazy = false) 解决  getOne()  could not initialize proxy - no Session
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
@Table(appliesTo = "tenement_info", comment = "租户信息表")
public class TenementInfo extends BaseEntity {

    private static final long serialVersionUID = 1475322943977844667L;

    /** 租户电话 */
    @Column(length = 20, nullable = false, columnDefinition="varchar(20) NOT NULL COMMENT '租户电话'")
    private String tenementPhone;

    /** 租户名称 */
    @Column(length = 32, nullable = false, columnDefinition="varchar(32) NOT NULL COMMENT '租户名称'")
    private String tenementName;

    /**  client_id 第三方应用ID */
    @Column(length = 45, columnDefinition="varchar(45) DEFAULT NULL COMMENT 'client_id'")
    private String appId;

    /**  app_key */
    @Column(length = 45, columnDefinition="varchar(45) DEFAULT NULL COMMENT 'client_key'")
    private String appKey;

    /** client_secret 密钥 */
    @Column(length = 128, columnDefinition="varchar(45) DEFAULT NULL COMMENT 'client_secret'")
    private String appSecret;

    /** 域名 */
    @Column(length = 200, columnDefinition="varchar(200) DEFAULT NULL COMMENT '域名'")
    private String domainName;

    /** 运营程序版本号（不同租户可能使用不同的版本程序） */
    @Column(columnDefinition="varchar(10) NOT NULL COMMENT '运营程序版本号（不同租户可能使用不同的版本程序）'")
    private String specialVersion;

    /** 到期时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(columnDefinition="timestamp DEFAULT NULL  COMMENT '到期时间'")
    private Date expireTime;

    /** 0: 启动 1：禁用  */
    @Column(columnDefinition="tinyint(4) DEFAULT '0' COMMENT '状态：0：正常  1：禁用 '")
    private Byte status;

    /** 描述信息 */
    @Column(columnDefinition="varchar(100) DEFAULT NULL  COMMENT '描述信息'")
    private String description;

    /** 预留字段1 */
    @Column(columnDefinition="varchar(45) DEFAULT NULL  COMMENT '预留字段1'")
    private String attributeOne;

    /** 预留字段2 */
    @Column(columnDefinition="varchar(65) DEFAULT NULL  COMMENT '预留字段2'")
    private String attributeTwo;

    /** 文件夹 */
    @Column(columnDefinition="varchar(32) DEFAULT NULL  COMMENT '文件夹'")
    private String folder;

    /** 租户数据源配置 */
    /*List<CientDataSourceConfigure> cientDataSource;*/
}