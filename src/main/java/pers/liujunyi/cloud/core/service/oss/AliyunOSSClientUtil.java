package pers.liujunyi.cloud.core.service.oss;


import com.aliyun.oss.HttpMethod;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import pers.liujunyi.cloud.core.domain.file.FileDataDto;

import javax.imageio.stream.FileImageInputStream;
import java.io.*;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/***
 * 文件名称: AliyunOSSClientUtil.java
 * 文件描述: 上传文件到 阿里云 oss
 * 公 司:
 * 内容摘要:
 * 其他说明:
 * 完成日期:2019年05月05日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
@Log4j2
@Component
public class AliyunOSSClientUtil {


    public static final String FORMAT = new SimpleDateFormat("yyyyMMdd").format(new Date());
    public static final String FORMATS = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());

    @Autowired
    private OssProperties ossProperties;

    private OSS ossClient = null;

    public AliyunOSSClientUtil() {

    }


    /**
     * 获取阿里云OSS客户端对象
     *
     * @return ossClient
     */
    public OSS getOssClient() {
        ossClient = new OSSClientBuilder().build(ossProperties.getEndpoint(), ossProperties.getAccessKeyId(), ossProperties.getAccessKeySecret());
        return ossClient;
    }

    /**
     * 销毁
     */
    public void destory() {
        ossClient.shutdown();
    }


    /**
     * 创建存储空间
     *
     * @param bucketName     存储空间名称
     * @return
     */
    public void createBucketName(String bucketName) {
        // 检测存储空间是否存在
        if (!ossClient.doesBucketExist(bucketName)) {
            // 创建存储空间
            ossClient.createBucket(bucketName);
            CreateBucketRequest createBucketRequest = new CreateBucketRequest(bucketName);
            createBucketRequest.setCannedACL(CannedAccessControlList.PublicRead);
            ossClient.createBucket(createBucketRequest);
            log.info("阿里云 OSS : 创建存储空间成功.");
        }
    }

    /**
     * 删除存储空间buckName
     *
     * @param bucketName 存储空间
     */
    public void deleteBucket(String bucketName) {
        this.getOssClient();
        ossClient.deleteBucket(bucketName);
        log.info("阿里云 OSS : 删除" + bucketName + "Bucket成功.");
    }

    /**
     * 创建文件夹
     *
     * @param bucketName 存储空间
     * @param folder 文件夹名称
     * @return 文件夹名
     */
    public  String createFolder(String bucketName, String folder) {
        this.getOssClient();
        // 文件夹名
        final String keySuffixWithSlash = folder;
        // 判断文件夹是否存在，不存在则创建
        if (!ossClient.doesObjectExist(bucketName, keySuffixWithSlash)) {
            // 创建文件夹
            ossClient.putObject(bucketName, keySuffixWithSlash, new ByteArrayInputStream(new byte[0]));
            log.info("阿里云 OSS : 创建文件夹成功.");
            // 得到文件夹名
            OSSObject object = ossClient.getObject(bucketName, keySuffixWithSlash);
            String fileDir = object.getKey();
            ossClient.shutdown();
            return fileDir;
        }
        return keySuffixWithSlash;
    }

    /**
     * 判断文件是否存在

     * @param key 文件所在的具体位置  例如:abc/efg/123.jpg
     */
    public Boolean objectExist(String key) {
        boolean exist = ossClient.doesObjectExist(ossProperties.getBucketName(), key);
        return exist;
    }

    /**
     * 根据key删除OSS服务器上的文件

     * @param key 文件所在的具体位置  例如:abc/efg/123.jpg
     */
    public void deleteFile(String key) {
        this.getOssClient();
        if (!objectExist(key)) {
            log.info("文件在阿里云 OSS上不存在,filePath={}", key);
        } else {
            // 删除文件。如需删除文件夹，请将ObjectName设置为对应的文件夹名称。如果文件夹非空，则需要将文件夹下的所有object删除后才能删除该文件夹。
            ossClient.deleteObject(ossProperties.getBucketName(), key);
            ossClient.shutdown();
            log.info("阿里云 OSS : 删除 " + ossProperties.getBucketName() + " 下的文件 " + key + " 成功.");
        }
    }

    /**
     * 批量删除
     * @param keys 文件所在的具体位置  例如:abc/efg/123.jpg
     */
    public  void deleteFile(List<String> keys) {
        this.getOssClient();
        DeleteObjectsResult deleteObjectsResult = ossClient.deleteObjects(new DeleteObjectsRequest(ossProperties.getBucketName()).withKeys(keys));
        List<String> deletedObjects = deleteObjectsResult.getDeletedObjects();
        ossClient.shutdown();
        log.info("阿里云 OSS : 批量删除文件成功.");
    }





    /**
     *
     * 替换文件:删除原文件并上传新文件，文件名和地址同时替换
     *         解决原数据缓存问题，只要更新了地址，就能重新加载数据)
     * @param file
     * @param filePath 文件所在位置
     * @param fileData
     * @return String 文件地址
     */
    public  AliyunOSSDataVo replaceFile(MultipartFile file, String filePath, FileDataDto fileData){
        //先删除原文件
        deleteFile(filePath);
        return uploadFile(file, fileData);
    }

    /**
     * 上传文件至OSS 文件流方式
     *
     * @param file     上传文件
     * @param fileData 文件属性
     * @return String 返回的唯一MD5数字签名
     */
    public AliyunOSSDataVo uploadFile(MultipartFile file,  FileDataDto fileData) {
        // 创建OSSClient实例
        this.getOssClient();
        AliyunOSSDataVo ossData = null;
        try {
            String bucketName = ossProperties.getBucketName();
            this.createBucketName(bucketName);
            // 以输入流的形式上传文件
            InputStream is = file.getInputStream();
            //文件名称
            String fileName = file.getOriginalFilename();
            // 设置文件路径和名称
            String curFilePath = fileData.getFolder() + "/" + fileData.getFilePath();
            log.info("文件上传路径：" + curFilePath);
            // 文件大小
            long fileSize = file.getSize();
            // 上传文件 (上传文件流的形式)
            ObjectMetadata metadata = this.buildMetadata(fileSize, fileName);
            PutObjectResult  putResult = ossClient.putObject(bucketName, curFilePath, is, metadata);
            // 设置权限(公开读)
            ossClient.setBucketAcl(bucketName, CannedAccessControlList.PublicRead);
            ossData = this.buildOssData(putResult, curFilePath, fileName);
            ossData.setFileSize(fileSize);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("上传阿里云OSS服务器异常." + e.getMessage(), e);
        }
        return ossData;
    }


    /**
     * 上传文件至OSS Byte数组 方式
     * @param content   文件 byte 数组
     * @param fileSuffix 文件扩展名
     * @param fileData  文件属性
     * @return
     */
     public  AliyunOSSDataVo uploadByte(byte[] content, String fileSuffix, FileDataDto fileData ) {
        this.getOssClient();
        AliyunOSSDataVo ossData = null;
        try {
            String bucketName = ossProperties.getBucketName();
            this.createBucketName(bucketName);
            // 文件名
            String timefile = FORMATS;
            // 后缀扩展名
            String fileName = fileSuffix;
            fileName = timefile + fileName;
            String curFilePath = fileData.getFolder() + "/" + fileName;
            log.info("文件上传路径：" + curFilePath);
            long fileSize = (long) content.length;
            ObjectMetadata metadata = this.buildMetadata(fileSize, fileName);
            PutObjectResult putResult = ossClient.putObject(bucketName, curFilePath, new ByteArrayInputStream(content),
                    metadata);
            ossData = this.buildOssData(putResult, curFilePath, fileName);
            ossData.setFileSize(fileSize);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("上传阿里云OSS服务器异常." + e.getMessage(), e);
        }
        return ossData;
     }

    /**
     * 创建上传Object的Metadata
     * @param fileSize
     * @param fileName
     * @return
     */
     private ObjectMetadata buildMetadata(Long fileSize, String fileName) {
         ObjectMetadata metadata = new ObjectMetadata();
         metadata.setContentLength(fileSize);
         // 指定该Object被下载时的网页的缓存行为
         metadata.setCacheControl("no-cache");
         // 指定该Object下设置Header
         metadata.setHeader("Pragma", "no-cache");
         // 指定该Object被下载时的内容编码格式
         metadata.setContentEncoding("utf-8");
         // 文件的MIME，定义文件的类型及网页编码，决定浏览器将以什么形式、什么编码读取文件。如果用户没有指定则根据Key或文件名的扩展名生成，
         // 如果没有扩展名则填默认值application/octet-stream
         metadata.setContentType(getContentType(fileName));
         // 指定该Object被下载时的名称（指示MINME用户代理如何显示附加的文件，打开或下载，及文件名称）
         metadata.setContentDisposition("filename/filesize=" + fileName);
         return metadata;
     }

    /**
     *
     * @param putResult
     * @param filePath
     * @param fileName
     * @return
     */
     private AliyunOSSDataVo buildOssData(PutObjectResult putResult, String filePath, String fileName) {
         AliyunOSSDataVo ossData = new AliyunOSSDataVo();
         // 解析结果 上传后的文件MD5数字唯一签名
         String md5 = putResult.getETag();
         ossData.setMd5(md5);
         ossData.setFilePath(filePath);
         String fileUrl = "https://" + ossProperties.getBucketName() + "." + ossProperties.getEndpoint() + "/" + filePath;
         ossData.setUrl(fileUrl);
         ossClient.shutdown();
         return ossData;
     }


    /**
     * 文件转化为byte数组
     * @param filePath 文件路径
     * @return
     */
    public  byte[] fileToByte(String filePath) {
        byte[] data = null;
        FileImageInputStream input = null;
        try {
            input = new FileImageInputStream(new File(filePath));
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            byte[] buf = new byte[2048];
            int numBytesRead = 0;
            while ((numBytesRead = input.read(buf)) != -1) {
                output.write(buf, 0, numBytesRead);
            }
            data = output.toByteArray();
            output.close();
            input.close();
        } catch (FileNotFoundException ex1) {
            ex1.printStackTrace();
        } catch (IOException ex1) {
            ex1.printStackTrace();
        }
        return data;
    }


    /**
     * 获得url链接
     *
     * @param fileName
     * @return
     */
    public  String getFileUrl(String fileName) {
        this.getOssClient();
        // 设置URL过期时间为10年 3600L* 1000*24*365*10
        Date expiration = new Date(System.currentTimeMillis() + 3600L * 1000 * 24 * 365 * 10);
        GeneratePresignedUrlRequest req = new GeneratePresignedUrlRequest(ossProperties.getBucketName(), fileName, HttpMethod.GET);
        req.setExpiration(expiration);
        // 生成URL
        URL url = ossClient.generatePresignedUrl(req);
        // 设置权限(公开读)
        ossClient.setBucketAcl(ossProperties.getBucketName(), CannedAccessControlList.PublicRead);
        if (url != null) {
            return url.toString();
        }
        return "获网址路径出错";
    }



    /**
     * 通过文件名判断并获取OSS服务文件上传时文件的contentType
     *
     * @param fileName
     *            文件名
     * @return 文件的contentType
     */
    public  String getContentType(String fileName) {
        // 文件的后缀名
        String fileExtension = fileName.substring(fileName.lastIndexOf("."));
        if (fileExtension.equalsIgnoreCase(".bmp")) {
            return "image/bmp";
        }
        if (fileExtension.equalsIgnoreCase(".gif")) {
            return "image/gif";
        }
        if (fileExtension.equalsIgnoreCase(".jpeg") ||
                fileExtension.equalsIgnoreCase(".jpg") ||
                fileExtension.equalsIgnoreCase(".png")) {
            return "image/jpg";
        }
        if (fileExtension.equalsIgnoreCase(".html")) {
            return "text/html";
        }
        if (fileExtension.equalsIgnoreCase(".txt")) {
            return "text/plain";
        }
        if (fileExtension.equalsIgnoreCase(".vsd")) {
            return "application/vnd.visio";
        }
        if (fileExtension.equalsIgnoreCase(".pptx") ||
                fileExtension.equalsIgnoreCase(".ppt")) {
            return "application/vnd.ms-powerpoint";
        }
        if (fileExtension.equalsIgnoreCase(".docx") ||
                fileExtension.equalsIgnoreCase(".doc")) {
            return "application/msword";
        }
        if (fileExtension.equalsIgnoreCase(".xml")) {
            return "text/xml";
        }
        if (fileExtension.equalsIgnoreCase(".mp4")) {
            return "video/mp4";
        }
        if (fileExtension.equalsIgnoreCase(".avi")) {
            return "video/avi";
        }
        if (fileExtension.equalsIgnoreCase(".zip")) {
            return "application/zip";
        }
        return "image/jpg";
    }

}
