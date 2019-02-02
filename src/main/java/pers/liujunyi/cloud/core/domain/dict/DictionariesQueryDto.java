package pers.liujunyi.cloud.core.domain.dict;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;
import pers.liujunyi.common.dto.BaseQuery;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/***
 * 文件名称: DictionariesQueryDto.java
 * 文件描述: 数据字典 query dto
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
public class DictionariesQueryDto extends BaseQuery {

    /** 字典代码 */
    @ApiModelProperty(value = "字典代码")
    @Length(min = 0, max = 32, message = "字典代码 最大长度为32个字符")
    private String dictCode;

    /** 字典名称 */
    @ApiModelProperty(value = "字典名称")
    @Length(min = 0, max = 32, message = "字典名称 最大长度为32个字符")
    private String dictName;

    /** 上级ID */
    @ApiModelProperty(value = "pid")
    @NotNull(message = "pid 必须填写")
    @Min(value = 0, message = "pid 必须是数字类型")
    private Long pid;

    /** 所属系统编码  例如：1001 相册管理系统 */
    @ApiModelProperty(value = "系统编码")
    @NotBlank(message = "systemCode 必须填写")
    @Length(min = 1, max = 10, message = "systemCode 最大长度为10个字符")
    private String systemCode;

    /** 标签标注 */
    @ApiModelProperty(value = "标签标注")
    @Length(min = 0, max = 32, message = "标签 最大长度为32个字符")
    private String dictLabel;

    /** 凭证 */
    @ApiModelProperty(value = "凭证")
    @NotBlank(message = "credential 必须填写")
    @Length(min = 0, max = 64, message = "credential 最大长度为64个字符")
    private String credential;
}
