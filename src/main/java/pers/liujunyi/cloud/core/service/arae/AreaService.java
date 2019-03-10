package pers.liujunyi.cloud.core.service.arae;

import pers.liujunyi.cloud.core.entity.area.Area;
import pers.liujunyi.common.restful.ResultInfo;
import pers.liujunyi.common.service.BaseService;

/***
 * 文件名称: AreaService.java
 * 文件描述: 行政区划 Service
 * 公 司:
 * 内容摘要:
 * 其他说明:
 * 完成日期:2019年01月17日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
public interface AreaService extends BaseService<Area, Long> {


    /**
     * 同步数据到es中
     * @return
     */
    ResultInfo syncDataToElasticsearch();
}
