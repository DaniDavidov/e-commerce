package bg.softuni.ecommerce.web;

import bg.softuni.ecommerce.model.dto.user.UserDetailsDto;
import bg.softuni.ecommerce.model.entity.UserEntity;
import bg.softuni.ecommerce.service.BlacklistService;
import bg.softuni.ecommerce.service.UserService;
import bg.softuni.ecommerce.web.interceptor.BlacklistInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final BlacklistService blacklistService;
    private final BlacklistInterceptor blacklistInterceptor;

    @Autowired
    public UserController(UserService userService, BlacklistService blacklistService, BlacklistInterceptor blacklistInterceptor) {
        this.userService = userService;
        this.blacklistService = blacklistService;
        this.blacklistInterceptor = blacklistInterceptor;
    }

    @GetMapping("/{id}/details")
    public String getUserDetails(@PathVariable("id") Long userId, Model model) {
        UserEntity userEntity = this.userService.getUserById(userId);
        UserDetailsDto userDetails = this.userService.getUserDetails(userEntity);
        boolean isBlacklisted = blacklistService.isUserBlacklisted(userEntity.getUsername());

        model.addAttribute("userDetails", userDetails);
        model.addAttribute("isBlacklisted", isBlacklisted);
        return "user-details";
    }

    @PostMapping("/blacklist/add/{id}")
    public String addToBlacklist(@PathVariable("id") Long userId) {
        UserEntity userEntity = this.userService.getUserById(userId);

        blacklistService.addToBlacklist(userEntity);
        return String.format("redirect:/users/%d/details", userId);
    }

    @PostMapping("/blacklist/remove/{id}")
    public String removeFromBlacklist(@PathVariable("id") Long userId) {
        UserEntity userEntity = this.userService.getUserById(userId);

        blacklistService.removeFromBlacklist(userEntity);
        return String.format("redirect:/users/%d/details", userId);
    }
}
