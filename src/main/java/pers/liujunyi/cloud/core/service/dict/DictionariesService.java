package pers.liujunyi.cloud.core.service.dict;

import pers.liujunyi.cloud.common.restful.ResultInfo;
import pers.liujunyi.cloud.common.service.BaseService;
import pers.liujunyi.cloud.core.domain.dict.DictionariesDto;
import pers.liujunyi.cloud.core.entity.dict.Dictionaries;

import java.util.List;

/***
 * 文件名称: DictionariesService.java
 * 文件描述: 数据字典 DictionariesService
 * 公 司:
 * 内容摘要:
 * 其他说明:
 * 完成日期:2019年01月17日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
public interface DictionariesService extends BaseService<Dictionaries, Long> {

    /**
     * 保存数据
     * @param record
     * @return
     */
    ResultInfo saveRecord(DictionariesDto record);

    /**
     * 修改状态
     * @param status
     * @param putParams
     * @param ids
     *
     * @return
     */
    ResultInfo updateStatus(Byte status, List<Long> ids, String putParams);

    /**
     * 修改状态
     * @param status
     * @param id
     * @param version
     * @return
     */
    ResultInfo updateStatus(Byte status, Long id, Long version);

    /**
     * 批量删除
     * @param ids
     * @return
     */
    ResultInfo batchDeletes(List<Long> ids);

    /**
     * 单条删除
     * @param id
     * @return
     */
    ResultInfo singleDelete(Long id);

    /**
     * 同步数据到es中
     * @return
     */
    ResultInfo syncDataToElasticsearch();
}
