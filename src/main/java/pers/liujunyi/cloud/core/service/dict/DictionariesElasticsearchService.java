package pers.liujunyi.cloud.core.service.dict;

import org.springframework.data.domain.Pageable;
import pers.liujunyi.cloud.common.restful.ResultInfo;
import pers.liujunyi.cloud.common.service.BaseElasticsearchService;
import pers.liujunyi.cloud.common.vo.tree.ZtreeNode;
import pers.liujunyi.cloud.core.domain.dict.DictionariesQueryDto;
import pers.liujunyi.cloud.core.entity.dict.Dictionaries;

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
     * 根据 pid 获取 符合 ztree 结构的数据
     * @param pid
     * @param status 状态
     * @param systemCode
     * @return
     */
    List<ZtreeNode> dictTree(Long pid, Byte status , String systemCode);

    /**
     * 根据 fullParentCode 获取符合 ztree 结构的数据
     * @param fullParentCode
     * @param status 状态
     * @param systemCode
     * @return
     */
    List<ZtreeNode> dictCodeTree(String fullParentCode, Byte status ,String systemCode);

    /**
     *  获取 存在 叶子节点的 字典数据
     * @param pid
     * @return 客户端没有设置分页，es服务端会有默认填充 默认只返回10条
     */
    List<Dictionaries> findByPid(Long pid);

    /**
     * 分页列表
     * @param query
     * @return
     */
    ResultInfo findPageGird(DictionariesQueryDto query);

    /**
     * 字典 Combox
     * @param systemCode
     * @param parentCode  父级 code
     * @param empty
     * @return
     */
    List<Map<String, String>> dictCombox(String systemCode, String parentCode, Boolean empty);


    /**
     * 根据 fullDictCode 获取字典名称值
     * @param systemCode
     * @param parentCode  父级 code
     * @param dictCode    要取值的 字典 code
     * @return
     */
    String getDictName(String systemCode, String parentCode ,String dictCode);


    /**
     * 根据fullParentCode获取字典值 返回map
     * @param systemCode
     * @param fullParentCode  父级 dict code
     * @return  返回 map   key = 字典代码   value = 字典名称
     */
    Map<String, String> getDictNameToMap(String systemCode, String fullParentCode);

    /**
     * 根据fullParentCodes 获取字典值 返回map
     * @param systemCode
     * @param fullParentCodes  父级 dict code
     * @return  返回 map   key = 字典代码   value = map
     */
    Map<String, Map<String, String>> getDictNameToMap(String systemCode, List<String> fullParentCodes);
}
