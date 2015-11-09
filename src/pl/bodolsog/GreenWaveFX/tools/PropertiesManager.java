package pl.bodolsog.GreenWaveFX.tools;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * This class handle properties from config.properties file.
 *
 * @author Paweł B.B. Drozd
 *
 */
public class PropertiesManager {

    private Properties properties;

    /**
     * Constructor create properties object from stream to config.properties file.
     */
    public PropertiesManager(){
        properties = new Properties();
        try {
            properties.load(new FileInputStream("config.properties"));
        } catch (IOException e) {
            System.out.println("Sorry, could not load config file.");
        }
    }

    /**
     * Read from config file Google API key.
     * @return Google API key
     */
    public String getGoogleAPIKey(){
        return properties.getProperty("googleAPIKey");
    }

}
