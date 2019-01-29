package pers.liujunyi.cloud.core.repository.jpa.authorization;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import pers.liujunyi.cloud.core.entity.authorization.SystemAuthorization;
import pers.liujunyi.common.repository.BaseRepository;

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
public interface SystemAuthorizationRepository extends BaseRepository<SystemAuthorization, Long> {

    /**
     * 修改状态
     * @param status  0:启动 1：禁用
     * @param ids
     * @return
     */
    @Modifying
    @Query("update SystemAuthorization u set u.status = ?1 where u.id in (?2)")
    int setStatusByIds(Byte status, List<Long> ids);


    /**
     * 修改状态
     * @param status  0:启动 1：禁用
     * @param sysCodes
     * @return
     */
    @Modifying
    @Query("update SystemAuthorization u set u.status = ?1 where u.sysCode in (?2)")
    int setStatusBySysCode(Byte status, List<String> sysCodes);

    /**
     * 根据ID 批量删除
     * @param ids
     * @return
     */
    @Modifying
    @Query("delete from SystemAuthorization u where u.id in (?1)")
    int deleteAllByIdIn(List<Long> ids);

    /**
     * 根据sysCode 批量删除
     * @param sysCodes
     * @return
     */
    @Modifying
    @Query("delete from SystemAuthorization u where u.sysCode in (?1)")
    int deleteAllBySysCodeIn(List<String> sysCodes);



}
