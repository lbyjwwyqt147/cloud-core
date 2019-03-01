package pers.liujunyi.cloud.core.domain.file;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.io.Serializable;

/***
 * 文件名称: FileDataDto.java
 * 文件描述: 文件数据 dto
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
@EqualsAndHashCode(callSuper = false)
public class FileDataDto implements Serializable {
    private static final long serialVersionUID = -4979656133062494676L;

    /** 所属系统编码  例如：1001 相册管理系统 */
    @ApiModelProperty(value = "系统编码")
    @Length(min = 0, max = 10, message = "systemCode 最多可以输入10个字符")
    private String systemCode;

    /** 所属系统业务模块编码 例如：10：相册管理  20：视频管理  30： 博文管理 */
    @ApiModelProperty(value = "所属系统业务模块编码")
    @Length(min = 0, max = 10, message = "businessCode 最多可以输入10个字符")
    private String businessCode;

    /** 所属系统业务模块中的字段  */
    @ApiModelProperty(value = "所属系统业务模块中的字段")
    @Length(min = 0, max = 32, message = "businessField 最多可以输入32个字符")
    private String businessField;

    /** 优先级 */
    @ApiModelProperty(value = "优先级")
    @Max(value = 127, message = "priority 数值最大值为127")
    private Byte priority;

    /** 描述信息 */
    @ApiModelProperty(value = "描述信息")
    @Length(min = 0, max = 100, message = "description 最多可以输入100个字符")
    private String description;

    /** 备注 */
    @ApiModelProperty(value = "备注")
    @Length(min = 0, max = 100, message = "remarks 最多可以输入100个字符")
    private String remarks;

    /** 上传者ID */
    @ApiModelProperty(value = "上传者ID ")
    @Min(value = 1, message = "uploaderId 必须是合法数字")
    private Long uploaderId;

    /** 上传者名称 */
    @ApiModelProperty(value = "上传者名称 ")
    @Length(min = 0, max = 32, message = "uploaderName 最多可以输入32个字符")
    private String uploaderName;

    /** 扩展字段1 */
    @ApiModelProperty(value = "扩展字段1")
    @Length(min = 0, max = 45, message = "extensionOne 最多可以输入45个字符")
    private String extensionOne;

    /** 扩展字段2 */
    @ApiModelProperty(value = "扩展字段2")
    @Length(min = 0, max = 45, message = "extensionTwo 最多可以输入45个字符")
    private String extensionTwo;

    /** 扩展字段3 */
    @ApiModelProperty(value = "扩展字段3")
    @Length(min = 0, max = 45, message = "extensionThree 最多可以输入45个字符")
    private String extensionThree;

    /** 扩展字段4 */
    @ApiModelProperty(value = "扩展字段4")
    @Length(min = 0, max = 45, message = "extensionFour 最多可以输入45个字符")
    private String extensionFour;

    @ApiModelProperty(value = "是否需要重命名上传文件名称 默认：true")
    private Boolean rename = true;

    @ApiModelProperty(value = "水印名称")
    @Length(min = 0, max = 15, message = "watermark 最多可以输入15个字符")
    private String watermark;

    @ApiModelProperty(value = "是否添加水印 默认：false")
    private Boolean addWatermark = false;

}
