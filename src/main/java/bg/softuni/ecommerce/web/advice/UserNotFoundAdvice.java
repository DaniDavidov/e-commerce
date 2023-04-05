package bg.softuni.ecommerce.web.advice;

import bg.softuni.ecommerce.model.error.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class UserNotFoundAdvice {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(UserNotFoundException.class)
    public ModelAndView onUserNotFound(UserNotFoundException ex) {
        ModelAndView modelAndView = new ModelAndView("object-not-found");

        modelAndView.addObject("title", "User not found.");
        modelAndView.addObject("message", "The user has not been found.");

        return modelAndView;
    }
}
