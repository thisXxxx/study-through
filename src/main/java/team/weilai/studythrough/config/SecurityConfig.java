package team.weilai.studythrough.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import team.weilai.studythrough.config.filter.JwtAuthenticationTokenFilter;
import team.weilai.studythrough.config.handler.AccessDeniedHandlerImpl;
import team.weilai.studythrough.config.handler.AuthenticationEntryPointImpl;

import javax.annotation.Resource;
import java.util.Collections;

/**
 * @author gwj
 * @since 2024/5/16 14:46
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Resource
    private JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;
    @Resource
    private AccessDeniedHandlerImpl accessDeniedHandler;
    @Resource
    private AuthenticationEntryPointImpl authenticationEntryPoint;


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                //关闭csrf
                .csrf().disable()
                //不通过Session获取SecurityContext
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                // 对于登录接口 允许未登录时访问，已登录状态不允许访问
                //permitAll是无论是否匿名都允许访问
                .antMatchers("/user/login").permitAll()
                .antMatchers(
                        "/*.html",
                        "/**/*.html",
                        "/**/*.css",
                        "/**/*.js",
                        "/webjars*//**",
                        "/*/api-docs",
                        "/swagger-resources/**"
                ).permitAll()
                // 除上面外的所有请求全部需要鉴权认证
                .anyRequest().authenticated();
        http.addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);
        //配置异常处理器
        http.exceptionHandling().authenticationEntryPoint(authenticationEntryPoint)
                .accessDeniedHandler(accessDeniedHandler);
        http.cors().configurationSource(corsConfiguration());
        return http.build();
    }

    /**
     * Cors 的配置信息 配置+路径
     */
    CorsConfigurationSource corsConfiguration (){
        // Cors配置类
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowCredentials(false); // 是否返回时生成凭证
        corsConfiguration.setAllowedHeaders(Collections.singletonList("*")); // 允许请求携带哪些请求头信息
        corsConfiguration.setAllowedMethods(Collections.singletonList("*")); // 允许哪些类型的请求方法
        corsConfiguration.setAllowedOrigins(Collections.singletonList("*")); // 允许哪些域可以进行方法
        corsConfiguration.setMaxAge(3600L); // 设置预检的最大的时长
        corsConfiguration.setExposedHeaders(Collections.emptyList()); // 设置返回暴露的响应头信息

        // 设置注册URL 配置类
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource() ;
        source.registerCorsConfiguration("/**",corsConfiguration);

        return source;
    }

}
