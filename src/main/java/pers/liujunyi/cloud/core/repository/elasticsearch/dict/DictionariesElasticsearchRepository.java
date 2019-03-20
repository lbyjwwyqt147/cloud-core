package pers.liujunyi.cloud.core.repository.elasticsearch.dict;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import pers.liujunyi.cloud.core.entity.dict.Dictionaries;
import pers.liujunyi.common.repository.elasticsearch.BaseElasticsearchRepository;

import java.util.List;

/***
 * 文件名称: DictionariesElasticsearchRepository.java
 * 文件描述: 数据字典 Elasticsearch Repository
 * 公 司:
 * 内容摘要:
 * 其他说明:
 * 完成日期:2019年01月17日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
@Repository
public interface DictionariesElasticsearchRepository extends BaseElasticsearchRepository<Dictionaries, Long> {


    /**
     *  根据 pid 获取字典数据
     * @param pid
     * @param systemCode
     * @param status   0: 启动 1：禁用
     * @param  pageable
     * @return 客户端没有设置分页，es服务端会有默认填充 默认只返回10条
     */
    List<Dictionaries> findByPidAndSystemCodeAndStatusOrderByPriorityAsc(Long pid, String systemCode, Byte status, Pageable pageable);

    /**
     * 根据父级代码 获取字典数据
     * @param fullParentCode
     * @param systemCode
     * @param status
     * @param pageable
     * @return
     */
    List<Dictionaries> findByFullParentCodeLikeAndSystemCodeAndStatusOrderByPriorityAsc(String fullParentCode, String systemCode, Byte status, Pageable pageable);

    /**
     *  获取 存在 叶子节点的 字典数据
     * @param pid
     * @param  pageable
     * @return 客户端没有设置分页，es服务端会有默认填充 默认只返回10条
     */
    List<Dictionaries> findByPidIn(List<Long> pid, Pageable pageable);

    /**
     *  根据 systemCode  pid  dictCode 获取数据
     * @param systemCode
     * @param pid
     * @param dictCode
     * @return 客户端没有设置分页，es服务端会有默认填充 默认只返回10条
     */
    List<Dictionaries> findBySystemCodeAndPidAndAndDictCode(String systemCode, Long pid, String dictCode);

    /**
     * 根据   pid   获取数据
     * @param pid
     * @return
     */
    List<Dictionaries> findByPid(Long pid);

    /**
     * 根据 systemCode  dictCode status   获取 第一条 数据
     * @param systemCode
     * @param dictCode
     * @param status
     * @return 客户端没有设置分页，es服务端会有默认填充 默认只返回10条
     */
    Dictionaries findFirstBySystemCodeAndDictCodeAndStatus(String systemCode, String dictCode, Byte status);
}
