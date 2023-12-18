package org.example.vkAPI;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class Configuration {
    private List<String[]> dataConfig;
    public List<String[]> getConfigData() {
        CSVParser csvParser = new CSVParserBuilder().withSeparator(';').build();
        try (CSVReader reader = new CSVReaderBuilder(
                new InputStreamReader(new FileInputStream("vk_id/vk_api.csv"),"UTF-8"))
                .withCSVParser(csvParser)
                .build()) {
            dataConfig = reader.readAll();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return dataConfig;
    }
}
