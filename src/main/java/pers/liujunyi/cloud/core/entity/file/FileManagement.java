package pers.liujunyi.cloud.core.entity.file;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
public class FileManagement implements Serializable {

    private static final long serialVersionUID = 6188588137218482700L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 文件初始名称(上传文件的原始名称)
     */
    private String fileInitialName;

    /**
     * 文件名称
     */
    private String fileName;

    /**
     * 文件所在目录
     */
    private String fileDirectory;

    /**
     * 文件具体位置
     */
    private String filePath;

    /**
     * 文件访问地址
     */
    private String fileCallAddress;

    /**
     * 文件大小（kb）
     */
    private Double fileSize;

    /**
     * 文件后缀
     */
    private String fileSuffix;

    /**
     * 文件分类 0：图片 1：文档  2：视频  5：其他
     */
    private Byte fileCategory;

    /**
     * 所属系统编码  例如：1001 相册管理系统
     */
    private String systemCode;

    /**
     * 所属系统业务模块编码 例如：10：相册管理  20：视频管理  30： 博文管理
     */
    private String businessCode;

    /**
     * 所属系统业务模块中的字段
     */
    private String businessField;

    /**
     * 优先级
     */
    private Byte priority;

    /**
     * 描述信息
     */
    private String description;

    /**
     * 备注
     */
    private String remarks;

    /**
     * 上传者ID
     */
    private Long uploaderId;

    /**
     * 上传者名称
     */
    private String uploaderName;

    /**
     * 上传时间
     */
    private Date uploadTime;

    /**
     * 扩展字段1
     */
    private String extensionOne;

    /**
     * 扩展字段2
     */
    private String extensionTwo;

    /**
     * 扩展字段3
     */
    private String extensionThree;

    /**
     * 扩展字段4
     */
    private String extensionFour;

}