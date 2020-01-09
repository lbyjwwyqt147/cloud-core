package pers.liujunyi.cloud.core.service.oss;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 阿里云OSS 属性
 * @author ljy
 */
@Data
@Component
public class OssProperties {

    /**  阿里云API的内或外网域名 */
    @Value("${aliyun.oss.endpoint}")
    private  String endpoint;
    /**  阿里云API的密钥Access Key ID */
    @Value("${aliyun.oss.accessKeyId}")
    private  String accessKeyId;
    /**  阿里云API的密钥Access Key Secret */
    @Value("${aliyun.oss.accessKeySecret}")
    private  String accessKeySecret;
    /**  阿里云API的bucket名称 主目录*/
    @Value("${aliyun.oss.bucketName}")
    public  String bucketName;
}
