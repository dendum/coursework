package ua.lviv.iot.Server.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ua.lviv.iot.Server.models.Car;
import ua.lviv.iot.Server.services.CarService;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CarControllerTest {

    static final String CAR_CSV_URL = "Data/Tests/cars.csv";
    static final String EXPECTED_CAR_CSV_URL = "Data/Tests/expected/cars.csv";

    int currentCarsCount = 0;

    @Autowired
    CarController controller;

    @Autowired
    CarService service;

    @org.junit.jupiter.api.BeforeEach
    void setUp() throws IOException {
        service.setCsvFileURL(CAR_CSV_URL);
        service.removeAll();
        service.readCSV();

        Car car = new Car();
        car.setId(1);
        car.setName("test_car");
        car.setVinCode("VIN000000");
        car.setStateNumber("BC33-88HC");
        car.setDriverId(1);
        car.setDescription("Universal");
        service.add(car);
        currentCarsCount = service.getAll().size();
    }

    @Test
    void addPost() throws IOException {
        Car car = new Car();
        int id = service.getNextID();
        car.setId(id);
        car.setName("test_car");
        car.setVinCode("VIN000000");
        car.setStateNumber("BC33-88HC");
        car.setDriverId(1);
        car.setDescription("Universal");
        service.add(car);

        var expectedFile = fileReader(EXPECTED_CAR_CSV_URL);
        var actualFile = fileReader(CAR_CSV_URL);
        assertIterableEquals(expectedFile, actualFile);
        currentCarsCount = service.getAll().size();
    }

    @Test
    void getAllTest() {
        var orders = service.getAll();
        service.toStringAll();
        assertTrue(orders.size() == currentCarsCount);
    }

    @Test
    void put() throws IOException {
        var actualUpdaterCar = (Car)service.getById(1);

        actualUpdaterCar.setName("Car-3-test");
        actualUpdaterCar.setVinCode("VIN000000_test");
        actualUpdaterCar.setStateNumber("BC33-88HC_test");
        service.edit(1, actualUpdaterCar);

        var result = (Car)service.getById(1);
        assertEquals(actualUpdaterCar, result);
    }

    @Test
    void remove() throws IOException {
        int lastId = service.getNextID() - 1;
        service.removeById(lastId);
        var carsCount = service.getAll().size();
        assertTrue(carsCount == currentCarsCount - 1);
        currentCarsCount = service.getAll().size();

    }

    private List<String> fileReader(String csvFileURL) {

        List<String> res = new ArrayList<String>();

        try (FileReader fileReader = new FileReader(csvFileURL);
             BufferedReader actualBR = new BufferedReader(fileReader)) {
            String currentLine = actualBR.readLine();
            while(currentLine != null)
            {
                currentLine = actualBR.readLine();
                res.add(currentLine);
            }
            fileReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }

}