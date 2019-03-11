package pers.liujunyi.cloud.core.service.dict.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import pers.liujunyi.cloud.core.domain.dict.DictionariesQueryDto;
import pers.liujunyi.cloud.core.entity.dict.Dictionaries;
import pers.liujunyi.cloud.core.repository.elasticsearch.dict.DictionariesElasticsearchRepository;
import pers.liujunyi.cloud.core.service.dict.DictionariesElasticsearchService;
import pers.liujunyi.cloud.core.util.Constant;
import pers.liujunyi.common.repository.elasticsearch.BaseElasticsearchRepository;
import pers.liujunyi.common.restful.ResultInfo;
import pers.liujunyi.common.restful.ResultUtil;
import pers.liujunyi.common.service.impl.BaseElasticsearchServiceImpl;
import pers.liujunyi.common.vo.tree.AbstractZTreeComponent;
import pers.liujunyi.common.vo.tree.ZTreeBuilder;
import pers.liujunyi.common.vo.tree.ZTreeComposite;
import pers.liujunyi.common.vo.tree.ZTreeNode;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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
    public List<ZTreeNode> dictTree(Long pid, String systemCode) {
        List<ZTreeNode> treeNodes = new LinkedList<>();
        List<Dictionaries> list = this.dictionariesElasticsearchRepository.findByPidAndSystemCodeAndLeafAndStatusOrderByIdAsc(pid, systemCode, null, Constant.ENABLE_STATUS, super.allPageable);
        if (!CollectionUtils.isEmpty(list)){
            list.stream().forEach(item -> {
                ZTreeNode zTreeNode = new ZTreeNode(item.getId(), item.getPid(), item.getDictName());
                treeNodes.add(zTreeNode);
            });
        }
        return ZTreeBuilder.buildListToTree(treeNodes);
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
        // 排序方式
        Sort sort =  new Sort(Sort.Direction.ASC, "priority");

        // 分页参数
       /* Pageable pageable = PageRequest.of(query.getPageNumber() - 1, query.getPageSize(), sort);
        // 条件过滤
        BoolQueryBuilder filter = QueryBuilders.boolQuery();
      //  filter.must(QueryBuilders.matchQuery("dictCode", query.getDictCode()));
        filter.must(QueryBuilders.termQuery("systemCode", query.getSystemCode()));
        filter.must(QueryBuilders.termQuery("pid", query.getPid()));
       // filter.must(QueryBuilders.fuzzyQuery("dictName", query.getDictName()));
        SearchQuery searchQuery = new NativeSearchQueryBuilder().withPageable(pageable)
                .withQuery(filter).build();*/
        //分页参数
        Pageable pageable = query.toPageable(sort);
        // 查询数据
        SearchQuery searchQuery = query.toSpecPageable(pageable);
        Page<Dictionaries> searchPageResults = this.dictionariesElasticsearchRepository.search(searchQuery);
        Long totalElements =  searchPageResults.getTotalElements();
        ResultInfo result = ResultUtil.success(searchPageResults.getContent());
        result.setTotal(totalElements);
        return  result;
    }

    @Override
    public List<Map<String, String>> dictCombox(String systemCode, String dictCode, Boolean empty) {
        List<Map<String, String>> result  = new LinkedList<>();
        if (empty != null && empty == true) {
            Map<String, String> emptyMap = new ConcurrentHashMap<>();
            emptyMap.put("id", "");
            emptyMap.put("text", "-请选择-");
            result.add(emptyMap);
        }
        List<Dictionaries> list = this.findBySystemCodeAndDictCodeAndStatus(systemCode, dictCode);
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
    public String getDictName(String systemCode, String pidDictCode, String dictCode) {
        String result = "";
        List<Dictionaries> list = this.findBySystemCodeAndDictCodeAndStatus(systemCode, pidDictCode);
        if (!CollectionUtils.isEmpty(list)) {
            Dictionaries dictionaries = list.stream().filter(o -> o.getDictCode().trim().equals(dictCode.trim())).findAny().orElse(null);
            if (dictionaries != null) {
                result = dictionaries.getDictName();
            }
        }
        return result;
    }

    /**
     * 递归查找子节点
     * @param leaf
     * @param dictList
     * @return
     */
    private AbstractZTreeComponent findTreeChildren(AbstractZTreeComponent leaf, List<Dictionaries> dictList) {
        AbstractZTreeComponent zTree =  leaf;
        dictList.stream().forEach(item ->{
            if (zTree.getId().equals(item.getPid())){
                AbstractZTreeComponent leafTree = new ZTreeComposite(item.getId(), item.getDictName(),"");
                ZTreeComposite moduleTreeComposite = (ZTreeComposite) zTree;
                moduleTreeComposite.add(leafTree);
                findTreeChildren(leafTree, dictList);
            }
        });
        return zTree;
    }

    /**
     * 根据父级 dictCode  获取下级 数据
     * @param systemCode
     * @param dictCode
     * @return
     */
    private List<Dictionaries> findBySystemCodeAndDictCodeAndStatus(String systemCode, String dictCode) {
        Dictionaries dictionaries = this.dictionariesElasticsearchRepository.findFirstBySystemCodeAndDictCodeAndStatus(systemCode, dictCode, Constant.ENABLE_STATUS);
        if (dictionaries != null) {
            List<Dictionaries> list = this.dictionariesElasticsearchRepository.findByPidAndSystemCodeAndLeafAndStatusOrderByIdAsc(dictionaries.getId(), systemCode, null, Constant.ENABLE_STATUS, super.allPageable);
            return list;
        }
        return null;
    }
}
