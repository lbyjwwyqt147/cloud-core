package pers.liujunyi.cloud.core.service.file;

import org.springframework.web.multipart.MultipartFile;
import pers.liujunyi.cloud.common.restful.ResultInfo;
import pers.liujunyi.cloud.core.domain.file.FileDataDto;

import java.util.List;

/***
 * 文件名称: FlieUploadingService.java
 * 文件描述: 文件上传 service
 * 公 司:
 * 内容摘要:
 * 其他说明:
 * 完成日期:2019年01月17日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
public interface FlieUploadingService {

    /**
     * 文件上传
     *
     * @param files    文件
     * @param fileData 文件附加数据
     * @return
     */
    ResultInfo startUploading(List<MultipartFile> files, FileDataDto fileData);

    /**
     * 阿里云oss文件上传
     *
     * @param files    文件
     * @param fileData 文件附加数据
     * @return
     */
    ResultInfo aliyunBatchUploadFile(List<MultipartFile> files, FileDataDto fileData);

}
