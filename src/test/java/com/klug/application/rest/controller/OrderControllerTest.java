package com.klug.application.rest.controller;

import com.klug.application.rest.dto.OrderDTO;
import com.klug.domain.models.DomainOrder;
import com.klug.domain.services.impl.OrderServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class OrderControllerTest {

    @Mock
    private OrderServiceImpl orderService;

    @InjectMocks
    private OrderController orderController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(orderController).build();
    }

    @Test
    void testCreateOrder() throws Exception {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setOrderDate(LocalDateTime.now());
        orderDTO.setCustomerName("John Doe");
        orderDTO.setTotalAmount(199.99);
        orderDTO.setProductId(1L);

        DomainOrder domainOrder = new DomainOrder();
        domainOrder.setId(1L);
        domainOrder.setOrderDate(LocalDateTime.now());
        domainOrder.setCustomerName("John Doe");
        domainOrder.setTotalAmount(199.99);
        domainOrder.setProductId(1L);

        when(orderService.createOrder(ArgumentMatchers.any(DomainOrder.class))).thenReturn(domainOrder);

        mockMvc.perform(post("/api/orders")
                .contentType("application/json")
                .content("{\"orderDate\": \"2024-05-29T15:30:00\", \"customerName\": \"John Doe\", \"totalAmount\": 199.99, \"productId\": 1}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.customerName").value("John Doe"))
                .andExpect(jsonPath("$.totalAmount").value(199.99))
                .andExpect(jsonPath("$.productId").value(1));
    }

    @Test
    void testGetOrderById() throws Exception {
        DomainOrder domainOrder = new DomainOrder();
        domainOrder.setId(1L);
        domainOrder.setOrderDate(LocalDateTime.now());
        domainOrder.setCustomerName("John Doe");
        domainOrder.setTotalAmount(199.99);
        domainOrder.setProductId(1L);

        when(orderService.getOrderById(1L)).thenReturn(Optional.of(domainOrder));

        mockMvc.perform(get("/api/orders/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.customerName").value("John Doe"))
                .andExpect(jsonPath("$.totalAmount").value(199.99))
                .andExpect(jsonPath("$.productId").value(1));
    }

    @Test
    void testGetOrderByIdNotFound() throws Exception {
        when(orderService.getOrderById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/orders/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetAllOrders() throws Exception {
        DomainOrder order1 = new DomainOrder();
        order1.setId(1L);
        order1.setOrderDate(LocalDateTime.now());
        order1.setCustomerName("John Doe");
        order1.setTotalAmount(199.99);
        order1.setProductId(1L);

        DomainOrder order2 = new DomainOrder();
        order2.setId(2L);
        order2.setOrderDate(LocalDateTime.now());
        order2.setCustomerName("Jane Doe");
        order2.setTotalAmount(299.99);
        order2.setProductId(2L);

        List<DomainOrder> orderList = Arrays.asList(order1, order2);

        when(orderService.getAllOrders()).thenReturn(orderList);

        mockMvc.perform(get("/api/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].customerName").value("John Doe"))
                .andExpect(jsonPath("$[0].totalAmount").value(199.99))
                .andExpect(jsonPath("$[0].productId").value(1))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].customerName").value("Jane Doe"))
                .andExpect(jsonPath("$[1].totalAmount").value(299.99))
                .andExpect(jsonPath("$[1].productId").value(2));
    }

    @Test
    void testUpdateOrder() throws Exception {
        DomainOrder domainOrder = new DomainOrder();
        domainOrder.setId(1L);
        domainOrder.setOrderDate(LocalDateTime.now());
        domainOrder.setCustomerName("John Doe Updated");
        domainOrder.setTotalAmount(299.99);
        domainOrder.setProductId(1L);

        when(orderService.getOrderById(1L)).thenReturn(Optional.of(domainOrder));
        when(orderService.updateOrder(ArgumentMatchers.any(DomainOrder.class))).thenReturn(domainOrder);

        mockMvc.perform(put("/api/orders/1")
                .contentType("application/json")
                .content("{\"orderDate\": \"2024-05-30T15:30:00\", \"customerName\": \"John Doe Updated\", \"totalAmount\": 299.99, \"productId\": 1}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.customerName").value("John Doe Updated"))
                .andExpect(jsonPath("$.totalAmount").value(299.99))
                .andExpect(jsonPath("$.productId").value(1));
    }

    @Test
    void testDeleteOrder() throws Exception {
        DomainOrder domainOrder = new DomainOrder();
        domainOrder.setId(1L);
        domainOrder.setOrderDate(LocalDateTime.now());
        domainOrder.setCustomerName("John Doe");
        domainOrder.setTotalAmount(199.99);
        domainOrder.setProductId(1L);

        when(orderService.getOrderById(1L)).thenReturn(Optional.of(domainOrder));

        mockMvc.perform(delete("/api/orders/1"))
                .andExpect(status().isNoContent());

        verify(orderService, times(1)).deleteOrder(1L);
    }
}
