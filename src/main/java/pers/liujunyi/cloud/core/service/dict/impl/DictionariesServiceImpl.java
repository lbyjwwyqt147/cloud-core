package pers.liujunyi.cloud.core.service.dict.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import pers.liujunyi.cloud.core.domain.dict.DictionariesDto;
import pers.liujunyi.cloud.core.entity.dict.Dictionaries;
import pers.liujunyi.cloud.core.repository.elasticsearch.dict.DictionariesElasticsearchRepository;
import pers.liujunyi.cloud.core.repository.jpa.dict.DictionariesRepository;
import pers.liujunyi.cloud.core.service.dict.DictionariesService;
import pers.liujunyi.cloud.core.util.Constant;
import pers.liujunyi.common.repository.jpa.BaseRepository;
import pers.liujunyi.common.restful.ResultInfo;
import pers.liujunyi.common.restful.ResultUtil;
import pers.liujunyi.common.service.impl.BaseServiceImpl;
import pers.liujunyi.common.util.DozerBeanMapperUtil;

import java.util.Date;
import java.util.List;
import java.util.Optional;

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
        if (this.checkRepetition(record.getSystemCode(), record.getId(), record.getPid(), record.getDictCode())){
            return ResultUtil.params("字典代码重复,请重新输入.");
        }
        Dictionaries dictionaries = DozerBeanMapperUtil.copyProperties(record, Dictionaries.class);
        if (record.getPid() == 0) {
            dictionaries.setLeaf((byte)0);
        }
        dictionaries.setStatus(Constant.ENABLE_STATUS);
        Dictionaries saveObj = this.dictionariesRepository.save(dictionaries);
        if (saveObj == null || saveObj.getId() == null) {
            return ResultUtil.fail();
        }
        this.dictionariesElasticsearchRepository.save(saveObj);
        Dictionaries exists = this.selectById(record.getPid());
        if (exists != null) {
            this.dictionariesRepository.setLeafById((byte)0, new Date(), record.getPid());
            exists.setLeaf((byte)0);
            this.dictionariesElasticsearchRepository.save(exists);
        }
        return ResultUtil.success(saveObj.getId());
    }

    @Override
    public ResultInfo updateStatus(Byte status, List<Long> ids) {
        int count = this.dictionariesRepository.setStatusByIds(status, new Date(), ids);
        if (count > 0) {
            List<Dictionaries> dictionaries = this.dictionariesElasticsearchRepository.findByIdIn(ids);
            if (!CollectionUtils.isEmpty(dictionaries)) {
                dictionaries.stream().forEach(dict -> {
                    dict.setStatus(status);
                    dict.setUpdateTime(new Date());
                });
                this.dictionariesElasticsearchRepository.saveAll(dictionaries);
            }
            return ResultUtil.success();
        }
        return ResultUtil.fail();
    }

    @Override
    public ResultInfo batchDeletes(List<Long> ids) {
        List<Dictionaries> list = this.dictionariesElasticsearchRepository.findByPidIn(ids, super.page);
        if (!CollectionUtils.isEmpty(list)) {
            return ResultUtil.params("无法被删除.");
        }
        int count = this.dictionariesRepository.deleteAllByIdIn(ids);
        if (count > 0) {
            this.dictionariesElasticsearchRepository.deleteByIdIn(ids);
            return ResultUtil.success();
        }
        return ResultUtil.fail();
    }


    /**
     * 检查字典代码是否重复
     * @param sysCode
     * @param id
     * @param pid
     * @param dictCode
     * @return
     */
    private Boolean checkRepetition(String sysCode, Long id, Long pid, String dictCode) {
        if (id == null) {
           return this.checkDictCodeData(sysCode, pid, dictCode);
        }
        Dictionaries dictionaries = this.selectById(id);
        if (dictionaries != null && !dictionaries.getDictCode().equals(dictCode)) {
            return this.checkDictCodeData(sysCode, pid, dictCode);
        }
        return false;
    }

    /**
     * 检查中是否存在dictCode 数据
     * @param sysCode
     * @param pid
     * @param dictCode
     * @return
     */
    private Boolean checkDictCodeData (String sysCode, Long pid, String dictCode) {
        List<Dictionaries> exists = this.dictionariesElasticsearchRepository.findBySystemCodeAndPidAndAndDictCode(sysCode, pid, dictCode);
        if (CollectionUtils.isEmpty(exists)) {
            return false;
        }
        return true;
    }

    /**
     * 根据主键ID 获取数据
     * @param id
     * @return
     */
    private Dictionaries selectById(Long id) {
        Optional<Dictionaries> optional = this.dictionariesElasticsearchRepository.findById(id);
        if (optional.isPresent()) {
            return optional.get();
        }
        return null;
    }
}
