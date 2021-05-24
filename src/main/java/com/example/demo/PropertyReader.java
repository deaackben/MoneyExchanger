package com.example.demo;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyReader {

    public static String getProperty(String propertyKey)  {
        try {
            InputStream input = new FileInputStream("src/main/resources/currency.properties");
            Properties prop = new Properties();
            prop.load(input);
            System.out.println(prop.getProperty("Property read: " + propertyKey));
            return prop.getProperty(propertyKey);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
