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
    private void initialize(){
        // Adds listener to markersMap.
        addMarkersListener();
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
     * Adds listener to marker's map.
     *
     * When item is added to map, listener call addMarkerToPane (set new TitlePane for Marker into right pane) and set
     * new listener for nameProperty of it. If reverse geocoding is finish his work and set new name of streets cross -
     * this will change TitlePane title for this value.
     */
    private void addMarkersListener(){
        markersMap.addListener((MapChangeListener.Change<? extends String, ? extends Marker> change) -> {
            if (change.wasAdded()) {
                // Adds marker to AccordionPane.
                addMarkerToPane(markersMap.get(change.getKey()));
                // Adds listener to nameProperty of new marker. Listener change TitledPane title, when new
                // nameProperty was setted.
                markersMap.get(change.getKey()).nameProperty().addListener(
                        ((observableValue, oldName, newName) ->
                                setMarkerNameInPane(markersMap.get(change.getKey()))
                        )
                );
            }
        });
    }

    /**
     * Adds TitledPane for new marker.
     * @param   marker
     */
    private void addMarkerToPane(Marker marker){
        // New TitledPane.
        TitledPane tp = new TitledPane();
        // Set this TitledPane into map <id, titledPane>.
        titledPanesMap.put(marker.idProperty().getValue(), tp);
        // Set cross name if exists, else set id as name.
        setMarkerNameInPane(marker);
        // VBox for content.
        VBox hb = new VBox();
        // Temporary latLng values.
        hb.getChildren().addAll(
                new Label("Lat: "+marker.latProperty().getValue()),
                new Label("Lng: "+marker.lngProperty().getValue())
        );
        tp.setContent(hb);
        // Add this pane to Accordion and set this active.
        markersPane.getPanes().add(tp);
        markersPane.setExpandedPane(tp);
    }


    /**
     * Edits TitledPane for new marker.
     * @param marker    Marker instance
     */
    private void setMarkerNameInPane(Marker marker){
        // Get pane from map.
        TitledPane tp = titledPanesMap.get(marker.idProperty().getValue());

        // Try set cross name if that exists. Else set id as name.
        if(marker.nameProperty().getValue().equals("")){
            tp.setText(marker.idProperty().getValue());
        } else {
            tp.setText(marker.nameProperty().getValue());
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
     * From streets names list get cross name. Set this for Marker and return. This is fired from listener, when all
     * four location was geocoded (duplicated street names was set to empty string).
     * @param   id - id of this marker
     * @param   streetsNames - list of streets names
     * @return  name of cross (street1/street2)
     */
    public String setCrossName(String id, ObservableList<? extends String> streetsNames){
        // Init string.
        String crossName = "";
        // Build name from every not empty name.
        for(String street : streetsNames){
            // Append street when is not empty string.
            if(!street.equals(""))
                // If that isn't first street - add slash before street name.
                if(!crossName.equals(""))
                    crossName += "/"+street;
                else
                    crossName += street;
        }
        // Set name to Marker and return.
        markersMap.get(id).setName(crossName);
        return crossName;
    }
}