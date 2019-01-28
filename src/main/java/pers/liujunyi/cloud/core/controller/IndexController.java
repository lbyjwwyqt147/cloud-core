package pers.liujunyi.cloud.core.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import pers.liujunyi.common.restful.ResultInfo;
import pers.liujunyi.common.restful.ResultUtil;

/***
 * 文件名称: IndexController.java
 * 文件描述: 访问系统访问默认数据
 * 公 司:
 * 内容摘要:
 * 其他说明:
 * 完成日期:2019年01月17日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
@RestController
public class IndexController {

    /**
     * 系统访问url
     */
    @Value("${data.indexView}")
    private String indexView;

    @GetMapping(value = "/")
    public ResultInfo index() {
        return ResultUtil.resultInfo(200, indexView, null, true);
    }
}
