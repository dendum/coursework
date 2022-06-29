package ua.lviv.iot.Server.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import ua.lviv.iot.Server.models.Order;
import ua.lviv.iot.Server.services.OrderService;

import java.io.IOException;
import java.util.List;


@RestController
@RequestMapping("/order")
public final class OrderController extends TaxiController {
    @Autowired
    OrderController(final OrderService orderService) throws IOException {
        service = orderService;
    }

    @PostMapping("/add")
    public ResponseEntity<?> addPost(@RequestBody final List<Order> orders) throws IOException {
        for (var order: orders) {
            service.add(order);
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<?> put(@PathVariable(name = "id") final String id,
                                 @RequestBody final Order order) throws IOException {
        var parseId = Integer.parseInt(id);
        if (service.getById(Integer.parseInt(id)) != null) {
            if (service.edit(parseId, order)) {
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
