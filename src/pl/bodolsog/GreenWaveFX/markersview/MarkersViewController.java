package pl.bodolsog.GreenWaveFX.markersview;

import javafx.beans.Observable;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableMap;
import javafx.fxml.FXML;
import javafx.scene.control.Accordion;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import pl.bodolsog.GreenWaveFX.MainApp;

import java.beans.PropertyChangeListener;

/**
 * Created by bodolsog on 11.11.15.
 */
public class MarkersViewController {

    // Reference to main app.
    private MainApp mainApp;

    public static ObservableMap<String,TitledPane> titledPanes = FXCollections.observableHashMap();

    @FXML
    private Accordion markersPane;

    @FXML
    private void initialize(){

        MarkersList.markers.addListener(new MapChangeListener<String,Marker>() {

            @Override
            public void onChanged(MapChangeListener.Change change) {
                if(change.wasAdded()){
                    // Adds marker to AccordionPane.
                    addMarkerToPane(MarkersList.markers.get(change.getKey()));
                    // Adds listener to nameProperty of new marker. Listener change TitledPane title, when new
                    // nameProperty was setted.
                    MarkersList.markers.get(change.getKey()).nameProperty().addListener(
                            ((observableValue, oldName, newName) ->
                                    editMarkerNameInPane(MarkersList.markers.get(change.getKey()), newName)
                            )
                    );
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


    /**
     * Adds TitledPane for new marker.
     * @param marker
     */
    private void addMarkerToPane(Marker marker){
        TitledPane tp = new TitledPane();
        titledPanes.put(marker.idProperty().getValue(), tp);
        try{
            tp.setText(marker.nameProperty().getValue());
        } catch (NullPointerException e){
            tp.setText(marker.idProperty().getValue());
        }
        VBox hb = new VBox();
        hb.getChildren().addAll(
                new Label("Lat: "+marker.latProperty().getValue()),
                new Label("Lng: "+marker.lngProperty().getValue())
        );
        tp.setContent(hb);
        markersPane.getPanes().add(tp);
        markersPane.setExpandedPane(tp);
    }

    /**
     * Edits TitledPane for new marker.
     * @param newName   new marker's name
     */
    private void editMarkerNameInPane(Marker marker, String newName){
        TitledPane tp = titledPanes.get(marker.idProperty().getValue());
        try{
            tp.setText(marker.nameProperty().getValue());
        } catch (NullPointerException e){
            tp.setText(marker.idProperty().getValue());
        }
    }
}