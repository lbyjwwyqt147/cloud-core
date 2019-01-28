package pers.liujunyi.cloud.core.service.file.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;
import pers.liujunyi.cloud.core.domain.file.FileDataDto;
import pers.liujunyi.cloud.core.entity.file.FileManagement;
import pers.liujunyi.cloud.core.service.file.FileManagementService;
import pers.liujunyi.cloud.core.service.file.FlieUploadingService;
import pers.liujunyi.cloud.core.util.FileEnum;
import pers.liujunyi.cloud.core.util.FileUtil;
import pers.liujunyi.common.exception.ErrorCodeEnum;
import pers.liujunyi.common.restful.ResultInfo;
import pers.liujunyi.common.restful.ResultUtil;
import pers.liujunyi.common.util.DozerBeanMapperUtil;
import pers.liujunyi.common.vo.file.FileDataVo;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/***
 * 文件名称: FlieUploadingServiceImpl.java
 * 文件描述: 文件上传 service  实现
 * 公 司: 积微物联
 * 内容摘要:
 * 其他说明:
 * 完成日期:2018年01月17日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
@Service
public class FlieUploadingServiceImpl implements FlieUploadingService {

    @Autowired
    private FileManagementService fileManagementService;

    private static Map<String, String> explain = null;

    static {
        explain = new ConcurrentHashMap<>();
        explain.put("id", "文件id");
        explain.put("fileInitialName", "文件上传前原始名称");
        explain.put("fileName", "文件上传后的名称");
        explain.put("fileCallAddress", "文件访问http路径");
        explain.put("fileSize", "文件大小");
    }

    /**
     * 文件保存路径
     */
    @Value("${data.file.dir}")
    private String filePath;

    /**
     * 系统访问url
     */
    @Value("${data.hostName}")
    private String hostName;


    @Override
    public ResultInfo startUploading(List<MultipartFile> files, FileDataDto fileData) {
        //判断文件是否为空
        if (files.isEmpty()) {
            return ResultUtil.resultInfo(ErrorCodeEnum.PARAMS.getCode(), "缺少上传文件", null, false);
        }
        List<FileManagement> recordList = new CopyOnWriteArrayList<>();
        for (MultipartFile file : files) {
            // 获取文件名
            String fileName = file.getOriginalFilename();
            //得到文件后缀名
            String suffixName = FileUtil.getSuffixName(fileName).toLowerCase();
            String newFileName = fileName;
            if (fileData.getRename()) {
                //重命名文件名(避免文件名重复)
                newFileName = FileUtil.getNewFileName(suffixName);
            }
            //得到文件所属分类(文档、图片、视频,ZIP)
            FileEnum fileEnum = FileUtil.getFileTypeEnum(suffixName);
            String fileDirectory = this.getFileDirectoryNew(fileEnum.getName(), fileData);
            //文件目录
            String path = this.getPath(fileDirectory, newFileName);
            //创建文件路径
            File dataFile = new File(path);
            //判断文件父目录是否存在 如果不存在则创建
            if (!dataFile.getParentFile().exists()) {
                dataFile.getParentFile().mkdirs();
            }
            try {
                //上传文件到本地磁盘
                file.transferTo(dataFile);
                // 请求访问文件的 url 地址 url="http://域名/项目名/images/所属系统编码/所属系统用户ID/所属系统业务编码/年份/月份/日/上传的文件名/"
                // http://localhost:18080/images/1001/10/11111111/2019/1/20/48c634fc413c4d07b303926130fd510b.jpg
                String requestUrl = this.getFileRequestUrl(fileDirectory, newFileName);
                // 组织文件数据入库
                FileManagement fileRecord = DozerBeanMapperUtil.copyProperties(fileData, FileManagement.class);
                fileRecord.setUploadTime(new Date());
                fileRecord.setFileInitialName(newFileName);
                fileRecord.setFileName(newFileName);
                fileRecord.setFilePath(path);
                fileRecord.setFileCallAddress(requestUrl);
                fileRecord.setFileCategory(fileEnum.getCode());
                fileRecord.setFileSize(FileUtil.getFileSize(dataFile.length()));
                fileRecord.setFileDirectory(FileUtil.convertFilePath(filePath + "/" + fileDirectory));
                fileRecord.setFileSuffix(suffixName);
                recordList.add(fileRecord);
            } catch (IOException e) {
                e.printStackTrace();
                return ResultUtil.error(ErrorCodeEnum.ERROR.getCode(), "上传文件出现错误.");
            }
        }
        ResultInfo resultInfo = null;
        List<FileDataVo> fileDataVos = this.fileManagementService.saveFileData(recordList);
        if (CollectionUtils.isEmpty(fileDataVos)) {
            resultInfo = ResultUtil.restfulInfo(false, fileDataVos);
        } else {
            resultInfo = ResultUtil.restfulInfo(true, fileDataVos);
            resultInfo.setExtend(explain);
        }
        return resultInfo;
    }


    /**
     * 文件路径
     *
     * @param fileType 文件类型
     * @param fileName 文件名
     * @return
     */
    private String getPath(String fileType, String fileName) {
        StringBuffer path = new StringBuffer(filePath);
        path.append("/").append(fileType);
        path.append("/").append(fileName);
        return FileUtil.convertFilePath(path.toString());
    }

    /**
     * 得到文件请求的url 地址
     *
     * @param fileType 文件类型
     * @param fileName 文件名称
     * @return
     */
    private String getFileRequestUrl(String fileType, String fileName) {
        StringBuffer requestUrl = new StringBuffer(hostName);
        requestUrl.append(fileType).append("/").append(fileName);
        return requestUrl.toString();
    }

    /**
     * 组织 文件存放路径
     *
     * @param prefix
     * @param fileData
     * @return images/10001/1/10/2019/1/20/5119bc8336ee4bb2bbc2b523e88db745.jpg
     */
    private String getFileDirectoryNew(String prefix, FileDataDto fileData) {
        StringBuffer filePatchBuffer = new StringBuffer(prefix);
        if (StringUtils.isNotBlank(fileData.getSystemCode())) {
            filePatchBuffer.append("/").append(fileData.getSystemCode());
        }
        if (fileData.getUploaderId() != null) {
            filePatchBuffer.append("/").append(fileData.getUploaderId());
        }
        if (StringUtils.isNotBlank(fileData.getBusinessCode())) {
            filePatchBuffer.append("/").append(fileData.getBusinessCode());
        }
        LocalDate localDate = LocalDate.now();
        filePatchBuffer.append("/").append(localDate.getYear());
        filePatchBuffer.append("/").append(localDate.getMonthValue());
        filePatchBuffer.append("/").append(localDate.getDayOfMonth());
        return filePatchBuffer.toString();
    }
}
