package pers.liujunyi.cloud.core.service.file;


import pers.liujunyi.cloud.common.restful.ResultInfo;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/***
 * 文件名称: FlieDownloadService.java
 * 文件描述: 文件下载 service
 * 公 司:
 * 内容摘要:
 * 其他说明:
 * 完成日期:2019年01月17日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
public interface FlieDownloadService {

    /**
     * 文件下载
     *
     * @param id       文件id
     * @param response
     * @return
     */
    ResultInfo downloadFile(Long id, HttpServletResponse response);

    /**
     * 多个文件打包下载
     *
     * @param ids      一组文件ID
     * @param response
     * @return
     */
    ResultInfo downloadZipFile(List<Long> ids, HttpServletResponse response);
}
