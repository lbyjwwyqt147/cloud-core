package pers.liujunyi.cloud.core.domain.tenement;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;
import pers.liujunyi.cloud.common.dto.BaseDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/***
 * 文件名称: CientDataSourceConfigureDto.java
 * 文件描述: 租户客户端数据源配置信息 dto
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
public class CientDataSourceConfigureDto  extends BaseDto {

    /** 租户ID */
    @ApiModelProperty(value = "租户ID")
    @NotNull(message = "租户ID必须填写")
    private Long tenementId;

    /** 数据类型 1：MySql  2：redis 3: MongoDb 4: Elasticsearch */
    @ApiModelProperty(value = "数据类型 1：MySql  2：redis 3: MongoDb 4: Elasticsearch")
    @NotNull(message = "数据类型 必须填写")
    private Byte category;

    /** applicationName 对应每个运营程序 application.yml 中的 application.name */
    @ApiModelProperty(value = "applicationName 对应每个运营程序 application.yml 中的 application.name ")
    @NotBlank(message = "applicationName 必须填写")
    @Length(min = 0, max = 20, message = "applicationName 最多可以输入20个字符")
    private String applicationName;

    /** JDBC数据源类型*/
    @ApiModelProperty(value = "JDBC数据源类型")
    @Length(min = 0, max = 65, message = "JDBC数据源类型 最多可以输入65个字符")
    private String dataSourceType;

    /** JDBC驱动 默认 com.mysql.cj.jdbc.Driver */
    @ApiModelProperty(value = "数据源类型")
    @Length(min = 0, max = 45, message = "JDBC驱动 最多可以输入45个字符")
    private String driverClassName;

    /** 数据库地址 */
    @ApiModelProperty(value = "数据库地址")
    @NotBlank(message = "数据库地址 必须填写")
    @Length(min = 0, max = 255, message = "数据库地址 最多可以输入255个字符")
    private String dataSourceUrl;

    /** 数据库登录名 */
    @ApiModelProperty(value = "数据库登录名")
    @NotBlank(message = "数据库登录名 必须填写")
    @Length(min = 0, max = 32, message = "数据库登录名 最多可以输入32个字符")
    private String dataSourceUserName;

    /** 数据库登录密码 */
    @ApiModelProperty(value = "数据库登录密码")
    @Length(min = 0, max = 65, message = "数据库登录密码 最多可以输入65个字符")
    private String dataSourcePassword;

    /** 数据库名称 */
    @ApiModelProperty(value = "数据库名称")
    @Length(min = 0, max = 20, message = "数据库名称 最多可以输入20个字符")
    private String database;

    /** 端口 */
    @ApiModelProperty(value = "端口")
    private Integer port;

    /** 0: 启动 1：禁用  */
    @ApiModelProperty(value = "状态：0：正常  1：禁用")
    private Byte status = 0;
}
