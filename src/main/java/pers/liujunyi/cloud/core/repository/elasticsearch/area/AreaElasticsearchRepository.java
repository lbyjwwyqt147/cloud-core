package pers.liujunyi.cloud.core.repository.elasticsearch.area;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import pers.liujunyi.cloud.common.repository.elasticsearch.BaseElasticsearchRepository;
import pers.liujunyi.cloud.core.entity.area.Area;

import java.util.List;

/***
 * 文件名称: AreaElasticsearchRepository.java
 * 文件描述: 行政区划 Elasticsearch Repository
 * 公 司:
 * 内容摘要:
 * 其他说明:
 * 完成日期:2019年01月17日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
@Repository
public interface AreaElasticsearchRepository extends BaseElasticsearchRepository<Area, Long> {


    /**
     * 根据pid 获取数据
     * @param pid
     * @return
     */
    List<Area> findByPid(Long pid, Pageable pageable);


}
