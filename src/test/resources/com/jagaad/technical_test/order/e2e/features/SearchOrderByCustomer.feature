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
