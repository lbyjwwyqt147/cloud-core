package pers.liujunyi.cloud.core.controller.authorization;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pers.liujunyi.cloud.core.domain.IdParamDto;
import pers.liujunyi.cloud.core.domain.authorization.SystemAuthorizationDto;
import pers.liujunyi.cloud.core.service.authorization.SystemAuthorizationService;
import pers.liujunyi.common.annotation.ApiVersion;
import pers.liujunyi.common.controller.BaseController;
import pers.liujunyi.common.dto.BaseQuery;
import pers.liujunyi.common.restful.ResultInfo;
import pers.liujunyi.common.restful.ResultUtil;

import javax.validation.Valid;
import java.util.Arrays;

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
    public ResultInfo saveRecord(@Valid @RequestBody SystemAuthorizationDto param) {
        return this.systemAuthorizationService.saveRecord(param);
    }

    /**
     * 根据ID单条删除数据
     *
     * @param param
     * @return
     */
    @ApiOperation(value = "根据ID单条删除数据", notes = "适用于根据ID单条删除数据 请求示例：127.0.0.1:18080/api/v1/system/authorization/delete")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "version", value = "版本号", paramType = "path", required = true, dataType = "integer", defaultValue = "v1")
    })
    @DeleteMapping(value = "system/authorization/delete")
    @ApiVersion(1)
    public ResultInfo singleDelete(@Valid @RequestBody IdParamDto param) {
        this.systemAuthorizationService.deleteById(Long.getLong(param.getId()));
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
            @ApiImplicitParam(name = "version", value = "版本号", paramType = "path", required = true, dataType = "integer", defaultValue = "v1")
    })
    @DeleteMapping(value = "system/authorization/batchDelete")
    @ApiVersion(1)
    public ResultInfo batchDelete(@Valid @RequestBody IdParamDto param) {
        return this.systemAuthorizationService.deleteAllBySysCodeIn(Arrays.asList(param.getId().split(",")));
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
    @GetMapping(value = "system/authorization/grid")
    @ApiVersion(1)
    public ResultInfo findPageGrid(BaseQuery query) {
        return this.systemAuthorizationService.dataGrid(query);
    }

}
