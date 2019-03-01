package pers.liujunyi.cloud.core.service.dict;

import pers.liujunyi.cloud.core.domain.dict.DictionariesQueryDto;
import pers.liujunyi.cloud.core.entity.dict.Dictionaries;
import pers.liujunyi.common.restful.ResultInfo;
import pers.liujunyi.common.service.BaseElasticsearchService;
import pers.liujunyi.common.vo.tree.ZTreeNode;

import java.util.List;
import java.util.Map;

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
public interface DictionariesElasticsearchService extends BaseElasticsearchService<Dictionaries, Long> {


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

    /**
     * 字典 Combox
     * @param systemCode
     * @param dictCode
     * @return
     */
    List<Map<String, String>> dictCombox(String systemCode, String dictCode);

    /**
     * 根据字典代码 获取字典值
     * @param systemCode
     * @param pidDictCode  父级 dict code
     * @param dictCode
     * @return
     */
    String getDictName(String systemCode, String pidDictCode, String dictCode);
}
