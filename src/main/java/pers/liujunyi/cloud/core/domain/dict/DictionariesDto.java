package pers.liujunyi.cloud.core.domain.dict;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;
import pers.liujunyi.cloud.common.dto.BaseDto;
import pers.liujunyi.cloud.common.util.RegexpUtils;

import javax.validation.constraints.*;

/***
 * 文件名称: DictionariesDto.java
 * 文件描述: 数据字典 dto
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
public class DictionariesDto extends BaseDto {

    /** 字典代码 */
    @ApiModelProperty(value = "字典代码")
    @NotBlank(message = "字典代码必须填写")
    @Length(min = 0, max = 32, message = "字典代码 最多可以输入32个字符")
    @Pattern(regexp = RegexpUtils.ALNUM_CODE_REGEXP, message = "字典代码" + RegexpUtils.ALNUM_CODE_MSG)
    private String dictCode;

    /** 字典名称 */
    @ApiModelProperty(value = "字典名称")
    @NotBlank(message = "字典名称必须填写")
    @Length(min = 0, max = 32, message = "字典名称 最多可以输入32个字符")
    @Pattern(regexp = RegexpUtils.ALNUM_NAME_REGEXP, message = "字典名称" + RegexpUtils.ALNUM_NAME_MSG)
    private String dictName;

    /** 上级ID */
    @NotNull(message = "pid 必须填写")
    @Min(value = 0, message = "pid 必须是数字类型")
    private Long pid;

    /** 所属系统编码  例如：1001 相册管理系统 */
    @ApiModelProperty(value = "系统编码")
    @NotBlank(message = "系统编码 必须填写")
    @Length(min = 0, max = 10, message = "系统编码 最多可以输入10个字符")
    private String systemCode;

    /** 优先级 */
    @ApiModelProperty(value = "优先级")
    @Min(value = 1, message = "优先级 必须是合法数字")
    @Max(value = 999,  message = "优先级 不能大于999")
    private Integer priority;

    /** 标签标注 */
    @ApiModelProperty(value = "标签标注")
    @Length(min = 0, max = 32, message = "标签 最多可以输入32个字符")
    @Pattern(regexp = RegexpUtils.ALNUM_NAME_REGEXP, message = "标签" + RegexpUtils.ALNUM_NAME_MSG)
    private String dictLabel;

    /** 描述信息 */
    @ApiModelProperty(value = "描述信息")
    @Length(min = 0, max = 50, message = "描述信息 最多可以输入50个字符")
    @Pattern(regexp = RegexpUtils.ILLEGITMACY_REGEXP, message = "描述信息" + RegexpUtils.ILLEGITMACY_MSG)
    private String description;

    /** 预留字段1 */
    @ApiModelProperty(value = "attributeOne")
    @Length(min = 0, max = 45, message = "attributeOne 最多可以输入45个字符")
    @Pattern(regexp = RegexpUtils.ALNUM_NAME_REGEXP, message = "attributeOne" + RegexpUtils.ALNUM_NAME_MSG)
    private String attributeOne;

    /** 预留字段2 */
    @ApiModelProperty(value = "attributeTwo")
    @Length(min = 0, max = 65, message = "attributeTwo 最多可以输入65个字符")
    @Pattern(regexp = RegexpUtils.ALNUM_NAME_REGEXP, message = "attributeTwo" + RegexpUtils.ALNUM_NAME_MSG)
    private String attributeTwo;

    /** 预留字段3 */
    @ApiModelProperty(value = "attributeThree")
    @Length(min = 0, max = 100, message = "attributeThree 最多可以输入100个字符")
    @Pattern(regexp = RegexpUtils.ALNUM_NAME_REGEXP, message = "attributeThree" + RegexpUtils.ALNUM_NAME_MSG)
    private String attributeThree;
    /** 完整的层级ID */
    private String fullParent;

    /** 完整的层级代码 */
    private String fullParentCode;


}
