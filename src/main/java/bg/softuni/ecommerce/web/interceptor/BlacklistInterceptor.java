package bg.softuni.ecommerce.web.interceptor;


import bg.softuni.ecommerce.service.BlacklistService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.View;
import org.thymeleaf.spring6.view.ThymeleafViewResolver;

import java.security.Principal;
import java.util.Locale;
import java.util.Map;

@Component
public class BlacklistInterceptor implements HandlerInterceptor{
//
    private final BlacklistService blacklistService;
    private final ThymeleafViewResolver thymeleafViewResolver;

    @Autowired
    public BlacklistInterceptor(BlacklistService blacklistService, ThymeleafViewResolver thymeleafViewResolver) {
        this.blacklistService = blacklistService;
        this.thymeleafViewResolver = thymeleafViewResolver;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Principal userPrincipal = request.getUserPrincipal();

        if (userPrincipal != null && blacklistService.isUserBlacklisted(userPrincipal.getName())) {
            View blockedView = thymeleafViewResolver.resolveViewName("blocked", Locale.getDefault());
            if (blockedView != null) {
                blockedView.render(Map.of(), request, response);
            }
            return false;
        }
        return true;
    }
    
}
