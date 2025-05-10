package com.striim.expensemanager.driver;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigLoader {
    private static ConfigLoader instance = new ConfigLoader();

    public static ConfigLoader getInstance() {
        return instance;
    }

    private Properties properties = new Properties();

    private ConfigLoader() {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                throw new IOException("config.properties not found in classpath");
            }
            properties.load(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getXMLSchemaPath() {
        return properties.getProperty("xsd.schema.path");
    }
}
