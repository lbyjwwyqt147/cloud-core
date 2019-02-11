package pers.liujunyi.cloud.core.service.dict;

import pers.liujunyi.cloud.core.domain.dict.DictionariesQueryDto;
import pers.liujunyi.common.restful.ResultInfo;
import pers.liujunyi.common.vo.tree.ZTreeNode;

import java.util.List;

/***
 * 文件名称: DictionariesElasticsearchService.java
 * 文件描述: 数据字典 Elasticsearch Service
 * 公 司:
 * 内容摘要:
 * 其他说明:
 * 完成日期:2019年01月17日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
public interface DictionariesElasticsearchService {

    /**
     * 符合 ztree 结构的数据
     * @param pid
     * @param systemCode
     * @return
     */
    ResultInfo dictZtree(Long pid, String systemCode);

    /**
     * 符合 ztree 结构的数据
     * @param pid
     * @param systemCode
     * @return
     */
    List<ZTreeNode> dictTree(Long pid, String systemCode);

    /**
     * 分页列表
     * @param query
     * @return
     */
    ResultInfo findPageGird(DictionariesQueryDto query);

}
