# Food Order Fulfillment System


This project implements a real-time system for fulfilling food orders in a delivery-only kitchen. The system allows orders to be received, stored, and picked up concurrently. It ensures that orders are stored at their ideal temperature and discarded if they exceed their freshness duration.

1. [Requirements](#prerequisites)

2. [Build and Run](#build-and-run)

3. [Order Discard Criteria](#order-discard-criteria)

4. [Dependencies](#dependencies)

# How to Build and Run the Program

## Prerequisites:

* Java 11 or higher


* Gradle (for dependency management)


## Build and Run:

1. Clone the repository:

``
git clone https://github.com/bpuje/FoodOrderSystem.git
``

``
cd FoodOrderSystem
``

2. Build the Project:

Use Gradle to build the project:

``
$ ./gradlew build
``
This will compile the code, run tests, and create an executable JAR file in the build/libs directory.

## Running the Harness Program:

To execute the program, run the following command:

```
$ ./gradlew run --args="--auth=<YOUR_AUTH_TOKEN>"
```

Replace YOUR_AUTH_TOKEN with the authentication token


Optional parameters:

* ``
--endpoint:`` Problem server endpoint https://api.cloudkitchens.com.


* ``
--auth:`` Authentication token (required).


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
$ ./gradle run --args="--auth d5cy8f5u537z --rate 1000 --min 5 --max 10"
``

## Order Discard Criteria

#### When the shelf is full and a new order needs to be placed, the system must discard an existing order to make room. The criteria for selecting which order to discard are as follows:

1. Oldest Order on the Shelf:
    * The system discards the oldest order on the shelf. This is based on the principle of First-In-First-Out (FIFO), ensuring that orders that have been on the shelf the longest are discarded first.
    * This approach is simple and ensures fairness, as no order is unfairly prioritized for discard.

2. Priority for Ideal Storage:
    * Before discarding an order, the system attempts to move an existing order from the shelf to its ideal storage (cooler or heater) if space becomes available. This helps maintain the freshness of orders for as long as possible.

3. Freshness Consideration:
    * Orders that have exceeded their freshness duration are automatically discarded during periodic freshness checks, regardless of whether the shelf is full.



## Why This Strategy?

1. Simplicity: Discarding the oldest order is straightforward to implement and understand.


2. Fairness: No order is unfairly targeted for discard, as the oldest order is always removed first.


3. Efficiency: Moving orders to their ideal storage before discarding helps maintain the quality of as many orders as possible.

### Dependencies
This project uses the following dependencies:

* Gradle: Build and dependency management tool.


* Jackson: For JSON parsing and serialization.


* SLF4J: For logging.


* JUnit 5: For unit testing.

The dependencies are managed by Gradle and can be found in the build.gradle file.