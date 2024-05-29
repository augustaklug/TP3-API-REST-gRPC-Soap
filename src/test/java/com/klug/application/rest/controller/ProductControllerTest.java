package com.klug.application.rest.controller;

import com.klug.application.rest.dto.ProductDTO;
import com.klug.domain.models.DomainProduct;
import com.klug.domain.services.impl.ProductServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ProductControllerTest {

    @Mock
    private ProductServiceImpl productService;

    @InjectMocks
    private ProductController productController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(productController).build();
    }

    @Test
    void testCreateProduct() throws Exception {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setName("Product X");
        productDTO.setDescription("Description of Product X");
        productDTO.setPrice(99.99);

        DomainProduct domainProduct = new DomainProduct();
        domainProduct.setId(1L);
        domainProduct.setName("Product X");
        domainProduct.setDescription("Description of Product X");
        domainProduct.setPrice(99.99);

        when(productService.createProduct(ArgumentMatchers.any(DomainProduct.class))).thenReturn(domainProduct);

        mockMvc.perform(post("/api/products")
                        .contentType("application/json")
                        .content("{\"name\": \"Product X\", \"description\": \"Description of Product X\", \"price\": 99.99}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Product X"))
                .andExpect(jsonPath("$.description").value("Description of Product X"))
                .andExpect(jsonPath("$.price").value(99.99));
    }

    @Test
    void testGetProductById() throws Exception {
        DomainProduct domainProduct = new DomainProduct();
        domainProduct.setId(1L);
        domainProduct.setName("Product X");
        domainProduct.setDescription("Description of Product X");
        domainProduct.setPrice(99.99);

        when(productService.getProductById(1L)).thenReturn(Optional.of(domainProduct));

        mockMvc.perform(get("/api/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Product X"))
                .andExpect(jsonPath("$.description").value("Description of Product X"))
                .andExpect(jsonPath("$.price").value(99.99));
    }

    @Test
    void testGetProductByIdNotFound() throws Exception {
        when(productService.getProductById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/products/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetAllProducts() throws Exception {
        DomainProduct product1 = new DomainProduct();
        product1.setId(1L);
        product1.setName("Product X");
        product1.setDescription("Description of Product X");
        product1.setPrice(99.99);

        DomainProduct product2 = new DomainProduct();
        product2.setId(2L);
        product2.setName("Product Y");
        product2.setDescription("Description of Product Y");
        product2.setPrice(199.99);

        List<DomainProduct> productList = Arrays.asList(product1, product2);

        when(productService.getAllProducts()).thenReturn(productList);

        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Product X"))
                .andExpect(jsonPath("$[0].description").value("Description of Product X"))
                .andExpect(jsonPath("$[0].price").value(99.99))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("Product Y"))
                .andExpect(jsonPath("$[1].description").value("Description of Product Y"))
                .andExpect(jsonPath("$[1].price").value(199.99));
    }

    @Test
    void testUpdateProduct() throws Exception {
        DomainProduct domainProduct = new DomainProduct();
        domainProduct.setId(1L);
        domainProduct.setName("Product X Updated");
        domainProduct.setDescription("Updated Description of Product X");
        domainProduct.setPrice(129.99);

        when(productService.getProductById(1L)).thenReturn(Optional.of(domainProduct));
        when(productService.updateProduct(ArgumentMatchers.any(DomainProduct.class))).thenReturn(domainProduct);

        mockMvc.perform(put("/api/products/1")
                        .contentType("application/json")
                        .content("{\"name\": \"Product X Updated\", \"description\": \"Updated Description of Product X\", \"price\": 129.99}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Product X Updated"))
                .andExpect(jsonPath("$.description").value("Updated Description of Product X"))
                .andExpect(jsonPath("$.price").value(129.99));
    }

    @Test
    void testDeleteProduct() throws Exception {
        DomainProduct domainProduct = new DomainProduct();
        domainProduct.setId(1L);
        domainProduct.setName("Product X");
        domainProduct.setDescription("Description of Product X");
        domainProduct.setPrice(99.99);

        when(productService.getProductById(1L)).thenReturn(Optional.of(domainProduct));

        mockMvc.perform(delete("/api/products/1"))
                .andExpect(status().isNoContent());

        verify(productService, times(1)).deleteProduct(1L);
    }
}