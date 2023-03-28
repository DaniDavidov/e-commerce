package bg.softuni.ecommerce.web.advice;

import bg.softuni.ecommerce.model.error.BrandNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class BrandNotFoundAdvice {

    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ExceptionHandler(BrandNotFoundException.class)
    public ModelAndView onBrandNotFound(BrandNotFoundException ex) {
        ModelAndView modelAndView = new ModelAndView("object-not-found");

        modelAndView.addObject("title", "Brand not found.");
        modelAndView.addObject("message", String.format("A brand with id: %d has not been found.", ex.getId()));

        return modelAndView;
    }
}
