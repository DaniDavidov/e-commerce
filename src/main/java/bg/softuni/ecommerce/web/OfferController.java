package bg.softuni.ecommerce.web;

import bg.softuni.ecommerce.model.dto.BrandDto;
import bg.softuni.ecommerce.model.dto.CreateOfferDto;
import bg.softuni.ecommerce.model.dto.OfferDetailsDto;
import bg.softuni.ecommerce.service.BrandService;
import bg.softuni.ecommerce.service.OfferService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class OfferController {
    private final BrandService brandService;
    private final OfferService offerService;

    public OfferController(BrandService brandService, OfferService offerService) {
        this.brandService = brandService;
        this.offerService = offerService;
    }

    @ModelAttribute("createOfferDto")
    public void initCreateOfferModel(Model model) {
        model.addAttribute("createOfferDto", new CreateOfferDto());
    }

    @GetMapping("/offers/add")
    public String addOffer(Model model) {
        List<BrandDto> brands = this.brandService.allBrands();
        model.addAttribute("brands", brands);

        return "offer-add";
    }


    @PostMapping("/offers/add")
    public String addOffer(@Valid CreateOfferDto createOfferDto,
                           BindingResult bindingResult,
                           RedirectAttributes redirectAttributes,
                           @AuthenticationPrincipal UserDetails userDetails) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("createOfferDto", createOfferDto);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.createOfferDto", bindingResult);
            return "redirect:/offers/add";
        }

        this.offerService.createOffer(createOfferDto, userDetails);
        return "redirect:/";
    }

    @GetMapping("/offers/all")
    public String allOffers(Model model, @PageableDefault(page = 0, size = 5) Pageable pageable) {
        model.addAttribute("offers", this.offerService.getAllOffers(pageable));
        return "offers";
    }

    @GetMapping("/offers/{offerId}/details")
    public String getOfferDetails(@PathVariable("offerId") Long offerId, Model model) {
        OfferDetailsDto offerDetailsDto = this.offerService.getOfferById(offerId);
        model.addAttribute("offerDetails", offerDetailsDto);
        return "offer-details";
    }

    @DeleteMapping("/offers/delete/{id}")
    public String deleteOffer(@PathVariable("id") Long id) {

        offerService.deleteOfferById(id);
        return "redirect:/offers/all";
    }

}
