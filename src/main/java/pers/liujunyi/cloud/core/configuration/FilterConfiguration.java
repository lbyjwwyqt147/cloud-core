package pers.liujunyi.cloud.core.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import pers.liujunyi.cloud.common.encrypt.filter.SignAuthFilter;

/***
 * 过滤器 配置
 *
 * @author ljy
 */
@Configuration
@Order(20)
public class FilterConfiguration {

    /**  AES 密匙 */
    @Value("${spring.encrypt.secretKey}")
    private String secretKey;

    /**  签名过期分钟数  */
    @Value("${spring.encrypt.signExpireTime}")
    private Integer signExpireMinute;

    /**
     *  签名认证过滤器
     * @return
     */
    @Bean
    public SignAuthFilter signAuthFilter() {
        return new SignAuthFilter(this.secretKey, this.signExpireMinute);
    }

    /**
     * 注册 签名认证过滤器
     * @return
     */
    @Bean
    public FilterRegistrationBean signAuthFilterRegistration(
            SignAuthFilter signAuthFilter) {
        FilterRegistrationBean bean = new FilterRegistrationBean(signAuthFilter);
        bean.setOrder(1);
        return bean;
    }
}
