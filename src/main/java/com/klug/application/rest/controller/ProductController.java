package com.klug.application.rest.controller;

import com.klug.application.rest.dto.ProductDTO;
import com.klug.domain.models.DomainProduct;
import com.klug.domain.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping
    public ResponseEntity<ProductDTO> createProduct(@RequestBody ProductDTO productDTO) {
        DomainProduct product = new DomainProduct();
        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setPrice(productDTO.getPrice());

        DomainProduct createdProduct = productService.createProduct(product);

        ProductDTO responseDTO = new ProductDTO(createdProduct);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Long id) {
        return productService.getProductById(id)
                .map(product -> ResponseEntity.ok(new ProductDTO(product)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        List<ProductDTO> products = productService.getAllProducts().stream()
                .map(ProductDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(products);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable Long id, @RequestBody ProductDTO productDTO) {
        return productService.getProductById(id)
                .map(existingProduct -> {
                    existingProduct.setName(productDTO.getName());
                    existingProduct.setDescription(productDTO.getDescription());
                    existingProduct.setPrice(productDTO.getPrice());
                    DomainProduct updatedProduct = productService.updateProduct(existingProduct);
                    return new ResponseEntity<>(new ProductDTO(updatedProduct), HttpStatus.CREATED);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        if (productService.getProductById(id).isPresent()) {
            productService.deleteProduct(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
