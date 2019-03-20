package pers.liujunyi.cloud.core.controller.file;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pers.liujunyi.cloud.common.annotation.ApiVersion;
import pers.liujunyi.cloud.common.controller.BaseController;
import pers.liujunyi.cloud.common.restful.ResultInfo;
import pers.liujunyi.cloud.core.service.file.FlieDownloadService;

import javax.servlet.http.HttpServletResponse;

/***
 * 文件名称: FlieDownloadController.java
 * 文件描述: 文件下载 Controller
 * 公 司:
 * 内容摘要:
 * 其他说明:
 * 完成日期:2019年01月19日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
@Api(tags = "文件下载相关的api")
@RestController
public class FlieDownloadController extends BaseController {
    @Autowired
    private FlieDownloadService flieDownloadService;


    /**
     * 单个文件下载
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "下载单个文件", notes = "根据文件id下载文件 请求示例：127.0.0.1:18080/api/v1/file/download/11")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "version", value = "版本号", paramType = "path", required = true, dataType = "integer", defaultValue = "v1"),
            @ApiImplicitParam(name = "id", value = "文件id", paramType = "path", required = true, dataType = "integer")
    })
    @GetMapping(value = "file/download/{id}")
    @ApiVersion(1)
    public ResultInfo downloadFile(@RequestParam(name = "id", required = true) @PathVariable(value = "id") Long id, HttpServletResponse response) {
        return this.flieDownloadService.downloadFile(id, response);
    }
}
