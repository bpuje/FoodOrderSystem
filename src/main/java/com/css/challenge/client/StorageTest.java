package com.css.challenge.client;import java.util.UUID;import static org.junit.jupiter.api.Assertions.*;class StorageTest {    private Storage storage;    @org.junit.jupiter.api.BeforeEach    void setUp() {        storage = new Storage();    }    @org.junit.jupiter.api.Test    void testPlaceOrderInCooler() {        Order coldOrder = new Order(UUID.randomUUID().toString(), "Ice Cream", "cold", 120);        storage.placeOrder(coldOrder);        assertEquals(1, storage.coolerSize());    }    @org.junit.jupiter.api.Test    void testPlaceOrderInHeater() {        Order hotOrder = new Order(UUID.randomUUID().toString(), "Pizza", "hot", 120);        storage.placeOrder(hotOrder);        assertEquals(1, storage.heaterSize());    }    @org.junit.jupiter.api.Test    void testMoveOrderInShelf() {        Order hotOrder = new Order(UUID.randomUUID().toString(), "Burger", "hot", 120);        storage.placeOrder(hotOrder);        storage.processOrders();        assertEquals(1, storage.heaterSize());    }}