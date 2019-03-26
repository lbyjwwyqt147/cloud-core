package pers.liujunyi.cloud.core.controller.dict;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pers.liujunyi.cloud.common.annotation.ApiVersion;
import pers.liujunyi.cloud.common.controller.BaseController;
import pers.liujunyi.cloud.common.encrypt.annotation.Decrypt;
import pers.liujunyi.cloud.common.encrypt.annotation.Encrypt;
import pers.liujunyi.cloud.common.restful.ResultInfo;
import pers.liujunyi.cloud.common.restful.ResultUtil;
import pers.liujunyi.cloud.common.util.SystemUtils;
import pers.liujunyi.cloud.common.vo.tree.ZtreeNode;
import pers.liujunyi.cloud.core.domain.IdParamDto;
import pers.liujunyi.cloud.core.domain.dict.DictionariesDto;
import pers.liujunyi.cloud.core.domain.dict.DictionariesQueryDto;
import pers.liujunyi.cloud.core.service.dict.DictionariesElasticsearchService;
import pers.liujunyi.cloud.core.service.dict.DictionariesService;
import pers.liujunyi.cloud.core.util.Constant;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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
     * 保存数据 (参数已加密)
     *
     * @param param
     * @return
     */
    @ApiOperation(value = "保存数据(数据加密处理)", notes = "适用于保存数据 请求示例：127.0.0.1:18080/api/v1/dict/s")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "version", value = "版本号", paramType = "path", required = true, dataType = "integer", defaultValue = "v1")
    })
    @Decrypt
    @Encrypt
    @PostMapping(value = "dict/s")
    @ApiVersion(1)
    public ResultInfo encryptSaveDataRecord(@Valid @RequestBody DictionariesDto param) {
        return this.dictionariesService.saveRecord(param);
    }

    /**
     * 单条删除数据
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "单条删除数据", notes = "适用于单条删除数据 请求示例：127.0.0.1:18080/api/v1/dict/delete")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "version", value = "版本号", paramType = "path", required = true, dataType = "integer", defaultValue = "v1"),
            @ApiImplicitParam(name = "id", value = "id",  required = true, dataType = "Long")
    })
    @DeleteMapping(value = "dict/delete")
    @ApiVersion(1)
    public ResultInfo singleDelete(@Valid @NotNull(message = "id 必须填写")
                                       @RequestParam(name = "id", required = true) Long id) {
        return this.dictionariesService.singleDelete(id);
    }

    /**
     * 单条删除数据(数据加密处理)
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "单条删除数据(数据加密处理)", notes = "适用于单条删除数据 请求示例：127.0.0.1:18080/api/v1/dict/d")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "version", value = "版本号", paramType = "path", required = true, dataType = "integer", defaultValue = "v1"),
            @ApiImplicitParam(name = "id", value = "id",  required = true, dataType = "Long")
    })
    @Encrypt
    @Decrypt
    @DeleteMapping(value = "dict/d")
    @ApiVersion(1)
    public ResultInfo encryptSingleDelete(@Valid @NotNull(message = "id 必须填写")
                                   @RequestParam(name = "id", required = true) Long id) {
        return this.dictionariesService.singleDelete(id);
    }

    /**
     * 批量删除
     *
     * @param param 　 多个id 用 , 隔开
     * @return
     */
    @ApiOperation(value = "删除多条数据", notes = "适用于批量删除数据 请求示例：127.0.0.1:18080/api/v1/dict/batchDelete")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "version", value = "版本号", paramType = "path", required = true, dataType = "integer", defaultValue = "v1"),
            @ApiImplicitParam(name = "ids", value = "ids",  required = true, dataType = "String")
    })
    @DeleteMapping(value = "dict/batchDelete")
    @ApiVersion(1)
    public ResultInfo batchDelete(@Valid IdParamDto param) {
        return this.dictionariesService.batchDeletes(param.getIdList());
    }

    /**
     * 批量删除(数据加密处理)
     *
     * @param param 　 多个id 用 , 隔开
     * @return
     */
    @ApiOperation(value = "删除多条数据(数据加密处理)", notes = "适用于批量删除数据 请求示例：127.0.0.1:18080/api/v1/dict/batch/d")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "version", value = "版本号", paramType = "path", required = true, dataType = "integer", defaultValue = "v1"),
            @ApiImplicitParam(name = "ids", value = "ids",  required = true, dataType = "String")
    })
    @Encrypt
    @Decrypt
    @DeleteMapping(value = "dict/batch/d")
    @ApiVersion(1)
    public ResultInfo encryptBatchDelete(@Valid @RequestBody IdParamDto param) {
        return this.dictionariesService.batchDeletes(param.getIdList());
    }

    /**
     * 分页列表数据
     *
     * @param query
     * @return
     */
    @ApiOperation(value = "分页列表数据", notes = "适用于分页grid 显示数据 请求示例：127.0.0.1:18080/api/v1/table/dict/grid")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "version", value = "版本号", paramType = "path", required = true, dataType = "integer", defaultValue = "v1")
    })
    @GetMapping(value = "table/dict/grid")
    @ApiVersion(1)
    public ResultInfo findPageGrid(@Valid  DictionariesQueryDto query) {
        return this.dictionariesElasticsearchService.findPageGird(query);
    }

    /**
     * 分页列表数据
     *
     * @param query
     * @return
     */

    @ApiOperation(value = "分页列表数据(数据加密处理)", notes = "适用于分页grid 显示数据 请求示例：127.0.0.1:18080/api/v1/table/dict/g")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "version", value = "版本号", paramType = "path", required = true, dataType = "integer", defaultValue = "v1")
    })
    @GetMapping(value = "table/dict/g")
    @ApiVersion(1)
    public ResultInfo encryptPageGrid(@Valid DictionariesQueryDto query) {
        return this.dictionariesElasticsearchService.findPageGird(query);
    }

    /**
     *  根据pid 获取 字典tree 结构数据 (只包含正常数据  禁用数据不展示)
     *
     * @param param
     * @return
     */
    @ApiOperation(value = "字典tree 结构数据 (只包含正常数据  禁用数据不展示)", notes = "适用于 根据pid  显示 tree 数据 请求示例：127.0.0.1:18080/api/v1/tree/dict/tree")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "version", value = "版本号", paramType = "path", required = true, dataType = "integer", defaultValue = "v1"),
            @ApiImplicitParam(name = "id", value = "id",  required = true, dataType = "Long")
    })
    @GetMapping(value = "tree/dict/z")
    @ApiVersion(1)
    public List<ZtreeNode> dictZTree(@Valid IdParamDto param ) {
        return this.dictionariesElasticsearchService.dictTree(param.getId(), Constant.ENABLE_STATUS, param.getSystemCode());
    }

    /**
     * 根据 fullParentCode 获取 字典tree 结构数据 (只包含正常数据  禁用数据不展示)
     *
     * @param param
     * @return
     */
    @ApiOperation(value = "根据 fullParentCode 获取  字典tree 结构数据 (只包含正常数据  禁用数据不展示)", notes = "适用于 根据 fullParentCode 显示 tree 数据 请求示例：127.0.0.1:18080/api/v1/tree/dict/tree")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "version", value = "版本号", paramType = "path", required = true, dataType = "integer", defaultValue = "v1"),
            @ApiImplicitParam(name = "codes", value = "codes",  required = true, dataType = "String")
    })
    @GetMapping(value = "tree/dict/parentCode/z")
    @ApiVersion(1)
    public List<ZtreeNode> dictCodeZTree(@Valid IdParamDto param ) {
        return this.dictionariesElasticsearchService.dictCodeTree(param.getCode(), Constant.ENABLE_STATUS, param.getSystemCode());
    }

    /**
     * 根据pid 获取 字典tree 结构数据 (包含禁用数据 )
     *
     * @param param
     * @return
     */
    @ApiOperation(value = "字典tree 结构数据 (包含禁用数据 )", notes = "适用于 根据pid  显示 tree 数据 请求示例：127.0.0.1:18080/api/v1/tree/dict/all/tree")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "version", value = "版本号", paramType = "path", required = true, dataType = "integer", defaultValue = "v1"),
            @ApiImplicitParam(name = "id", value = "id",  required = true, dataType = "Long")
    })
    @GetMapping(value = "tree/dict/all/z")
    @ApiVersion(1)
    public List<ZtreeNode> allDictZTree(@Valid IdParamDto param ) {
        return this.dictionariesElasticsearchService.dictTree(param.getId(), null ,param.getSystemCode());
    }


    /**
     *  修改数据状态
     *
     * @param param
     * @return
     */
    @ApiOperation(value = "修改数据状态", notes = "适用于修改数据状态 请求示例：127.0.0.1:18080/api/v1/dict/status")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "version", value = "版本号", paramType = "path", required = true, dataType = "integer", defaultValue = "v1"),
            @ApiImplicitParam(name = "ids", value = "ids",  required = true, dataType = "String"),
            @ApiImplicitParam(name = "status", value = "status",  required = true, dataType = "integer")
    })
    @PutMapping(value = "dict/status")
    @ApiVersion(1)
    public ResultInfo updateDataStatus(@Valid IdParamDto param ) {
        return this.dictionariesService.updateStatus(param.getStatus(), param.getIdList());
    }


    /**
     *  修改数据状态(数据加密处理)
     *
     * @param param
     * @return
     */
    @ApiOperation(value = "修改数据状态(数据加密处理)", notes = "适用于修改数据状态 请求示例：127.0.0.1:18080/api/v1/dict/st")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "version", value = "版本号", paramType = "path", required = true, dataType = "integer", defaultValue = "v1"),
            @ApiImplicitParam(name = "ids", value = "ids",  required = true, dataType = "String"),
            @ApiImplicitParam(name = "status", value = "status",  required = true, dataType = "integer")
    })
    @Encrypt
    @Decrypt
    @PutMapping(value = "dict/st")
    @ApiVersion(1)
    public ResultInfo encryptUpdateDataStatus(@Valid @RequestBody IdParamDto param ) {
        return this.dictionariesService.updateStatus(param.getStatus(), param.getIdList());
    }

    /**
     *  字典 Combox
     * @param systemCode
     * @param parentCode
     * @param empty
     * @return
     */
    @ApiOperation(value = "字典 Combox", notes = "适用于下拉框选择 请求示例：127.0.0.1:18080/api/v1/dict/combox")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "version", value = "版本号", paramType = "path", required = true, dataType = "integer", defaultValue = "v1"),
            @ApiImplicitParam(name = "systemCode", value = "系统码", required = true),
            @ApiImplicitParam(name = "parentCode", value = "父级字典代码",  required = true),
            @ApiImplicitParam(name = "empty", value = "是否第一项是空",  required = true)
    })
    @GetMapping(value = "dict/combox")
    @ApiVersion(1)
    public List<Map<String, String>> dictCombox(@Valid @NotBlank(message = "systemCode 必须填写")
                                                    @RequestParam(name = "systemCode", required = true) String systemCode, @NotBlank(message = "parentCode 必须填写")
    @RequestParam(name = "parentCode", required = true)  String parentCode, Boolean empty) {
        return this.dictionariesElasticsearchService.dictCombox(systemCode, parentCode, empty);
    }

    /**
     *  字典 Combox (数据加密处理)
     * @param systemCode
     * @param parentCode
     * @param empty
     * @return
     */
    @ApiOperation(value = "字典 Combox (数据加密处理)", notes = "适用于下拉框选择 请求示例：127.0.0.1:18080/api/v1/dict/box")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "version", value = "版本号", paramType = "path", required = true, dataType = "integer", defaultValue = "v1"),
            @ApiImplicitParam(name = "systemCode", value = "系统码", required = true),
            @ApiImplicitParam(name = "parentCode", value = "父级字典代码",  required = true),
            @ApiImplicitParam(name = "empty", value = "是否第一项是空",  required = true)
    })
    @Encrypt
    @GetMapping(value = "dict/box")
    @ApiVersion(1)
    public List<Map<String, String>> encryptDictCombox(@Valid @NotBlank(message = "systemCode 必须填写")
                                                @RequestParam(name = "systemCode", required = true) String systemCode, @NotBlank(message = "parentCode 必须填写")
                                                @RequestParam(name = "parentCode", required = true)  String parentCode, Boolean empty) {
        return this.dictionariesElasticsearchService.dictCombox(systemCode, parentCode, empty);
    }

    /**
     *  字典代码转换为字典值
     * @param systemCode
     * @param dictCode
     * @param  parentCode
     * @return
     */
    @ApiOperation(value = "字典代码转换为字典值", notes = "适用于字典代码转换为字典值 请求示例：127.0.0.1:18080/api/v1/dict/dictName")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "version", value = "版本号", paramType = "path", required = true, dataType = "integer", defaultValue = "v1"),
            @ApiImplicitParam(name = "systemCode", value = "系统码", required = true),
            @ApiImplicitParam(name = "parentCode", value = "父级字典代码",  required = true),
            @ApiImplicitParam(name = "dictCode", value = "字典代码",  required = true)
    })
    @GetMapping(value = "dict/dictName")
    @ApiVersion(1)
    public ResultInfo dictName(@Valid @NotBlank(message = "systemCode 必须填写")
                                   @RequestParam(name = "systemCode", required = true) String systemCode,  @NotBlank(message = "parentCode 必须填写")
    @RequestParam(name = "parentCode", required = true) String parentCode,  @NotBlank(message = "dictCode 必须填写")
    @RequestParam(name = "dictCode", required = true)  String dictCode) {
        return  ResultUtil.success(this.dictionariesElasticsearchService.getDictName(systemCode, parentCode, dictCode));
    }



    /**
     *  字典 fullParentCode 父级代码 转换为字典值 map
     * @param systemCode
     * @param fullParentCode
     * @param
     * @return
     */
    @ApiOperation(value = "字典 fullParentCode 父级代码 转换为字典值 map", notes = "适用于字典 fullParentCode 父级代码 转换为字典值 map 请求示例：127.0.0.1:18080/api/v1/dict/map/dictName")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "version", value = "版本号", paramType = "path", required = true, dataType = "integer", defaultValue = "v1"),
            @ApiImplicitParam(name = "systemCode", value = "系统码", required = true),
            @ApiImplicitParam(name = "fullParentCode", value = "父级字典代码",  required = true)

    })
    @GetMapping(value = "dict/map/dictName")
    @ApiVersion(1)
    public ResultInfo getDictNameToMap(@Valid @NotBlank(message = "systemCode 必须填写")
                                       @RequestParam(name = "systemCode", required = true) String systemCode, @NotBlank(message = "fullParentCode 必须填写")
                                       @RequestParam(name = "fullParentCode", required = true) String fullParentCode) {
        return  ResultUtil.success(this.dictionariesElasticsearchService.getDictNameToMap(systemCode, fullParentCode));
    }

    /**
     *  字典 fullParentCode 父级代码 转换为字典值 map
     * @param systemCode
     * @param fullParentCodes
     * @return
     */
    @ApiOperation(value = "字典 fullParentCode 父级代码 转换为字典值 map", notes = "适用于字典 fullParentCode 父级代码 转换为字典值 map 请求示例：127.0.0.1:18080/api/v1/dict/map/dictName")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "version", value = "版本号", paramType = "path", required = true, dataType = "integer", defaultValue = "v1"),
            @ApiImplicitParam(name = "systemCode", value = "系统码", required = true),
            @ApiImplicitParam(name = "fullParentCodes", value = "父级字典代码",  required = true),
            @ApiImplicitParam(name = "dictLevel", value = "层次级别",  required = false)
    })
    @GetMapping(value = "dict/map/list/dictName")
    @ApiVersion(1)
    public ResultInfo getDictNameToMapList(@Valid @NotBlank(message = "systemCode 必须填写")
                                           @RequestParam(name = "systemCode", required = true) String systemCode,
                                           @NotBlank(message = "fullParentCodes 必须填写")
                                           @RequestParam(name = "fullParentCodes", required = true) String fullParentCodes) {
        return  ResultUtil.success(this.dictionariesElasticsearchService.getDictNameToMap(systemCode, SystemUtils.stringToList(fullParentCodes)));
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
