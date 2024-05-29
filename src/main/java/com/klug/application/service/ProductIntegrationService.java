package com.klug.application.service;

import com.klug.application.rest.dto.ProductDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ProductIntegrationService {

    @Autowired
    private RestTemplate restTemplate;

    public ProductDTO getProductById(Long productId) {
        return restTemplate.getForObject("http://localhost:8080/api/products/" + productId, ProductDTO.class);
    }
}
