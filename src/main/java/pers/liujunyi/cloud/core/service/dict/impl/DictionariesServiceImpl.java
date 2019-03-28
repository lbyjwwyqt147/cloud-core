package pers.liujunyi.cloud.core.service.dict.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import pers.liujunyi.cloud.common.repository.jpa.BaseRepository;
import pers.liujunyi.cloud.common.restful.ResultInfo;
import pers.liujunyi.cloud.common.restful.ResultUtil;
import pers.liujunyi.cloud.common.service.impl.BaseServiceImpl;
import pers.liujunyi.cloud.common.util.DozerBeanMapperUtil;
import pers.liujunyi.cloud.common.util.UserUtils;
import pers.liujunyi.cloud.core.domain.dict.DictionariesDto;
import pers.liujunyi.cloud.core.entity.dict.Dictionaries;
import pers.liujunyi.cloud.core.repository.elasticsearch.dict.DictionariesElasticsearchRepository;
import pers.liujunyi.cloud.core.repository.jpa.dict.DictionariesRepository;
import pers.liujunyi.cloud.core.service.dict.DictionariesService;
import pers.liujunyi.cloud.core.util.Constant;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

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
    @Autowired
    private UserUtils userUtils;

    public DictionariesServiceImpl(BaseRepository<Dictionaries, Long> baseRepository) {
        super(baseRepository);
    }


    @Override
    public ResultInfo saveRecord(DictionariesDto record) {
        if (this.checkRepetition(record.getSystemCode(), record.getId(), record.getPid(), record.getDictCode())){
            return ResultUtil.params("字典代码重复,请重新输入.");
        }
        Dictionaries dictionaries = DozerBeanMapperUtil.copyProperties(record, Dictionaries.class);
        boolean add = true;
        if (record.getId() != null) {
            add = false;
        }
        if (record.getPid().longValue() > 0) {
            Dictionaries parent = this.selectById(record.getPid());
            dictionaries.setFullParent(parent.getFullParent() + ":"  + parent.getId());
            String curParentCode = StringUtils.isNotBlank(parent.getFullParentCode()) && !parent.getFullParentCode().equals("0") ? parent.getFullParentCode()   + ":" + parent.getDictCode() : parent.getDictCode();
            dictionaries.setFullParentCode(curParentCode);
            String curFullDictCode = StringUtils.isNotBlank(parent.getFullDictCode()) ? parent.getFullDictCode()   + ":" + record.getDictCode() : parent.getDictCode();
            dictionaries.setFullDictCode(curFullDictCode);
            dictionaries.setDictLevel((byte)(parent.getDictLevel() +  1));
        } else {
            dictionaries.setLeaf((byte)0);
            dictionaries.setFullParent("0");
            dictionaries.setFullParentCode("0");
            dictionaries.setDictLevel((byte)1);
            dictionaries.setFullDictCode(record.getDictCode());
        }
        if (record.getPriority() == null) {
            dictionaries.setPriority(10);
        }
        if (record.getStatus() == null) {
            dictionaries.setStatus(Constant.ENABLE_STATUS);
        }
        if (!add) {
            dictionaries.setUpdateTime(new Date());
            dictionaries.setUpdateUserId(this.userUtils.getPresentLoginUserId());
        }
        Dictionaries saveObj = this.dictionariesRepository.save(dictionaries);
        if (saveObj == null || saveObj.getId() == null) {
            return ResultUtil.fail();
        }
        saveObj.setDataVersion(record.getDataVersion() + 1);
        this.dictionariesElasticsearchRepository.save(saveObj);
        // 新增操作才会去更新leaf 字段值
        if (add) {
            Dictionaries exists = this.selectById(record.getPid());
            if (exists != null) {
                this.dictionariesRepository.setLeafById((byte)0, new Date(), record.getPid());
                exists.setLeaf((byte)0);
                this.dictionariesElasticsearchRepository.save(exists);
            }
        }

        return ResultUtil.success(saveObj.getId());
    }

    @Override
    public ResultInfo updateStatus(Byte status, List<Long> ids) {
        if (status.byteValue() == 1) {
            List<Dictionaries> list = this.dictionariesElasticsearchRepository.findByPidIn(ids, super.getPageable(ids.size()));
            if (!CollectionUtils.isEmpty(list)) {
                return ResultUtil.params("无法被禁用.");
            }
        }
        int count = this.dictionariesRepository.setStatusByIds(status, new Date(), ids);
        if (count > 0) {
            Map<String, Map<String, Object>> sourceMap = new ConcurrentHashMap<>();
            Map<String, Object> docDataMap = new HashMap<>();
            docDataMap.put("status", status);
            docDataMap.put("updateTime", System.currentTimeMillis());
            ids.stream().forEach(item -> {
                sourceMap.put(String.valueOf(item), docDataMap);
            });
            // 更新 Elasticsearch 中的数据
            super.updateBatchElasticsearchData(sourceMap);
            return ResultUtil.success();
        }
        return ResultUtil.fail();
    }

    @Override
    public ResultInfo updateStatus(Byte status, Long id, Long version) {
        if (status.byteValue() == 1) {
            List<Dictionaries> list = this.dictionariesElasticsearchRepository.findByPid(id, super.allPageable);
            if (!CollectionUtils.isEmpty(list)) {
                return ResultUtil.params("无法被禁用.");
            }
        }
        int count = this.dictionariesRepository.setStatusById(status, new Date(), id, version);
        if (count > 0) {
            Map<String, Map<String, Object>> sourceMap = new ConcurrentHashMap<>();
            Map<String, Object> docDataMap = new HashMap<>();
            docDataMap.put("status", status);
            docDataMap.put("dataVersion", version + 1);
            docDataMap.put("updateTime", System.currentTimeMillis());
            sourceMap.put(String.valueOf(id), docDataMap);
            super.updateBatchElasticsearchData(sourceMap);
            return ResultUtil.success();
        }
        return ResultUtil.fail();
    }

    @Override
    public ResultInfo batchDeletes(List<Long> ids) {
        List<Dictionaries> list = this.dictionariesElasticsearchRepository.findByPidIn(ids, super.getPageable(ids.size()));
        if (!CollectionUtils.isEmpty(list)) {
            return ResultUtil.params("无法被删除.");
        }
        int count = this.dictionariesRepository.deleteAllByIdIn(ids);
        if (count > 0) {
            this.dictionariesElasticsearchRepository.deleteByIdIn(ids);
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return ResultUtil.success();
        }
        return ResultUtil.fail();
    }

    @Override
    public ResultInfo singleDelete(Long id) {
        this.dictionariesRepository.deleteById(id);
        this.dictionariesElasticsearchRepository.deleteById(id);
        return ResultUtil.success();
    }

    @Override
    public ResultInfo syncDataToElasticsearch() {
        Sort sort =  new Sort(Sort.Direction.ASC, "id");
        List<Dictionaries> list = this.dictionariesRepository.findAll(sort);
        if (!CollectionUtils.isEmpty(list)) {
            this.dictionariesElasticsearchRepository.deleteAll();
            // 限制条数
            int pointsDataLimit = 1000;
            int size = list.size();
            //判断是否有必要分批
            if(pointsDataLimit < size){
                //分批数
                int part = size/pointsDataLimit;
                for (int i = 0; i < part; i++) {
                    //1000条
                    List<Dictionaries> partList = new LinkedList<>(list.subList(0, pointsDataLimit));
                    //剔除
                    list.subList(0, pointsDataLimit).clear();
                    this.dictionariesElasticsearchRepository.saveAll(partList);
                }
                //表示最后剩下的数据
                if (!CollectionUtils.isEmpty(list)) {
                    this.dictionariesElasticsearchRepository.saveAll(list);
                }
            } else {
                this.dictionariesElasticsearchRepository.saveAll(list);
            }
        } else {
            this.dictionariesElasticsearchRepository.deleteAll();
        }
        return ResultUtil.success();
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
        List<Dictionaries> exists = this.dictionariesElasticsearchRepository.findBySystemCodeAndPidAndDictCode(sysCode, pid, dictCode);
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
