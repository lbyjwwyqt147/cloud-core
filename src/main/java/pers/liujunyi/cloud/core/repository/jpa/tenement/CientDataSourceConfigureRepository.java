package pers.liujunyi.cloud.core.repository.jpa.tenement;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import pers.liujunyi.cloud.common.repository.jpa.BaseJpaRepository;
import pers.liujunyi.cloud.core.entity.tenement.CientDataSourceConfigure;

import java.util.List;

/***
 * 文件名称: CientDataSourceConfigureRepository.java
 * 文件描述: 租户客户端数据源配置 Repository
 * 公 司:
 * 内容摘要:
 * 其他说明:
 * 完成日期:2019年01月17日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
public interface CientDataSourceConfigureRepository extends BaseJpaRepository<CientDataSourceConfigure, Long> {

    /**
     * 修改状态
     * @param status  0:启动 1：禁用
     * @param ids
     * @return
     */
    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    @Modifying(clearAutomatically = true)
    @Query("update CientDataSourceConfigure u set u.status = ?1 where u.id in (?2)")
    int setStatusByIds(Byte status, List<Long> ids);


    /**
     * 根据 租户ID 批量删除
     * @param tenementIds
     * @return
     */
    int deleteByTenementIdIn(List<Long> tenementIds);
}
