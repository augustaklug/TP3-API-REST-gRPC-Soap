package com.klug.domain.services;

import com.klug.domain.models.DomainProduct;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface ProductService {
    DomainProduct createProduct(DomainProduct product);
    Optional<DomainProduct> getProductById(Long id);
    List<DomainProduct> getAllProducts();
    DomainProduct updateProduct(DomainProduct product);
    void deleteProduct(Long id);
}
