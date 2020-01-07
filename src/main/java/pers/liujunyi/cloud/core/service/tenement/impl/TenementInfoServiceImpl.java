package pers.liujunyi.cloud.core.service.tenement.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.DigestUtils;
import pers.liujunyi.cloud.common.redis.RedisTemplateUtils;
import pers.liujunyi.cloud.common.repository.jpa.BaseJpaRepository;
import pers.liujunyi.cloud.common.restful.ResultInfo;
import pers.liujunyi.cloud.common.restful.ResultUtil;
import pers.liujunyi.cloud.common.service.impl.BaseJpaServiceImpl;
import pers.liujunyi.cloud.common.util.DateTimeUtils;
import pers.liujunyi.cloud.common.util.DozerBeanMapperUtil;
import pers.liujunyi.cloud.common.util.SystemUtils;
import pers.liujunyi.cloud.common.vo.BaseRedisKeys;
import pers.liujunyi.cloud.core.domain.tenement.TenementInfoDto;
import pers.liujunyi.cloud.core.domain.tenement.TenementQuery;
import pers.liujunyi.cloud.core.entity.tenement.TenementInfo;
import pers.liujunyi.cloud.core.repository.jpa.tenement.TenementInfoRepository;
import pers.liujunyi.cloud.core.service.tenement.TenementInfoService;
import pers.liujunyi.cloud.core.util.Constant;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/***
 * 文件名称: TenementInfoServiceImpl.java
 * 文件描述: 租户信息 Service impl
 * 公 司:
 * 内容摘要:
 * 其他说明:
 * 完成日期:2019年01月17日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
@Service
public class TenementInfoServiceImpl extends BaseJpaServiceImpl<TenementInfo, Long> implements TenementInfoService {

    @Autowired
    private TenementInfoRepository tenementInfoRepository;
    @Autowired
    private RedisTemplateUtils redisTemplateUtils;

    public TenementInfoServiceImpl(BaseJpaRepository<TenementInfo, Long> baseRepository) {
        super(baseRepository);
    }

    @Override
    public ResultInfo saveRecord(TenementInfoDto record) {
        if (this.checkRepetition(record.getTenementPhone(), record.getId())) {
            return ResultUtil.params("手机号已经被注册,请重新输入!");
        }
        if (record.getId() == null) {
            record.setCreateTime(new Date());
        } else {
            record.setUpdateTime(new Date());
        }
        record.setAppId(String.valueOf(System.currentTimeMillis()));
        record.setAppKey(SystemUtils.uuid());
        record.setAppSecret(this.generateSign(record.getAppId(), record.getAppKey()));
        if (record.getExpireTime() == null) {
            record.setExpireTime(DateTimeUtils.additionalYear(10));
        }
        TenementInfo tenementInfo = DozerBeanMapperUtil.copyProperties(record, TenementInfo.class);
        if (StringUtils.isBlank(tenementInfo.getSpecialVersion())) {
            tenementInfo.setSpecialVersion("v1");
        }
        TenementInfo saveObj =  this.tenementInfoRepository.save(tenementInfo);
        if (saveObj != null && saveObj.getId() != null) {
            record.setId(saveObj.getId());
            this.redisTemplateUtils.hset(BaseRedisKeys.LESSEE, saveObj.getTenementPhone(), JSON.toJSONString(record));
            return ResultUtil.success();
        }
        return ResultUtil.fail();
    }

    @Override
    public ResultInfo updateStatus(Byte status, List<Long> ids) {
        int count = this.tenementInfoRepository.setStatusByIds(status, ids);
        if (count > 0) {
            ids.stream().forEach(item -> {
                TenementInfo tenement = this.jsonToObject(item);
                if (tenement != null) {
                    tenement.setStatus(status);
                    this.redisTemplateUtils.hset(BaseRedisKeys.LESSEE, item.toString(), JSON.toJSONString(tenement));
                }
            });
            return  ResultUtil.success();
        }
        return ResultUtil.fail();
    }

    @Override
    public TenementInfo findById(Long id) {
        return this.jsonToObject(id);
    }

    @Override
    public ResultInfo dataGrid(TenementQuery query) {
        Map<String, Object> map = this.redisTemplateUtils.hgetAll(BaseRedisKeys.LESSEE);
        List<TenementInfoDto> dataList = new LinkedList<>();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            JSONObject json = JSONObject.parseObject(entry.getValue().toString());
            TenementInfoDto tenement = JSON.toJavaObject(json, TenementInfoDto.class);
            tenement.setExpireDate(DateTimeUtils.dateFormatYmd(tenement.getExpireTime()));
            dataList.add(tenement);
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
    public Boolean authCheck(Long id, String signature) {
        boolean result = false;
        TenementInfo tenement = this.jsonToObject(id);
        if (tenement != null && tenement.getStatus().byteValue() == Constant.ENABLE_STATUS.byteValue() && tenement.getAppSecret().equals(signature)) {
            result = true;
        }
        return result;
    }

    @Override
    public ResultInfo deleteBatchById(List<Long> ids) {
        long count = this.tenementInfoRepository.deleteByIdIn(ids);
        boolean success = false;
        if (count > 0) {
            String[] tenementIdArray = new String[ids.size()];
            this.redisTemplateUtils.hdel(BaseRedisKeys.LESSEE, ids.toArray(tenementIdArray));
            success = true;
        }
        return ResultUtil.info(success);
    }

    @Override
    public ResultInfo syncDataToRedis() {
        Sort sort =  Sort.by(Sort.Direction.ASC, "id");
        List<TenementInfo>  list = this.tenementInfoRepository.findAll(sort);
        if (!CollectionUtils.isEmpty(list)) {
            this.redisTemplateUtils.del(BaseRedisKeys.LESSEE);
            Map<String, Object> map = new ConcurrentHashMap<>();
            list.stream().forEach(item -> {
                map.put(item.getTenementPhone(), item);
            });
            this.redisTemplateUtils.hmset(BaseRedisKeys.LESSEE, map);
        } else {
            this.redisTemplateUtils.del(BaseRedisKeys.LESSEE);
        }
        return ResultUtil.success();
    }

    /**
     * 检查tenementCode是否重复
     * @param tenementCode
     * @param id
     * @return
     */
    private Boolean checkRepetition(String tenementCode, Long id) {
        if (id == null ) {
           return this.checkTenementCode(tenementCode);
        }
        boolean result = false;
        TenementInfo tenementInfo = this.getOne(id);
        if (tenementInfo != null && !tenementInfo.getTenementPhone().equals(tenementCode)) {
            result = this.checkTenementCode(tenementCode);
        }
        return  result;
    }

    /**
     * 检查 tenementPhone 是否重复
     * @param tenementPhone
     * @return
     */
    private Boolean checkTenementCode (String tenementPhone) {
        TenementInfo tenement = this.tenementInfoRepository.findFirstByTenementPhone(tenementPhone);
        if (tenement != null) {
            return true;
        }
        return false;
    }

    /**
     *  redis 中数据 转为对象
     * @param id
     * @return
     */
    private TenementInfo jsonToObject(Long id){
        Object object = this.redisTemplateUtils.hget(BaseRedisKeys.LESSEE, id.toString());
        if (object != null) {
            JSONObject json = JSONObject.parseObject(object.toString());
            TenementInfo tenement = JSON.toJavaObject(json, TenementInfo.class);
            return tenement;
        }
        return null;
    }

    /**
     * 生成签名
     * @param appId
     * @param appKey
     * @return
     */
    private String generateSign(String appId, String appKey) {
        long time = System.currentTimeMillis();
        StringBuffer signParam = new StringBuffer();
        signParam.append("appid=").append(appId).append("&");
        signParam.append("appkey=").append(appKey).append("&");
        signParam.append("time=").append(time);
        String sign =  DigestUtils.md5DigestAsHex(signParam.toString().getBytes());
        return sign;
    }
}
