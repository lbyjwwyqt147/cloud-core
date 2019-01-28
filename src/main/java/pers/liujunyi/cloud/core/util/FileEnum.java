package pers.liujunyi.cloud.core.util;

/***
 *
 * @FileName: FileEnum
 * @Company:
 * @author ljy
 * @Date 2019年01月17日
 * @version 1.0.0
 * @remark: 上传文件分类 ENUM
 * @explain
 *
 *
 */
public enum FileEnum {

    IMAGES((byte) 1, "images"), DOCUMENTS((byte) 2, "documents"), VIDEOS((byte) 3, "videos"), ZIPS((byte) 4, "zips"), OTHERS((byte) 5, "others");


    private Byte code;
    private String name;

    FileEnum(Byte code, String name) {
        this.name = name;
        this.code = code;
    }

    public Byte getCode() {
        return code;
    }

    public void setCode(Byte code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
