package pers.liujunyi.cloud.core.service.arae.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import pers.liujunyi.cloud.common.repository.elasticsearch.BaseElasticsearchRepository;
import pers.liujunyi.cloud.common.restful.ResultInfo;
import pers.liujunyi.cloud.common.restful.ResultUtil;
import pers.liujunyi.cloud.common.service.impl.BaseElasticsearchServiceImpl;
import pers.liujunyi.cloud.core.entity.area.Area;
import pers.liujunyi.cloud.core.entity.dict.Dictionaries;
import pers.liujunyi.cloud.core.repository.elasticsearch.area.AreaElasticsearchRepository;
import pers.liujunyi.cloud.core.service.arae.AreaElasticsearchService;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

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

    @Override
    public List<Map<String, Object>> areaCombox(Long pid, Boolean empty) {
        List<Map<String, Object>> result  = new LinkedList<>();
        if (empty != null && empty == true) {
            Map<String, Object> emptyMap = new ConcurrentHashMap<>();
            emptyMap.put("id", "");
            emptyMap.put("text", "-请选择-");
            result.add(emptyMap);
        }
        List<Area> list = this.areaElasticsearchRepository.findByPid(pid, super.allPageable);
        if (!CollectionUtils.isEmpty(list)) {
            list.stream().forEach(item -> {
                Map<String, Object> map = new ConcurrentHashMap<>();
                map.put("id", item.getId());
                map.put("text", item.getName());
                result.add(map);
            });
        }
        return result;
    }

    @Override
    public ResultInfo areaNameToMap(List<Long> ids) {
        return ResultUtil.success(this.getAreaNameToMap(ids));
    }

    @Override
    public Map<Long, String> getAreaNameToMap(List<Long> ids) {
        List<Area> list = super.findByIdIn(ids);
        if (!CollectionUtils.isEmpty(list)) {
            return list.stream().collect(Collectors.toMap(Area::getId, Area::getMergerName));
        }
        return null;
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
