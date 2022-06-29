package ua.lviv.iot.Server.services;
import org.springframework.stereotype.Service;
import ua.lviv.iot.Server.models.Car;
import ua.lviv.iot.Server.models.TaxiEntity;

import java.io.IOException;
import java.util.Map;

@Service
public final class CarService extends TaxiService {

    public CarService() throws IOException {
        super();
        csvFileURL = CSV_FILES_PATH + "cars.csv";
        entityClassName = Car.class.getSimpleName();
        readCSV();
    }

    @Override
    public String toStringAll() {
        String result = "=============== ALL CARS ===============<br>";
        for (Map.Entry<Integer, TaxiEntity> item : data.entrySet()) {
            result += toString(item.getValue(), "");
        }
        return result;
    }

    @Override
    public String addEntity(final Map<String, String> params) throws IOException {
        if (!params.containsKey("driverId")) {
            return "Cannot add Car!!! Driver ID must to be specified!!!";
        }
        Car car = new Car();
        car.setId(getNextID());
        car.setName(params.getOrDefault("name", "Car #" + car.getId().toString()));
        car.setVinCode(params.getOrDefault("vin", "Vin not specified!"));
        car.setStateNumber(params.getOrDefault("stateNumber", "State number not specified"));
        car.setDriverId(Integer.parseInt(params.get("driverId")));
        car.setDescription(params.getOrDefault("description", "Description not specified"));
        writeToCSV();

        return "***************** CAR ADDED *****************<br>" + toString(car, "");
    }

    @Override
    protected String toString(final TaxiEntity entity, final String margin) {
        String result = "<br>" + margin + "---------------<br>";
        if (entity != null) {
            Car car = (Car) entity;
            DriverService driverService = (DriverService) getService(DriverService.class.getName());
            String driverString = driverService == null
                    ? HTML_TAB + "Cannot access to the Driver service. Service is null"
                    : driverService.toStringById(car.getDriverId(), margin + HTML_TAB);
            result += margin + "Car Id: " + car.getId().toString() + "<br>";
            result += margin + "Car Name: " + car.getName() + "<br>";
            result += margin + "Car VIN: " + car.getVinCode() + "<br>";
            result += margin + "Car Number: " + car.getStateNumber() + "<br>";
            result += margin + "Driver: " + driverString + "<br>";
            result += margin + "Car Description: " + car.getDescription() + "<br>";
        } else {
            result += margin + "!!! No Cars to print. Entity is null !!!" + "<br>";
        }
        result += margin + "---------------<br>";
        return result;
    }

    @Override
    protected String buildDataLine(final TaxiEntity entity) {
        Car car = (Car) entity;
        return car.getId().toString() + CSV_SEPARATOR
                + car.getName() + CSV_SEPARATOR
                + car.getVinCode() + CSV_SEPARATOR
                + car.getStateNumber() + CSV_SEPARATOR
                + car.getDriverId().toString() + CSV_SEPARATOR
                + car.getDescription();
    }

    @Override
    protected String buildHeaderLine() {
        return super.buildHeaderLine()
                + CSV_SEPARATOR + "VIN"
                + CSV_SEPARATOR + "State Number"
                + CSV_SEPARATOR + "Driver ID"
                + CSV_SEPARATOR + "Description";
    }

    @Override
    protected TaxiEntity parseLine(final String line) {
        var properties = line.split(CSV_SEPARATOR);

        Car car = new Car();
        car.setId(Integer.parseInt(properties[0]));
        car.setName(properties[1]);
        car.setVinCode(properties[2]);
        car.setStateNumber(properties[3]);
        car.setDriverId(Integer.parseInt(properties[4]));
        car.setDescription(properties[5]);

        return car;
    }
}
