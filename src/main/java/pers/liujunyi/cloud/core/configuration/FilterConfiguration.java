package pers.liujunyi.cloud.core.configuration;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pers.liujunyi.cloud.common.encrypt.filter.SignAuthFilter;

/***
 * 过滤器 配置
 *
 * @author ljy
 */
@Configuration
public class FilterConfiguration {

    /**
     *  签名认证过滤器
     * @return
     */
    @Bean
    public SignAuthFilter signAuthFilter() {
        return new SignAuthFilter();
    }

    /**
     * 注册 签名认证过滤器
     * @return
     */
    @Bean
    public FilterRegistrationBean signAuthFilterRegistration(
            SignAuthFilter signAuthFilter) {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean(signAuthFilter);
        registrationBean.addUrlPatterns("/api/*");
        //过滤应用程序中所有资源,当前应用程序根下的所有文件包括多级子目录下的所有文件，注意这里*前有“/”
        //registration.addUrlPatterns("/*");
        //过滤指定的类型文件资源, 当前应用程序根目录下的所有html文件，注意：*.html前没有“/”,否则错误
       // registration.addUrlPatterns(".html");
        //过滤指定的目录下的所有文件,当前应用程序根目录下的folder_name子目录（可以是多级子目录）下所有文件
       // registration.addUrlPatterns("/folder_name/*");
        //过滤指定文件
       // registration.addUrlPatterns("/index.html");
        registrationBean.setOrder(1);
        return registrationBean;
    }
}
