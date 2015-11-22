package pl.bodolsog.GreenWaveFX.markersview;

import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Accordion;
import javafx.scene.control.Hyperlink;
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
        addAccordionListener();
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

    private void addAccordionListener(){
        markersPane.expandedPaneProperty().addListener((observable, oldTitledPane, newTitledPane) -> {
            String oldMarkerId = "";
            String newMarkerId = "";
            if(oldTitledPane != null)
                oldMarkerId = oldTitledPane.getId();
            if(newTitledPane != null)
                newMarkerId = newTitledPane.getId();
            mainApp.getMapViewController().setMarkerFocus(oldMarkerId, newMarkerId);
        });
    }


    /**
     * Adds TitledPane for new marker.
     * @param   marker
     */
    private void addMarkerToPane(Marker marker){
        // New TitledPane.
        TitledPane tp = new TitledPane();

        tp.setId(marker.idProperty().getValue());
        // Set this TitledPane into map <id, titledPane>.
        titledPanesMap.put(marker.idProperty().getValue(), tp);
        // Set cross name if exists, else set id as name.
        setMarkerNameInPane(marker);
        // VBox for content.
        VBox hb = new VBox();
        // Delete label.
        Hyperlink delete = new Hyperlink("Delete");
        delete.setOnMouseClicked(mouseEvent -> {
            deleteMarker(marker.idProperty().getValue());
        });
        // Temporary latLng values.
        hb.getChildren().addAll(
                new Label("Lat: "+marker.latProperty().getValue()),
                new Label("Lng: "+marker.lngProperty().getValue()),
                delete
        );

        tp.setContent(hb);
        // Add this pane to Accordion and set this active.
        markersPane.getPanes().add(tp);
        markersPane.setExpandedPane(tp);
    }

    /**
     * Remove TitledPane from Accordion. Called after user clicks delete hyperlink.
     * @param markerId  Marker's id
     */
    private void deleteMarkerFromPane(String markerId){
        // Get TitledPane.
        TitledPane tp = titledPanesMap.get(markerId);
        // Remove TitledPane from Accordion.
        markersPane.getPanes().remove(tp);
        // Remove TitledPane from map.
        titledPanesMap.remove(markerId);
    }

    /**
     * Call deleteMarker from MapViewController
     * @param id Marker's id
     */
    private void deleteMarkerFromMap(String id){
        mainApp.getMapViewController().deleteMarker(id);
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

    public void addConnectionToTitledPane(Marker marker, int conectionIndex){
        // Get pane from map.
        TitledPane tp = titledPanesMap.get(marker.idProperty().getValue());
        String connection = marker.getConnections().get(conectionIndex);
        VBox vbox = (VBox) tp.getContent();
        vbox.getChildren().add(new Label(": "+connection));
        tp.setContent(vbox);
    }

    public void setClickedFocus(String id){
        TitledPane tp = titledPanesMap.get(id);
        markersPane.setExpandedPane(tp);
    }

    /**
     * Bound function to remove Marker. Call functions that removes from Pane, Map and remove from markersMap.
     * @param id Marker's id
     */
    public void deleteMarker(String id){
        deleteMarkerFromPane(id);
        deleteMarkerFromMap(id);
        markersMap.remove(id);
    }

    /**
     * Add marker to observable list.
     * @param id  marker's name
     * @param lat   latitude from Google Maps
     * @param lng   longitude from Google Maps
     */
    public void addMarker(String id, double lat, double lng){
        Marker newMarker = new Marker(id, lat, lng);
        newMarker.setController(this);
        markersMap.put(id, newMarker);
    }

    /**
     * Get Marker from markersMap and set new latLng for them.
     * @param id Marker's id
     * @param lat latitiude
     * @param lng longitude
     */
    public void setMarkerLatLng(String id, double lat, double lng){
        Marker marker = markersMap.get(id);
        marker.setLat(lat);
        marker.setLng(lng);
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

    public void connectMarkers(String mode, String markerOne, String markerTwo){
        markersMap.get(markerOne).addConnection(markerTwo);
        if(mode.equals("connect2w")){
            markersMap.get(markerTwo).addConnection(markerOne);
        }
    }
}