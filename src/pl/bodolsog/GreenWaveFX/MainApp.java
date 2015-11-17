package pl.bodolsog.GreenWaveFX;


import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableMap;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Accordion;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import pl.bodolsog.GreenWaveFX.mapview.MapViewController;
import pl.bodolsog.GreenWaveFX.markersview.Marker;
import pl.bodolsog.GreenWaveFX.markersview.MarkersViewController;

import java.io.IOException;
import java.util.concurrent.ExecutorService;

public class MainApp extends Application {

    private Stage primaryStage;
    private BorderPane rootLayout;

    /**
     * Reference to MarkersViewController.
     */
    private MarkersViewController markersViewController;

    // Starts app
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("GreenWaveFX");

        // Init layout
        initRootLayout();
        showMapView();
        showMarkersView();
    }


    /**
     * Initializes root layout.
     */
    private void initRootLayout(){
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
    private void showMapView(){
        try {
            // Load map from fxml file.
            final FXMLLoader loader = new FXMLLoader(getClass().getResource("mapview/MapView.fxml"));
            WebView mapView = (WebView) loader.load();

            // Give the controller access to the main app.
            MapViewController mapViewController = loader.getController();
            mapViewController.setMainApp(this);

            // Show the scene containing the root layout.
            rootLayout.setCenter(mapView);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Show markers list in root layout.
     */
    private void showMarkersView(){
        try {
            // Load accordion from fxml file.
            final FXMLLoader loader = new FXMLLoader(getClass().getResource("markersview/MarkersView.fxml"));
            ScrollPane markersView = (ScrollPane) loader.load();

            // Give the controller access to the main app.
            markersViewController = loader.getController();
            markersViewController.setMainApp(this);

            // Show the scene containing the root layout.
            rootLayout.setRight(markersView);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Return markersViewController
     * @return  markersViewController
     */
    public MarkersViewController getMarkersViewController(){
        return markersViewController;
    }
}
