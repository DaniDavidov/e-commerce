package bg.softuni.ecommerce.web;

import bg.softuni.ecommerce.model.dto.brand.BrandDto;
import bg.softuni.ecommerce.model.dto.offer.CreateOfferDto;
import bg.softuni.ecommerce.model.dto.offer.OfferDetailsDto;
import bg.softuni.ecommerce.model.entity.OfferEntity;
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
@RequestMapping("/offers")
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

    @GetMapping("/add")
    public String addOffer(Model model) {
        List<BrandDto> brands = this.brandService.allBrands();
        model.addAttribute("brands", brands);

        return "offer-add";
    }


    @PostMapping("/add")
    public String addOffer(@Valid CreateOfferDto createOfferDto,
                           BindingResult bindingResult,
                           RedirectAttributes redirectAttributes,
                           @AuthenticationPrincipal UserDetails userDetails) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("createOfferDto", createOfferDto);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.createOfferDto", bindingResult);
            return "redirect:/offers/add";
        }

        Long offerId = this.offerService.createOffer(createOfferDto, userDetails);
        return String.format("redirect:/offers/%d/details", offerId);
    }

    @GetMapping("/all")
    public String allOffers(Model model, @PageableDefault(page = 0, size = 5) Pageable pageable) {
        model.addAttribute("offers", this.offerService.getAllOffers(pageable));
        return "offers";
    }

    @GetMapping("/{offerId}/details")
    public String getOfferDetails(@PathVariable("offerId") Long offerId, Model model) {
        OfferEntity offerEntity = this.offerService.getOfferById(offerId);
        if (offerEntity == null) {
            throw new RuntimeException("No such offer");
        }
        OfferDetailsDto offerDetailsDto = this.offerService.mapToOfferDetails(offerEntity);
        model.addAttribute("offerDetails", offerDetailsDto);
        return "offer-details";
    }

    @DeleteMapping("/delete/{id}")
    public String deleteOffer(@PathVariable("id") Long id) {

        offerService.deleteOfferById(id);
        return "redirect:/offers/all";
    }

    @GetMapping("/brand/{id}")
    public String getOffersByBrand(@PathVariable("id") Long brandId,
                                   Model model,
                                   @PageableDefault(page = 0, size = 5) Pageable pageable) {
        model.addAttribute("offers", this.offerService.getOffersByBrandId(pageable, brandId));
        return "offers";
    }
}
