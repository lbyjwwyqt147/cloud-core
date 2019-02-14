package pers.liujunyi.cloud.core.repository.elasticsearch.dict;

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
     *  获取 存在 叶子节点的 字典数据
     * @param pid
     * @param systemCode
     * @param leaf    叶子  0:存在叶子节点  1： 不存在
     * @param status   0: 启动 1：禁用
     * @return
     */
    List<Dictionaries> findByPidAndSystemCodeAndLeafAndStatusOrderByIdAsc(Long pid, String systemCode, Byte leaf, Byte status);

    /**
     *  获取 存在 叶子节点的 字典数据
     * @param pid
     * @param systemCode
     * @param status   0: 启动 1：禁用
     * @return
     */
    List<Dictionaries> findByPidAndSystemCodeAndStatusOrderByIdAsc(Long pid, String systemCode, Byte status);

    /**
     *  根据 systemCode  pid  dictCode 获取数据
     * @param systemCode
     * @param pid
     * @param dictCode
     * @return
     */
    List<Dictionaries> findBySystemCodeAndPidAndAndDictCode(String systemCode, Long pid, String dictCode);

}
