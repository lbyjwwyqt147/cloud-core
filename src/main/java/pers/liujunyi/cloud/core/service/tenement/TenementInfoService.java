package pers.liujunyi.cloud.core.service.tenement;

import pers.liujunyi.cloud.common.restful.ResultInfo;
import pers.liujunyi.cloud.common.service.BaseJpaService;
import pers.liujunyi.cloud.core.domain.tenement.TenementInfoDto;
import pers.liujunyi.cloud.core.domain.tenement.TenementQuery;
import pers.liujunyi.cloud.core.entity.tenement.TenementInfo;

import java.util.List;

/***
 * 文件名称: TenementInfoService.java
 * 文件描述: 租户 Service
 * 公 司:
 * 内容摘要:
 * 其他说明:
 * 完成日期:2019年01月17日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
public interface TenementInfoService extends BaseJpaService<TenementInfo, Long> {

    /**
     * 保存数据
     * @param record
     * @return
     */
    ResultInfo saveRecord(TenementInfoDto record);

    /**
     * 修改状态
     * @param status
     * @param ids
     * @return
     */
    ResultInfo updateStatus(Byte status, List<Long> ids);


    /**
     * grid
     * @param query
     * @return
     */
    ResultInfo dataGrid(TenementQuery query);

    /**
     * 检测是否拥有权限
     * @param id 租户ID
     * @param signature  签名
     * @return true:有权限   false:无权限
     */
    Boolean authCheck(Long id, String signature);

    /**
     * 根据 id 批量删除
     * @param ids
     * @return
     */
    ResultInfo deleteBatchById(List<Long> ids);

    /**
     * 同步数据到redis 中
     * @return
     */
    ResultInfo syncDataToRedis();
}
