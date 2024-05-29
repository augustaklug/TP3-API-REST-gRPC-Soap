package com.klug.domain.services.impl;

import com.klug.domain.models.DomainProduct;
import com.klug.domain.repositories.ProductRepository;
import com.klug.domain.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public DomainProduct createProduct(DomainProduct product) {
        return productRepository.save(product);
    }

    @Override
    public Optional<DomainProduct> getProductById(Long id) {
        return productRepository.findById(id);
    }

    @Override
    public List<DomainProduct> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public DomainProduct updateProduct(DomainProduct product) {
        return productRepository.save(product);
    }

    @Override
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
}
