package com.klug.domain.services.impl;

import com.klug.application.rest.dto.ProductDTO;
import com.klug.domain.models.DomainOrder;
import com.klug.domain.repositories.OrderRepository;
import com.klug.domain.services.OrderService;
import com.klug.application.service.ProductIntegrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductIntegrationService productIntegrationService;

    @Override
    public DomainOrder createOrder(DomainOrder order) {
        ProductDTO product = productIntegrationService.getProductById(order.getProductId());
        // Agora você pode usar informações do produto, se necessário
        return orderRepository.save(order);
    }

    @Override
    public Optional<DomainOrder> getOrderById(Long id) {
        return orderRepository.findById(id);
    }

    @Override
    public List<DomainOrder> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public DomainOrder updateOrder(DomainOrder order) {
        return orderRepository.save(order);
    }

    @Override
    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }
}