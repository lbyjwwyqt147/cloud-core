package pers.liujunyi.cloud.core.controller.file;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import pers.liujunyi.cloud.common.annotation.ApiVersion;
import pers.liujunyi.cloud.common.controller.BaseController;
import pers.liujunyi.cloud.common.restful.ResultInfo;
import pers.liujunyi.cloud.core.domain.file.FileDataDto;
import pers.liujunyi.cloud.core.service.file.FlieUploadingService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;


/***
 * 文件名称: UploadFileController.java
 * 文件描述: 文件上传 Controller
 * 公 司:
 * 内容摘要:
 * 其他说明:
 * 完成日期:2019年01月17日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
@Api(tags = "文件上传相关API")
@RestController
public class FlieUploadingController extends BaseController {
    @Autowired
    private FlieUploadingService flieUploadingService;

    /**
     * 单文件上传
     *
     * @param file
     * @param data
     * @return
     */
    @ApiOperation(value = "单个文件上传")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "version", value = "版本号", paramType = "path",  dataType = "String", defaultValue = "v1"),
            @ApiImplicitParam(name = "file", value = "文件", paramType = "query", required = true, dataType = "MultipartFile")
    })
    @PostMapping(value = "verify/file/upload/single", produces = "application/json;charset=UTF-8")
    @ApiVersion(1)
    public ResultInfo singleUploadFile(@RequestParam("file") MultipartFile file, @Valid FileDataDto data) {
        List<MultipartFile> files = new ArrayList<>();
        files.add(file);
        return this.startUploading(files, data);
    }


    /**
     * 阿里云oss批量上传文件
     *
     * @param request
     * @param data
     * @return
     */
    @ApiOperation(value = "阿里云oss批量上传文件")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "version", value = "版本号", paramType = "path",  dataType = "String", defaultValue = "v1"),
            @ApiImplicitParam(name = "file", value = "文件", paramType = "query", required = true, dataType = "MultipartFile")
    })
    @PostMapping(value = "verify/file/oss/upload/batch", produces = "application/json;charset=UTF-8")
    @ApiVersion(1)
    public ResultInfo aliyunBatchUploadFile(HttpServletRequest request, @Valid FileDataDto data) {
        List<MultipartFile> files = ((MultipartHttpServletRequest) request).getFiles("file");
        return this.flieUploadingService.aliyunBatchUploadFile(files, data);
    }


    /**
     * 批量上传文件
     *
     * @param request
     * @param data
     * @return
     */
    @ApiOperation(value = "多个文件上传")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "version", value = "版本号", paramType = "path",  dataType = "String", defaultValue = "v1"),
            @ApiImplicitParam(name = "file", value = "文件", paramType = "query", required = true, dataType = "MultipartFile")
    })
    @PostMapping(value = "verify/file/upload/batch", produces = "application/json;charset=UTF-8")
    @ApiVersion(1)
    public ResultInfo batchUploadFile(HttpServletRequest request, @Valid FileDataDto data) {
        List<MultipartFile> files = ((MultipartHttpServletRequest) request).getFiles("file");
        return this.startUploading(files, data);
    }


    /**
     * 开始上传文件
     *
     * @param files
     * @param data
     * @return
     */
    private ResultInfo startUploading(List<MultipartFile> files, FileDataDto data) {
        return this.flieUploadingService.startUploading(files, data);
    }


}
