package pers.liujunyi.cloud.core.controller.dict;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pers.liujunyi.cloud.core.domain.IdParamDto;
import pers.liujunyi.cloud.core.domain.dict.DictionariesDto;
import pers.liujunyi.cloud.core.domain.dict.DictionariesQueryDto;
import pers.liujunyi.cloud.core.service.dict.DictionariesElasticsearchService;
import pers.liujunyi.cloud.core.service.dict.DictionariesService;
import pers.liujunyi.common.annotation.ApiVersion;
import pers.liujunyi.common.controller.BaseController;
import pers.liujunyi.common.restful.ResultInfo;
import pers.liujunyi.common.restful.ResultUtil;

import javax.validation.Valid;

/***
 * 文件名称: DictionariesController.java
 * 文件描述: 数据字典 Controller
 * 公 司:
 * 内容摘要:
 * 其他说明:
 * 完成日期:2019年01月17日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
@Api(tags = "数据字典 API")
@RestController
public class DictionariesController extends BaseController {

    @Autowired
    private DictionariesService dictionariesService;
    @Autowired
    private DictionariesElasticsearchService dictionariesElasticsearchService;

    /**
     * 保存数据
     *
     * @param param
     * @return
     */
    @ApiOperation(value = "保存数据", notes = "适用于保存数据 请求示例：127.0.0.1:18080/api/v1/dict/save")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "version", value = "版本号", paramType = "path", required = true, dataType = "integer", defaultValue = "v1")
    })
    @PostMapping(value = "dict/save")
    @ApiVersion(1)
    public ResultInfo saveRecord(@Valid @RequestBody DictionariesDto param) {
        return this.dictionariesService.saveRecord(param);
    }

    /**
     * 单条删除数据
     *
     * @param param
     * @return
     */
    @ApiOperation(value = "单条删除数据", notes = "适用于单条删除数据 请求示例：127.0.0.1:18080/api/v1/dict/delete")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "version", value = "版本号", paramType = "path", required = true, dataType = "integer", defaultValue = "v1")
    })
    @DeleteMapping(value = "dict/delete")
    @ApiVersion(1)
    public ResultInfo singleDelete(@Valid @RequestBody IdParamDto param) {
        this.dictionariesService.deleteById(param.getId());
        return ResultUtil.success();
    }

    /**
     * 批量删除
     *
     * @param param 　 多个id 用 , 隔开
     * @return
     */
    @ApiOperation(value = "删除多条数据", notes = "适用于批量删除数据 请求示例：127.0.0.1:18080/api/v1/dict/batchDelete")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "version", value = "版本号", paramType = "path", required = true, dataType = "integer", defaultValue = "v1")
    })
    @DeleteMapping(value = "dict/batchDelete")
    @ApiVersion(1)
    public ResultInfo batchDelete(@Valid @RequestBody IdParamDto param) {
        if (this.dictionariesService.deleteAllByIdIn(param.getIdList())) {
            return ResultUtil.success();
        }
        return ResultUtil.fail();
    }

    /**
     * 分页列表数据
     *
     * @param query
     * @return
     */
    @ApiOperation(value = "分页列表数据", notes = "适用于分页grid 显示数据 请求示例：127.0.0.1:18080/api/v1/dict/grid")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "version", value = "版本号", paramType = "path", required = true, dataType = "integer", defaultValue = "v1")
    })
    @GetMapping(value = "dict/grid")
    @ApiVersion(1)
    public ResultInfo findPageGrid(@Valid @RequestBody DictionariesQueryDto query) {
        return this.dictionariesElasticsearchService.findPageGird(query);
    }

    /**
     * 字典tree 结构数据
     *
     * @param pid
     * @return
     */
    @ApiOperation(value = "字典tree 结构数据", notes = "适用于tree 显示数据 请求示例：127.0.0.1:18080/api/v1/dict/tree")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "version", value = "版本号", paramType = "path", required = true, dataType = "integer", defaultValue = "v1"),
            @ApiImplicitParam(name = "pid", value = "pid",  required = true, dataType = "Long")
    })
    @GetMapping(value = "dict/tree")
    @ApiVersion(1)
    public ResultInfo dictTree(Long pid) {
        return this.dictionariesElasticsearchService.dictZtree(pid);
    }


    /**
     * 根据　一组　id　获取信息
     *
     * @param id 多个id 用 , 隔开
     * @return
     */
    @ApiOperation(value = "根据文件id获取信息", notes = "适用于根据文件id获取信息 请求示例：127.0.0.1:18080/api/v1/file/details/11,12")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "version", value = "版本号", paramType = "path", required = true, dataType = "integer", defaultValue = "v1"),
            @ApiImplicitParam(name = "id", value = "文件id,多个id 用,隔开", paramType = "path", required = true, dataType = "string")
    })
    @GetMapping(value = "dict/details/{id}")
    @ApiVersion(1)
    public ResultInfo findAllById(@PathVariable(value = "id") String id) {
        return ResultUtil.success();
    }


}
