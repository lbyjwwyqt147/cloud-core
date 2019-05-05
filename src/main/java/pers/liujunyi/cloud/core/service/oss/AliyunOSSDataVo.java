package pers.liujunyi.cloud.core.service.oss;

import lombok.Data;

import java.io.Serializable;

/***
 * 文件名称: AliyunOSSDataVo.java
 * 文件描述: 阿里云 oss 返回数据
 * 公 司:
 * 内容摘要:
 * 其他说明:
 * 完成日期:2019年05月05日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
@Data
public class AliyunOSSDataVo implements Serializable {

    private static final long serialVersionUID = 8435970307882055042L;
    /** 上传后的文件MD5数字唯一签名 */
    private String md5;
    /** 访问网址路径 */
    private String url;
    /** 文件路径  */
    private String filePath;
    /** 文件大小  */
    private Long fileSize;

}
