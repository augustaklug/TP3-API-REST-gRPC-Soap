package com.klug.application.grpc.service;

import com.klug.application.grpc.Empty;
import com.klug.application.grpc.Product;
import com.klug.application.grpc.ProductId;
import com.klug.application.grpc.ProductList;
import com.klug.application.grpc.ProductServiceGrpc;
import com.klug.domain.models.DomainProduct;
import com.klug.domain.services.impl.ProductServiceImpl;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

@GrpcService
public class ProductGrpcService extends ProductServiceGrpc.ProductServiceImplBase {

    @Autowired
    private ProductServiceImpl productService;

    @Override
    public void createProduct(Product request, StreamObserver<Product> responseObserver) {
        DomainProduct domainProduct = new DomainProduct();
        domainProduct.setName(request.getName());
        domainProduct.setDescription(request.getDescription());
        domainProduct.setPrice(request.getPrice());

        DomainProduct createdDomainProduct = productService.createProduct(domainProduct);

        Product response = Product.newBuilder()
                .setId(createdDomainProduct.getId())
                .setName(createdDomainProduct.getName())
                .setDescription(createdDomainProduct.getDescription())
                .setPrice(createdDomainProduct.getPrice()).build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getProductById(ProductId request, StreamObserver<Product> responseObserver) {
        Optional<DomainProduct> productOptional = productService.getProductById(request.getId());

        if (productOptional.isPresent()) {
            DomainProduct product = productOptional.get();
            Product response = Product.newBuilder().setId(product.getId()).setName(product.getName()).setDescription(product.getDescription()).setPrice(product.getPrice()).build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } else {
            responseObserver.onError(new RuntimeException("Product not found"));
        }
    }

    @Override
    public void getAllProducts(Empty request, StreamObserver<ProductList> responseObserver) {
        List<DomainProduct> products = productService.getAllProducts();

        ProductList.Builder responseBuilder = ProductList.newBuilder();
        products.forEach(product -> {
            Product productProto = Product.newBuilder().setId(product.getId()).setName(product.getName()).setDescription(product.getDescription()).setPrice(product.getPrice()).build();
            responseBuilder.addProducts(productProto);
        });

        responseObserver.onNext(responseBuilder.build());
        responseObserver.onCompleted();
    }

    @Override
    public void updateProduct(Product request, StreamObserver<Product> responseObserver) {
        Optional<DomainProduct> productOptional = productService.getProductById(request.getId());

        if (productOptional.isPresent()) {
            DomainProduct product = productOptional.get();
            product.setName(request.getName());
            product.setDescription(request.getDescription());
            product.setPrice(request.getPrice());

            DomainProduct updatedProduct = productService.updateProduct(product);

            Product response = Product.newBuilder().setId(updatedProduct.getId()).setName(updatedProduct.getName()).setDescription(updatedProduct.getDescription()).setPrice(updatedProduct.getPrice()).build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } else {
            responseObserver.onError(new RuntimeException("Product not found"));
        }
    }

    @Override
    public void deleteProduct(ProductId request, StreamObserver<Empty> responseObserver) {
        Optional<DomainProduct> productOptional = productService.getProductById(request.getId());

        if (productOptional.isPresent()) {
            productService.deleteProduct(productOptional.get().getId());
            responseObserver.onNext(Empty.getDefaultInstance());
            responseObserver.onCompleted();
        } else {
            responseObserver.onError(new RuntimeException("Product not found"));
        }
    }
}
