Feature: Update An Order

  Scenario: Update An Existing Order Successfully
    Given  I assume the order with the following data exists 10 seconds ago in the database
      | orderNumber    | firstname                           | lastname                           | phoneCountryCode | phoneNumber | quantity | deliveryCity                   | deliveryPostalCode | deliveryStreet                   | total    |
      | B 0000 000 101 | UpdateAnOrderSuccessfully_firstname | UpdateAnOrderSuccessfully_lastname | 237              | 653492410   | FIVE     | UpdateAnOrderSuccessfully_city | 9911               | UpdateAnOrderSuccessfully_Street | EUR;6.65 |
    When   I update the order number 'B 0000 000 101' with the following details
      | firstname     | lastname     | phoneNumber | phoneCountryCode | quantity | deliveryCity | deliveryPostalCode | deliveryStreet |
      | new_firstname | new_lastname | 655555555   | 237              | FIFTEEN  | new_city     | 9922               | new_Street     |
    And   I should see that the Order with the following data exist inside the database
      | orderNumber    | firstname     | lastname     | phoneCountryCode | phoneNumber | quantity | deliveryCity | deliveryPostalCode | deliveryStreet | total     |
      | B 0000 000 101 | new_firstname | new_lastname | 237              | 655555555   | FIFTEEN  | new_city     | 9922               | new_Street     | EUR;19.95 |


  Scenario: Update An Existing Order When the orderNumber is missing
    Given  I assume the order number 'B 0000 120 001' is not present in the database
    When   I try to update the order number 'B 0000 120 001' with the following details
      | firstname     | lastname     | phoneNumber | phoneCountryCode | quantity | deliveryCity | deliveryPostalCode | deliveryStreet |
      | new_firstname | new_lastname | 655555555   | 237              | FIFTEEN  | new_city     | 9922               | new_Street     |
    And   The last request failed with the following data
      | status    | body                                  |
      | NOT_FOUND | Order number B 0000 120 001 not found |

  Scenario: Refuse to update an existing Order if it is more than 5 minutes old
    Given  I assume the order with the following data exists 301 seconds ago in the database
      | orderNumber    | firstname                           | lastname                           | phoneCountryCode | phoneNumber | quantity | deliveryCity                   | deliveryPostalCode | deliveryStreet                   | total    |
      | B 0000 001 102 | UpdateAnOrderSuccessfully_firstname | UpdateAnOrderSuccessfully_lastname | 237              | 653492410   | FIVE     | UpdateAnOrderSuccessfully_city | 9911               | UpdateAnOrderSuccessfully_Street | EUR;6.65 |
    When   I try to update the order number 'B 0000 001 102' with the following details
      | firstname     | lastname     | phoneNumber | phoneCountryCode | quantity | deliveryCity | deliveryPostalCode | deliveryStreet |
      | new_firstname | new_lastname | 655555555   | 237              | FIFTEEN  | new_city     | 9922               | new_Street     |
    And   The last request failed with the following data
      | status    | body                                                 |
      | FORBIDDEN | Order can not be changed after 5 minutes of creation |