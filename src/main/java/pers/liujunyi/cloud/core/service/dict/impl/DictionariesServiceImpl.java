package pers.liujunyi.cloud.core.service.dict.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import pers.liujunyi.cloud.core.domain.dict.DictionariesDto;
import pers.liujunyi.cloud.core.entity.dict.Dictionaries;
import pers.liujunyi.cloud.core.repository.elasticsearch.dict.DictionariesElasticsearchRepository;
import pers.liujunyi.cloud.core.repository.jpa.dict.DictionariesRepository;
import pers.liujunyi.cloud.core.service.dict.DictionariesService;
import pers.liujunyi.common.repository.jpa.BaseRepository;
import pers.liujunyi.common.restful.ResultInfo;
import pers.liujunyi.common.restful.ResultUtil;
import pers.liujunyi.common.service.impl.BaseServiceImpl;
import pers.liujunyi.common.util.DozerBeanMapperUtil;

import java.util.List;

/***
 * 文件名称: DictionariesServiceImpl.java
 * 文件描述: 数据字典 Service Impl
 * 公 司:
 * 内容摘要:
 * 其他说明:
 * 完成日期:2019年01月17日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
@Service
public class DictionariesServiceImpl extends BaseServiceImpl<Dictionaries, Long> implements DictionariesService {

    @Autowired
    private DictionariesRepository dictionariesRepository;
    @Autowired
    private DictionariesElasticsearchRepository dictionariesElasticsearchRepository;

    public DictionariesServiceImpl(BaseRepository<Dictionaries, Long> baseRepository) {
        super(baseRepository);
    }


    @Override
    public ResultInfo saveRecord(DictionariesDto record) {
        List<Dictionaries> exists = this.dictionariesElasticsearchRepository.findBySystemCodeAndPidAndAndDictCode(record.getSystemCode(), record.getPid(), record.getDictCode());
        if (!CollectionUtils.isEmpty(exists)){
            return ResultUtil.params("字典代码重复,请重新输入.");
        }
        Dictionaries dictionaries = DozerBeanMapperUtil.copyProperties(record, Dictionaries.class);
        Dictionaries saveObj = this.dictionariesRepository.save(dictionaries);
        if (saveObj == null || saveObj.getId() == null) {
            return ResultUtil.fail();
        }
        this.dictionariesElasticsearchRepository.save(saveObj);
        return ResultUtil.success();
    }

    @Override
    public ResultInfo updateStatus(Byte status, List<Long> ids) {
        int count = this.dictionariesRepository.setStatusByIds(status, ids);
        if (count > 0) {
            return ResultUtil.success();
        }
        return ResultUtil.fail();
    }

}
