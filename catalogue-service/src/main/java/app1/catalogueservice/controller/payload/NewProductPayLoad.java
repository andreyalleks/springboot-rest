package app1.catalogueservice.controller.payload;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record NewProductPayLoad(
        @NotNull( message = "{catalogue.products.create.errors.title_is_null}")
        @Size(min=3, max=20, message = "{catalogue.products.create.errors.title_size_is_invalid}")
        String title,

        @Size(max = 500, message = "{catalogue.products.create.errors.details_size_is_invalid}" )
        String details

) {
}
