package app1.managerapp.controller;


import app1.managerapp.client.ProductRestClient;
import app1.managerapp.controller.payload.NewProductPayLoad;
import app1.managerapp.entity.Product;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("catalogue/products")
public class ProductsController {
    private final ProductRestClient productRestClient;


    @GetMapping("list")
    public String getProductsList(Model model) {
        model.addAttribute("products", this.productRestClient.findAllProducts());

        return "catalogue/products/list";
    }

    @GetMapping("create")
    public String getNewProductPage() {
        return "catalogue/products/new_product";
    }

    @PostMapping("create")
    public String createProduct(@Valid NewProductPayLoad payLoad, BindingResult bindingResult,
                                Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("payload", payLoad);
            model.addAttribute("errors", bindingResult.getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage).toList());
            return "catalogue/products/new_product";
        } else {
            Product product = this.productRestClient.createProduct(payLoad.title(), payLoad.details());
            return "redirect:/catalogue/products/%d".formatted(product.id());
        }

    }
}

