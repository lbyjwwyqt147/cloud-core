package pers.liujunyi.cloud.core.controller.tenement;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pers.liujunyi.cloud.common.annotation.ApiVersion;
import pers.liujunyi.cloud.common.controller.BaseController;
import pers.liujunyi.cloud.common.dto.IdParamDto;
import pers.liujunyi.cloud.common.query.jpa.annotation.BaseQuery;
import pers.liujunyi.cloud.common.restful.ResultInfo;
import pers.liujunyi.cloud.common.restful.ResultUtil;
import pers.liujunyi.cloud.core.domain.tenement.TenementInfoDto;
import pers.liujunyi.cloud.core.service.tenement.TenementInfoService;

import javax.validation.Valid;

/***
 * 文件名称: TenementInfoController.java
 * 文件描述: 租户 Controller
 * 公 司:
 * 内容摘要:
 * 其他说明:
 * 完成日期:2019年01月17日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
@Api(tags = "租户信息 API")
@RestController
public class TenementInfoController extends BaseController {

    @Autowired
    private TenementInfoService tenementInfoService;

    /**
     * 保存数据
     *
     * @param param
     * @return
     */
    @ApiOperation(value = "保存数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "version", value = "版本号", paramType = "path", required = true, dataType = "integer", defaultValue = "v1")
    })
    @PostMapping(value = "verify/tenement/s")
    @ApiVersion(1)
    public ResultInfo saveRecord(@Valid TenementInfoDto param) {
        return this.tenementInfoService.saveRecord(param);
    }


    /**
     * 根据id 批量删除
     *
     * @param param 　 多个id用 , 隔开
     * @return
     */
    @ApiOperation(value = "根据id删除多条数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "version", value = "版本号", paramType = "path", required = true, dataType = "integer", defaultValue = "v1"),
            @ApiImplicitParam(name = "ids", value = "ids",  required = true, dataType = "String")
    })
    @DeleteMapping(value = "verify/tenement/d/b")
    @ApiVersion(1)
    public ResultInfo batchDelete(@Valid IdParamDto param ) {
        return this.tenementInfoService.deleteBatchById(param.getIdList());
    }

    /**
     * 分页列表数据
     *
     * @param query
     * @return
     */
    @ApiOperation(value = "分页列表数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "version", value = "版本号", paramType = "path", required = true, dataType = "integer", defaultValue = "v1")
    })
    @GetMapping(value = "table/tenement/g")
    @ApiVersion(1)
    public ResultInfo findPageGrid(BaseQuery query) {
        return this.tenementInfoService.dataGrid(query);
    }


    /**
     *  根据id修改数据状态
     *
     * @param param
     * @return
     */
    @ApiOperation(value = "根据id修改数据状态")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "version", value = "版本号", paramType = "path", required = true, dataType = "integer", defaultValue = "v1"),
            @ApiImplicitParam(name = "ids", value = "ids",  required = true, dataType = "String"),
            @ApiImplicitParam(name = "status", value = "status",  required = true, dataType = "integer")
    })
    @PutMapping(value = "verify/tenement/p")
    @ApiVersion(1)
    public ResultInfo updateDataStatus(@Valid IdParamDto param ) {
        return this.tenementInfoService.updateStatus(param.getStatus(), param.getIdList());
    }

    /**
     *  根据ID 获取数据
     *
     * @return
     */
    @ApiOperation(value = "根据ID 获取数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "version", value = "版本号", paramType = "path", required = true, dataType = "integer", defaultValue = "v1"),
            @ApiImplicitParam(name = "id", value = "id",  required = true, dataType = "integer")
    })
    @GetMapping(value = "ignore/tenement/details")
    @ApiVersion(1)
    public ResultInfo findById(Long id) {
        return ResultUtil.success(this.tenementInfoService.findById(id));
    }

    /**
     *  同步数据到redis 中
     *
     * @return
     */
    @ApiOperation(value = "同步数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "version", value = "版本号", paramType = "path", required = true, dataType = "integer", defaultValue = "v1")
    })
    @PostMapping(value = "verify/tenement/sync")
    @ApiVersion(1)
    public ResultInfo syncDataToRedis() {
        return this.tenementInfoService.syncDataToRedis();
    }

}
