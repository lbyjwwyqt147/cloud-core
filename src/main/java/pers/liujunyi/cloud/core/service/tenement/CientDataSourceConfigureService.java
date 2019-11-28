package pers.liujunyi.cloud.core.service.tenement;

import pers.liujunyi.cloud.common.restful.ResultInfo;
import pers.liujunyi.cloud.common.service.BaseService;
import pers.liujunyi.cloud.core.domain.tenement.CientDataSourceConfigureDto;
import pers.liujunyi.cloud.core.entity.tenement.CientDataSourceConfigure;

import java.util.List;

/***
 * 文件名称: CientDataSourceConfigureService.java
 * 文件描述: 租户客户端数据源配置 Service
 * 公 司:
 * 内容摘要:
 * 其他说明:
 * 完成日期:2019年01月17日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
public interface CientDataSourceConfigureService  extends BaseService<CientDataSourceConfigure, Long> {

    /**
     * 保存数据
     * @param record
     * @return
     */
    ResultInfo saveRecord(CientDataSourceConfigureDto record);

    /**
     * 修改状态
     * @param status
     * @param ids
     * @return
     */
    ResultInfo updateStatus(Byte status, List<Long> ids);


    /**
     * 根据 租户ID 批量删除
     * @param tenementIds
     * @return
     */
    ResultInfo deleteByTenementIdIn(List<Long> tenementIds);

}
