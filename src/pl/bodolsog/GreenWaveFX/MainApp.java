package pl.bodolsog.GreenWaveFX;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Accordion;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApp extends Application {

    private Stage primaryStage;
    private BorderPane rootLayout;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("GreenWaveFX");

        initRootLayout();
        showMapView();
        showMarkersList();

    }

    /**
     * Initializes root layout.
     */
    public void initRootLayout(){
        try {
            // Load root layout from fxml file.
            final FXMLLoader loader = new FXMLLoader(getClass().getResource("MainAppView.fxml"));
            rootLayout = (BorderPane) loader.load();

            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout, 1000, 600);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Show map in root layout.
     */
    public void showMapView(){
        try {
            // Load map from fxml file.
            final FXMLLoader loader = new FXMLLoader(getClass().getResource("mapview/MapView.fxml"));
            WebView mapView = (WebView) loader.load();

            // Give the controller access to the main app.
            //MapViewController controller = loader.getController();
            //controller.setMainApp(this);

            // Show the scene containing the root layout.
            rootLayout.setCenter(mapView);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Show markers list in root layout.
     */
    public void showMarkersList(){
        try {
            // Load accordion from fxml file.
            final FXMLLoader loader = new FXMLLoader(getClass().getResource("markersview/MarkersView.fxml"));
            Accordion markersList = (Accordion) loader.load();

            // Show the scene containing the root layout.
            rootLayout.setRight(markersList);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
