package pers.liujunyi.cloud.core.service.oss;


import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import pers.liujunyi.cloud.core.domain.file.FileDataDto;

import javax.imageio.stream.FileImageInputStream;
import java.io.*;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

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
@Slf4j
@Component
public class AliyunOSSClientUtil {

    /**  阿里云API的内或外网域名 */
    @Value("${aliyun.oss.endpoint}")
    private  String ENDPOINT;
    /**  阿里云API的密钥Access Key ID */
    @Value("${aliyun.oss.accessKeyId}")
    private  String ACCESS_KEY_ID;
    /**  阿里云API的密钥Access Key Secret */
    @Value("${aliyun.oss.accessKeySecret}")
    private  String ACCESS_KEY_SECRET;
    /**  阿里云API的bucket名称 主目录*/
    @Value("${aliyun.oss.bucketName}")
    public  String BACKET_NAME;
    public static final String FORMAT = new SimpleDateFormat("yyyyMMdd").format(new Date());
    public static final String FORMATS = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());


    private OSSClient ossClient;

    public AliyunOSSClientUtil() {
        ossClient = new OSSClient(ENDPOINT, ACCESS_KEY_ID, ACCESS_KEY_SECRET);
    }

    /**
     * 获取阿里云OSS客户端对象
     *
     * @return ossClient
     */
    public  OSSClient getOSSClient() {
        return new OSSClient(ENDPOINT, ACCESS_KEY_ID, ACCESS_KEY_SECRET);
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
     * @param bucketName     存储空间
     * @return
     */
    public String createBucketName(String bucketName) {
        // 存储空间
        final String bucketNames = bucketName;
        if (!ossClient.doesBucketExist(bucketName)) {
            // 创建存储空间
            Bucket bucket = ossClient.createBucket(bucketName);
            log.info("阿里云 OSS : 创建存储空间成功.");
            return bucket.getName();
        }
        return bucketNames;
    }

    /**
     * 删除存储空间buckName
     *
     * @param bucketName 存储空间
     */
    public void deleteBucket(String bucketName) {
        ossClient.deleteBucket(bucketName);
        log.info("阿里云 OSS : 删除" + bucketName + "Bucket成功.");
    }

    /**
     * 创建模拟文件夹
     *
     * @param bucketName 存储空间
     * @param folder 模拟文件夹名如"qj_nanjing/"
     * @return 文件夹名
     */
    public  String createFolder(String bucketName, String folder) {
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
            return fileDir;
        }
        return keySuffixWithSlash;
    }


    /**
     * 根据key删除OSS服务器上的文件

     * @param bucketName     存储空间
     * @param filePath 文件所在位置
     */
    public  void deleteFile(String bucketName, String filePath) {
        ossClient.deleteObject(bucketName, filePath);
        ossClient.shutdown();
        log.info("阿里云 OSS : 删除 " + bucketName + " 下的文件 " + filePath + " 成功.");
    }

    /**
     *
     * @MethodName: deleteFile
     * @Description: 单文件删除
     * @param fileUrl 需要删除的文件url
     * @return boolean 是否删除成功
     */
    public  boolean deleteFile(String fileUrl){
        //根据url获取bucketName
        String bucketName = this.getBucketName(fileUrl);
        //根据url获取fileName
        String fileName = this.getFileName(fileUrl);
        if (bucketName == null || fileName == null) {
            return false;
        }
        try {
            GenericRequest request = new DeleteObjectsRequest(bucketName).withKey(fileName);
            ossClient.deleteObject(request);
        } catch (Exception oe) {
            oe.printStackTrace();
            return false;
        } finally {
            ossClient.shutdown();
        }
        return true;
    }


    /**
     *
     * 替换文件:删除原文件并上传新文件，文件名和地址同时替换
     *         解决原数据缓存问题，只要更新了地址，就能重新加载数据)
     * @param file
     * @param bucketName 空间
     * @param filePath 文件所在位置
     * @param fileData
     * @return String 文件地址
     */
    public  AliyunOSSDataVo replaceFile(MultipartFile file, String bucketName, String filePath, FileDataDto fileData){
        //先删除原文件
        deleteFile(bucketName, filePath);
        return uploadFile(file, bucketName, fileData);
    }

    /**
     * 上传文件至OSS 文件流方式
     *
     * @param file     上传文件
     * @param bucketName 存储空间
     * @param fileData 文件属性
     * @return String 返回的唯一MD5数字签名
     */
    public AliyunOSSDataVo uploadFile(MultipartFile file, String bucketName, FileDataDto fileData) {
        AliyunOSSDataVo ossData = null;
        try {
            // 以输入流的形式上传文件
            InputStream is = file.getInputStream();
            // 文件名
            String fileName = file.getName();
            String curFilePath = fileData.getFilePath();
            log.info("上传到路径：" + curFilePath);
            // 文件大小
            long fileSize = file.getSize();
            // 创建上传Object的Metadata
            ObjectMetadata metadata = new ObjectMetadata();
            // 上传的文件的长度
            metadata.setContentLength(is.available());
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
            metadata.setContentDisposition("filename/filesize=" + fileName + "/" + fileSize + "Byte.");
            // 上传文件 (上传文件流的形式)
            PutObjectResult putResult = ossClient.putObject(bucketName, curFilePath, is, metadata);
            // 解析结果 上传后的文件MD5数字唯一签名
            String resultStr = putResult.getETag();
            ossData = new AliyunOSSDataVo();
            ossData.setMd5(resultStr);
            ossData.setFilePath(curFilePath);
            ossData.setUrl(this.getUrl(bucketName, curFilePath));
            ossData.setFileSize(fileSize);
            ossClient.shutdown();
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
     * @param bucketName 存储空间
     * @param fileData  文件属性
     * @return
     */
     public  AliyunOSSDataVo uploadByte(byte[] content, String fileSuffix, String bucketName, FileDataDto fileData ) {
        AliyunOSSDataVo ossData = null;
        try {
            String folder = fileData.getFilePath();
            // 文件名
            String timefile = FORMATS;
            // 后缀扩展名
            String fileName = fileSuffix;
            fileName = timefile + fileName;
            String curFilePath = folder + fileName;
            log.info("上传到路径：" + curFilePath);
            long fileSize = (long) content.length;
            // 创建上传Object的Metadata
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
            metadata.setContentDisposition("filename/filesize=" + fileName + "/" + fileSize + "Byte.");
            PutObjectResult putResult = ossClient.putObject(bucketName, curFilePath, new ByteArrayInputStream(content),
                    metadata);
            String resultStr = putResult.getETag();
            ossData = new AliyunOSSDataVo();
            ossData.setMd5(resultStr);
            ossData.setFilePath(curFilePath);
            ossData.setUrl(this.getUrl(bucketName, curFilePath));
            ossClient.shutdown();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("上传阿里云OSS服务器异常." + e.getMessage(), e);
        }
        return ossData;
     }


    /**
     *
     * @Description: 根据url获取bucketName
     * @param fileUrl 文件url
     * @return String bucketName
     */
    private  String getBucketName(String fileUrl){
        String http = "http://";
        String https = "https://";
        int httpIndex = fileUrl.indexOf(http);
        int httpsIndex = fileUrl.indexOf(https);
        int startIndex  = 0;
        if(httpIndex==-1){
            if(httpsIndex==-1){
                return null;
            }else{
                startIndex = httpsIndex+https.length();
            }
        }else{
            startIndex = httpIndex+http.length();
        }
        int endIndex = fileUrl.indexOf(".oss-");
        return fileUrl.substring(startIndex, endIndex);
    }

    /**
     *
     * @Description: 根据url获取fileName
     * @param fileUrl 文件url
     * @return String fileName
     */
    private  String getFileName(String fileUrl){
        String str = "aliyuncs.com/";
        int beginIndex = fileUrl.indexOf(str);
        if (beginIndex == -1) {
            return null;
        }
        return fileUrl.substring(beginIndex+str.length());
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
     * @param bucketName
     * @param fileName
     * @return
     */
    public  String getUrl(String bucketName, String fileName) {
        // 设置URL过期时间为10年 3600L* 1000*24*365*10
        Date expiration = new Date(System.currentTimeMillis() + 3600L * 1000 * 24 * 365 * 10);
        // 生成URL
        URL url = ossClient.generatePresignedUrl(bucketName, fileName, expiration);
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
        if (".bmp".equalsIgnoreCase(fileExtension)) {
            return "image/bmp";
        }
        if (".gif".equalsIgnoreCase(fileExtension)) {
            return "image/gif";
        }
        if (".jpeg".equalsIgnoreCase(fileExtension) || ".jpg".equalsIgnoreCase(fileExtension)
                || ".png".equalsIgnoreCase(fileExtension)) {
            return "image/jpeg";
        }
        if (".html".equalsIgnoreCase(fileExtension)) {
            return "text/html";
        }
        if (".txt".equalsIgnoreCase(fileExtension)) {
            return "text/plain";
        }
        if (".vsd".equalsIgnoreCase(fileExtension)) {
            return "application/vnd.visio";
        }
        if (".ppt".equalsIgnoreCase(fileExtension) || "pptx".equalsIgnoreCase(fileExtension)) {
            return "application/vnd.ms-powerpoint";
        }
        if (".doc".equalsIgnoreCase(fileExtension) || "docx".equalsIgnoreCase(fileExtension)) {
            return "application/msword";
        }
        if (".xml".equalsIgnoreCase(fileExtension)) {
            return "text/xml";
        }
        if (".mp4".equalsIgnoreCase(fileExtension)) {
            return "video/mp4";
        }
        return "image/jpeg";
    }

}
