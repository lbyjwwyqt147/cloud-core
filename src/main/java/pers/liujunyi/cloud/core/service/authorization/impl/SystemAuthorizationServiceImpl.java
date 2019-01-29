package pers.liujunyi.cloud.core.service.authorization.impl;

import com.alibaba.fastjson.JSON;
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
import pers.liujunyi.common.restful.ResultInfo;
import pers.liujunyi.common.restful.ResultUtil;
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
public class SystemAuthorizationServiceImpl implements SystemAuthorizationService {

    @Autowired
    private SystemAuthorizationRepository systemAuthorizationRepository;
    @Autowired
    private RedisUtil redisUtil;

    @Override
    public ResultInfo saveRecord(SystemAuthorizationDto record) {
        Object object = this.redisUtil.hget(RedisKeys.SYSTEM_AUTH, record.getSysCode());
        if (object != null) {
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
            this.redisUtil.hsetnx(RedisKeys.SYSTEM_AUTH, saveObj.getSysCode(), JSON.toJSONString(record));
            return ResultUtil.success();
        }
        return ResultUtil.fail();
    }

    @Override
    public ResultInfo updateStatus(Byte status, List<String> sysCodes) {
        long count = 0;
        int number = this.systemAuthorizationRepository.setStatusBySysCode(status, sysCodes);
        if (number > 0) {
            sysCodes.stream().forEach(item -> {
                Object object = redisUtil.hget(RedisKeys.SYSTEM_AUTH, item);
                SystemAuthorizationDto systemAuthorization = JSON.parseObject(JSON.toJSONString(object), SystemAuthorizationDto.class);
                systemAuthorization.setStatus(status);
                this.redisUtil.hset(RedisKeys.SYSTEM_AUTH, item, JSON.toJSONString(systemAuthorization));
            });
        }
        if (count > 0) {
            return  ResultUtil.success();
        }
        return ResultUtil.fail();
    }

    @Override
    public ResultInfo dataGrid(BaseQuery query) {
        Map<String, Object> map = this.redisUtil.hgetAll(RedisKeys.SYSTEM_AUTH);
        List<SystemAuthorizationDto> dataList = new LinkedList<>();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            SystemAuthorizationDto systemAuthorization = JSON.parseObject(JSON.toJSONString(entry.getValue()), SystemAuthorizationDto.class);
            dataList.add(systemAuthorization);
        }
        int size = dataList.size();
        long total = size;
        ResultInfo result =  ResultUtil.success(dataList);
        result.setTotal(total);
        return result;
    }

    @Override
    public Boolean authCheck(String systemCode, String signature) {
        boolean result = false;
        Object object = redisUtil.hget(RedisKeys.SYSTEM_AUTH, systemCode);
        if (object != null) {
            SystemAuthorizationDto systemAuthorization = JSON.parseObject(JSON.toJSONString(object), SystemAuthorizationDto.class);
            if (systemAuthorization != null && systemAuthorization.getStatus().byteValue() == Constant.ENABLE_STATUS.byteValue() && systemAuthorization.getSignature().equals(signature)) {
                result = true;
            }
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

    @Override
    public Boolean deleteAllByIdIn(List<Long> ids) {
        int count = this.systemAuthorizationRepository.deleteAllByIdIn(ids);
        return count > 0 ? true : false;
    }

    @Override
    public Boolean deleteById(Long id) {
        this.systemAuthorizationRepository.deleteById(id);
        return true;
    }

    @Override
    public List<SystemAuthorization> findByIdIn(List<Long> ids) {
        return this.systemAuthorizationRepository.findByIdIn(ids);
    }
}
