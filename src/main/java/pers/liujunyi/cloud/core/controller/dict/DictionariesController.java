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
import pers.liujunyi.common.vo.tree.ZTreeNode;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

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
    public ResultInfo saveRecord(@Valid DictionariesDto param) {
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
    public ResultInfo singleDelete(@Valid IdParamDto param) {
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
    public ResultInfo batchDelete(@Valid IdParamDto param) {
        return this.dictionariesService.batchDeletes(param.getIdList());
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
    public ResultInfo findPageGrid(@Valid  DictionariesQueryDto query) {
        return this.dictionariesElasticsearchService.findPageGird(query);
    }


    /**
     * 字典tree 结构数据
     *
     * @param param
     * @return
     */
    @ApiOperation(value = "字典tree 结构数据", notes = "适用于tree 显示数据 请求示例：127.0.0.1:18080/api/v1/dict/tree")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "version", value = "版本号", paramType = "path", required = true, dataType = "integer", defaultValue = "v1"),
            @ApiImplicitParam(name = "pid", value = "pid",  required = true, dataType = "Long")
    })
    @PostMapping(value = "dict/ztree")
    @ApiVersion(1)
        public List<ZTreeNode> dictZTree(@Valid IdParamDto param ) {
        return this.dictionariesElasticsearchService.dictTree(param.getId(), param.getSystemCode());
    }


    /**
     *  修改数据状态
     *
     * @param param
     * @return
     */
    @ApiOperation(value = "修改数据状态", notes = "适用于修改数据状态 请求示例：127.0.0.1:18080/api/v1/dict/status")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "version", value = "版本号", paramType = "path", required = true, dataType = "integer", defaultValue = "v1")
    })
    @PutMapping(value = "dict/status")
    @ApiVersion(1)
    public ResultInfo updateDataStatus(@Valid IdParamDto param ) {
        return this.dictionariesService.updateStatus(param.getStatus(), param.getIdList());
    }


    /**
     *  字典 Combox
     * @param systemCode
     * @param dictCode
     * @param empty
     * @return
     */
    @ApiOperation(value = "字典 Combox", notes = "适用于下拉框选择 请求示例：127.0.0.1:18080/api/v1/dict/combox")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "version", value = "版本号", paramType = "path", required = true, dataType = "integer", defaultValue = "v1"),
            @ApiImplicitParam(name = "systemCode", value = "系统码", required = true),
            @ApiImplicitParam(name = "credential", value = "凭证",  required = true),
            @ApiImplicitParam(name = "dictCode", value = "字典代码",  required = true),
            @ApiImplicitParam(name = "empty", value = "是否第一项是空",  required = true)
    })
    @GetMapping(value = "dict/combox")
    @ApiVersion(1)
    public List<Map<String, String>> dictCombox(String systemCode,  String dictCode, Boolean empty) {
        return this.dictionariesElasticsearchService.dictCombox(systemCode, dictCode, empty);
    }

    /**
     *  字典代码转换为字典值
     * @param systemCode
     * @param dictCode
     * @param  pidDictCode
     * @return
     */
    @ApiOperation(value = "字典代码转换为字典值", notes = "适用于字典代码转换为字典值 请求示例：127.0.0.1:18080/api/v1/dict/dictName")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "version", value = "版本号", paramType = "path", required = true, dataType = "integer", defaultValue = "v1"),
            @ApiImplicitParam(name = "systemCode", value = "系统码", required = true),
            @ApiImplicitParam(name = "pidDictCode", value = "父级字典代码",  required = true),
            @ApiImplicitParam(name = "dictCode", value = "字典代码",  required = true)
    })
    @GetMapping(value = "dict/dictName")
    @ApiVersion(1)
    public ResultInfo dictName(String systemCode, String pidDictCode, String dictCode) {
        return  ResultUtil.success(this.dictionariesElasticsearchService.getDictName(systemCode, pidDictCode, dictCode));
    }

    /**
     *  同步数据到es中
     * @param
     * @return
     */
    @ApiOperation(value = "同步数据", notes = "同步数据 请求示例：127.0.0.1:18080/api/v1/dict/sync")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "version", value = "版本号", paramType = "path", required = true, dataType = "integer", defaultValue = "v1"),
    })
    @PostMapping(value = "dict/sync")
    @ApiVersion(1)
    public ResultInfo syncDataToElasticsearch() {
        return this.dictionariesService.syncDataToElasticsearch();
    }
}
