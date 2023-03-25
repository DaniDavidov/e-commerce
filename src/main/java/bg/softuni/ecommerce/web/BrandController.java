package bg.softuni.ecommerce.web;

import bg.softuni.ecommerce.model.dto.BrandDto;
import bg.softuni.ecommerce.model.dto.CreateBrandDto;
import bg.softuni.ecommerce.service.BrandService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/brands")
public class BrandController {
    private BrandService brandService;

    public BrandController(BrandService brandService) {
        this.brandService = brandService;
    }

    @ModelAttribute("createBrandDto")
    public void initBrandDto(Model model) {
        model.addAttribute("createBrandDto", new CreateBrandDto());
    }
    @GetMapping("/all")
    public String allBrands(Model model) {

        List<BrandDto> brandDtos = this.brandService.allBrands();
        model.addAttribute("brands", brandDtos);
        return "brands";
    }

    @GetMapping("/add")
    public String addBrand() {

        return "brand-add";
    }

    @PostMapping("/add")
    public String addBrand(@Valid CreateBrandDto createBrandDto,
                           BindingResult bindingResult,
                           RedirectAttributes redirectAttributes ) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("createBrandDto", createBrandDto);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.createBrandDto", bindingResult);
            return "redirect:/brands/add";
        }

        this.brandService.createBrand(createBrandDto);
        return "redirect:/brands/all";
    }
}
