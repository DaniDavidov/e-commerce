package bg.softuni.ecommerce.web;

import bg.softuni.ecommerce.model.dto.BrandDto;
import bg.softuni.ecommerce.service.BrandService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class BrandController {
    private BrandService brandService;

    public BrandController(BrandService brandService) {
        this.brandService = brandService;
    }

    @GetMapping("/brands/all")
    public String allBrands(Model model) {

        List<BrandDto> brandDtos = this.brandService.allBrands();
        model.addAttribute("brands", brandDtos);
        return "brands";
    }
}
