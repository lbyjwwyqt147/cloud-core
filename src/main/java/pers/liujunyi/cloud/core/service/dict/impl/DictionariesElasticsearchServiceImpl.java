package pers.liujunyi.cloud.core.service.dict.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import pers.liujunyi.cloud.common.encrypt.AesEncryptUtils;
import pers.liujunyi.cloud.common.repository.elasticsearch.BaseElasticsearchRepository;
import pers.liujunyi.cloud.common.restful.ResultInfo;
import pers.liujunyi.cloud.common.restful.ResultUtil;
import pers.liujunyi.cloud.common.service.impl.BaseElasticsearchServiceImpl;
import pers.liujunyi.cloud.common.vo.tree.ZtreeBuilder;
import pers.liujunyi.cloud.common.vo.tree.ZtreeNode;
import pers.liujunyi.cloud.core.domain.dict.DictZtreeDto;
import pers.liujunyi.cloud.core.domain.dict.DictionariesQueryDto;
import pers.liujunyi.cloud.core.entity.dict.Dictionaries;
import pers.liujunyi.cloud.core.repository.elasticsearch.dict.DictionariesElasticsearchRepository;
import pers.liujunyi.cloud.core.service.dict.DictionariesElasticsearchService;
import pers.liujunyi.cloud.core.util.Constant;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/***
 * 文件名称: DictionariesElasticsearchServiceImpl.java
 * 文件描述: 数据字典 Elasticsearch Service impl
 * 公 司:
 * 内容摘要:
 * 其他说明:
 * 完成日期:2019年01月17日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
@Service
public class DictionariesElasticsearchServiceImpl extends BaseElasticsearchServiceImpl<Dictionaries, Long> implements DictionariesElasticsearchService {

    @Autowired
    private DictionariesElasticsearchRepository dictionariesElasticsearchRepository;

    public DictionariesElasticsearchServiceImpl(BaseElasticsearchRepository<Dictionaries, Long> baseElasticsearchRepository) {
        super(baseElasticsearchRepository);
    }

    @Override
    public List<ZtreeNode> dictTree(Long pid, Byte status , String systemCode) {
        if (pid == null || pid.longValue() == 0) {
            pid = null;
        }
        List<Dictionaries> list = this.dictionariesElasticsearchRepository.findByPidAndSystemCodeAndStatusOrderByPriorityAsc(pid, systemCode, status, super.allPageable);
        return this.startBuilderZtree(list);
    }

    @Override
    public List<ZtreeNode> dictCodeTree(String fullParentCode, Byte status, String systemCode) {
        List<Dictionaries> list = this.dictionariesElasticsearchRepository.findByFullParentCodeLikeAndSystemCodeAndStatusOrderByPriorityAsc(fullParentCode, systemCode, status, super.allPageable);
        return this.startBuilderZtree(list);
    }

    @Override
    public List<Dictionaries> findByPid(Long pid) {
        return this.dictionariesElasticsearchRepository.findByPid(pid, super.allPageable);
    }

    /**
     *
     *  使用QueryBuilder
     *  termQuery("key", obj) 完全匹配
     *  termsQuery("key", obj1, obj2..)   一次匹配多个值
     *  matchQuery("key", Obj) 单个匹配, field不支持通配符, 前缀具高级特性
     *  multiMatchQuery("text", "field1", "field2"..);  匹配多个字段, field有通配符忒行
     *  fuzzyQuery("key"，value)  模糊查询
     *  matchAllQuery();         匹配所有文件
     *
     *  must 相当于 与 & =
     *
     * must not 相当于 非 ~   ！=
     *
     * should 相当于 或  |   or
     *
     * filter  过滤
     *
     * @param query
     * @return
     */
    @Override
    public ResultInfo findPageGird(DictionariesQueryDto query) {
        //分页参数
        Pageable pageable = query.toPageable(Sort.Direction.ASC, "priority");
        // 查询数据
        SearchQuery searchQuery = query.toSpecPageable(pageable);
        Page<Dictionaries> searchPageResults = this.dictionariesElasticsearchRepository.search(searchQuery);
        Long totalElements =  searchPageResults.getTotalElements();
        ResultInfo result = ResultUtil.success(AesEncryptUtils.aesEncrypt(searchPageResults.getContent(), super.secretKey));
        result.setTotal(totalElements);
        return  result;
    }

    @Override
    public List<Map<String, String>> dictCombox(String systemCode, String parentCode, Boolean empty) {
        List<Map<String, String>> result  = new LinkedList<>();
        if (empty != null && empty == true) {
            Map<String, String> emptyMap = new ConcurrentHashMap<>();
            emptyMap.put("id", "");
            emptyMap.put("text", "-请选择-");
            result.add(emptyMap);
        }
        List<Dictionaries> list = this.dictionariesElasticsearchRepository.findBySystemCodeAndStatusAndFullParentCodeOrderByPriorityAsc(systemCode, Constant.ENABLE_STATUS, parentCode, super.pageable);
        if (!CollectionUtils.isEmpty(list)) {
            list.stream().forEach(item -> {
                Map<String, String> map = new ConcurrentHashMap<>();
                map.put("id", item.getDictCode());
                map.put("text", item.getDictName());
                result.add(map);
            });
        }
        return result;
    }


    @Override
    public String getDictName(String systemCode, String parentCode, String dictCode) {
        String result = "";
        Dictionaries dictionaries = this.dictionariesElasticsearchRepository.findFirstBySystemCodeAndFullDictCodeAndStatus(systemCode, parentCode + ":" + dictCode, null);
        if (dictionaries != null) {
                result = dictionaries.getDictName();
        }
        return result;
    }

    @Override
    public Map<String, String> getDictNameToMap(String systemCode,  String fullParentCode) {
        List<Dictionaries> list = this.dictionariesElasticsearchRepository.findBySystemCodeAndFullParentCode(systemCode, fullParentCode, super.allPageable);
        if (!CollectionUtils.isEmpty(list)) {
            return list.stream().collect(Collectors.toMap(Dictionaries::getDictCode, Dictionaries::getDictName));
        }
        return null;
    }

    @Override
    public Map<String, Map<String, String>> getDictNameToMap(String systemCode,  List<String> fullParentCodes) {
        Map<String, Map<String, String>> dictNameMap = new ConcurrentHashMap<>();
        List<Dictionaries> list = this.dictionariesElasticsearchRepository.findBySystemCodeAndFullParentCodeIn(systemCode, fullParentCodes, super.allPageable);
        if (!CollectionUtils.isEmpty(list)) {
            // 以 fullParentCode 分组
            Map<String, List<Dictionaries>> parentCodeGroup = list.stream().collect(Collectors.groupingBy(Dictionaries::getFullParentCode));
            Set<Map.Entry<String, List<Dictionaries>>> entrySet = parentCodeGroup.entrySet();
            Iterator<Map.Entry<String, List<Dictionaries>>> iter = entrySet.iterator();
            while (iter.hasNext()) {
                Map.Entry<String, List<Dictionaries>> entry = iter.next();
                List<Dictionaries> dictionariesList = entry.getValue();
                Map<String, String> tempMap = dictionariesList.stream().collect(Collectors.toMap(Dictionaries::getDictCode, Dictionaries::getDictName));
                dictNameMap.put(entry.getKey(), tempMap);
            }
        }
        return dictNameMap;
    }



    /**
     * 构建 ztree 树
     * @param list
     * @return
     */
    private List<ZtreeNode> startBuilderZtree(List<Dictionaries> list){
        List<ZtreeNode> treeNodes = new LinkedList<>();
        if (!CollectionUtils.isEmpty(list)){
            list.stream().forEach(item -> {
                ZtreeNode zTreeNode = new ZtreeNode(item.getId(), item.getPid(), item.getDictName());
                DictZtreeDto dictZtreeDto = new DictZtreeDto();
                dictZtreeDto.setId(item.getId());
                dictZtreeDto.setDictCode(item.getDictCode());
                dictZtreeDto.setDictName(item.getDictName());
                dictZtreeDto.setDictLabel(item.getDictLabel());
                dictZtreeDto.setPid(item.getPid());
                dictZtreeDto.setFullParentCode(item.getFullParentCode());
                zTreeNode.setOtherAttributes(dictZtreeDto);
                treeNodes.add(zTreeNode);
            });
        }
        return ZtreeBuilder.buildListToTree(treeNodes);
    }
}
