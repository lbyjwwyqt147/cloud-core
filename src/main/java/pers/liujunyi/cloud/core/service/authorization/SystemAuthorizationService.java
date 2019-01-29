package pers.liujunyi.cloud.core.service.authorization;

import pers.liujunyi.cloud.core.domain.authorization.SystemAuthorizationDto;
import pers.liujunyi.cloud.core.entity.authorization.SystemAuthorization;
import pers.liujunyi.common.dto.BaseQuery;
import pers.liujunyi.common.restful.ResultInfo;
import pers.liujunyi.common.service.BaseService;

import java.util.List;

/***
 * 文件名称: SystemAuthorizationService.java
 * 文件描述: 系统授权 Service
 * 公 司:
 * 内容摘要:
 * 其他说明:
 * 完成日期:2019年01月17日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
public interface SystemAuthorizationService extends BaseService<SystemAuthorization, Long> {

    /**
     * 保存数据
     * @param record
     * @return
     */
    ResultInfo saveRecord(SystemAuthorizationDto record);

    /**
     * 修改状态
     * @param status
     * @param sysCodes
     * @return
     */
    ResultInfo updateStatus(Byte status, List<String> sysCodes);

    /**
     * grid
     * @param query
     * @return
     */
    ResultInfo dataGrid(BaseQuery query);

    /**
     * 检测是否拥有权限
     * @param systemCode 系统编码
     * @param signature  签名
     * @return true:有权限   false:无权限
     */
    Boolean authCheck(String systemCode, String signature);

    /**
     * 根据sysCode 批量删除
     * @param sysCodes
     * @return
     */
    ResultInfo deleteAllBySysCodeIn(List<String> sysCodes);
}
