package bg.softuni.ecommerce.web;

import bg.softuni.ecommerce.model.dto.user.UserDetailsDto;
import bg.softuni.ecommerce.model.entity.UserEntity;
import bg.softuni.ecommerce.model.entity.enums.UserRoleEnum;
import bg.softuni.ecommerce.service.BlacklistService;
import bg.softuni.ecommerce.service.UserService;
import bg.softuni.ecommerce.web.interceptor.BlacklistInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
        List<String> roles = Arrays.stream(UserRoleEnum.values())
                .map(Enum::name)
                .collect(Collectors.toList());

        model.addAttribute("userDetails", userDetails);
        model.addAttribute("isBlacklisted", isBlacklisted);
        model.addAttribute("rolesToBeChosen", roles);
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

    @GetMapping("/all")
    public String allUsers(Model model, @PageableDefault(page = 0, size = 20) Pageable pageable) {
        Page<UserDetailsDto> users = this.userService.getAllUsers(pageable);
        model.addAttribute("users", users);
        return "users";
    }

    @PostMapping("/toModerator/{id}")
    public String promoteToModerator(@PathVariable("id") Long id) {
        this.userService.promoteToModerator(id);

        return String.format("redirect:/users/%d/details",id);
    }

    @PostMapping("/toUser/{id}")
    public String demoteToUser(@PathVariable("id") Long id) {
        this.userService.demoteToUser(id);

        return String.format("redirect:/users/%d/details",id);
    }

}
