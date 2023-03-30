package bg.softuni.ecommerce.config;

import bg.softuni.ecommerce.web.interceptor.BlacklistInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ApplicationConfig implements WebMvcConfigurer {
    private BlacklistInterceptor blacklistInterceptor;

    @Autowired
    public ApplicationConfig(BlacklistInterceptor blacklistInterceptor) {
        this.blacklistInterceptor = blacklistInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(blacklistInterceptor);
        WebMvcConfigurer.super.addInterceptors(registry);
    }
}
