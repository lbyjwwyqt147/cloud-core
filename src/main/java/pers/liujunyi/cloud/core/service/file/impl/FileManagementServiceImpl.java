package pers.liujunyi.cloud.core.service.file.impl;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import pers.liujunyi.cloud.common.repository.jpa.BaseRepository;
import pers.liujunyi.cloud.common.service.impl.BaseServiceImpl;
import pers.liujunyi.cloud.common.util.ThreadPoolExecutorFactory;
import pers.liujunyi.cloud.common.vo.file.FileDataVo;
import pers.liujunyi.cloud.core.entity.file.FileManagement;
import pers.liujunyi.cloud.core.repository.jpa.file.FileManagementRepository;
import pers.liujunyi.cloud.core.service.file.FileManagementService;
import pers.liujunyi.cloud.core.service.oss.AliyunOSSClientUtil;
import pers.liujunyi.cloud.core.util.FileUtil;

import java.util.Date;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ThreadPoolExecutor;

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
@Log4j2
@Service
public class FileManagementServiceImpl extends BaseServiceImpl<FileManagement, Long> implements FileManagementService {
    private ThreadPoolExecutor threadPoolExecutor = ThreadPoolExecutorFactory.getThreadPoolExecutor();

    @Autowired
    private FileManagementRepository fileManagementRepository;
    @Autowired
    private AliyunOSSClientUtil aliyunOSSClientUtil;

    public FileManagementServiceImpl(BaseRepository<FileManagement, Long> baseRepository) {
        super(baseRepository);
    }

    @Override
    public Long insert(FileManagement record) {
        Long id = null;
        record.setUploadTime(new Date());
        FileManagement saveObj = this.fileManagementRepository.save(record);
        if (saveObj != null) {
            id = saveObj.getId();
        }
        return id;
    }

    @Override
    public List<FileManagement> insertBatch(List<FileManagement> record) {
        return this.fileManagementRepository.saveAll(record);
    }

    @Override
    public FileManagement saveRecord(FileManagement record) {
        return this.fileManagementRepository.save(record);
    }

    @Override
    public List<FileDataVo> saveFileData(List<FileManagement> records) {
        List<FileManagement> fileRecordList = this.fileManagementRepository.saveAll(records);
        List<FileDataVo> fileDataList = new CopyOnWriteArrayList<>();
        if (!CollectionUtils.isEmpty(fileRecordList)) {
            for (FileManagement fileRecord : fileRecordList) {
                FileDataVo fileData = new FileDataVo();
                fileData.setId(fileRecord.getId());
                fileData.setFileInitialName(fileRecord.getFileInitialName());
                fileData.setFileName(fileRecord.getFileName());
                fileData.setFileSize(fileRecord.getFileSize());
                fileData.setFileCallAddress(fileRecord.getFileCallAddress());
                fileData.setFileSignature(fileRecord.getFileSignature());
                fileData.setSequence(fileRecord.getPriority());
                fileData.setFileCategory(fileRecord.getFileCategory());
                fileData.setFileSuffix(fileRecord.getFileSuffix());
                fileDataList.add(fileData);
            }
        } else {
            // 如果入库失败就删除上传文件
            this.deleteFile(records);
        }
        return fileDataList;
    }

    @Override
    public Boolean deleteAliyunAllByIdIn(List<Long> ids) {
        List<FileManagement> recordList = this.findByIdIn(ids);
        // 删除数据库纪录
        this.fileManagementRepository.deleteInBatch(recordList);
        // 删除阿里云oss 上的文件
        this.deleteAliyunFile(recordList);
        return true;
    }

    @Override
    public Boolean deleteAliyunAllById(Long id) {
        FileManagement record = this.fileManagementRepository.getOne(id);
        // 删除数据库纪录
        this.fileManagementRepository.delete(record);
        // 删除阿里云oss 上的文件
        aliyunOSSClientUtil.deleteFile(record.getFileCallAddress());
        return true;
    }

    @Override
    public Boolean deleteAllByIdIn(List<Long> ids) {
        List<FileManagement> recordList = this.findByIdIn(ids);
        // 删除数据库纪录
        this.fileManagementRepository.deleteInBatch(recordList);
        // 删除本地文件
        this.deleteFile(recordList);
        return true;
    }

    @Override
    public Boolean deleteById(Long id) {
        FileManagement record = this.fileManagementRepository.getOne(id);
        // 删除数据库纪录
        this.fileManagementRepository.delete(record);
        // 删除本地文件
        FileUtil.delete(record.getFilePath());
        return true;
    }


    /**
     * 删除阿里云oss 上的文件
     *
     * @param records
     */
    private void deleteAliyunFile(List<FileManagement> records) {
        // 删除阿里云oss 上的文件
        threadPoolExecutor.execute(() -> {
            for (FileManagement fileRecord : records) {
                aliyunOSSClientUtil.deleteFile(fileRecord.getFileCallAddress());
            }
        });
    }

    /**
     * 删除 磁盘上的文件
     *
     * @param records
     */
    private void deleteFile(List<FileManagement> records) {
        threadPoolExecutor.execute(() -> {
            for (FileManagement fileRecord : records) {
                FileUtil.delete(fileRecord.getFilePath());
            }
        });
    }
}
