package space.kuikuioj.kuikuiojbankendcommon.config;

import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import space.kuikuioj.kuikuiojbankendcommon.interceptor.LoginInterceptor;
import space.kuikuioj.kuikuiojbankendcommon.utils.JwtLoginUtils;
import space.kuikuioj.kuikuiojbankendcommon.utils.RedisSetTokenExample;


@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Resource
    private RedisSetTokenExample redisSetTokenExample;
    @Resource
    private JwtLoginUtils jwtLoginUtils;

    // 将 LoginInterceptor 配置为 Bean
    @Bean
    public LoginInterceptor loginInterceptor() {
        return new LoginInterceptor(redisSetTokenExample, jwtLoginUtils);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns( "/", "/v3/api-docs", "/static/**");
    }
}