package com.klug.application.rest.controller;

import com.klug.application.rest.dto.OrderDTO;
import com.klug.domain.models.DomainOrder;
import com.klug.domain.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderDTO> createOrder(@RequestBody OrderDTO orderDTO) {
        DomainOrder order = new DomainOrder();
        order.setOrderDate(orderDTO.getOrderDate());
        order.setCustomerName(orderDTO.getCustomerName());
        order.setTotalAmount(orderDTO.getTotalAmount());
        order.setProductId(orderDTO.getProductId()); // Set product ID

        DomainOrder createdOrder = orderService.createOrder(order);

        OrderDTO responseDTO = new OrderDTO(createdOrder);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable Long id) {
        return orderService.getOrderById(id)
                .map(order -> ResponseEntity.ok(new OrderDTO(order)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<OrderDTO>> getAllOrders() {
        List<OrderDTO> orders = orderService.getAllOrders().stream()
                .map(OrderDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(orders);
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderDTO> updateOrder(@PathVariable Long id, @RequestBody OrderDTO orderDTO) {
        return orderService.getOrderById(id)
                .map(existingOrder -> {
                    existingOrder.setOrderDate(orderDTO.getOrderDate());
                    existingOrder.setCustomerName(orderDTO.getCustomerName());
                    existingOrder.setTotalAmount(orderDTO.getTotalAmount());
                    existingOrder.setProductId(orderDTO.getProductId()); // Update product ID
                    DomainOrder updatedOrder = orderService.updateOrder(existingOrder);
                    return new ResponseEntity<>(new OrderDTO(updatedOrder), HttpStatus.OK);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        if (orderService.getOrderById(id).isPresent()) {
            orderService.deleteOrder(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
