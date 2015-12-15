package pl.bodolsog.GreenWaveFX.tools;

import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.*;

public class PropertiesManagerTest {
    String filePathString = "config.properties";
    PropertiesManager propertiesManager;

    @Ignore @Test
    public void whenPropertiesManagerIsCreatedThenNewConfigPropertiesFileIsCreated() throws IOException {
        Path f = Paths.get(filePathString);
        Files.deleteIfExists(f);
        PropertiesManager pm = new PropertiesManager();
        File config = new File(filePathString);
        assertTrue("File don't exists.", config.exists());
    }

    @Test
    public void whenPropertiesManagerIsLoadedThenConfigFileWItchDefaultDataIsLoaded(){
        propertiesManager = new PropertiesManager();
        assertNotNull("GoogleAPIKey is null", propertiesManager.getGoogleAPIKey());
        //assertEquals("GoogleAPIKey is not same", "[here add your Google API Key for browsers]", propertiesManager.getGoogleAPIKey());
    }


}