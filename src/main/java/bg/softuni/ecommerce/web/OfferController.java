package bg.softuni.ecommerce.web;

import bg.softuni.ecommerce.model.dto.brand.BrandDto;
import bg.softuni.ecommerce.model.dto.offer.CreateOfferDto;
import bg.softuni.ecommerce.model.dto.offer.OfferDetailsDto;
import bg.softuni.ecommerce.model.entity.OfferEntity;
import bg.softuni.ecommerce.model.mapper.OfferMapper;
import bg.softuni.ecommerce.service.BrandService;
import bg.softuni.ecommerce.service.OfferService;
import bg.softuni.ecommerce.service.UserService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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
    private final OfferMapper offerMapper;
    private final UserService userService;

    @Autowired
    public OfferController(BrandService brandService, OfferService offerService, OfferMapper offerMapper, UserService userService) {
        this.brandService = brandService;
        this.offerService = offerService;
        this.offerMapper = offerMapper;
        this.userService = userService;
    }

    @ModelAttribute("createOfferDto")
    public void initCreateOfferModel(Model model) {
        model.addAttribute("createOfferDto", new CreateOfferDto());
    }

    @GetMapping("/add")
    public String addOffer(Model model) {
        List<BrandDto> brands = this.brandService.allBrands();
        model.addAttribute("brands", brands);

        model.addAttribute("action", "/offers/add");
        model.addAttribute("method", "POST");
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

        this.offerService.createOffer(createOfferDto, userDetails);
        return "redirect:/offers/all";
    }

    @GetMapping("/all")
    public String allOffers(Model model, @PageableDefault(page = 0, size = 5) Pageable pageable) {
        model.addAttribute("offers", this.offerService.getAllOffers(pageable));
        return "offers";
    }

    @GetMapping("/{offerId}/details")
    public String getOfferDetails(@PathVariable("offerId") Long offerId, Model model, @AuthenticationPrincipal UserDetails userDetails) {
        OfferEntity offerEntity = this.offerService.getOfferById(offerId);
        if (offerEntity == null) {
            throw new RuntimeException("No such offer");
        }
        OfferDetailsDto offerDetailsDto = this.offerService.mapToOfferDetails(offerEntity);
        model.addAttribute("offerDetails", offerDetailsDto);

        boolean isAdmin = this.userService.isAdmin(userDetails);
        boolean isSeller = offerEntity.getSeller().getUsername().equals(userDetails.getUsername());

        model.addAttribute("isAdmin", isAdmin);
        model.addAttribute("isSeller", isSeller);
        return "offer-details";
    }

    @PostMapping("/delete/{id}")
    public String deleteOffer(@PathVariable("id") Long id, @AuthenticationPrincipal UserDetails userDetails) {
        OfferEntity offerEntity = this.offerService.getOfferById(id);

        if(!isSellerOrAdmin(offerEntity, userDetails)) {
            return String.format("redirect:/offers/%d/details", id);
        }

        offerService.deleteOfferById(id);
        return "redirect:/offers/all";
    }

    @GetMapping("/update/{id}")
    public String updateOffer(@PathVariable("id") Long offerId, Model model, @AuthenticationPrincipal UserDetails userDetails) {

        OfferEntity offerEntity = this.offerService.getOfferById(offerId);

        if(!isSellerOrAdmin(offerEntity, userDetails)) {
            return String.format("redirect:/offers/%d/details", offerId);
        }

        List<BrandDto> brands = this.brandService.allBrands();
        model.addAttribute("brands", brands);
        model.addAttribute("title", "Update Offer");
        model.addAttribute("action", "/offers/update/" + offerId);
        model.addAttribute("method", "post");

        CreateOfferDto createOfferDto = this.offerService.mapToCreateOfferDto(offerEntity);
        model.addAttribute("createOfferDto", createOfferDto);
        return "offer-add";
    }

    @PostMapping("/update/{id}")
    public String updateOffer(@PathVariable("id") Long offerId,
                              @Valid CreateOfferDto createOfferDto,
                              BindingResult bindingResult,
                              RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("createOfferDto", createOfferDto);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.createOfferDto", bindingResult);
            return "redirect:/offers/update/" + offerId;
        }

        this.offerService.updateOffer(offerId, createOfferDto);

        return String.format("redirect:/offers/%d/details", offerId);
    }


    @GetMapping("/brand/{id}")
    public String getOffersByBrand(@PathVariable("id") Long brandId,
                                   Model model,
                                   @PageableDefault(page = 0, size = 5) Pageable pageable) {
        model.addAttribute("offers", this.offerService.getOffersByBrandId(pageable, brandId));
        return "offers";
    }

    private boolean isSellerOrAdmin(OfferEntity offerEntity, UserDetails userDetails) {
        boolean isSeller = offerEntity.getSeller().getUsername().equals(userDetails.getUsername());
        boolean isAdmin = this.userService.isAdmin(userDetails);

        return isAdmin || isSeller;
    }
}
