package com.klug.domain.services;

import com.klug.domain.models.DomainOrder;
import java.util.List;
import java.util.Optional;

public interface OrderService {
    DomainOrder createOrder(DomainOrder order);
    Optional<DomainOrder> getOrderById(Long id);
    List<DomainOrder> getAllOrders();
    DomainOrder updateOrder(DomainOrder order);
    void deleteOrder(Long id);
}