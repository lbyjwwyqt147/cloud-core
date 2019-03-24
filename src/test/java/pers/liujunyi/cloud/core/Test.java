package pers.liujunyi.cloud.core;

import pers.liujunyi.cloud.core.entity.dict.Dictionaries;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Test {

    public  static  void  main(String[] strings) {
        /*String sign = MD5Utils.getMD5String(DateTimeUtils.getCurrentDateTimeAsString());
        System.out.println(sign);
        String md5 =  DigestUtils.md5DigestAsHex(DateTimeUtils.getCurrentDateTimeAsString().getBytes());
        System.out.println(md5);

        LocalDateTime localDateTime = LocalDateTime.now().plusYears(10);
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zdt = localDateTime.atZone(zoneId);
        Date date = Date.from(zdt.toInstant());
        System.out.println(date);*/
        List<Dictionaries> list = new ArrayList<>();

        Dictionaries dictionaries1 = new Dictionaries();
        dictionaries1.setFullParentCode("1");
        dictionaries1.setDictName("1");

        list.add(dictionaries1);

        Dictionaries dictionaries2 = new Dictionaries();
        dictionaries2.setFullParentCode("1");
        dictionaries2.setDictName("2");

        list.add(dictionaries2);


        Dictionaries dictionaries3 = new Dictionaries();
        dictionaries3.setFullParentCode("10");
        dictionaries3.setDictName("10");

        list.add(dictionaries3);


        Dictionaries dictionaries4 = new Dictionaries();
        dictionaries4.setFullParentCode("10");
        dictionaries4.setDictName("20");

        list.add(dictionaries4);

        Map<String, List<Dictionaries>> parentCodeGroup = list.stream().collect(Collectors.groupingBy(Dictionaries::getFullParentCode));
        System.out.println(parentCodeGroup.size());
    }
}
