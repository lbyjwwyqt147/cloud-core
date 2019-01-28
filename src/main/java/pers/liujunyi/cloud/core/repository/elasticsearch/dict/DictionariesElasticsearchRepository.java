package pers.liujunyi.cloud.core.repository.elasticsearch.dict;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;
import pers.liujunyi.cloud.core.entity.dict.Dictionaries;

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
public interface DictionariesElasticsearchRepository extends ElasticsearchRepository<Dictionaries, Long> {

    /**
     * 根据pid 获取数据
     * @param pid
     * @return
     */
    List<Dictionaries> findByPid(Long pid);

    /**
     *  根据 systemCode  pid  dictCode 获取数据
     * @param systemCode
     * @param pid
     * @param dictCode
     * @return
     */
    List<Dictionaries> findBySystemCodeAndPidAndAndDictCode(String systemCode, Long pid, String dictCode);

}
