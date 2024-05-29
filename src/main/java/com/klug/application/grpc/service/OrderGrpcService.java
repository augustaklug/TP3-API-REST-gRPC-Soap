package com.klug.application.grpc.service;

import com.klug.application.grpc.*;
import com.klug.domain.models.DomainOrder;
import com.klug.domain.services.OrderService;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@GrpcService
public class OrderGrpcService extends OrderServiceGrpc.OrderServiceImplBase {

    @Autowired
    private OrderService orderService;

    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_DATE_TIME;

    @Override
    public void createOrder(Order request, StreamObserver<Order> responseObserver) {
        DomainOrder domainOrder = new DomainOrder();
        domainOrder.setOrderDate(LocalDateTime.parse(request.getOrderDate(), dateTimeFormatter));
        domainOrder.setCustomerName(request.getCustomerName());
        domainOrder.setTotalAmount(request.getTotalAmount());
        domainOrder.setProductId(request.getProductId());

        DomainOrder createdDomainOrder = orderService.createOrder(domainOrder);

        Order response = Order.newBuilder()
                .setId(createdDomainOrder.getId())
                .setOrderDate(createdDomainOrder.getOrderDate().toString())
                .setCustomerName(createdDomainOrder.getCustomerName())
                .setTotalAmount(createdDomainOrder.getTotalAmount())
                .setProductId(createdDomainOrder.getProductId()).build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getOrderById(OrderId request, StreamObserver<Order> responseObserver) {
        Optional<DomainOrder> orderOptional = orderService.getOrderById(request.getId());

        if (orderOptional.isPresent()) {
            DomainOrder order = orderOptional.get();
            Order response = Order.newBuilder()
                    .setId(order.getId())
                    .setOrderDate(order.getOrderDate().toString())
                    .setCustomerName(order.getCustomerName())
                    .setTotalAmount(order.getTotalAmount())
                    .setProductId(order.getProductId()).build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } else {
            responseObserver.onError(new RuntimeException("Order not found"));
        }
    }

    @Override
    public void getAllOrders(Empty request, StreamObserver<OrderList> responseObserver) {
        List<DomainOrder> orders = orderService.getAllOrders();

        OrderList.Builder responseBuilder = OrderList.newBuilder();
        orders.forEach(order -> {
            Order orderProto = Order.newBuilder()
                    .setId(order.getId())
                    .setOrderDate(order.getOrderDate().toString())
                    .setCustomerName(order.getCustomerName())
                    .setTotalAmount(order.getTotalAmount())
                    .setProductId(order.getProductId()).build();
            responseBuilder.addOrders(orderProto);
        });

        responseObserver.onNext(responseBuilder.build());
        responseObserver.onCompleted();
    }

    @Override
    public void updateOrder(Order request, StreamObserver<Order> responseObserver) {
        Optional<DomainOrder> orderOptional = orderService.getOrderById(request.getId());

        if (orderOptional.isPresent()) {
            DomainOrder order = orderOptional.get();
            order.setOrderDate(LocalDateTime.parse(request.getOrderDate(), dateTimeFormatter));
            order.setCustomerName(request.getCustomerName());
            order.setTotalAmount(request.getTotalAmount());
            order.setProductId(request.getProductId());

            DomainOrder updatedOrder = orderService.updateOrder(order);

            Order response = Order.newBuilder()
                    .setId(updatedOrder.getId())
                    .setOrderDate(updatedOrder.getOrderDate().toString())
                    .setCustomerName(updatedOrder.getCustomerName())
                    .setTotalAmount(updatedOrder.getTotalAmount())
                    .setProductId(updatedOrder.getProductId()).build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } else {
            responseObserver.onError(new RuntimeException("Order not found"));
        }
    }

    @Override
    public void deleteOrder(OrderId request, StreamObserver<Empty> responseObserver) {
        Optional<DomainOrder> orderOptional = orderService.getOrderById(request.getId());

        if (orderOptional.isPresent()) {
            orderService.deleteOrder(orderOptional.get().getId());
            responseObserver.onNext(Empty.getDefaultInstance());
            responseObserver.onCompleted();
        } else {
            responseObserver.onError(new RuntimeException("Order not found"));
        }
    }
}