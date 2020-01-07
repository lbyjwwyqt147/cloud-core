package pers.liujunyi.cloud.core.service.tenement.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pers.liujunyi.cloud.common.redis.RedisTemplateUtils;
import pers.liujunyi.cloud.common.repository.jpa.BaseJpaRepository;
import pers.liujunyi.cloud.common.restful.ResultInfo;
import pers.liujunyi.cloud.common.service.impl.BaseJpaServiceImpl;
import pers.liujunyi.cloud.core.domain.tenement.CientDataSourceConfigureDto;
import pers.liujunyi.cloud.core.entity.tenement.CientDataSourceConfigure;
import pers.liujunyi.cloud.core.repository.jpa.tenement.CientDataSourceConfigureRepository;
import pers.liujunyi.cloud.core.service.tenement.CientDataSourceConfigureService;

import java.util.List;

/***
 * 文件名称: CientDataSourceConfigureServiceImpl.java
 * 文件描述: 租户客户端数据源配置 Service impl
 * 公 司:
 * 内容摘要:
 * 其他说明:
 * 完成日期:2019年01月17日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
@Service
public class CientDataSourceConfigureServiceImpl  extends BaseJpaServiceImpl<CientDataSourceConfigure, Long> implements CientDataSourceConfigureService {

    @Autowired
    private CientDataSourceConfigureRepository cientDataSourceConfigureRepository;
    @Autowired
    private RedisTemplateUtils redisTemplateUtils;

    public CientDataSourceConfigureServiceImpl(BaseJpaRepository<CientDataSourceConfigure, Long> baseRepository) {
        super(baseRepository);
    }


    @Override
    public ResultInfo saveRecord(CientDataSourceConfigureDto record) {
        return null;
    }

    @Override
    public ResultInfo updateStatus(Byte status, List<Long> ids) {
        return null;
    }

    @Override
    public ResultInfo deleteByTenementIdIn(List<Long> tenementIds) {
        return null;
    }
}
