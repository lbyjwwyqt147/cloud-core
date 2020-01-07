package pers.liujunyi.cloud.core.service.arae.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import pers.liujunyi.cloud.common.repository.jpa.BaseJpaRepository;
import pers.liujunyi.cloud.common.restful.ResultInfo;
import pers.liujunyi.cloud.common.restful.ResultUtil;
import pers.liujunyi.cloud.common.service.impl.BaseJpaElasticsearchServiceImpl;
import pers.liujunyi.cloud.core.entity.area.Area;
import pers.liujunyi.cloud.core.repository.elasticsearch.area.AreaElasticsearchRepository;
import pers.liujunyi.cloud.core.repository.jpa.area.AreaRepository;
import pers.liujunyi.cloud.core.service.arae.AreaService;

import java.util.LinkedList;
import java.util.List;

/***
 * 文件名称: AreaServiceImpl.java
 * 文件描述: 行政区划 Service Impl
 * 公 司:
 * 内容摘要:
 * 其他说明:
 * 完成日期:2019年01月17日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
@Service
public class AreaServiceImpl extends BaseJpaElasticsearchServiceImpl<Area, Long> implements AreaService {

    @Autowired
    private AreaRepository areaRepoitory;
    @Autowired
    private AreaElasticsearchRepository areaElasticsearchRepository;

    public AreaServiceImpl(BaseJpaRepository<Area, Long> baseJpaRepository) {
        super(baseJpaRepository);
    }

    @Override
    public ResultInfo syncDataToElasticsearch() {
        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        List<Area> list = this.areaRepoitory.findAll(sort);
        if (!CollectionUtils.isEmpty(list)) {
            list.stream().forEach(item -> {
                String oldName = item.getMergerName();
                String newName = null;
                int index = oldName.indexOf("中国,");
                if (index != -1) {
                    newName = oldName.substring(3).replaceAll(",", "-");
                } else {
                    newName = oldName.replaceAll(",", "-");
                }
                item.setMergerName(newName);
            });

            this.areaElasticsearchRepository.deleteAll();
            // 限制条数
            int pointsDataLimit = 1000;
            int size = list.size();
            //判断是否有必要分批
            if(pointsDataLimit < size){
                //分批数
                int part = size/pointsDataLimit;
                for (int i = 0; i < part; i++) {
                    //1000条
                    List<Area> partList = new LinkedList<>(list.subList(0, pointsDataLimit));
                    //剔除
                    list.subList(0, pointsDataLimit).clear();
                    this.areaElasticsearchRepository.saveAll(partList);
                }
                //表示最后剩下的数据
                if (!CollectionUtils.isEmpty(list)) {
                    this.areaElasticsearchRepository.saveAll(list);
                }
            } else {
                this.areaElasticsearchRepository.saveAll(list);
            }
        } else {
            this.areaElasticsearchRepository.deleteAll();
        }
        return ResultUtil.success();
    }


}
