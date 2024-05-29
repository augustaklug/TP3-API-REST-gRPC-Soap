# TP3-API-REST-gRPC-Soap

Este projeto é uma aplicação de gerenciamento de produtos construída usando Spring Boot. A aplicação oferece APIs RESTful e serviços gRPC para criar, ler, atualizar e deletar produtos.

## Tecnologias Utilizadas

- Java 21
- Spring Boot 3.x
- Spring Data JPA
- H2 Database (para desenvolvimento e testes)
- Lombok
- gRPC
- Maven

## Estrutura do Projeto

```plaintext
src
├── main
│   ├── java
│   │   └── com
│   │       └── klug
│   │           ├── application
│   │           │   ├── grpc
│   │           │   │   ├── ProductGrpcService.java
│   │           │   │   └── OrderGrpcService.java
│   │           │   ├── rest
│   │           │   │   ├── controller
│   │           │   │   │   ├── ProductController.java
│   │           │   │   │   └── OrderController.java
│   │           │   │   └── dto
│   │           │   │       ├── ProductDTO.java
│   │           │   │       └── OrderDTO.java
│   │           │   ├── service
│   │           │   │   └── ProductIntegrationService.java
│   │           ├── domain
│   │           │   ├── models
│   │           │   │   ├── DomainProduct.java
│   │           │   │   └── DomainOrder.java
│   │           │   ├── repositories
│   │           │   │   ├── ProductRepository.java
│   │           │   │   └── OrderRepository.java
│   │           │   ├── services
│   │           │   │   ├── ProductService.java
│   │           │   │   ├── OrderService.java
│   │           │   │   └── impl
│   │           │   │       ├── ProductServiceImpl.java
│   │           │   │       └── OrderServiceImpl.java
│   │           └── config
│   │               └── AppConfig.java
│   ├── proto
│   │   ├── common.proto
│   │   ├── product.proto
│   │   └── order.proto
│   └── resources
│       └── application.properties
└── test
    └── java
        └── com
            └── klug
                └── application
                    └── rest
                        └── controller
                            └── ProductControllerTest.java
                            └── TestConfig.java
```

## Domínio e Context Mapping

### Domain-Driven Design (DDD)
Este projeto segue os princípios do Domain-Driven Design (DDD) para organizar e modular o código de acordo com diferentes contextos de negócios. Cada contexto delimitado (Bounded Context) encapsula sua lógica de negócio específica, garantindo que o domínio permaneça coerente e fácil de manter.

### Context Mapping
No projeto, temos dois contextos principais:

* **1. Contexto de Produtos:** Gerencia produtos, suas características e operações CRUD.
* **2. Contexto de Pedidos:** Gerencia pedidos, incluindo data do pedido, nome do cliente, total do pedido e referência ao produto.
  
Para facilitar a comunicação entre esses contextos, utilizamos o padrão de serviço de aplicação. Por exemplo, o OrderService se comunica com o ProductService para obter informações sobre um produto ao criar ou atualizar um pedido.

### Princípios SOLID

O projeto adota os princípios SOLID para garantir um código mais limpo, modular e fácil de manter:

* **1. Single Responsibility Principle (SRP):** Cada classe tem uma única responsabilidade. Por exemplo, OrderService é responsável apenas pela lógica de negócios dos pedidos.
* **2. Open/Closed Principle (OCP):** As classes estão abertas para extensão, mas fechadas para modificação. Novas funcionalidades podem ser adicionadas estendendo as classes existentes.
* **3. Liskov Substitution Principle (LSP):** As subclasses podem ser usadas no lugar das classes base sem quebrar a aplicação. Implementações de interfaces, como ProductService e OrderService, seguem este princípio.
* **4. Interface Segregation Principle (ISP):** As interfaces são segregadas conforme necessário. Temos interfaces distintas para repositórios (ProductRepository, OrderRepository) e serviços (ProductService, OrderService).
* **5. Dependency Inversion Principle (DIP):** As classes de alto nível não dependem de classes de baixo nível, mas de abstrações. Os serviços dependem de interfaces de repositório em vez de implementações concretas.

## Endpoints REST

### Produtos

#### Criar um Produto
* URL: ```/api/products```
* Método: ```POST```
* Corpo da Requisição:
```json
{
  "name": "Produto X",
  "description": "Descrição do Produto X",
  "price": 99.99
}
```
* Resposta:
```json
{
  "id": 1,
  "name": "Produto X",
  "description": "Descrição do Produto X",
  "price": 99.99
}
```

#### Buscar Produto por ID
* URL: ```/api/products/{id}```
* Método: ```GET```
* Resposta:
```json
{
  "id": 1,
  "name": "Produto X",
  "description": "Descrição do Produto X",
  "price": 99.99
}
```

#### Atualizar Produto
* URL: ```/api/products/{id}```
* Método: ```PUT```
* Corpo da Requisição:
```json
{
  "name": "Produto Y",
  "description": "Descrição do Produto Y",
  "price": 199.99
}
```
* Resposta:
```json
{
  "id": 1,
  "name": "Produto Y",
  "description": "Descrição do Produto Y",
  "price": 199.99
}
```

#### Deletar Produto
* URL: ```/api/products/{id}```
* Método: ```DELETE```

#### Listar Todos os Produtos
* URL: ```/api/products```
* Método: ```GET```
* Resposta:
```json
[
  {
    "id": 1,
    "name": "Produto X",
    "description": "Descrição do Produto X",
    "price": 99.99
  },
  {
    "id": 2,
    "name": "Produto Y",
    "description": "Descrição do Produto Y",
    "price": 199.99
  }
]
```
### Pedidos

#### Criar um Pedido
* URL: ```/api/orders```
* Método: ```POST```
* Corpo da Requisição:
```json
{
  "orderDate": "2024-05-29T15:30:00",
  "customerName": "John Doe",
  "totalAmount": 199.99,
  "productId": 1
}
```
* Resposta:
```json
{
  "id": 1,
  "orderDate": "2024-05-29T15:30:00",
  "customerName": "John Doe",
  "totalAmount": 199.99,
  "productId": 1
}
```

#### Buscar Pedido por ID
* URL: ```/api/orders/{id}```
* Método: ```GET```
* Resposta:
```json
{
  "id": 1,
  "orderDate": "2024-05-29T15:30:00",
  "customerName": "John Doe",
  "totalAmount": 199.99,
  "productId": 1
}
```

#### Atualizar Pedido
* URL: ```/api/orders/{id}```
* Método: ```PUT```
* Corpo da Requisição:
```json
{
  "orderDate": "2024-06-01T12:00:00",
  "customerName": "Jane Doe",
  "totalAmount": 299.99,
  "productId": 2
}
```
* Resposta:
```json
{
  "id": 1,
  "orderDate": "2024-06-01T12:00:00",
  "customerName": "Jane Doe",
  "totalAmount": 299.99,
  "productId": 2
}
```

#### Deletar Pedido
* URL: ```/api/orders/{id}```
* Método: ```DELETE```

#### Listar Todos os Pedidos
* URL: ```/api/orders```
* Método: ```GET```
* Resposta:
```json
[
  {
    "id": 1,
    "orderDate": "2024-05-29T15:30:00",
    "customerName": "John Doe",
    "totalAmount": 199.99,
    "productId": 1
  },
  {
    "id": 2,
    "orderDate": "2024-06-01T12:00:00",
    "customerName": "Jane Doe",
    "totalAmount": 299.99,
    "productId": 2
  }
]
```

## Serviços gRPC

### Definições proto
```common.proto```

```proto
syntax = "proto3";

option java_multiple_files = true;
option java_package = "com.klug.application.grpc";
option java_outer_classname = "CommonProto";

package common;

message Empty {}
```

```product.proto```

```proto
syntax = "proto3";

option java_multiple_files = true;
option java_package = "com.klug.application.grpc";
option java_outer_classname = "ProductProto";

package product;

import "common.proto";

message Product {
    int64 id = 1;
    string name = 2;
    string description = 3;
    double price = 4;
}

message ProductId {
    int64 id = 1;
}

message ProductList {
    repeated Product products = 1;
}

service ProductService {
    rpc CreateProduct(Product) returns (Product);
    rpc GetProductById(ProductId) returns (Product);
    rpc GetAllProducts(common.Empty) returns (ProductList);
    rpc UpdateProduct(Product) returns (Product);
    rpc DeleteProduct(ProductId) returns (common.Empty);
}
```

```order.proto```

```proto
syntax = "proto3";

option java_multiple_files = true;
option java_package = "com.klug.application.grpc";
option java_outer_classname = "OrderProto";

package order;

import "common.proto";

message Order {
    int64 id = 1;
    string orderDate = 2;  // assuming orderDate is a string representation of date-time
    string customerName = 3;
    double totalAmount = 4;
    int64 productId = 5;  // Referência ao produto
}

message OrderId {
    int64 id = 1;
}

message OrderList {
    repeated Order orders = 1;
}

service OrderService {
    rpc CreateOrder(Order) returns (Order);
    rpc GetOrderById(OrderId) returns (Order);
    rpc GetAllOrders(common.Empty) returns (OrderList);
    rpc UpdateOrder(Order) returns (Order);
    rpc DeleteOrder(OrderId) returns (common.Empty);
}
```

## Exemplos de Context Type

### Partnership
Dois contextos colaboram de maneira próxima e coordenada, compartilhando responsabilidades e sincronizando mudanças. Eles têm uma forte dependência mútua. Por exemplo, os contextos **Shipping** (Envio) e **Billing** (Faturamento), onde ambos dependem um do outro para garantir que as entregas e os pagamentos sejam sincronizados.

**Código para o Contexto Shipping:**
```java
package com.klug.shipping.domain.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
public class Shipment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long orderId;
    private LocalDateTime shippedDate;
    private String status;
}
```

**Código para o Contexto Billing:**
```java
package com.klug.billing.domain.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long orderId;
    private LocalDateTime billingDate;
    private double amount;
    private String status;
}
```

**Sincronização entre Shipping e Billing:**
```java
package com.klug.shipping.service;

import com.klug.billing.domain.models.Invoice;
import com.klug.billing.service.BillingService;
import com.klug.shipping.domain.models.Shipment;
import com.klug.shipping.repository.ShipmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShippingService {

    @Autowired
    private ShipmentRepository shipmentRepository;

    @Autowired
    private BillingService billingService;

    public void shipOrder(Long orderId) {
        // Criação e atualização de um envio
        Shipment shipment = new Shipment();
        shipment.setOrderId(orderId);
        shipment.setShippedDate(LocalDateTime.now());
        shipment.setStatus("SHIPPED");
        shipmentRepository.save(shipment);

        // Atualização do faturamento
        Invoice invoice = billingService.findInvoiceByOrderId(orderId);
        if (invoice != null) {
            invoice.setStatus("SHIPPED");
            billingService.updateInvoice(invoice);
        }
    }
}
```

### Customer-Supplier
Um contexto (cliente) usa serviços de outro contexto (fornecedor) para realizar suas tarefas. O fornecedor não depende diretamente do cliente. Por exemplo, um contexto **Order** (Pedido) como cliente e um contexto **Inventory** (Inventário) como fornecedor. O contexto de pedidos verifica o inventário antes de criar um pedido.

**Código para o Contexto Inventory:**
```java
package com.klug.inventory.domain.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class ProductInventory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long productId;
    private int availableQuantity;
}
```

**Serviço de Inventário:**
```java
package com.klug.inventory.service;

import com.klug.inventory.domain.models.ProductInventory;
import com.klug.inventory.repository.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InventoryService {

    @Autowired
    private InventoryRepository inventoryRepository;

    public boolean checkAvailability(Long productId, int quantity) {
        ProductInventory inventory = inventoryRepository.findByProductId(productId);
        return inventory != null && inventory.getAvailableQuantity() >= quantity;
    }
}
```

**Código para o Contexto Order:**
```java
package com.klug.order.service;

import com.klug.inventory.service.InventoryService;
import com.klug.order.domain.models.Order;
import com.klug.order.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private InventoryService inventoryService;

    public void createOrder(Long productId, int quantity) {
        if (inventoryService.checkAvailability(productId, quantity)) {
            Order order = new Order();
            order.setProductId(productId);
            order.setQuantity(quantity);
            order.setStatus("CREATED");
            orderRepository.save(order);
        } else {
            throw new RuntimeException("Product not available in inventory");
        }
    }
}
```

### Anticorruption Layer
Um contexto implementa uma camada de tradução/adaptação para interagir com outro contexto ou sistema legado, evitando a "corrupção" do modelo de domínio. Por exemplo, um contexto **Order** (Pedido) que precisa se comunicar com um sistema legado de **Customer** (Cliente). Em vez de interagir diretamente, o contexto de pedidos usa uma camada anticorrupção para traduzir entre os dois sistemas

**Serviço de Sistema Legado de Cliente:**
```java
package com.legacy.customer.service;

import org.springframework.stereotype.Service;

@Service
public class LegacyCustomerService {

    public String getCustomerData(Long customerId) {
        // Simulação de uma chamada a um sistema legado
        return "Legacy Customer Data for ID: " + customerId;
    }
}
```

**Camada Anticorrupção:**
```java
package com.klug.order.acl;

import com.legacy.customer.service.LegacyCustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerAclService {

    @Autowired
    private LegacyCustomerService legacyCustomerService;

    public String getCustomerInfo(Long customerId) {
        String legacyData = legacyCustomerService.getCustomerData(customerId);
        // Traduzir dados legados para o modelo de domínio
        return translateLegacyData(legacyData);
    }

    private String translateLegacyData(String legacyData) {
        // Implementar a lógica de tradução
        return "Translated: " + legacyData;
    }
}
```

**Serviço de Pedido usando a Camada Anticorrupção:**
```java
package com.klug.order.service;

import com.klug.order.acl.CustomerAclService;
import com.klug.order.domain.models.Order;
import com.klug.order.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CustomerAclService customerAclService;

    public void createOrder(Long customerId, Long productId, int quantity) {
        String customerInfo = customerAclService.getCustomerInfo(customerId);
        Order order = new Order();
        order.setCustomerId(customerId);
        order.setProductId(productId);
        order.setQuantity(quantity);
        order.setCustomerInfo(customerInfo);
        order.setStatus("CREATED");
        orderRepository.save(order);
    }
}
```
