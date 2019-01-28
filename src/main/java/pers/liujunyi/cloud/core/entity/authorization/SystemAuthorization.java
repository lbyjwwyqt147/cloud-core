package pers.liujunyi.cloud.core.entity.authorization;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import pers.liujunyi.common.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

/***
 * 文件名称: SystemAuthorization.java
 * 文件描述: 系统访问授权体类
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
public class SystemAuthorization extends BaseEntity {

    private static final long serialVersionUID = 1475322943977844667L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 系统编码 */
    private String sysCode;

    /** 系统名称 */
    private String sysName;

    /** 签名（用于访问权限认证） */
    private String signature;

    /** 到期时间 */
    private Date expireTime;

    /** 0: 启动 1：禁用  */
    private Byte status = 0;
}