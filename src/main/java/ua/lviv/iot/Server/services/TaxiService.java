package ua.lviv.iot.Server.services;

import ua.lviv.iot.Server.models.TaxiEntity;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class TaxiService {
    public static final String CSV_FILES_PATH = "Data/";
    public static final String CSV_SEPARATOR = ";";
    public static final String HTML_TAB = "&emsp;&emsp;";

    private static final Map<String, TaxiService> services = new HashMap<>();

    protected Map<Integer, TaxiEntity> data;
    protected String csvFileURL = "";
    protected String entityClassName = "";

    public TaxiService() {
        data = new HashMap<>();
        String serviceClassName = this.getClass().getName();
        if (!services.containsKey(serviceClassName)) {
            services.put(serviceClassName, this);
        }
    }

    public abstract String addEntity(Map<String, String> params) throws IOException;

    public abstract String toStringAll();

    public void setCsvFileURL(final String url) {
        csvFileURL = url;
    }

    public TaxiService getService(final String serviceClassName) {
        return services.get(serviceClassName);
    }

    public void add(final TaxiEntity entity) throws IOException {
        entity.setId(getNextID());
        data.put(entity.getId(), entity);
        writeToCSV();
    }

    public String toStringById(final int id, final String margin) {
        return toString(data.get(id), margin);
    }

    public String toStringById(final int id) {
        return toStringById(id, "");
    }

    public TaxiEntity getById(final int id) {
        return data.get(id);
    }

    public List<TaxiEntity> getAll() {
        return new ArrayList<TaxiEntity>(data.values());
    }

    public void readCSV() throws IOException {
        data.clear();
        int entitiesCount = 0;
        try (FileReader fileReader = new FileReader(csvFileURL);
            BufferedReader actualBR = new BufferedReader(fileReader)) {
            String currentLine = actualBR.readLine();
            while (currentLine != null) {
                if (entitiesCount > 0) {
                    TaxiEntity entity = parseLine(currentLine);
                    data.put(entity.getId(), entity);
                }
                currentLine = actualBR.readLine();
                entitiesCount++;
            }
            fileReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeToCSV() throws IOException {
        try (FileWriter fileWriter = new FileWriter(csvFileURL, false)) {
            String header = buildHeaderLine();
            fileWriter.write(header);
            fileWriter.write("\r\n");
            for (Map.Entry<Integer, TaxiEntity> item : data.entrySet()) {
                String line = buildDataLine(item.getValue());
                fileWriter.write(line);
                fileWriter.write("\r\n");
            }
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void removeAll() throws IOException {
        data.clear();
        writeToCSV();
    }

    public String removeByIdString(final int id) throws IOException {
        String result = "The " + entityClassName + " with id:" + id;
        result += data.remove(id) != null ? " have removed!" : " not found!!!";
        writeToCSV();
        return result;
    }

    public TaxiEntity removeById(final int id) throws IOException {
        var result = data.remove(id);
        if (result != null) {
            writeToCSV();
        }
        return result;
    }

    protected abstract String toString(TaxiEntity entity, String margin);
    protected abstract String buildDataLine(TaxiEntity entity);
    protected abstract TaxiEntity parseLine(String line);

    public Integer getNextID() {
        return data.keySet().stream().max(Integer::compareTo).orElse(0) + 1;
    }

    protected String buildHeaderLine() {
        return "ID" + CSV_SEPARATOR + "Name";
    }

    public boolean edit(final int id, final TaxiEntity entity) throws IOException {
        if (entity != null) {
            data.put(entity.getId(), entity);
            writeToCSV();
            return true;
        }
        return false;
    }
}
