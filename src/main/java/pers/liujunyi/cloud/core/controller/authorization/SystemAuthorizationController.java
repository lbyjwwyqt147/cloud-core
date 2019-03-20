package pers.liujunyi.cloud.core.controller.authorization;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pers.liujunyi.cloud.common.annotation.ApiVersion;
import pers.liujunyi.cloud.common.controller.BaseController;
import pers.liujunyi.cloud.common.query.jpa.annotation.BaseQuery;
import pers.liujunyi.cloud.common.restful.ResultInfo;
import pers.liujunyi.cloud.common.restful.ResultUtil;
import pers.liujunyi.cloud.core.domain.IdParamDto;
import pers.liujunyi.cloud.core.domain.authorization.SystemAuthorizationDto;
import pers.liujunyi.cloud.core.service.authorization.SystemAuthorizationService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/***
 * 文件名称: SystemAuthorizationController.java
 * 文件描述: 系统授权 Controller
 * 公 司:
 * 内容摘要:
 * 其他说明:
 * 完成日期:2019年01月17日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
@Api(tags = "系统授权 API")
@RestController
public class SystemAuthorizationController extends BaseController {

    @Autowired
    private SystemAuthorizationService systemAuthorizationService;

    /**
     * 保存数据
     *
     * @param param
     * @return
     */
    @ApiOperation(value = "保存数据", notes = "适用于保存数据 请求示例：127.0.0.1:18080/api/v1/system/authorization/save/save")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "version", value = "版本号", paramType = "path", required = true, dataType = "integer", defaultValue = "v1")
    })
    @PostMapping(value = "system/authorization/save")
    @ApiVersion(1)
    public ResultInfo saveRecord(@Valid SystemAuthorizationDto param) {
        return this.systemAuthorizationService.saveRecord(param);
    }

    /**
     * 根据ID单条删除数据
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "根据ID单条删除数据", notes = "适用于根据ID单条删除数据 请求示例：127.0.0.1:18080/api/v1/system/authorization/delete")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "version", value = "版本号", paramType = "path", required = true, dataType = "integer", defaultValue = "v1"),
            @ApiImplicitParam(name = "id", value = "id",  required = true, dataType = "Long")
    })
    @DeleteMapping(value = "system/authorization/delete")
    @ApiVersion(1)
    public ResultInfo singleDelete(@Valid @NotNull(message = "id 必须填写")
                                       @RequestParam(name = "id", required = true) Long id) {
        this.systemAuthorizationService.deleteById(id);
        return ResultUtil.success();
    }

    /**
     * 根据sysCode 批量删除
     *
     * @param param 　 多个sysCode 用 , 隔开
     * @return
     */
    @ApiOperation(value = "根据sysCode删除多条数据", notes = "适用于根据sysCode批量删除数据 请求示例：127.0.0.1:18080/api/v1/system/authorization/batchDelete")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "version", value = "版本号", paramType = "path", required = true, dataType = "integer", defaultValue = "v1"),
            @ApiImplicitParam(name = "codes", value = "codes",  required = true, dataType = "String")
    })
    @DeleteMapping(value = "system/authorization/batchDelete")
    @ApiVersion(1)
    public ResultInfo batchDelete(@Valid IdParamDto param ) {
        return this.systemAuthorizationService.deleteAllBySysCodeIn(param.getCodeList());
    }

    /**
     * 分页列表数据
     *
     * @param query
     * @return
     */
    @ApiOperation(value = "分页列表数据", notes = "适用于分页grid 显示数据 请求示例：127.0.0.1:18080/api/v1/system/authorization/grid")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "version", value = "版本号", paramType = "path", required = true, dataType = "integer", defaultValue = "v1")
    })
    @GetMapping(value = "table/system/authorization/grid")
    @ApiVersion(1)
    public ResultInfo findPageGrid(BaseQuery query) {
        return this.systemAuthorizationService.dataGrid(query);
    }


    /**
     *  根据sysCode 修改数据状态
     *
     * @param param
     * @return
     */
    @ApiOperation(value = "根据sysCode 修改数据状态", notes = "适用于根据sysCode 修改数据状态 请求示例：127.0.0.1:18080/api/v1/system/authorization/status")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "version", value = "版本号", paramType = "path", required = true, dataType = "integer", defaultValue = "v1"),
            @ApiImplicitParam(name = "codes", value = "codes",  required = true, dataType = "String"),
            @ApiImplicitParam(name = "status", value = "status",  required = true, dataType = "integer")
    })
    @PutMapping(value = "system/authorization/status")
    @ApiVersion(1)
    public ResultInfo updateDataStatus(@Valid IdParamDto param ) {
        return this.systemAuthorizationService.updateStatus(param.getStatus(), param.getCodeList());
    }

    /**
     *  同步数据到redis 中
     *
     * @return
     */
    @ApiOperation(value = "同步数据", notes = "适用于同步数据 请求示例：127.0.0.1:18080/api/v1/system/authorization/sync")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "version", value = "版本号", paramType = "path", required = true, dataType = "integer", defaultValue = "v1")
    })
    @PostMapping(value = "system/authorization/sync")
    @ApiVersion(1)
    public ResultInfo syncDataToRedis() {
        return this.systemAuthorizationService.syncDataToRedis();
    }

}
