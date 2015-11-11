package pl.bodolsog.GreenWaveFX.markersview;

import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.Accordion;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import pl.bodolsog.GreenWaveFX.MainApp;

/**
 * Created by bodolsog on 11.11.15.
 */
public class MarkersViewController {

    // Reference to main app.
    private MainApp mainApp;

    @FXML
    private Accordion markersPane;

    @FXML
    private void initialize(){

        MarkersList.markers.addListener(new ListChangeListener<Marker>() {

            @Override
            public void onChanged(ListChangeListener.Change change) {
                while (change.next()) {
                    if(change.wasAdded()){
                        addMarkerToPane(MarkersList.markers.get(change.getFrom()));
                    }
                }
            }
        });
    }

    /**
     * Is called by the main application to give a reference back to itself.
     *
     * @param mainApp
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    private void addMarkerToPane(Marker marker){
        TitledPane tp = new TitledPane();
        tp.setText("#");
        VBox hb = new VBox();
        hb.getChildren().addAll(
                new Label("Lat: "+marker.latProperty().getValue()),
                new Label("Lng: "+marker.lngProperty().getValue())
        );
        tp.setContent(hb);
        markersPane.getPanes().add(tp);
        markersPane.setExpandedPane(tp);
    }
}

/*
        //using a two-parameter constructor
        TitledPane tp = new TitledPane("My Titled Pane", new Button("Button"));
        //applying methods
        //TitledPane tp = new TitledPane();
        //tp.setText("My Titled Pane");
        //tp.setContent(new Button("Button"));
        markersList.getPanes().addAll(tp);
*/