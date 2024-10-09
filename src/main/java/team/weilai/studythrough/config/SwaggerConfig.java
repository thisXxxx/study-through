package team.weilai.studythrough.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket coreApiConfig() {
        return new Docket(DocumentationType.SWAGGER_2)
                .ignoredParameterTypes(HttpSession.class, HttpServletRequest.class, RedirectAttributes.class)
                .apiInfo(adminApiInfo())
                .enable(true)
                .select()
                //需要扫描的控制类包
                .apis(RequestHandlerSelectors.basePackage("team.weilai.studythrough.controller"))
                .build()
                .securityContexts(securityContexts())
                .securitySchemes(securitySchemes());
    }

    private ApiInfo adminApiInfo() {
        return new ApiInfoBuilder()
                .title("学习tong")
                .description("接口描述")
                .version("1.0")
                .build();
    }


    private List<SecurityContext> securityContexts() {
        return new ArrayList(
                Collections.singleton(SecurityContext.builder()
                        .securityReferences(defaultAuth())
                        .forPaths(PathSelectors.regex("^(?!auth).*$"))
                        .build())
        );
    }


    private SecurityContext getContextByPath(String pathRegex) {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                .forPaths(PathSelectors.regex(pathRegex)) // 按照 String 的 matches 方法进行匹配
                .build();
    }

    /**
     * 配置默认的全局鉴权策略；其中返回的 SecurityReference 中，reference 即为 ApiKey 对象里面的name，保持一致才能开启全局鉴权
     * @return SecurityReference
     */
    private List<SecurityReference> defaultAuth() {
        List<SecurityReference> references = new ArrayList<>();
        // scope 参数：
        AuthorizationScope authorizationScope = new AuthorizationScope("global","accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        references.add(new SecurityReference("Authorization",authorizationScopes));
        return references;
    }


    /**
     * securitySchemes
     * 安全体方案
     */
    private List<SecurityScheme> securitySchemes(){
        List<SecurityScheme> apiKeys = new ArrayList<>();
        // 设置请求头信息
        apiKeys.add(new ApiKey("Authorization","token","Header"));
        return apiKeys;
    }

}
