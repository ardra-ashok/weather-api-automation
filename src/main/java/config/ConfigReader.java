package config;

import java.io.FileInputStream;
import java.util.Properties;

    /* ConfigReader for reading property files (url, api key) */

public class ConfigReader {

    static Properties prop;
    static {

        try {
            prop = new Properties();
            String propFile = "src/supportData/config.properties";
            FileInputStream file =
                    new FileInputStream(propFile);
            prop.load(file);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getProperty(String key) {
        return prop.getProperty(key);
    }
}
