package pers.liujunyi.cloud.core.domain.tenement;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;
import pers.liujunyi.cloud.common.dto.BaseDto;
import pers.liujunyi.cloud.common.util.RegexpUtils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.Date;

/***
 * 文件名称: TenementInfoDto.java
 * 文件描述: 租户信息 dto
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
public class TenementInfoDto extends BaseDto {

    /** 租户名称 */
    @ApiModelProperty(value = "租户名称")
    @NotBlank(message = "租户名称必须填写")
    @Length(min = 0, max = 32, message = "租户名称 最多可以输入32个字符")
    @Pattern(regexp = RegexpUtils.NAME_REGEXP, message = "租户名称" + RegexpUtils.NAME_MSG)
    private String tenementName;

    /** 到期时间 */
    @ApiModelProperty(value = "到期时间")
    private Date expireTime;

    /** 租户手机号  */
    @ApiModelProperty(value = "手机号码")
    @NotBlank(message = "手机号码 必须填写")
    @Length(min = 0, max = 11, message = "手机号码 最多可以输入11个字符")
    private String tenementCode;

    /**  client_id 第三方应用ID */
    @ApiModelProperty(value = "client_id 第三方应用ID")
    private String appId;

    /**  appkey */
    @ApiModelProperty(value = "appKey")
    private String appKey;

    /** client_secret 密钥 */
    @ApiModelProperty(value = "密钥")
    private String appSecret;

    /** 描述信息 */
    @ApiModelProperty(value = "描述信息")
    @Length(min = 0, max = 100, message = "描述信息 最多可以输入100个字符")
    @Pattern(regexp = RegexpUtils.ILLEGITMACY_REGEXP, message = "描述信息" + RegexpUtils.ILLEGITMACY_MSG)
    private String description;

    /** 预留字段1 */
    @ApiModelProperty(value = "attributeOne")
    @Length(min = 0, max = 45, message = "attributeOne 最多可以输入45个字符")
    @Pattern(regexp = RegexpUtils.NAME_REGEXP, message = "attributeOne" + RegexpUtils.NAME_MSG)
    private String attributeOne;

    /** 预留字段2 */
    @ApiModelProperty(value = "attributeTwo")
    @Length(min = 0, max = 65, message = "attributeTwo 最多可以输入65个字符")
    @Pattern(regexp = RegexpUtils.NAME_REGEXP, message = "attributeTwo" + RegexpUtils.NAME_MSG)
    private String attributeTwo;

}
