package pers.liujunyi.cloud.core.controller.area;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pers.liujunyi.cloud.common.annotation.ApiVersion;
import pers.liujunyi.cloud.common.controller.BaseController;
import pers.liujunyi.cloud.common.restful.ResultInfo;
import pers.liujunyi.cloud.common.restful.ResultUtil;
import pers.liujunyi.cloud.common.util.SystemUtils;
import pers.liujunyi.cloud.core.service.arae.AreaElasticsearchService;
import pers.liujunyi.cloud.core.service.arae.AreaService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/***
 * 文件名称: AreaController.java
 * 文件描述: 行政区划 Controller
 * 公 司:
 * 内容摘要:
 * 其他说明:
 * 完成日期:2019年01月17日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
@Api(tags = "行政区划 API")
@RestController
public class AreaController extends BaseController {

    @Autowired
    private AreaService areaService;
    @Autowired
    private AreaElasticsearchService areaElasticsearchService;


    /**
     *  行政区划 Combox
     * @param pid
     * @param empty
     * @return
     */
    @ApiOperation(value = "行政区划 Combox", notes = "适用于下拉框选择 请求示例：127.0.0.1:18080/api/v1/area/combox")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "version", value = "版本号", paramType = "path",  dataType = "String", defaultValue = "v1"),
            @ApiImplicitParam(name = "pid", value = "pid", required = true),
            @ApiImplicitParam(name = "empty", value = "是否第一项是空",  required = true)
    })
    @GetMapping(value = "ignore/area/combox")
    @ApiVersion(1)
    public List<Map<String, Object>> araeCombox(@Valid @NotNull(message = "pid 必须填写")
                                                    @RequestParam(name = "pid", required = true) Long pid, Boolean empty) {
        return this.areaElasticsearchService.areaCombox(pid, empty);
    }

    /**
     *  根据ID获取名称
     * @param id
     * @return
     */
    @ApiOperation(value = "根据ID获取名称", notes = "适用于根据ID获取名称 请求示例：127.0.0.1:18080/api/v1/area/name")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "version", value = "版本号", paramType = "path",  dataType = "String", defaultValue = "v1"),
            @ApiImplicitParam(name = "id", value = "id", required = true, dataType = "Long")
    })
    @GetMapping(value = "ignore/area/name")
    @ApiVersion(1)
    public ResultInfo getAreaName(@Valid @NotNull(message = "id 必须填写")
                                      @RequestParam(name = "id", required = true) Long id) {
        return this.areaElasticsearchService.getAreaName(id);
    }

    /**
     *  根据ID获取名称  返回map
     * @param ids
     * @return
     */
    @ApiOperation(value = "根据ID获取名称  返回map", notes = "适用于根据ID获取名称 请求示例：127.0.0.1:18080/api/v1/area/map/name")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "version", value = "版本号", paramType = "path",  dataType = "String", defaultValue = "v1"),
            @ApiImplicitParam(name = "id", value = "id", required = true, dataType = "String")
    })
    @GetMapping(value = "ignore/area/map/name")
    @ApiVersion(1)
    public ResultInfo getAreaNameToMap(@Valid @NotNull(message = "ids 必须填写")
                                  @RequestParam(name = "ids", required = true) String ids) {
        return this.areaElasticsearchService.areaNameToMap(SystemUtils.idToLong(ids));
    }


    /**
     *  同步数据到Elasticsearch 中
     *
     * @return
     */
    @ApiOperation(value = "同步数据", notes = "适用于同步数据 请求示例：127.0.0.1:18080/api/v1/area/sync")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "version", value = "版本号", paramType = "path",  dataType = "String", defaultValue = "v1")
    })
    @PostMapping(value = "verify/area/sync")
    @ApiVersion(1)
    public ResultInfo syncDataToElasticsearch() {
        return this.areaService.syncDataToElasticsearch();
    }


    /**
     *  获取全部数据
     *
     * @return
     */
    @ApiOperation(value = "获取全部数据", notes = "适用于获取全部数据 请求示例：127.0.0.1:18080/api/v1/area/all")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "version", value = "版本号", paramType = "path",  dataType = "String", defaultValue = "v1")
    })
    @GetMapping(value = "verify/area/all")
    @ApiVersion(1)
    public ResultInfo findAll() {
        return ResultUtil.success(this.areaElasticsearchService.findAll());
    }
}
