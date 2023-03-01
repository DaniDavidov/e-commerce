package bg.softuni.ecommerce.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class RegisterController {

    @GetMapping("/users/register")
    public String register() {
        return "auth-register";
    }
}
