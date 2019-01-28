package pers.liujunyi.cloud.core.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author ljy
 */
@ApiModel
@Data
@EqualsAndHashCode(callSuper = false)
public class IdParamDto implements Serializable {
    private static final long serialVersionUID = -6603286179991508260L;

    @ApiModelProperty(value = "id")
    @NotBlank(message = "id 必须填写")
    private String id;

    @ApiModelProperty(value = "系统码")
    @NotBlank(message = "systemCode 必须填写")
    @Length(min = 1, max = 10, message = "systemCode 最大长度为10个字符")
    private String systemCode;

    @ApiModelProperty(value = "凭证")
    @NotBlank(message = "credential 必须填写")
    @Length(min = 1, max = 64, message = "credential 最大长度为64个字符")
    private String credential;
}
