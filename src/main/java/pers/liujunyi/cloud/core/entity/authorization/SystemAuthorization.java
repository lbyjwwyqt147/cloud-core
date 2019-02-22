package pers.liujunyi.cloud.core.entity.authorization;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Proxy;
import pers.liujunyi.common.entity.BaseEntity;

import javax.persistence.*;
import java.util.Date;

/***
 * 文件名称: SystemAuthorization.java
 * 文件描述: 系统访问授权体类
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
public class SystemAuthorization extends BaseEntity {

    private static final long serialVersionUID = 1475322943977844667L;

    /** 系统编码 */
    private String sysCode;

    /** 系统名称 */
    private String sysName;

    /**  client_id 第三方应用ID */
    private String appId;

    /**  appkey */
    private String appKey;

    /** 签名（用于访问权限认证） */
    private String signature;

    /** 到期时间 */
    @Temporal(TemporalType.TIMESTAMP)
    private Date expireTime;

    /** 0: 启动 1：禁用  */
    private Byte status = 0;
}