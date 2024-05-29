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
│   │           │   │   └── service
│   │           │   │       └── ProductGrpcService.java
│   │           │   ├── rest
│   │           │   │   ├── controller
│   │           │   │   │   └── ProductController.java
│   │           │   │   └── dto
│   │           │   │       └── ProductDTO.java
│   │           ├── domain
│   │           │   ├── models
│   │           │   │   └── DomainProduct.java
│   │           │   ├── repositories
│   │           │   │   └── ProductRepository.java
│   │           │   └── services
│   │           │       ├── ProductService.java
│   │           │       └── impl
│   │           │           └── ProductServiceImpl.java
│   ├── proto
│   │   └── product.proto
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

## Endpoints REST

### Criar um Produto
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

### Buscar Produto por ID
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

### Atualizar Produto
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

### Deletar Produto
* URL: ```/api/products/{id}```
* Método: ```DELETE```

### Listar Todos os Produtos
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

## Serviços gRPC

### Proto File
O arquivo ```product.proto``` define os serviços e mensagens gRPC:

```proto
syntax = "proto3";

option java_multiple_files = true;
option java_package = "com.klug.application.grpc";
option java_outer_classname = "ProductProto";

package product;

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
    rpc GetAllProducts(Empty) returns (ProductList);
    rpc UpdateProduct(Product) returns (Product);
    rpc DeleteProduct(ProductId) returns (Empty);
}

message Empty {}
```



## Exemplos de Context Type

### Partnership
Em um contexto de Parceria, dois ou mais contextos trabalham juntos e possuem uma relação de colaboração mútua.

```java
// Contexto A
public interface PartnerService {
    void notifyPartner(String message);
}

// Contexto B
public class OrderService {
    private final PartnerService partnerService;

    public OrderService(PartnerService partnerService) {
        this.partnerService = partnerService;
    }

    public void createOrder(Order order) {
        // Lógica de criação de pedido
        partnerService.notifyPartner("New order created");
    }
}

```

### Customer-Supplier
Em um contexto de Customer-Supplier, um contexto (Customer) utiliza serviços fornecidos por outro contexto (Supplier).

```java
// Contexto Supplier
public interface InventoryService {
    void reserveProduct(Long productId, int quantity);
}

// Contexto Customer
public class OrderService {
    private final InventoryService inventoryService;

    public OrderService(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    public void createOrder(Order order) {
        // Lógica de criação de pedido
        inventoryService.reserveProduct(order.getProductId(), order.getQuantity());
    }
}

```

### Anticorruption Layer
Em um contexto de Anticorruption Layer, criamos uma camada intermediária para isolar e adaptar a comunicação entre dois contextos que possuem modelos diferentes.

```java
// Contexto Legacy
public class LegacyInventoryService {
    public void legacyReserve(String productCode, int amount) {
        // Lógica de reserva no sistema legada
    }
}

// Anticorruption Layer
public class InventoryServiceAdapter implements InventoryService {
    private final LegacyInventoryService legacyInventoryService;

    public InventoryServiceAdapter(LegacyInventoryService legacyInventoryService) {
        this.legacyInventoryService = legacyInventoryService;
    }

    @Override
    public void reserveProduct(Long productId, int quantity) {
        String productCode = "PROD-" + productId; // Adaptação de ID para código
        legacyInventoryService.legacyReserve(productCode, quantity);
    }
}

// Contexto Atual
public class OrderService {
    private final InventoryService inventoryService;

    public OrderService(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    public void createOrder(Order order) {
        // Lógica de criação de pedido
        inventoryService.reserveProduct(order.getProductId(), order.getQuantity());
    }
}

```

## Princípios SOLID
* 1. Single Responsibility Principle (SRP): Cada classe tem uma única responsabilidade. Exemplo: ProductServiceImpl é responsável pela lógica de negócios do produto.
* 2. Open/Closed Principle (OCP): As classes estão abertas para extensão, mas fechadas para modificação. Exemplo: Podemos adicionar novos métodos a ProductService sem alterar a implementação existente.
* 3. Liskov Substitution Principle (LSP): As subclasses devem ser substituíveis por suas superclasses. Exemplo: ProductServiceImpl pode ser usada onde ProductService é esperado.
* 4. Interface Segregation Principle (ISP): Múltiplas interfaces específicas são melhores do que uma interface geral. Exemplo: ProductService é uma interface específica para operações de produtos.
* 5. Dependency Inversion Principle (DIP): As classes de alto nível não devem depender de classes de baixo nível; ambas devem depender de abstrações.
 
 
     
