package pers.liujunyi.cloud.core.service.dict.impl;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import pers.liujunyi.cloud.core.domain.dict.DictionariesQueryDto;
import pers.liujunyi.cloud.core.entity.dict.Dictionaries;
import pers.liujunyi.cloud.core.repository.elasticsearch.dict.DictionariesElasticsearchRepository;
import pers.liujunyi.cloud.core.service.dict.DictionariesElasticsearchService;
import pers.liujunyi.common.restful.ResultInfo;
import pers.liujunyi.common.restful.ResultUtil;
import pers.liujunyi.common.vo.tree.AbstractZTreeComponent;
import pers.liujunyi.common.vo.tree.ZTreeComposite;

import java.util.LinkedList;
import java.util.List;

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
public class DictionariesElasticsearchServiceImpl implements DictionariesElasticsearchService {

    @Autowired
    private DictionariesElasticsearchRepository dictionariesElasticsearchRepository;

    @Override
    public ResultInfo dictZtree(Long pid) {
        List<AbstractZTreeComponent> treeList = new LinkedList<>();
        byte type = 0;
        // 获取 第一级 数据
        List<Dictionaries> firstChildren = this.dictionariesElasticsearchRepository.findByPid(0L);
        if (!CollectionUtils.isEmpty(firstChildren)){
            // 根据 获取父级下的所有数据
            firstChildren.stream().forEach(item -> {
                AbstractZTreeComponent firstTree = new ZTreeComposite(item.getId(), item.getDictName(),"");
                firstTree.setParent(true);
                firstTree.setPid(item.getPid());
                AbstractZTreeComponent leafTree = this.findTreeChildren(firstTree, firstChildren);
                treeList.add(leafTree);
            });
        }
        ResultInfo result = ResultUtil.success(treeList);
        return result;
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
     * @param query
     * @return
     */
    @Override
    public ResultInfo findPageGird(DictionariesQueryDto query) {
        // 排序方式
        Sort sort =  new Sort(Sort.Direction.ASC, "priority");
        // 分页参数
        Pageable pageable = PageRequest.of(query.getPageNumber() - 1, query.getPageSize(), sort);
        // 条件过滤
        BoolQueryBuilder filter = QueryBuilders.boolQuery();
        filter.must(QueryBuilders.matchQuery("dictCode", query.getDictCode()));
        filter.must(QueryBuilders.fuzzyQuery("systemCode", query.getSystemCode()));
        filter.must(QueryBuilders.matchQuery("pid", query.getPid()));
        filter.must(QueryBuilders.matchQuery("dictName", query.getDictName()));
        SearchQuery searchQuery = new NativeSearchQueryBuilder().withPageable(pageable)
                .withQuery(filter).build();
        // 查询数据
        Page<Dictionaries> searchPageResults = this.dictionariesElasticsearchRepository.search(searchQuery);
        Long totalElements =  searchPageResults.getTotalElements();
        ResultInfo result = ResultUtil.success(searchPageResults.getContent());
        result.setTotal(totalElements);
        return  result;
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
}
