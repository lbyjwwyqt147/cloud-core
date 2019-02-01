package pers.liujunyi.cloud.core.service.authorization.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import pers.liujunyi.cloud.core.domain.authorization.SystemAuthorizationDto;
import pers.liujunyi.cloud.core.entity.authorization.SystemAuthorization;
import pers.liujunyi.cloud.core.repository.jpa.authorization.SystemAuthorizationRepository;
import pers.liujunyi.cloud.core.service.authorization.SystemAuthorizationService;
import pers.liujunyi.cloud.core.util.Constant;
import pers.liujunyi.cloud.core.util.RedisKeys;
import pers.liujunyi.common.dto.BaseQuery;
import pers.liujunyi.common.redis.RedisUtil;
import pers.liujunyi.common.repository.BaseRepository;
import pers.liujunyi.common.restful.ResultInfo;
import pers.liujunyi.common.restful.ResultUtil;
import pers.liujunyi.common.service.impl.BaseServiceImpl;
import pers.liujunyi.common.util.DateTimeUtils;
import pers.liujunyi.common.util.DozerBeanMapperUtil;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
@Service
public class SystemAuthorizationServiceImpl extends BaseServiceImpl<SystemAuthorization, Long> implements SystemAuthorizationService {

    @Autowired
    private SystemAuthorizationRepository systemAuthorizationRepository;
    @Autowired
    private RedisUtil redisUtil;

    public SystemAuthorizationServiceImpl(BaseRepository<SystemAuthorization, Long> baseRepository) {
        super(baseRepository);
    }

    @Override
    public ResultInfo saveRecord(SystemAuthorizationDto record) {
        if (this.checkRepetition(record.getSysCode(), record.getId())) {
            return ResultUtil.params("系统代码重复,请重新输入!");
        }
        String sign =  DigestUtils.md5DigestAsHex(DateTimeUtils.getCurrentDateTimeAsString().getBytes());
        record.setSignature(sign);
        if (record.getExpireTime() == null) {
            record.setExpireTime(DateTimeUtils.additionalYear(10));
        }
        SystemAuthorization systemAuthorization = DozerBeanMapperUtil.copyProperties(record, SystemAuthorization.class);
        SystemAuthorization saveObj =  this.systemAuthorizationRepository.save(systemAuthorization);
        if (saveObj != null && saveObj.getId() != null) {
            record.setId(saveObj.getId());
            this.redisUtil.hset(RedisKeys.SYSTEM_AUTH, saveObj.getSysCode(), JSON.toJSONString(record));
            return ResultUtil.success();
        }
        return ResultUtil.fail();
    }

    @Override
    public ResultInfo updateStatus(Byte status, List<String> sysCodes) {
        int count = this.systemAuthorizationRepository.setStatusBySysCode(status, sysCodes);
        if (count > 0) {
            sysCodes.stream().forEach(item -> {
                SystemAuthorizationDto systemAuthorization = this.jsonToObject(item);
                if (systemAuthorization != null) {
                    systemAuthorization.setStatus(status);
                    this.redisUtil.hset(RedisKeys.SYSTEM_AUTH, item, JSON.toJSONString(systemAuthorization));
                }
            });
            return  ResultUtil.success();
        }
        return ResultUtil.fail();
    }

    @Override
    public ResultInfo dataGrid(BaseQuery query) {
        Map<String, Object> map = this.redisUtil.hgetAll(RedisKeys.SYSTEM_AUTH);
        List<SystemAuthorizationDto> dataList = new LinkedList<>();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            JSONObject json = JSONObject.parseObject(entry.getValue().toString());
            SystemAuthorizationDto systemAuthorization = JSON.toJavaObject(json,SystemAuthorizationDto.class);
            dataList.add(systemAuthorization);
        }
        int size = dataList.size();
        long total = size;
        ResultInfo result =  ResultUtil.success(dataList);
        if (total == 0) {
            result.setMessage(Constant.DATA_GRID_MESSAGE);
        }
        result.setTotal(total);
        return result;
    }

    @Override
    public Boolean authCheck(String systemCode, String signature) {
        boolean result = false;
        SystemAuthorizationDto systemAuthorization = this.jsonToObject(systemCode);
        if (systemAuthorization != null && systemAuthorization.getStatus().byteValue() == Constant.ENABLE_STATUS.byteValue() && systemAuthorization.getSignature().equals(signature)) {
            result = true;
        }
        return result;
    }

    @Override
    public ResultInfo deleteAllBySysCodeIn(List<String> sysCodes) {
        int count = this.systemAuthorizationRepository.deleteAllBySysCodeIn(sysCodes);
        boolean success = false;
        if (count > 0) {
            String[] sysCodeArray = new String[sysCodes.size()];
            this.redisUtil.hdel(RedisKeys.SYSTEM_AUTH, sysCodes.toArray(sysCodeArray));
            success = true;
        }
        return ResultUtil.info(success);
    }

    /**
     * 检查系统代码是否重复
     * @param sysCode
     * @param id
     * @return
     */
    private Boolean checkRepetition(String sysCode, Long id) {
        if (id == null ) {
           return this.checkRedisData(sysCode);
        }
        boolean result = false;
        SystemAuthorization systemAuthorization = this.getOne(id);
        if (systemAuthorization != null && !systemAuthorization.getSysCode().equals(sysCode)) {
            result = this.checkRedisData(sysCode);
        }
        return  result;
    }

    /**
     * 检查redis 中是否存在数据
     * @param sysCode
     * @return
     */
    private Boolean checkRedisData (String sysCode) {
        return this.redisUtil.hexists(RedisKeys.SYSTEM_AUTH, sysCode);
    }

    /**
     *  redis 中数据 转为对象
     * @param sysCode
     * @return
     */
    private SystemAuthorizationDto jsonToObject(String sysCode){
        Object object = this.redisUtil.hget(RedisKeys.SYSTEM_AUTH, sysCode);
        if (object != null) {
            JSONObject json = JSONObject.parseObject(object.toString());
            SystemAuthorizationDto systemAuthorization = JSON.toJavaObject(json,SystemAuthorizationDto.class);
            return systemAuthorization;
        }
        return null;
    }
}
