package pers.liujunyi.cloud.core.service.arae;

import pers.liujunyi.cloud.core.entity.area.Area;
import pers.liujunyi.common.restful.ResultInfo;
import pers.liujunyi.common.service.BaseElasticsearchService;

/***
 * 文件名称: AreaElasticsearchService.java
 * 文件描述: 行政区划 Elasticsearch Service
 * 公 司:
 * 内容摘要:
 * 其他说明:
 * 完成日期:2019年01月17日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
public interface AreaElasticsearchService extends BaseElasticsearchService<Area, Long> {

    /**
     * 根据id 获取名称
     * @param id
     * @return
     */
    ResultInfo getAreaName(Long id);

}
