package app1.managerapp.controller;

import app1.managerapp.client.ProductRestClient;
import app1.managerapp.controller.payload.UpdateProductPayLoad;
import app1.managerapp.entity.Product;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;
import java.util.NoSuchElementException;

@Controller
@RequiredArgsConstructor
@RequestMapping("catalogue/products/{productId:\\d+}")
public class ProductController {

    private final ProductRestClient productRestClient;

    private final MessageSource messageSource;


    @ModelAttribute("product")
    public Product product(@PathVariable("productId") int productId) {
        return this.productRestClient.findProduct(productId).orElseThrow(() -> new NoSuchElementException("catalogue.errors.product.not_found"));
    }

    @GetMapping
    public String getProduct() {

        return "catalogue/products/product";
    }


    @GetMapping("edit")
    public String getProductEditPage() {

        return "catalogue/products/edit";
    }

    @PostMapping("edit")
    public String updateProductPayload(@ModelAttribute("product") Product product,
                                       @Valid UpdateProductPayLoad payLoad, BindingResult bindingResult,
                                       Model model) {


        if (bindingResult.hasErrors()) {
            model.addAttribute("payload", payLoad);
            model.addAttribute("errors", bindingResult.getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage).toList());
            return "catalogue/products/edit";
        } else {

            this.productRestClient.updateProduct(product.id(), payLoad.title(), payLoad.details());
            return "redirect:/catalogue/products/%d".formatted(product.id());

        }
    }

    @PostMapping("delete")
    public String deleteProduct(@ModelAttribute("product") Product product) {
        this.productRestClient.deleteProduct(product.id());
        return "redirect:/catalogue/products/list";
    }

    @ExceptionHandler(NoSuchElementException.class)
    public String handleNoSuchElementException(NoSuchElementException exception, Model model,
                                               HttpServletResponse response, Locale locale) {
        response.setStatus(HttpStatus.NOT_FOUND.value());
        model.addAttribute("error", this.messageSource.getMessage(exception.getMessage(), new Object[0],
                exception.getMessage(), locale));
        return "errors/404";
    }

}
