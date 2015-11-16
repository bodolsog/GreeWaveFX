package pl.bodolsog.GreenWaveFX.markersview;

import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.fxml.FXML;
import javafx.scene.control.Accordion;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.VBox;
import pl.bodolsog.GreenWaveFX.MainApp;


/**
 * Controller for markers displayed in right pane.
 *
 */
public class MarkersViewController {

    // Reference to main app.
    public MainApp mainApp;

    /**
     * Map of markers <id, marker>.
     */
    private ObservableMap<String,Marker> markersMap = FXCollections.observableHashMap();

    /**
     * Map of titledPanes <id, titledPane>.
     */
    private ObservableMap<String,TitledPane> titledPanesMap = FXCollections.observableHashMap();


    @FXML
    private Accordion markersPane;

    @FXML
    private void initialize(){}


    /**
     * Is called by the main application to give a reference back to itself.
     *
     * @param mainApp
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
        addMarkersListener();
    }

    private void addMarkersListener(){
        markersMap.addListener((MapChangeListener.Change<? extends String, ? extends Marker> change) -> {
            if (change.wasAdded()) {
                // Adds marker to AccordionPane.
                addMarkerToPane(markersMap.get(change.getKey()));
                // Adds listener to nameProperty of new marker. Listener change TitledPane title, when new
                // nameProperty was setted.
                markersMap.get(change.getKey()).nameProperty().addListener(
                        ((observableValue, oldName, newName) ->
                                editMarkerNameInPane(markersMap.get(change.getKey()), newName)
                        )
                );
            }
        });
    }

    /**
     * Adds TitledPane for new marker.
     * @param marker
     */
    private void addMarkerToPane(Marker marker){
        TitledPane tp = new TitledPane();
        titledPanesMap.put(marker.idProperty().getValue(), tp);
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
     * @param marker    Marker instance
     * @param newName   new marker's name
     */
    private void editMarkerNameInPane(Marker marker, String newName){
        TitledPane tp = titledPanesMap.get(marker.idProperty().getValue());
        try{
            tp.setText(marker.nameProperty().getValue());
        } catch (NullPointerException e){
            tp.setText(marker.idProperty().getValue());
        }
    }

    /**
     * Add marker to observable list.
     * @param id  marker's name
     * @param lat   latitude from Google Maps
     * @param lng   longitude from Google Maps
     */
    public void addMarker(String id, double lat, double lng){
        markersMap.put(id, new Marker(id, lat, lng));
    }

    /**
     * Set name for Marker.
     * @param id    hash
     * @param name  name (cross streets)
     */
    public void setName(String id, String name){
        markersMap.get(id).setName(name);
    }

    public String setCrossName(String id, ObservableList<? extends String> streetsNames){
        String crossName = "";
        for(String street : streetsNames){
            if(!street.equals(""))
                if(!crossName.equals(""))
                    crossName += "/"+street;
                else
                    crossName += street;
        }
        markersMap.get(id).setName(crossName);
        return crossName;

    }
}