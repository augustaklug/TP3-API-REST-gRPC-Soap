package com.klug.application.rest.controller;

import com.klug.domain.models.DomainProduct;
import com.klug.domain.repositories.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;


@SpringBootTest
@AutoConfigureMockMvc
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductRepository productRepository;

    @BeforeEach
    public void setup() {
        productRepository.deleteAll();
    }

    @Test
    public void testCreateProduto() throws Exception {
        String produtoJson = "{\"name\":\"Produto A\",\"description\":\"Lorem Ipsum\",\"price\":10}";

        mockMvc.perform(post("/api/products").contentType(MediaType.APPLICATION_JSON).content(produtoJson))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Produto A"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("Lorem Ipsum"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.price").value(10));
    }

    @Test
    public void testGetAllProdutos() throws Exception {
        DomainProduct product = new DomainProduct();
        product.setName("Produto A");
        product.setDescription("Lorem Ipsum");
        product.setPrice(100.0);
        productRepository.save(product);

        mockMvc.perform(get("/api/products")).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Produto A"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].description").value("Lorem Ipsum"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].price").value(100));
    }

    @Test
    public void testUpdateProduto() throws Exception {
        DomainProduct product = new DomainProduct();
        product.setName("Produto A");
        product.setDescription("Lorem Ipsum");
        product.setPrice(100.0);
        productRepository.save(product);

        String updatedProdutoJson = "{\"name\":\"Produto B\",\"description\":\"Ipsum Lorem\",\"price\":200}";

        mockMvc.perform(put("/api/products/" + product.getId()).contentType(MediaType.APPLICATION_JSON).content(updatedProdutoJson))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Produto B"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("Ipsum Lorem"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.price").value(200));
    }

    @Test
    public void testDeleteProduto() throws Exception {
        DomainProduct product = new DomainProduct();
        product.setName("Produto A");
        product.setDescription("Lorem Ipsum");
        product.setPrice(100.0);
        productRepository.save(product);

        mockMvc.perform(delete("/api/products/" + product.getId()))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }
}