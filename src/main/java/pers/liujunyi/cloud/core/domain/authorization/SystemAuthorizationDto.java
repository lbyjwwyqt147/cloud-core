package pers.liujunyi.cloud.core.domain.authorization;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;
import pers.liujunyi.common.dto.BaseDto;

import javax.validation.constraints.NotBlank;
import java.util.Date;

/***
 * 文件名称: SystemAuthorizationDto.java
 * 文件描述: 数据授权 dto
 * 公 司:
 * 内容摘要:
 * 其他说明:
 * 完成日期:2019年01月17日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
@ApiModel
@Data
@EqualsAndHashCode(callSuper = true)
public class SystemAuthorizationDto extends BaseDto {

    /** 系统名称 */
    @ApiModelProperty(value = "系统名称")
    @NotBlank(message = "系统名称必须填写")
    @Length(min = 0, max = 32, message = "系统 最多可以输入32个字符")
    private String sysName;

    /** 到期时间 */
    @ApiModelProperty(value = "到期时间")
    private Date expireTime;

    /** 所属系统编码  例如：1001 相册管理系统 */
    @ApiModelProperty(value = "系统编码")
    @NotBlank(message = "系统编码 必须填写")
    @Length(min = 0, max = 10, message = "系统编码 最多可以输入10个字符")
    private String sysCode;

    /**  client_id 第三方应用ID */
    @ApiModelProperty(value = "client_id 第三方应用ID")
    private String appId;

    /**  appkey */
    @ApiModelProperty(value = "appKey")
    private String appKey;

    /** 签名（用于访问权限认证） */
    @ApiModelProperty(value = "signature")
    private String signature;

    /** 描述信息 */
    @ApiModelProperty(value = "描述信息")
    @Length(min = 0, max = 50, message = "描述信息 最多可以输入50个字符")
    private String description;

    /** 预留字段1 */
    @ApiModelProperty(value = "attributeOne")
    @Length(min = 0, max = 45, message = "attributeOne 最多可以输入45个字符")
    private String attributeOne;

    /** 预留字段2 */
    @ApiModelProperty(value = "attributeTwo")
    @Length(min = 0, max = 65, message = "attributeTwo 最多可以输入65个字符")
    private String attributeTwo;

}
