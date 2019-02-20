package pers.liujunyi.cloud.core.entity.dict;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import pers.liujunyi.common.entity.BaseEntity;

import javax.persistence.Entity;

/***
 * 文件名称: Dictionaries.java
 * 文件描述: 数据字典实体类
 * 公 司:
 * 内容摘要:
 *      elasticsearch 配置信息
 *     document  ==》文档 ==》一个文档相当于Mysql一行的数据
 *  　index ==》索引 ==》Mysql中的一个库，库里面可以建立很多表，存储不同类型的数据，而表在ES中就是type。
 *   type ==》类型 ==》相当于Mysql中的一张表，存储json类型的数据
 *   field ==》列 ==》相当于mysql中的列，也就是一个属性
 * 其他说明: @Document(indexName = "cloud_core", type = "dictionaries", shards = 1, replicas = 0) 默认情况下添加@Document注解会对实体中的所有属性建立索引  indexName elasticsearch 的索引名称 可以理解为数据库名 必须为小写 不然会报org.elasticsearch.indices.InvalidIndexNameException异常  type类型 可以理解为表名
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
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Document(indexName = "cloud_core", type = "dictionaries", shards = 1, replicas = 0)
@DynamicInsert
@DynamicUpdate
public class Dictionaries extends BaseEntity {
    private static final long serialVersionUID = 4273145793432055139L;

    /** 字典代码 */
    private String dictCode;

    /** 字典名称 */
    private String dictName;

    /** 上级ID */
    private Long pid;

    /** 所属系统编码  例如：1001 相册管理系统 */
    private String systemCode;

    @Field(index = false)
    /** 优先级 */
    private Integer priority;

    /** 标签标注 */
    private String dictLabel;

    /** 描述信息 */
    @Field(index = false)
    private String description;

    /** 0: 启动 1：禁用  */
    private Byte status;

    /** 叶子  0:存在叶子节点  1： 不存在 */
    @Field(index = false)
    private Byte leaf = 1;
}