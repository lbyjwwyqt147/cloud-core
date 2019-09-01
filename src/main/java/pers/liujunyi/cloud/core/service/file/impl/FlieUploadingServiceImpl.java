package pers.liujunyi.cloud.core.service.file.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;
import pers.liujunyi.cloud.common.exception.ErrorCodeEnum;
import pers.liujunyi.cloud.common.restful.ResultInfo;
import pers.liujunyi.cloud.common.restful.ResultUtil;
import pers.liujunyi.cloud.common.util.DozerBeanMapperUtil;
import pers.liujunyi.cloud.common.vo.file.FileDataVo;
import pers.liujunyi.cloud.core.domain.file.FileDataDto;
import pers.liujunyi.cloud.core.entity.file.FileManagement;
import pers.liujunyi.cloud.core.service.file.FileManagementService;
import pers.liujunyi.cloud.core.service.file.FlieUploadingService;
import pers.liujunyi.cloud.core.service.oss.AliyunOSSClientUtil;
import pers.liujunyi.cloud.core.service.oss.AliyunOSSDataVo;
import pers.liujunyi.cloud.core.util.FileEnum;
import pers.liujunyi.cloud.core.util.FileUtil;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

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
    @Autowired
    private AliyunOSSClientUtil aliyunOSSClientUtil;

    /**  阿里云API的bucket名称 主目录 */
    @Value("${aliyun.oss.bucketName}")
    public  String bucketName;

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

    private static Map<String, String> explain = null;

    static {
        explain = new ConcurrentHashMap<>();
        explain.put("id", "文件id");
        explain.put("fileInitialName", "文件上传前原始名称");
        explain.put("fileName", "文件上传后的名称");
        explain.put("fileSignature", "文件上传后的唯一签名");
        explain.put("sequence", "顺序");
        explain.put("fileCallAddress", "文件访问http路径");
        explain.put("fileSize", "文件大小");
        explain.put("fileCategory", "文件分类 0：图片 1：文档  2：视频  5：其他");
    }


    @Override
    public ResultInfo startUploading(List<MultipartFile> files, FileDataDto fileData) {
        //判断文件是否为空
        if (files.isEmpty()) {
            return ResultUtil.params("缺少上传文件");
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
            String fileDirectory = this.getFileDirectory(fileEnum.getName(), fileData);
            //文件目录
            String path = filePath + this.getPath(fileDirectory, newFileName);
            //创建文件路径
            File dataFile = new File(path);
            //判断文件父目录是否存在 如果不存在则创建
            if (!dataFile.getParentFile().exists()) {
                dataFile.getParentFile().mkdirs();
            }
            try {
                //上传文件到本地磁盘
                file.transferTo(dataFile);
                // 请求访问文件的 url 地址 url="http://域名/项目名/images/所属系统编码/所属系统业务编码/年份/月份/日/上传的文件名/"
                // http://localhost:18080/images/1001/10/2019/1/20/48c634fc413c4d07b303926130fd510b.jpg
                String requestUrl = this.getFileRequestUrl(fileDirectory, newFileName);
                // 组织文件数据入库
                FileManagement fileRecord = DozerBeanMapperUtil.copyProperties(fileData, FileManagement.class);
                fileRecord.setUploadTime(new Date());
                fileRecord.setFileInitialName(fileName);
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
            resultInfo = ResultUtil.info(false, fileDataVos);
        } else {
            resultInfo = ResultUtil.info(true, fileDataVos);
            resultInfo.setExtend(explain);
        }
        return resultInfo;
    }

    @Override
    public ResultInfo aliyunBatchUploadFile(List<MultipartFile> files, FileDataDto fileData) {
        //判断文件是否为空
        if (files.isEmpty()) {
            return ResultUtil.params("缺少上传文件");
        }
        List<FileManagement> recordList = new CopyOnWriteArrayList<>();
        AtomicInteger seq  = new AtomicInteger(0);
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
            //文件位置
            String fliePath = this.getPath(fileDirectory, newFileName);
            fileData.setFilePath(fliePath);
            //上传文件到阿里云 OSS
            AliyunOSSDataVo ossData = aliyunOSSClientUtil.uploadFile(file, bucketName, fileData );
            if (ossData != null) {
                // 组织文件数据入库
                FileManagement fileRecord = DozerBeanMapperUtil.copyProperties(fileData, FileManagement.class);
                fileRecord.setUploadTime(new Date());
                fileRecord.setFileInitialName(fileName);
                fileRecord.setFileName(newFileName);
                fileRecord.setFilePath(ossData.getFilePath());
                fileRecord.setFileCallAddress(ossData.getUrl());
                fileRecord.setFileCategory(fileEnum.getCode());
                fileRecord.setFileSize(FileUtil.getFileSize(ossData.getFileSize()));
                fileRecord.setFileDirectory(fliePath);
                fileRecord.setFileSuffix(suffixName);
                fileRecord.setPriority((byte) seq.get());
                recordList.add(fileRecord);
                seq.set(1);
            }
        }
        ResultInfo resultInfo = null;
        List<FileDataVo> fileDataVos = this.fileManagementService.saveFileData(recordList);
        if (CollectionUtils.isEmpty(fileDataVos)) {
            resultInfo = ResultUtil.info(false, fileDataVos);
        } else {
            resultInfo = ResultUtil.info(true, fileDataVos);
            resultInfo.setExtend(explain);
        }
        return resultInfo;
    }


    /**
     * 文件路径
     *
     * @param fileDirectory 文件jia夹
     * @param fileName 文件名
     * @return
     */
    private String getPath(String fileDirectory, String fileName) {
        StringBuffer path = new StringBuffer(fileDirectory);
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
     * @return images/10001/10/2019/1/20/5119bc8336ee4bb2bbc2b523e88db745.jpg
     */
    private String getFileDirectory(String prefix, FileDataDto fileData) {
        StringBuffer filePatchBuffer = new StringBuffer(prefix);
        if (StringUtils.isNotBlank(fileData.getSystemCode())) {
            filePatchBuffer.append("/").append(fileData.getSystemCode());
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

    /**
     * 组织 文件存放路径
     *
     * @param prefix
     * @param fileData
     * @return document/images/10001/10/2019/1/20/5119bc8336ee4bb2bbc2b523e88db745.jpg
     */
    private String getFileDirectoryNew(String prefix, FileDataDto fileData) {
        StringBuffer filePatchBuffer = new StringBuffer("document");
        filePatchBuffer.append("/").append(prefix);
        if (StringUtils.isNotBlank(fileData.getSystemCode())) {
            filePatchBuffer.append("/").append(fileData.getSystemCode());
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
