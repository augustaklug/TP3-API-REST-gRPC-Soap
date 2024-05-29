package com.klug.application.rest.dto;

import com.klug.domain.models.DomainOrder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class OrderDTO {
    private Long id;
    private LocalDateTime orderDate;
    private String customerName;
    private double totalAmount;
    private Long productId; // Referência ao produto

    public OrderDTO() {}

    public OrderDTO(DomainOrder order) {
        this.id = order.getId();
        this.orderDate = order.getOrderDate();
        this.customerName = order.getCustomerName();
        this.totalAmount = order.getTotalAmount();
        this.productId = order.getProductId();
    }
}