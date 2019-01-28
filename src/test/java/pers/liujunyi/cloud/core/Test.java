package pers.liujunyi.cloud.core;

import org.springframework.util.DigestUtils;
import pers.liujunyi.common.util.DateTimeUtils;
import pers.liujunyi.common.util.MD5Utils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

public class Test {

    public  static  void  main(String[] strings) {
        String sign = MD5Utils.getMD5String(DateTimeUtils.getCurrentDateTimeAsString());
        System.out.println(sign);
        String md5 =  DigestUtils.md5DigestAsHex(DateTimeUtils.getCurrentDateTimeAsString().getBytes());
        System.out.println(md5);

        LocalDateTime localDateTime = LocalDateTime.now().plusYears(10);
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zdt = localDateTime.atZone(zoneId);
        Date date = Date.from(zdt.toInstant());
        System.out.println(date);
    }
}
