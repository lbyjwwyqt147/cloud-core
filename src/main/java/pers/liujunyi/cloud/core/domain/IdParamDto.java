package pers.liujunyi.cloud.core.domain;

import com.alibaba.fastjson.JSONArray;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.List;

/**
 * @author ljy
 */
@ApiModel
@Data
@EqualsAndHashCode(callSuper = false)
public class IdParamDto implements Serializable {
    private static final long serialVersionUID = -6603286179991508260L;

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "pid")
    private Long pid;

    /**  一组 id   格式必须是  1,2,3 */
    @ApiModelProperty(value = "ids")
    private String ids;

    @ApiModelProperty(value = "idList")
    private List<Long> idList;

    @ApiModelProperty(value = "系统码")
    @NotBlank(message = "systemCode 必须填写")
    @Length(min = 1, max = 10, message = "systemCode 最大长度为10个字符")
    private String systemCode;

    @ApiModelProperty(value = "code")
    private String code;

    /**  一组 code   格式必须是  1,2,3 */
    @ApiModelProperty(value = "codes")
    private String codes;

    @ApiModelProperty(value = "codes")
    private List<String> codeList;

    @ApiModelProperty(value = "凭证")
    @NotBlank(message = "credential 必须填写")
    @Length(min = 1, max = 64, message = "credential 最大长度为64个字符")
    private String credential;

    @ApiModelProperty(value = "状态")
    private Byte status;

    @ApiModelProperty(value = "版本号")
    private Long dataVersion;

    public void setIds(String ids) {
        if (StringUtils.isNotBlank(ids)) {
            this.setIdList(JSONArray.parseArray(ids, Long.class));
        }
        this.ids = ids;
    }

    public void setCodes(String codes) {
        if (StringUtils.isNotBlank(codes)) {
            this.setCodeList(JSONArray.parseArray(codes, String.class));
        }
        this.codes = codes;
    }

}
