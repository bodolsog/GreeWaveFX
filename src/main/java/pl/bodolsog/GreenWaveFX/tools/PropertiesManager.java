package pl.bodolsog.GreenWaveFX.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Handle properties from config.properties file.
 *
 * @author Paweł B.B. Drozd
 */
public class PropertiesManager {

    private String propertiesPath = "config.properties";
    private Properties properties = new Properties();

    /**
     * Constructor create properties object from stream to config.properties file.
     */
    public PropertiesManager() {

        File propertiesFile = new File(propertiesPath);

        // Create file with default data if not exists yet.
        if (!propertiesFile.exists()){
            try {
                propertiesFile.createNewFile();
                setDefaultProperties();
                FileOutputStream out = new FileOutputStream(propertiesFile);
                properties.store(out, "");
                out.close();
            } catch (IOException e) {
                System.out.println("Sorry, could not create or load config file.");
            }
        }
        try {
            FileInputStream in = new FileInputStream(propertiesFile);
            properties.load(in);
        } catch (IOException e){
            System.out.println("Sorry, could not load config file.");
        }

    }

    /**
     * Read from config file Google API key.
     *
     * @return Google API key
     */
    public String getGoogleAPIKey() {
        return properties.getProperty("googleAPIKey");
    }

    public int[] getSpeedList() {
        String speeds = properties.getProperty("speeds");
        String[] speedsList = speeds.split(",");
        int len = speedsList.length;
        int[] rSpeeds = new int[len];
        for (int i = 0; i < len; i++) {
            rSpeeds[i] = Integer.parseInt(speedsList[i]);
        }

        return rSpeeds;
    }

    public int getAcceleration() {
        return Integer.parseInt(properties.getProperty("acceleration"));
    }

    private void setDefaultProperties(){
        properties.setProperty("googleAPIKey", "[here add your Google API Key for browsers]");
    }
}
