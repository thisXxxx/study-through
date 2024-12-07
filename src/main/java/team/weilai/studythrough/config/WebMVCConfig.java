package team.weilai.studythrough.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import team.weilai.studythrough.config.interceptor.LessonInterceptor;
import team.weilai.studythrough.mapper.LessonMapper;
import team.weilai.studythrough.mapper.LessonStuMapper;

import javax.annotation.Resource;


@Configuration
@EnableSwagger2
public class WebMVCConfig extends WebMvcConfigurationSupport {
    @Resource
    private LessonStuMapper lessonStuMapper;
    @Resource
    private LessonMapper lessonMapper;


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry){
        //开放swagger静态资源，防止被拦截
        registry.addResourceHandler("/**")
                        .addResourceLocations("classpath:/static/");
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
        super.addResourceHandlers(registry);
    }


    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LessonInterceptor(lessonMapper,lessonStuMapper)).addPathPatterns(
                "/tea/listFile",
                "/tea/makeDir",
                "/tea/makeFile",
                "/exam/query"
        );

    }

}
