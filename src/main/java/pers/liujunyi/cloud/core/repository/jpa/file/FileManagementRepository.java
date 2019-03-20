package pers.liujunyi.cloud.core.repository.jpa.file;

import org.springframework.stereotype.Repository;
import pers.liujunyi.cloud.common.repository.jpa.BaseRepository;
import pers.liujunyi.cloud.core.entity.file.FileManagement;


/***
 * 文件名称: FileManagementRepository.java
 * 文件描述: 文件管理 Repository.
 * 公 司:
 * 内容摘要:
 * 其他说明:
 * 完成日期:2019年01月17日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
@Repository
public interface FileManagementRepository extends BaseRepository<FileManagement, Long> {


}
