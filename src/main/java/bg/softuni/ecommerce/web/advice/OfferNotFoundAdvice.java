package bg.softuni.ecommerce.web.advice;

import bg.softuni.ecommerce.model.error.OfferNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class OfferNotFoundAdvice {

    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ExceptionHandler(OfferNotFoundException.class)
    public ModelAndView onOfferNotFound(OfferNotFoundException ex) {
        ModelAndView modelAndView = new ModelAndView("object-not-found");

        modelAndView.addObject("title", "Book not found.");
        modelAndView.addObject("message", String.format("A book with id: %d has not been found.", ex.getId()));

        return modelAndView;
    }
}
