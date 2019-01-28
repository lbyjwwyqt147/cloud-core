package pers.liujunyi.cloud.core.service.file;

import pers.liujunyi.common.service.BaseService;
import pers.liujunyi.common.vo.file.FileDataVo;
import pers.liujunyi.cloud.core.entity.file.FileManagement;

import java.util.List;

/***
 * 文件名称: FileManagementService.java
 * 文件描述: 文件管理 Service
 * 公 司:
 * 内容摘要:
 * 其他说明:
 * 完成日期:2019年01月17日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
public interface FileManagementService extends BaseService<FileManagement, Long> {

    /**
     * 新增纪录 并返回新增纪录ID
     *
     * @param record
     * @return 数据ID
     */
    Long insert(FileManagement record);

    /**
     * 批量新增纪录
     *
     * @param record
     * @return
     */
    List<FileManagement> insertBatch(List<FileManagement> record);

    /**
     * 保存记录
     *
     * @param record
     * @return
     */
    FileManagement saveRecord(FileManagement record);

    /**
     * 保存上传的文件数据
     *
     * @param records
     * @return
     */
    List<FileDataVo> saveFileData(List<FileManagement> records);
}
