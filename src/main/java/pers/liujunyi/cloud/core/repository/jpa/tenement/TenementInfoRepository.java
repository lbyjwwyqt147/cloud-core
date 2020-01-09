package pers.liujunyi.cloud.core.repository.jpa.tenement;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import pers.liujunyi.cloud.common.repository.jpa.BaseJpaRepository;
import pers.liujunyi.cloud.core.entity.tenement.TenementInfo;

import java.util.List;

/***
 * 文件名称: SystemAuthorizationRepository.java
 * 文件描述: 系统授权 Repository
 * 公 司:
 * 内容摘要:
 * 其他说明:
 * 完成日期:2019年01月17日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
public interface TenementInfoRepository extends BaseJpaRepository<TenementInfo, Long> {

    /**
     * 修改状态
     * @param status  0:启动 1：禁用
     * @param ids
     * @return
     */
    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    @Modifying(clearAutomatically = true)
    @Query("update TenementInfo u set u.status = ?1 where u.id in (?2)")
    int setStatusByIds(Byte status, List<Long> ids);

    /**
     *
     * @param tenementPhone
     * @return
     */
    TenementInfo findFirstByTenementPhone(String tenementPhone);

    /**
     *
     * @param folder
     * @return
     */
    TenementInfo findFirstByFolder(String folder);
}
