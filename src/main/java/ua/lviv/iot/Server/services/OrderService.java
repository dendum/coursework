package ua.lviv.iot.Server.services;

import org.springframework.stereotype.Service;
import ua.lviv.iot.Server.models.Order;
import ua.lviv.iot.Server.models.TaxiEntity;

import java.io.IOException;
import java.util.Map;

@Service
public final class OrderService extends TaxiService {

    public OrderService() throws IOException {
        super();
        csvFileURL = CSV_FILES_PATH + "order.csv";
        entityClassName = Order.class.getSimpleName();
        readCSV();
    }

    @Override
    public String toStringAll() {
        String result = "=============== ALL ORDERS ===============<br>";
        for (Map.Entry<Integer, TaxiEntity> item : data.entrySet()) {
            result += toString(item.getValue(), "");
        }
        return result;
    }

    @Override
    public String addEntity(final Map<String, String> params) throws IOException {
        if (!params.containsKey("carId")) {
            return "Cannot add Order!!! Car ID must to be specified!!!";
        } else if (!params.containsKey("clientId")) {
            return "Cannot add Order!!! Client ID must to be specified!!!";
        } else if (!params.containsKey("distance")) {
            return "Cannot add Order!!! Distance must to be specified!!!";
        } else if (!params.containsKey("price")){
            return "Cannot add Order!!! Price must to be specified!!!";
        }
        Order order = new Order();
        order.setId(getNextID());
        order.setName(params.getOrDefault("name", "Order #" + order.getId().toString()));
        order.setCarID(Integer.parseInt(params.get("carId")));
        order.setClientID(Integer.parseInt(params.get("clientId")));
        order.setDistance(Double.parseDouble(params.get("distance")));
        order.setPrice(Double.parseDouble(params.get("price")));
        writeToCSV();

        return "***************** ORDER ADDED *****************<br>" + toString(order, "");
    }

    @Override
    protected String toString(final TaxiEntity entity, final String margin) {
        String result = "<br>" + margin + "---------------<br>";
        if (entity != null) {
            Order order = (Order) entity;
            CarService carService = (CarService) getService(CarService.class.getName());
            String carString = carService == null
                    ? HTML_TAB + "Cannot access to the Car service. Service is null"
                    : carService.toStringById(order.getCarID(), HTML_TAB);

            ClientService clientService = (ClientService) getService(ClientService.class.getName());
            String clientString = clientService == null
                    ? HTML_TAB + "Cannot access to the Client service. Service is null"
                    : clientService.toStringById(order.getClientID(), HTML_TAB);
            result += margin + "Order Id: " + order.getId().toString() + "<br>";
            result += margin + "Order Name: " + order.getName() + "<br>";
            result += margin + "Client: " + clientString + "<br>";
            result += margin + "Car: " + carString + "<br>";
            result += margin + "Distance: " + order.getDistance() + "<br>";
            result += margin + "Price: " + order.getPrice() + "<br>";
        } else {
            result += margin + "!!! No Orders to print. Entity is null !!!" + "<br>";
        }
        result += margin + "---------------<br>";
        return result;
    }

    @Override
    protected String buildDataLine(final TaxiEntity entity) {
        Order order = (Order) entity;
        return order.getId().toString() + CSV_SEPARATOR
                + order.getName() + CSV_SEPARATOR
                + order.getCarID() + CSV_SEPARATOR
                + order.getClientID() + CSV_SEPARATOR
                + order.getDistance() + CSV_SEPARATOR
                + order.getPrice() + CSV_SEPARATOR;
    }

    @Override
    protected String buildHeaderLine() {
        return super.buildHeaderLine()
                + CSV_SEPARATOR + "Car ID"
                + CSV_SEPARATOR + "Client ID"
                + CSV_SEPARATOR + "Distance"
                + CSV_SEPARATOR + "Price";
    }

    @Override
    protected TaxiEntity parseLine(final String line) {
        var properties = line.split(CSV_SEPARATOR);

        Order order  = new Order();
        order.setId(Integer.parseInt(properties[0]));
        order.setName(properties[1]);
        order.setCarID(Integer.parseInt(properties[2]));
        order.setClientID(Integer.parseInt(properties[3]));
        order.setDistance(Double.parseDouble(properties[4]));
        order.setPrice(Double.parseDouble(properties[5]));
        return order;
    }
}
