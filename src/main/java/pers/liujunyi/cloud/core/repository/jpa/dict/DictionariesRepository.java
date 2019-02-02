package pers.liujunyi.cloud.core.repository.jpa.dict;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import pers.liujunyi.cloud.core.entity.dict.Dictionaries;
import pers.liujunyi.common.repository.jpa.BaseRepository;

import java.util.List;

/***
 * 文件名称: DictionariesRepository.java
 * 文件描述: 数据字典 Repository
 * 公 司:
 * 内容摘要:
 * 其他说明:
 * 完成日期:2019年01月17日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
public interface DictionariesRepository extends BaseRepository<Dictionaries, Long> {

    /**
     * 根据ID 批量删除
     * @param ids
     * @return
     */
    @Transactional
    @Modifying
    @Query("delete from Dictionaries dict where dict.id in (?1)")
    int deleteAllByIdIn(List<Long> ids);

    /**
     * 修改状态
     * @param status  0:启动 1：禁用
     * @param ids
     * @return
     */
    @Transactional
    @Modifying
    @Query("update Dictionaries u set u.status = ?1 where u.id in (?2)")
    int setStatusByIds(Byte status, List<Long> ids);
}
