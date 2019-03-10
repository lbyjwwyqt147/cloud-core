package pers.liujunyi.cloud.core.service.arae.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pers.liujunyi.cloud.core.entity.area.Area;
import pers.liujunyi.cloud.core.repository.elasticsearch.area.AreaElasticsearchRepository;
import pers.liujunyi.cloud.core.service.arae.AreaElasticsearchService;
import pers.liujunyi.common.repository.elasticsearch.BaseElasticsearchRepository;
import pers.liujunyi.common.restful.ResultInfo;
import pers.liujunyi.common.restful.ResultUtil;
import pers.liujunyi.common.service.impl.BaseElasticsearchServiceImpl;

import java.util.Optional;

/***
 * 文件名称: AreaElasticsearchServiceImpl.java
 * 文件描述: 行政区划 Elasticsearch Service impl
 * 公 司:
 * 内容摘要:
 * 其他说明:
 * 完成日期:2019年01月17日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
@Service
public class AreaElasticsearchServiceImpl extends BaseElasticsearchServiceImpl<Area, Long> implements AreaElasticsearchService {

    @Autowired
    private AreaElasticsearchRepository areaElasticsearchRepository;

    public AreaElasticsearchServiceImpl(BaseElasticsearchRepository<Area, Long> baseElasticsearchRepository) {
        super(baseElasticsearchRepository);
    }

    @Override
    public ResultInfo getAreaName(Long id) {
        ResultInfo result = ResultUtil.success();
        Area area = this.getArea(id);
        if (area != null) {
            result.setData(area.getMergerName());
        }
        return result;
    }

    /**
     * 根据ID获取数据
     * @param id
     * @return
     */
    private Area getArea(Long id) {
        Optional<Area> optional = this.areaElasticsearchRepository.findById(id);
        if (optional.isPresent()) {
            return optional.get();
        }
        return null;
    }
}
