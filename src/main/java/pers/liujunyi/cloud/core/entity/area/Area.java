package pers.liujunyi.cloud.core.entity.area;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

/***
 * 文件名称: Area.java
 * 文件描述: 区划实体类
 * 公 司:
 * 内容摘要:
 * 其他说明: @Document(indexName = "cloud_core", type = "area", shards = 1, replicas = 0) 默认情况下添加@Document注解会对实体中的所有属性建立索引 indexName elasticsearch 的索引名称 可以理解为数据库名 必须为小写 不然会报org.elasticsearch.indices.InvalidIndexNameException异常  type类型 可以理解为表名
 *          @DynamicInsert属性:设置为true,设置为true,表示insert对象的时候,生成动态的insert语句,如果这个字段的值是null就不会加入到insert语句当中.默认true。
 *          比如希望数据库插入日期或时间戳字段时，在对象字段为空的情况下，表字段能自动填写当前的sysdate。
 *          @DynamicUpdate属性:设置为true,设置为true,表示update对象的时候,生成动态的update语句,如果这个字段的值是null就不会被加入到update语句中,默认true。
 *          比如只想更新某个属性，但是却把整个对象的属性都更新了，这并不是我们希望的结果，我们希望的结果是：我更改了哪些字段，只要更新我修改的字段就够了
 * 完成日期:2019年01月17日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
@Data
@Entity
@NoArgsConstructor
@ToString()
@EqualsAndHashCode(callSuper = false)
@Document(indexName = "cloud_core_area", type = "area", shards = 1, replicas = 0)
@DynamicInsert
@DynamicUpdate
public class Area implements Serializable {
    private static final long serialVersionUID = 891324344152585871L;
    @Id
    /** 编号 */
    private Long id;

    /** 父级 */
    private Long pid;

    /** 名称 */
    @Field(type = FieldType.Auto, index = false)
    private String name;

    /** 全名称 */
    @Field(type = FieldType.Auto, index = false)
    private String mergerName;

    /** 简称 */
    @Field(type = FieldType.Auto, index = false)
    private String shortName;

    /** 全名称简称 */
    @Field(type = FieldType.Auto, index = false)
    private String mergerShortName;

    /** 级别 */
    @Field(type = FieldType.Auto, index = false)
    private Byte levelType;

    /** 城市编号 */
    @Field(type = FieldType.Auto, index = false)
    private String cityCode;

    /** 邮编 */
    @Field(type = FieldType.Auto, index = false)
    private String zipCode;

    /** 拼音 */
    private String pinYin;

    /** 简拼 */
    private String jianPin;

    /** 第一个字符 */
    private String firstChar;

    /** 经度 */
    @Field(type = FieldType.Auto, index = false)
    private String lng;

    /** 纬度 */
    @Field(type = FieldType.Auto, index = false)
    private String lat;

    /** 备注 */
    @Field(type = FieldType.Auto, index = false)
    private String remarks;

    /**  */
    private Date updateTime;
}