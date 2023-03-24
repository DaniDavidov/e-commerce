package bg.softuni.ecommerce.web;

import bg.softuni.ecommerce.model.dto.UserDetailsDto;
import bg.softuni.ecommerce.model.entity.UserEntity;
import bg.softuni.ecommerce.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users/{id}/details")
    public String getUserDetails(@PathVariable("id") Long userId, Model model) {
        UserEntity userEntity = this.userService.getUserById(userId);
        UserDetailsDto userDetails = this.userService.getUserDetails(userEntity);

        model.addAttribute("userDetails", userDetails);
        return "user-details";
    }
}
