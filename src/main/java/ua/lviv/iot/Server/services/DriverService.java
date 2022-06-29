package ua.lviv.iot.Server.services;

import org.springframework.stereotype.Service;
import ua.lviv.iot.Server.models.Driver;
import ua.lviv.iot.Server.models.TaxiEntity;

import java.io.IOException;
import java.util.Map;

@Service
public final class DriverService extends TaxiService {

    public DriverService() throws IOException {
        super();
        csvFileURL = CSV_FILES_PATH + "drivers.csv";
        entityClassName = Driver.class.getSimpleName();
        readCSV();
    }

    @Override
    public String toStringAll() {
        String result = "=============== ALL DRIVERS ===============<br>";
        for (Map.Entry<Integer, TaxiEntity> item : data.entrySet()) {
            result += toString(item.getValue(), "");
        }
        return result;
    }

    @Override
    public String addEntity(final Map<String, String> params) throws IOException {
        Driver driver = new Driver();
        driver.setId(getNextID());
        driver.setName(params.getOrDefault("name", "Driver #" + driver.getId().toString()));
        driver.setPhone("+" + params.getOrDefault("phone", "Not specified"));
        driver.setExperience(params.getOrDefault("experience", "Not specified"));
        data.put(driver.getId(), driver);
        writeToCSV();
        return "***************** DRIVER ADDED *****************<br>" + toString(driver, "");
    }


    @Override
    protected String toString(final TaxiEntity entity, final String margin) {
        String result = "<br>" + margin + "---------------<br>";
        if (entity != null) {
            Driver driver = (Driver) entity;
            result += margin + "Driver Id: " + driver.getId().toString() + "<br>";
            result += margin + "Driver Name: " + driver.getName() + "<br>";
            result += margin + "Driver Phone: " + driver.getPhone() + "<br>";
            result += margin + "Driver Experience: " + driver.getExperience() + "<br>";
        } else {
            result += margin + "!!! No Drivers to print. Entity is null !!!" + "<br>";
        }
        result += margin + "---------------<br>";
        return result;
    }

    @Override
    protected String buildDataLine(final TaxiEntity entity) {
        Driver driver = (Driver) entity;
        return driver.getId().toString() + CSV_SEPARATOR
                + driver.getName() + CSV_SEPARATOR
                + driver.getPhone() + CSV_SEPARATOR
                + driver.getExperience();
    }

    @Override
    protected String buildHeaderLine() {
        return super.buildHeaderLine()
                + CSV_SEPARATOR + "Phone"
                + CSV_SEPARATOR + "Experience";
    }

    @Override
    protected TaxiEntity parseLine(final String line) {
        var properties = line.split(CSV_SEPARATOR);

        Driver driver = new Driver();
        driver.setId(Integer.parseInt(properties[0]));
        driver.setName(properties[1]);
        driver.setPhone(properties[2]);
        driver.setExperience(properties[3]);

        return driver;
    }
}
