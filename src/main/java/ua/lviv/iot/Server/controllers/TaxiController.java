package ua.lviv.iot.Server.controllers;

import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import ua.lviv.iot.Server.models.TaxiEntity;
import ua.lviv.iot.Server.services.TaxiService;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@NoArgsConstructor
public class TaxiController {

    protected TaxiService service;

    @GetMapping
    public String getDefault() {
        return service.toStringAll();
    }

    @GetMapping("/getById{id}")
    public TaxiEntity getById(@RequestParam final String id) {
        return service.getById(Integer.parseInt(id));
    }

    @GetMapping("/getAll")
    public List<TaxiEntity> getAll() {
        return service.getAll();
    }

    @GetMapping("/getStringById{id}")//  .../getStringById?id=13
    public String getStringById(@RequestParam final String id) {
        return service.toStringById(Integer.parseInt(id));
    }

    @GetMapping("/getStringAll")
    public String getStringAll() {
        return service.toStringAll();
    }

    @GetMapping("/add")
    public String add(@RequestParam final Map<String, String> params) throws IOException {
        return service.addEntity(params);
    }

    @GetMapping("/remove{id}")
    public String remove(@RequestParam final String id) throws IOException {
        return service.removeByIdString(Integer.parseInt(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable(name = "id") final String id) throws IOException {
        if (service.removeById(Integer.parseInt(id)) != null) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
