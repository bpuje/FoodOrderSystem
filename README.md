# Food Order Fulfillment System

## Overview

The Food Order Fulfillment System is designed to process and manage food orders efficiently using a shelf-based storage mechanism. It integrates with the CloudKitchens API to retrieve and process orders while ensuring orders are stored in appropriate storage sections (cooler, heater, or shelf) and discarded if necessary based on freshness criteria.

# How to Build and Run the Program

## Prerequisites:

* Java 11 or higher


* Gradle (for dependency management)


* Internet access (to connect to the API)

## Build Instructions:

1. Clone the repository:

``
git clone https://github.com/bpuje/FoodOrderSystem.git
``

``
cd FoodOrderSystem
``

2. Build the project:

``
$ ./gradlew build
``

## Running the Program:

To execute the program, run the following command:

``
java -jar build/libs/FoodOrderFulfillment.jar --auth <YOUR_AUTH_TOKEN> --endpoint <API_ENDPOINT>
``

Optional parameters:

* ``
--name:`` Specify a problem name (optional).


* ``
--seed:`` Use a fixed seed for reproducibility.


* ``
--rate:`` Control the rate at which orders arrive (default: 500ms).


* ``
--min:`` Minimum pickup time (default: 4 seconds).


* ``
--max:`` Maximum pickup time (default: 8 seconds).

Example usage:

``
java -jar build/libs/FoodOrderFulfillment.jar --auth d5cy8f5u537z --rate 500 --min 4 --max 8
``

## Order Discard Strategy

When the shelf reaches its maximum capacity, we must discard an order to make space for new ones. The current strategy selects the order with the lowest freshness for removal. This decision is based on the assumption that fresher orders have a higher likelihood of being picked up before expiration.

## Why This Strategy?

1. Maximizes Order Fulfillment: Keeping fresher orders in storage increases the chance that they will be successfully delivered before expiry.


2. Reduces Waste: Removing the least fresh order ensures that remaining orders have a better chance of completion.


3. Improves System Efficiency: By continuously discarding only the least fresh orders, the system maintains optimal storage utilization.

## Future Improvements

* Dynamic Shelf Management: Adjust storage capacity based on order volume.


* Predictive Order Placement: Use machine learning to optimize shelf space usage.


* Order Prioritization: Introduce ranking based on temperature sensitivity and expected pickup time.
