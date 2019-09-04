package pers.liujunyi.cloud.core.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.support.GenericConversionService;
import org.springframework.web.bind.support.ConfigurableWebBindingInitializer;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import pers.liujunyi.cloud.common.configuration.CustomRequestMappingHandlerMapping;
import pers.liujunyi.cloud.common.configuration.DateConverterConfig;
import pers.liujunyi.cloud.core.util.FileEnum;

import javax.annotation.PostConstruct;
import java.util.Arrays;

/***
 * 文件名称: WebConfiguration.java
 * 文件描述:
 * 公 司:
 * 内容摘要:
 * 其他说明:
 * 完成日期:2019年01月17日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
@Configuration
public class WebConfiguration extends WebMvcConfigurationSupport {

    @Autowired
    private RequestMappingHandlerAdapter handlerAdapter;

    /**
     * 在配置文件中配置的文件保存路径
     */
    @Value("${data.file.dir}")
    private String flieRelativePath;
    /**
     * 跨域设置
     */
    @Value("${data.allowedOrigin}")
    private String allowedOrigin;

    /**
     * 注册资源
     *
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //addResourceHandler是指你想通过url请求的路径
        //addResourceLocations是文件存放的真实路径
        registry.addResourceHandler("/" + FileEnum.IMAGES.getName() + "/**").addResourceLocations("file:" + flieRelativePath + "/" + FileEnum.IMAGES.getName() + "/");
        registry.addResourceHandler("/" + FileEnum.DOCUMENTS.getName() + "/**").addResourceLocations("file:" + flieRelativePath + "/" + FileEnum.DOCUMENTS.getName() + "/");
        registry.addResourceHandler("/" + FileEnum.VIDEOS.getName() + "/**").addResourceLocations("file:" + flieRelativePath + "/" + FileEnum.VIDEOS.getName() + "/");
        registry.addResourceHandler("/" + FileEnum.ZIPS.getName() + "/**").addResourceLocations("file:" + flieRelativePath + "/" + FileEnum.ZIPS.getName() + "/");
        registry.addResourceHandler("/" + FileEnum.OTHERS.getName() + "/**").addResourceLocations("file:" + flieRelativePath + "/" + FileEnum.OTHERS.getName() + "/");
        // 注册swagger 资源  不然访问swagger-ui.html 是404
        registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
        registry.addResourceHandler("/**").addResourceLocations("classpath:/static/");
        super.addResourceHandlers(registry);
    }


    /**
     * 跨域设置
     * @return
     */
    @Bean
    public FilterRegistrationBean corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(Arrays.asList(allowedOrigin.trim().split(",")));
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        config.setAllowCredentials(false);
        config.setMaxAge(3600L);
        source.registerCorsConfiguration("/**", config);
        FilterRegistrationBean bean = new FilterRegistrationBean(new CorsFilter(source));
        bean.setOrder(1);
        return bean;
    }


    @Override
    @Bean
    public RequestMappingHandlerMapping requestMappingHandlerMapping() {
        RequestMappingHandlerMapping handlerMapping = new CustomRequestMappingHandlerMapping();
        handlerMapping.setOrder(0);
        handlerMapping.setInterceptors(getInterceptors());
        return handlerMapping;
    }


    /**
     *  前端传递的日期字符串 自动转换为 Data 类型
     * @return
     */
    @PostConstruct
    public void initEditableAvlidation() {
        ConfigurableWebBindingInitializer initializer = (ConfigurableWebBindingInitializer)handlerAdapter.getWebBindingInitializer();
        if (initializer.getConversionService() != null) {
            GenericConversionService genericConversionService = (GenericConversionService)initializer.getConversionService();
            genericConversionService.addConverter(new DateConverterConfig());
        }
    }

}
