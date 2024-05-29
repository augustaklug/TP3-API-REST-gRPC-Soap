package com.klug.application.rest.dto;

import com.klug.domain.models.DomainProduct;
import lombok.Data;

@Data
public class ProductDTO {
    private Long id;
    private String name;
    private String description;
    private double price;

    public ProductDTO() {}

    public ProductDTO(DomainProduct product) {
        this.id = product.getId();
        this.name = product.getName();
        this.description = product.getDescription();
        this.price = product.getPrice();
    }
}
