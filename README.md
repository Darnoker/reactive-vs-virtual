# Comparison of Reactive Approach vs Virtual Threads

This project demonstrates two approaches to building high-performance REST API applications using Spring Boot:

- **ReactiveTestApp** – an application based on **Spring WebFlux** (reactive, non-blocking).
- **VirtualThreadsTestApp** – an application based on **Spring Web MVC** utilizing **virtual threads** (Project Loom).

Both applications connect to a **MongoDB** database running inside a **Docker** container.

Additionally, the repository contains the following components:

- **JavaScript Application** – a simple tool using the **Faker.js** library to generate synthetic data and store it in the MongoDB database.
- **Gatling Performance Tests** – implemented in **Scala**, these tests measure the load and response time of both applications (the reactive one and the one using virtual threads).

## Functionality

Both applications provide an identical REST API for managing:

- Users (`/users`)
- Products (`/products`)
- Orders (`/orders`)

Each of these resources supports basic CRUD operations.

## Main Differences

| Feature                   | ReactiveTestApp                          | VirtualThreadsTestApp                   |
|---------------------------|------------------------------------------|----------------------------------------|
| Technology                | Spring WebFlux                           | Spring Web MVC + Virtual Threads       |
| Concurrency Model         | Reactive Streams + Netty                 | Classic threading with virtual threads |
| HTTP Server               | Netty                                    | Tomcat                                 |
| Programming Style         | Declarative, reactive                    | Imperative, classical                  |
| Key Characteristic        | non-blocking I/O                         | lightweight threads                    |

## Project Goal

The project's aim is to compare both approaches in terms of:
- Simplicity of implementation
- Performance and scalability
- Code readability and maintainability
- Behavior under heavy load

## Application Setup

**Requirements:**

- Java 21 (required for virtual threads)
- Maven 3.8+
- Spring Boot 3.2+
- Node.js and npm (for the data generator)
- sbt (for running Gatling tests)

### Reactive Application

```bash
cd ReactiveTestApp
./mvnw spring-boot:run
```
### Virtual Threads Application
```bash
cd VirtualThreadsTestApp
./mvnw spring-boot:run
```

### Data Generator Application `data-generate`
```bash
cd data-generate
node connect.js
```
### Gatling tests
```bash
cd gatling-test
sbt "gatling:testOnly simulations.<SIMULATION_NAME>"
```

## Endpoints

### 1. GET `/users/lastname/{lastname}`
Returns a list of users with the specified last name.

#### Sample Response:
```json
[
    {
        "id": "67ba170e3b3fc3801e8f82da",
        "name": "Krystel",
        "lastname": "Dach",
        "email": "Cristobal79@yahoo.com",
        "registeredAt": "2024-12-19T12:56:14.070+00:00",
        "addresses": [
            {
                "street": "144 Marjory Passage",
                "city": "Leilaville",
                "country": "Pakistan",
                "zipCode": "43701"
            }
        ],
        "profile": {
            "avatar": "https://avatars.githubusercontent.com/u/54759434",
            "bio": "Artificiose victoria acquiro quidem adeo amet vorago argentum corona quam."
        },
        "phoneNumbers": [
            "888-816-0753 x446",
            "280-575-8986 x629"
        ]
    },
]
```

### 2. GET `/order/manage/{orderId}`
Returns information about an order, including user details and the names and descriptions of purchased products.

#### Sample Response:
```json
{
    "userId": "67ba171d3b3fc3801e937edc",
    "name": "Cyrus",
    "lastname": null,
    "email": "Jeff.Hermann58@gmail.com",
    "productInfos": [
        {
            "name": "Rustic Silk Keyboard",
            "description": "Professional-grade Tuna perfect for humble training and recreational use"
        },
        {
            "name": "Modern Silk Car",
            "description": "Savor the tangy essence in our Ball, designed for upright culinary adventures"
        }
    ]
}
```

### 3. GET `orders/products/{orderId}`
Returns a list of product details for the products purchased in an order.

#### Sample Response
```json
[
  {
        "id": "67ba17453b3fc3801e9ec5c8",
        "name": "Rustic Silk Keyboard",
        "description": "Professional-grade Tuna perfect for humble training and recreational use",
        "price": 726.59,
        "category": {
            "name": "Garden",
            "subCategory": "Tasty"
        },
        "tags": [
            "Rubber",
            "Ceramic",
            "Silk"
        ],
        "reviews": [
            {
                "reviewer": "Hester Reynolds",
                "rating": 1,
                "comment": "Aspicio teres versus abundans tutamen advenio decipio decumbo sulum tribuo.",
                "reviewDate": "2025-02-22"
            },
            {
                "reviewer": "Cyril Krajcik",
                "rating": 2,
                "comment": "Solvo consequatur audentia voluptates cilicium ademptio vicissitudo cenaculum ante.",
                "reviewDate": "2025-02-21"
            },
            {
                "reviewer": "Alexandrine Fay",
                "rating": 2,
                "comment": "Alter molestias aliquid adfero amor.",
                "reviewDate": "2025-02-22"
            },
            {
                "reviewer": "Doyle Steuber",
                "rating": 2,
                "comment": "Vaco voluptatum video curo.",
                "reviewDate": "2025-02-21"
            },
            {
                "reviewer": "Loy Quigley",
                "rating": 3,
                "comment": "Vae virga terreo patria turpis arma.",
                "reviewDate": "2025-02-22"
            },
            {
                "reviewer": "Damien Brakus",
                "rating": 5,
                "comment": "Benigne odio celebrer.",
                "reviewDate": "2025-02-22"
            },
            {
                "reviewer": "Josiah Morar",
                "rating": 4,
                "comment": "Vicissitudo molestias aer bis expedita iste earum calcar denique toties.",
                "reviewDate": "2025-02-22"
            }
        ]
    }, 
]
```


### 4. POST `/products/add-review`
Adds a new review to a product. The new review is appended to the product, and after saving, the change is rolled back by restoring a backup.

#### Sample Request Body:
```json
{
    "productId" : "67ba17453b3fc3801e9ec51a",
    "reviewer" : "Reviewer",
    "rating": 5,
    "comment" : "Example comment"
}
```
The response is the saved review.


### 5. POST `/order/manage/add`
Adds a new order to the database. Similar to the review addition, the order is immediately removed after saving.

#### Sample Request Body:
```json
{
  "userId": "67ba170e3b3fc3801e8f82da",
  "products": [
    {
      "productId": "67ba17453b3fc3801e9ec51a",
      "quantity": 2,
      "price": 25.99,
      "details": {
        "color": "red",
        "warranty" : "first warranty"
      }
    },
    {
      "productId": "67ba17453b3fc3801e9ec51b",
      "quantity": 1,
      "price": 15.50,
      "details": {
        "color": "blue",
        "warranty" : "second warranty"
      }
    }
  ],
  "total": 67.48,
  "street": "123 Main St",
  "city": "Gdansk",
  "country": "Poland",
  "zipCode": "80-123",
  "shippingMethod": "Standard",
  "paymentMethod": "CreditCard"
}
```
The response is the saved order.
