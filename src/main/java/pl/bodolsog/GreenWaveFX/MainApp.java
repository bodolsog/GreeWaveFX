package pl.bodolsog.GreenWaveFX;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import pl.bodolsog.GreenWaveFX.controller.MapViewController;
import pl.bodolsog.GreenWaveFX.model.Markers;

import java.io.IOException;

public class MainApp extends Application {

    private Stage primaryStage;
    private BorderPane rootLayout;

    // Reference to MapViewController.
    private MapViewController mapViewController;

    private Markers markers;

    // Map of markers <id, marker>.
    //private ObservableMap<String,Marker> ways = FXCollections.observableHashMap();

    //
    //private int focusedMarkerId;

    // Reference to MarkersController.
    //private MarkersController markersViewController;

    // Starts app
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("GreenWaveFX");

        // Set up Markers
        markers = new Markers();

        // Init layout
        initRootLayout();
        showMapView();
        //showMarkersView();
    }


    /**
     * Initializes root layout.
     */
    private void initRootLayout(){
        try {
            // Load root layout from fxml file.
            final FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/MainAppView.fxml"));
            rootLayout = loader.load();

            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout, 1600, 900);
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
            final FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/MapView.fxml"));
            WebView mapView = loader.load();

            // Give the controller access to the main app.
            mapViewController = loader.getController();
            //mapViewController.passMainAppReference(this);
            mapViewController.passMarkersReference(markers);

            // Show the scene containing the root layout.
            rootLayout.setCenter(mapView);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }



//    public int getFocusedMarkerId(){
//        return focusedMarkerId;
//    }
//
//    public void setFocusedMarkerId(int newFocusedId){
//        focusedMarkerId = newFocusedId;
//    }


/*    *//**
     * Show markers list in root layout.
     *//*
    private void showMarkersView(){
        try {
            // Load accordion from fxml file.
            final FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/MarkersView.fxml"));
            ScrollPane markersView = (ScrollPane) loader.load();

            // Give the controller access to the main app.
            //markersViewController = loader.getController();
            //markersViewController.setMainApp(this);

            // Show the scene containing the root layout.
            rootLayout.setRight(markersView);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/

    /**
     * Return markersViewController
     * @return  markersViewController
     */
//    public MarkersController getMarkersViewController(){
//        return markersViewController;
//    }

    /**
     * Return mapViewController
     * @return  mapViewController
     */
//    public MapViewController getMapViewController(){
//        return mapViewController;
//    }
//



}
