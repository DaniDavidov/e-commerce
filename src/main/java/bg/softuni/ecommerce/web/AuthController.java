package bg.softuni.ecommerce.web;

import bg.softuni.ecommerce.model.dto.user.UserProfileDto;
import bg.softuni.ecommerce.model.dto.user.UserRegisterDto;
import bg.softuni.ecommerce.model.entity.UserEntity;
import bg.softuni.ecommerce.service.UserService;
import jakarta.validation.Valid;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
public class AuthController {
    private UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @ModelAttribute("userRegisterDto")
    public void initUserRegisterDto(Model model) {
        model.addAttribute("userRegisterDto", new UserRegisterDto());
    }

    @GetMapping("/users/register")
    public String register() {
        return "auth-register";
    }

    @PostMapping("/users/register")
    public String register(@Valid UserRegisterDto userRegisterDto,
                           BindingResult bindingResult,
                           RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {

            redirectAttributes.addFlashAttribute("userRegisterDto", userRegisterDto);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.userRegisterDto", bindingResult);
            return "redirect:/users/register";
        }

        this.userService.register(userRegisterDto);
        return "redirect:/";
    }

    @GetMapping("/users/login")
    public String login() {
        return "auth-login";
    }

    @PostMapping("/users/login-error")
    public String onFailedLogin(
            @ModelAttribute(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY) String username,
            RedirectAttributes redirectAttributes) {

        redirectAttributes.addFlashAttribute(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY, username);
        redirectAttributes.addFlashAttribute("bad_credentials", true);

        return "redirect:/users/login";
    }

    @GetMapping("/users/profile")
    public String profile(Principal principal, Model model) {
        UserEntity user = this.userService.getUserByUsername(principal.getName());
        UserProfileDto userProfileDto = userService.getUserProfile(user);

        model.addAttribute("userProfileDto", userProfileDto);
        return "profile";
    }
}
