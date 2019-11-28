package pers.liujunyi.cloud.core.entity.file;

import lombok.*;
import org.hibernate.annotations.Table;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/***
 * 文件名称: FileManagement.java
 * 文件描述: 文件管理实体类
 * 公 司:
 * 内容摘要:
 * 其他说明:
 * 完成日期:2019年01月17日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = false)
@EqualsAndHashCode(callSuper = false)
@Table(appliesTo = "file_management", comment = "附件(文件)信息表")
public class FileManagement implements Serializable {

    private static final long serialVersionUID = 6188588137218482700L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 文件初始名称(上传文件的原始名称)
     */
    @Column(length = 128, nullable = false, columnDefinition="varchar(128) NOT NULL COMMENT '上传文件的原始名称'")
    private String fileInitialName;

    /**
     * 最新文件名(上传文件后的名称)
     */
    @Column(length = 45, nullable = false, columnDefinition="varchar(45) NOT NULL COMMENT '上传文件后的名称'")
    private String fileName;

    /**
     * 文件所在目录(文件夹)
     */
    @Column(length = 200, nullable = false, columnDefinition="varchar(200) NOT NULL COMMENT '文件所在目录(文件夹)'")
    private String fileDirectory;

    /**
     * 文件具体位置
     */
    @Column(nullable = false, columnDefinition="varchar(255) NOT NULL COMMENT '文件具体位置'")
    private String filePath;

    /**
     * 文件访问地址
     */
    @Column(length = 300, nullable = false, columnDefinition="varchar(300) NOT NULL COMMENT '文件访问地址'")
    private String fileCallAddress;

    /**
     * 文件大小（kb）
     */
    private Double fileSize;

    /**
     * 文件后缀
     */
    @Column(length = 6, nullable = false, columnDefinition="varchar(6) NOT NULL COMMENT '文件后缀'")
    private String fileSuffix;

    /**
     * 文件分类 0：图片 1：文档  2：视频  5：其他
     */
    @Column(columnDefinition="tinyint(4) DEFAULT '0' COMMENT '文件分类  0：图片 1：文档  2：视频  5：其他'")
    private Byte fileCategory;


    /**
     * 所属系统业务模块编码 例如：10：相册管理  20：视频管理  30： 博文管理
     */
    @Column(columnDefinition="varchar(15) NOT NULL COMMENT '所属系统业务模块编码 例如：10：相册管理  20：视频管理  30： 博文管理'")
    private String businessCode;

    /**
     * 所属系统业务模块中的字段
     */
    @Column(columnDefinition="varchar(32) DEFAULT NULL COMMENT '所属系统业务模块中的字段'")
    private String businessField;

    /**
     * 排序值
     */
    @Column(columnDefinition="tinyint(4) DEFAULT '10' COMMENT '排序值'")
    private Byte priority;

    /**
     * 描述信息
     */
    @Column(columnDefinition="varchar(50) DEFAULT NULL COMMENT '描述信息'")
    private String description;

    /**
     * 备注
     */
    @Column(columnDefinition="varchar(50) DEFAULT NULL COMMENT '备注'")
    private String remarks;

    /**
     * 上传者ID
     */
    @Column(columnDefinition="bigint(20) DEFAULT NULL COMMENT '上传者ID'")
    private Long uploaderId;

    /**
     * 上传者名称
     */
    @Column(columnDefinition="varchar(32) DEFAULT NULL COMMENT '上传者名称'")
    private String uploaderName;

    /**
     * 上传时间
     */
    @Column(columnDefinition="timestamp NOT NULL  COMMENT '上传时间'")
    private Date uploadTime;

    /**
     * 文件唯一签名
     */
    @Column(columnDefinition="varchar(128) DEFAULT NULL COMMENT '文件唯一签名'")
    private String fileSignature;

    /**
     * 扩展字段1
     */
    @Column(columnDefinition="varchar(45) DEFAULT NULL COMMENT '扩展字段1'")
    private String extensionOne;

    /**
     * 扩展字段2
     */
    @Column(columnDefinition="varchar(65) DEFAULT NULL COMMENT '扩展字段2'")
    private String extensionTwo;

    /**
     * 扩展字段3
     */
    @Column(columnDefinition="varchar(100) DEFAULT NULL COMMENT '扩展字段3'")
    private String extensionThree;

    /** 租户ID */
    @Column(columnDefinition="bigint(20) NOT NULL COMMENT '租户ID'")
    private Long tenementId;

    /** 租户名称  */
    @Column(columnDefinition="varchar(32) NOT NULL COMMENT '租户名称'")
    private String tenementName;

}