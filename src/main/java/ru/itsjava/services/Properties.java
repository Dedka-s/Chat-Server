package ru.itsjava.services;

import lombok.SneakyThrows;

import java.io.InputStream;

public class Properties {
        @SneakyThrows
        public static String getValue(String kay){
            InputStream inputStream = Properties.class.getClassLoader().getResourceAsStream("application.properties");

            java.util.Properties properties = new java.util.Properties();
            properties.load(inputStream);

            return properties.getProperty(kay);
        }
    }

