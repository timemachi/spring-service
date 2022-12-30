# spring-microservice order-inventory systme for practice

Tutorial: [Programming Techie Course](https://www.youtube.com/playlist?list=PLSVW22jAG8pBnhAdq9S8BpLnZ0_jVBj0c) 

## API Reference
### 1. Product Service
#### Put New product
```http
  PUT /api/product
```
| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `Product` | `ProductRequest` | **Required**. Product information(name, description, price)|

#### Get product by name
```http
  GET /api/product/${name(case insensitive)}
```
#### Delete product by name
```http
  Delete /api/product/${name(case insensitive)}
```
#### Get all products
```http
  GET /api/product/all
```
### 2. Order Service
#### Post new order
```http
  POST /api/order
```
| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `Order requests` | `OrderRequest` | **Required**. List of order(skuCode, price, quantity)|

#### Get order by order number
```http
  GET /api/order/{orderNumber}
```
#### Get all orders
```http
  GET /api/order/all
```
### 3. Inventory Service
#### Update inventory
```http
  PUT /api/inventory
```
| Parameter1 | Type  | Description |Parameter2 | Type     | Description |
| :-------- | :------- | :-----------|:-------- | :------- | :-----------|
| `skuCode` | `String` |**Required**  |`quantity` | `String` |**Required**. quantity will add to inventory if skuCode exist, otherwise a new inventory will be created |

#### Check if given skuCode product is in stock
```http
  GET /api/inventory
```
| Parameter1 | Type  | Description |Parameter2 | Type     | Description |
| :-------- | :------- | :-----------|:-------- | :------- | :-----------|
| `skuCode` | `String` |**Required**  |`quantity` | `String` |**Required**. If asked quantity is smaller than stock, return true; otherwise return false|

#### Get all stocks informations
```http
  GET /api/inventory/all
```
### 4. Notification Service
#### Get all notifications 
(Notification is event recorded by kafka server, include service name; action name; event detail like code number or information; event time)
```http
  GET /api/inventory/all
```

## Tech Stack

**Server:** Springboot web, Spring cloud, Netflix Eureka, Api gateway

**Security:** Keyclock, Oauth2

**Distributed tracing:** Zipkin 

**Monitoring:** Prometheus & Grafana

**Event register:** Kafka




