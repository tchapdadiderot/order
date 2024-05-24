# Read Me First
This solution tries to solve the following requirements:

- Offer the possibility to place an order for pilotes among the options FIVE, TEN and FIFTEEN
  - Actor: CUSTOMER
  - ENDPOINT: POST /public/order
- Update an existing pilote within the first 5 minutes after its creation
  - Actor: CUSTOMER
  - ENDPOINT: PUT /public/order/{orderNumber}
- Ability to look for Orders based on the customer data
  - Actor: ADMIN or a Person working for the restaurant behind
  - ENDPOINT: GET /order (This endpoint is protected.)

All features have been implemented and automated tested
  
The following technologies have been used to achieve this result:
- JAVA 21
- SPRING BOOT 3.2.4 (with JAVA virtual thread features enabled)
- Lombok
- Mapstruct
- Gradle 8.7
- Mockito
- Cucumber 
- Hibernate
- H2
- Moneta
- Spring Security
- OPEN API Code generator.

# Architecture

The Architecture used is here is the HEXAGONAL one, and Design approach followed the principle of Domain Driven Development
 combined with TDD. The API first strategy was followed with discipline.

Based on the requirements I went on with only one Aggregate "Order". All other classes in the domain are there to help
the class Order to achieve its Goals.


# Important Documents

### The open api contract
It is located at `<project-root>/openapi/api.yaml`. It contains the specifications of the offered endpoints.

This document goes as input to the code generator and produce the spring boot compatible code, whose major elements are:
- The Data Transfer Object (DTO)
- The Java Interfaces declaring the endpoints following the Spring specification

### The features files
The implemented features are described step by step in files with the ending `.features`. Here is one example for them:

```
Feature: Search Orders

  Scenario: Search Order By Customer
    Given   The database contains the following orders
      | firstname                | lastname                | phoneNumber | phoneCountryCode | quantity | deliveryCity          | deliveryPostalCode | deliveryStreet          | orderNumber    |
      | SearchOrders_Rodrigue    | SearchOrders_Lagoue     | 653492410   | 237              | FIVE     | SearchOrders_city     | 9911               | SearchOrders_Street     | B 0000 000 110 |
      | SearchOrders_John        | SearchOrders_Rodeo      | 653492410   | 237              | FIVE     | SearchOrders_city     | 9911               | SearchOrders_Street     | B 0000 000 111 |
      | SearchOrders_firstname   | SearchOrders_lastname   | 653492410   | 237              | FIVE     | SearchOrders_Roddrick | 9911               | SearchOrders_Street     | B 0000 000 112 |
      | SearchOrders_firstname   | SearchOrders_lastname   | 653492410   | 237              | FIVE     | SearchOrders_City     | 9911               | SearchOrders_Production | B 0000 000 114 |
      | SearchOrders_firstname_1 | SearchOrders_lastname_1 | 653492410   | 237              | FIVE     | SearchOrders_City     | 9911               | SearchOrders_Street     | B 0000 000 115 |
      | SearchOrders_firstname_2 | SearchOrders_lastname_2 | 653492410   | 237              | FIVE     | SearchOrders_City     | 9911               | SearchOrders_Street     | B 0000 000 116 |
    When    I look for orders placed by customer whose name contains 'rod'
    Then    The hits' list should contain the following orders
      | firstname                | lastname                | phoneNumber | phoneCountryCode | quantity | deliveryCity          | deliveryPostalCode | deliveryStreet          | orderNumber    |
      | SearchOrders_Rodrigue    | SearchOrders_Lagoue     | 653492410   | 237              | FIVE     | SearchOrders_city     | 9911               | SearchOrders_Street     | B 0000 000 110 |
      | SearchOrders_John        | SearchOrders_Rodeo      | 653492410   | 237              | FIVE     | SearchOrders_city     | 9911               | SearchOrders_Street     | B 0000 000 111 |
      | SearchOrders_firstname   | SearchOrders_lastname   | 653492410   | 237              | FIVE     | SearchOrders_Roddrick | 9911               | SearchOrders_Street     | B 0000 000 112 |
      | SearchOrders_firstname   | SearchOrders_lastname   | 653492410   | 237              | FIVE     | SearchOrders_City     | 9911               | SearchOrders_Production | B 0000 000 114 |

```
This Scenario describe the process of searching orders based on customer data. Those scenarios are then executed using the 
Cucumber Framework. The particularity of those tests is that, they running against a running instance of the application. 
That is why I call them `END-TO-END Tests`.

The solution at least one scenario for each of the required features.

NB: Almost all public methods have been tested, which leads us to code 96% for java classes and 95% code lines.

* No Docker Compose services found. As of now, the application won't start! Please add at least one service to the `compose.yaml` file.

## Run The project

In order to run the project you first need to build the docker image. This can be done using the command (being in the project root directory)

`$ gradle :jibDockerBuild`

Ths command create an image using the jib framework. Run the project by executing the command:

`$ docker run --name jagaad_order -d order:latest -p 8080:8080`
OR
`$ docker run --name jagaad_order -d order:0.0.1-SNAPSHOT -p 8080:8080`

After that you will be able to interact with system using the following endpoint:
- POST http://localhost:8080/public/order  To place a new Order
- PUT http://localhost:8080/public/order  To Modify an order
- GET http://localhost:8080/order?searchText=<query-text>  To search for orders based on the customer data
  - Credentials to use here are:
    - username: admin
    - password: admin

# Conclusion

I tried to follow the best practices as much as possible in the short period of time I had to work on this 
assessment. In the software industry there is always room for improvement, in fact we learn as software developer
new concepts every day. 
That is why I am available if you face any misunderstanding or issue regarding the proposed solution.

Enjoy


