package org.bot.infrastructure;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;


public class PropertyParser {
    public static Properties getProperties(String path) throws IOException {
        FileInputStream fileInputStream;
        Properties properties = new Properties();

        fileInputStream = new FileInputStream(path);
        properties.load(fileInputStream);
        return properties;
    }
}
