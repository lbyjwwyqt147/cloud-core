package pers.liujunyi.cloud.core.configuration;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import springfox.documentation.RequestHandler;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/***
 * 文件名称: Swagger2.java
 * 文件描述: Swagger2  配置类
 * 公 司:
 * 内容摘要:
 * 其他说明:
 * 完成日期:2019年01月17日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
@Configuration
@EnableSwagger2
public class Swagger2Config {

    /**
     * 创建API
     */
    @Bean
    public Docket customDocket() {
        /** 添加head参数 */
        List<Parameter> headerParameters = new ArrayList<>();
        ParameterBuilder tokenPar = new ParameterBuilder();
        tokenPar.name("Authorization").description("Authorization 令牌").modelRef(new ModelRef("string")).parameterType("header").required(false).build();
        headerParameters.add(tokenPar.build());
        ParameterBuilder tenementPar = new ParameterBuilder();
        tenementPar.name("tenement").description("租户").modelRef(new ModelRef("string")).parameterType("header").required(false).build();
        headerParameters.add(tenementPar.build());
        ParameterBuilder signPar = new ParameterBuilder();
        signPar.name("sign").description("签名").modelRef(new ModelRef("string")).parameterType("header").required(false).build();
        headerParameters.add(signPar.build());
        ParameterBuilder subscriberPar = new ParameterBuilder();
        subscriberPar.name("userId").description("当前登录用户ID").modelRef(new ModelRef("Long")).parameterType("header").required(false).build();
        headerParameters.add(subscriberPar.build());

        /** 指定需要扫描 的 controller 包路径   */
        List<String> basePackageList = new ArrayList<>();
        basePackageList.add("pers.liujunyi.cloud.core.controller");
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .enable(true)
                .select()
                // .apis(RequestHandlerSelectors.basePackage("pers.liujunyi.cloud.core.controller"))
                .apis(basePackage(basePackageList))
                .paths(PathSelectors.any())
                .build()
                .securitySchemes(securitySchemes())
                .securityContexts(securityContexts())
                // 将Date类型全部转为String类型
                .directModelSubstitute(Date.class, String.class)
                .genericModelSubstitutes(ResponseEntity.class)
                .globalOperationParameters(headerParameters);

    }

    /**
     * 添加摘要信息
     * 这里是接口的描述配置
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                //页面标题
                .title("文件上传下载服务 RESTful API ")
                //创建人
                .contact(new Contact("ljy", "http://127.0.0.1:18080/swagger-ui.html", ""))
                //版本号
                .version("1.0")
                //描述
                .description("API 描述")
                .build();
    }


    /**
     * 重写basePackage方法，使能够实现多包访问
     * @param basePackage
     * @return com.google.common.base.Predicate<springfox.documentation.RequestHandler>
     */
    public static Predicate<RequestHandler> basePackage(List<String> basePackage) {
        return input -> declaringClass(input).transform(handlerPackage(basePackage)).or(true);
    }

    private static Function<Class<?>, Boolean> handlerPackage(List<String> basePackage)     {
        return input -> {
            // 循环判断匹配
            for (String strPackage : basePackage) {
                boolean isMatch = input.getPackage().getName().startsWith(strPackage);
                if (isMatch) {
                    return true;
                }
            }
            return false;
        };
    }

    private static Optional<? extends Class<?>> declaringClass(RequestHandler input) {
        return Optional.fromNullable(input.declaringClass());
    }


    private List<ApiKey> securitySchemes() {
        List<ApiKey> apiKeys = new ArrayList<>();
        apiKeys.add(new ApiKey("Authorization", "Authorization", "header"));
        return apiKeys;
    }

    private List<SecurityContext> securityContexts() {
        List<SecurityContext> securityContexts = new ArrayList<>();
        securityContexts.add(SecurityContext.builder()
                .securityReferences(defaultAuth())
                .forPaths(PathSelectors.regex("^(?!auth).*$")).build());
        return securityContexts;
    }

    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        List<SecurityReference> securityReferences = new ArrayList<>();
        securityReferences.add(new SecurityReference("Authorization", authorizationScopes));
        return securityReferences;
    }
}

