package app1.catalogueservice.controller;

import app1.catalogueservice.controller.payload.NewProductPayLoad;
import app1.catalogueservice.entity.Product;
import app1.catalogueservice.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import java.util.List;
import java.util.Map;


@RestController
@RequiredArgsConstructor
@RequestMapping("catalogue-api/products")

public class ProductsRestController {

    private final ProductService productService;




    @GetMapping
    public List<Product> getProducts() {
        return this.productService.findAllProducts();
    }

    @PostMapping
    public ResponseEntity<?> createProduct(@Valid @RequestBody NewProductPayLoad payload,
                                           BindingResult bindingResult,
                                           UriComponentsBuilder uriBuilder)
    throws BindException {
        if (bindingResult.hasErrors()) {
            if (bindingResult instanceof BindException exception ) {throw  exception;}else {
                throw new BindException(bindingResult);
            }
        } else {
            Product product = this.productService.createProduct(payload.title(), payload.details());
            return ResponseEntity.created(uriBuilder.replacePath("/catalogue-api/products/{productId}")
                    .build(Map.of("productId", product.getId()))).body(product);
        }
    }

}
