package pers.liujunyi.cloud.core.service.dict;

import pers.liujunyi.cloud.core.domain.dict.DictionariesDto;
import pers.liujunyi.cloud.core.entity.dict.Dictionaries;
import pers.liujunyi.common.restful.ResultInfo;
import pers.liujunyi.common.service.BaseService;

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

}
