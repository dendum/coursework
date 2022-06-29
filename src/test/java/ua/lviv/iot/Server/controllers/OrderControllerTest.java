package ua.lviv.iot.Server.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ua.lviv.iot.Server.models.Order;
import ua.lviv.iot.Server.services.OrderService;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class OrderControllerTest {
    static final String ORDER_CSV_URL = "Data/Tests/order.csv";

    int currentOrdersCount = 0;

    @Autowired
    OrderController controller;

    @Autowired
    OrderService service;

    @org.junit.jupiter.api.BeforeEach
    void setUp() throws IOException {
        service.setCsvFileURL(ORDER_CSV_URL);
        service.removeAll();
        service.readCSV();

        Order order = new Order();
        order.setId(1);
        order.setName("Order-1");
        order.setClientID(1);
        order.setCarID(1);
        order.setDistance(10.0);
        order.setPrice(120.0);
        service.add(order);
        currentOrdersCount = service.getAll().size();
    }

    @Test
    void getAllTest() {
        var orders = service.getAll();
        service.toStringAll();
        assertTrue(orders.size() == currentOrdersCount);
    }

    @Test
    void addPost() throws IOException {
        int ordersCountBefore = service.getAll().size();
        int expectedId = service.getNextID();

        Order order = new Order();
        order.setId(expectedId);
        order.setName("Order-2");
        order.setClientID(5);
        order.setCarID(2);
        order.setDistance(15.5);
        order.setPrice(125.0);
        service.add(order);
        int ordersCountAfter = service.getAll().size();
        assertTrue(ordersCountAfter == currentOrdersCount + 1);
        currentOrdersCount = service.getAll().size();
    }

    @Test
    void put() throws IOException {
        var expectedOrder = (Order)service.getById(1);

        expectedOrder.setName("Order-1-test");
        expectedOrder.setDistance(25.5);
        expectedOrder.setPrice(130.0);
        service.edit(1, expectedOrder);

        var result = (Order)service.getById(1);
        assertEquals(expectedOrder, result);
    }

    @Test
    void remove() throws IOException {
        int lastId = service.getNextID() - 1;
        service.removeById(lastId);
        var ordersCount = service.getAll().size();
        assertTrue(ordersCount == currentOrdersCount - 1);
        currentOrdersCount = service.getAll().size();

    }
}