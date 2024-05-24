Feature: Place An Order

  Scenario: Place An Order Successfully
    When   I place an order with the following details
      | firstname                          | lastname                          | phoneNumber | phoneCountryCode | quantity | deliveryCity                  | deliveryPostalCode | deliveryStreet                  |
      | PlaceAnOrderSuccessfully_firstname | PlaceAnOrderSuccessfully_lastname | 653492410   | 237              | FIVE     | PlaceAnOrderSuccessfully_city | 9911               | PlaceAnOrderSuccessfully_Street |
    Then  I should receive an invoice with a number respecting the regex 'B \d{4} \d{3} \d{3}'
    And   I should see that the invoice was created lesser than 5 seconds ago
    And   I should see that the invoice total is € 6.65
    And   I should that the other information on the invoice are the following one
      | firstname                          | lastname                          | phone          | quantity | deliveryCity                  | deliveryPostalCode | deliveryStreet                  |
      | PlaceAnOrderSuccessfully_firstname | PlaceAnOrderSuccessfully_lastname | 00237653492410 | FIVE     | PlaceAnOrderSuccessfully_city | 9911               | PlaceAnOrderSuccessfully_Street |
    And   I should see a line in the database table representing an order with the following data
      | firstname                          | lastname                          | phoneCountryCode | phoneNumber | quantity | deliveryCity                  | deliveryPostalCode | deliveryStreet                  | total    |
      | PlaceAnOrderSuccessfully_firstname | PlaceAnOrderSuccessfully_lastname | 237              | 653492410   | FIVE     | PlaceAnOrderSuccessfully_city | 9911               | PlaceAnOrderSuccessfully_Street | EUR;6.65 |
