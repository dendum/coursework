package ua.lviv.iot.Server.services;

import org.springframework.stereotype.Service;
import ua.lviv.iot.Server.models.Client;
import ua.lviv.iot.Server.models.TaxiEntity;

import java.io.IOException;
import java.util.Map;

@Service
public final class ClientService  extends TaxiService {

    public ClientService() throws IOException {
        super();
        csvFileURL = CSV_FILES_PATH + "client.csv";
        entityClassName = Client.class.getSimpleName();
        readCSV();
    }

    @Override
    public String toStringAll() {
        String result = "=============== ALL CLIENTS ===============<br>";
        for (Map.Entry<Integer, TaxiEntity> item : data.entrySet())  {
            result += toString(item.getValue(), "");
        }
        return result;
    }

    @Override
    public String addEntity(final Map<String, String> params) throws IOException {
        Client client = new Client();
        client.setId(getNextID());
        client.setName(params.getOrDefault("name", "Client #" + client.getId().toString()));
        client.setPhone(params.getOrDefault("phone", "Phone not specified!"));
        writeToCSV();

        return "***************** CLIENT ADDED *****************<br>" + toString(client, "");
    }

    @Override
    protected String toString(final TaxiEntity entity, final String margin) {
        String result = "<br>" + margin + "---------------<br>";
        if (entity != null) {
            Client client = (Client) entity;
            result += margin + "Client Id: " + client.getId().toString() + "<br>";
            result += margin + "Client Name: " + client.getName() + "<br>";
            result += margin + "Client Phone: " + client.getPhone() + "<br>";
        } else {
            result += margin + "!!! No Clients to print. Entity is null !!!" + "<br>";
        }
        result += margin + "---------------<br>";
        return result;
    }

    @Override
    protected String buildDataLine(final TaxiEntity entity) {
        Client client = (Client) entity;
        return client.getId().toString() + CSV_SEPARATOR
                + client.getName() + CSV_SEPARATOR
                + client.getPhone() + CSV_SEPARATOR;
    }

    @Override
    protected String buildHeaderLine() {
        return super.buildHeaderLine()
                + CSV_SEPARATOR + "Phone";
    }

    @Override
    protected TaxiEntity parseLine(final String line) {
        var properties = line.split(CSV_SEPARATOR);

        Client client  = new Client();
        client.setId(Integer.parseInt(properties[0]));
        client.setName(properties[1]);
        client.setPhone(properties[2]);

        return client;
    }
}
