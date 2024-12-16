package app1.catalogueservice.repository;

import app1.catalogueservice.entity.Product;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {
    List<Product> findAll();

    Product save(Product product);

    Optional<Product> findById(Integer productId );

    void deleteById(Integer id);
}
