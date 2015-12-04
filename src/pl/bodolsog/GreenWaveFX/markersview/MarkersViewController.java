package pl.bodolsog.GreenWaveFX.markersview;

import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.fxml.FXML;
import javafx.scene.control.Accordion;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.VBox;
import pl.bodolsog.GreenWaveFX.MainApp;
import pl.bodolsog.GreenWaveFX.model.Markers;


/**
 * Controller for markers displayed in right pane.
 *
 */
public class MarkersViewController {

    // Reference to main app.
    public MainApp mainApp;

    // Map of titledPanes <id, titledPane>.
    private ObservableMap<String,TitledPane> titledPanesMap = FXCollections.observableHashMap();

    // Accordion that lists all Markers.
    @FXML
    private Accordion markersPane;

    /**
     * Initializes view.
     */
    @FXML
    private void initialize(){
        // Adds listener to markersMap.
        //TODO move listener to main app?
        addMarkersListener();
        addAccordionListener();
    }

    /**
     * Is called by the main application to give a reference back to itself.
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
        mainApp.markers.addListener((MapChangeListener.Change<? extends String, ? extends Markers> change) -> {
            if (change.wasAdded()) {
                // Adds marker to AccordionPane.
                addMarkerToPane(mainApp.markers.get(change.getKey()));
                // Adds listener to nameProperty of new marker. Listener change TitledPane title, when new
                // nameProperty was setted.
                mainApp.markers.get(change.getKey()).nameProperty().addListener(
                        ((observableValue, oldName, newName) ->
                                setMarkerNameInPane(mainApp.markers.get(change.getKey()))
                        )
                );
            }
        });
    }

    /**
     * Adds listener to accordion expander property.
     *
     * When expande is changed, listener get id of expanded Marker and launch method that calls js function
     * that set variables to previous marker id and actual (this will have focus).
     */
    private void addAccordionListener(){
        // Launch when new pane is expanded.
        markersPane.expandedPaneProperty().addListener((observable, oldTitledPane, newTitledPane) -> {
            // Set empty variables.
            String oldMarkerId = "";
            String newMarkerId = "";
            // TODO switch to catch
            if(oldTitledPane != null)
                oldMarkerId = oldTitledPane.getId();
            if(newTitledPane != null)
                newMarkerId = newTitledPane.getId();
            // Call MapViewController method for set new variables and marker focus.
            mainApp.getMapViewController().setMarkerFocus(oldMarkerId, newMarkerId);
        });
    }


    /**
     * Adds TitledPane for new marker.
     * @param   marker
     */
    private void addMarkerToPane(Markers marker){
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
    private void setMarkerNameInPane(Markers marker){
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
     * Add new label witch info about new estabilished connection to another pane.
     * @param marker reference to marker
     * @param conectionIndex index added/edited connection from marker's label.
     */
    public void addConnectionToTitledPane(Markers marker, int conectionIndex){
        // Get connection.
        String connection = marker.getConnections().get(conectionIndex);
        // Get pane from map.
        TitledPane tp = titledPanesMap.get(marker.idProperty().getValue());
        // Get VBox from TitledPane content.
        VBox vbox = (VBox) tp.getContent();
        // Adds new label
        vbox.getChildren().add(new Label(": "+connection));
        // Set new content.
        tp.setContent(vbox);
    }

    /**
     * Expand pane with informations about marker, that was clicked in map.
     * @param id Marker's id
     */
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
        mainApp.markers.remove(id);
    }

    /**
     * Add marker to observable list.
     * @param id  marker's name
     * @param lat   latitude from Google Maps
     * @param lng   longitude from Google Maps
     */
    public void addMarker(String id, double lat, double lng){
        Markers newMarker = new Markers(id, lat, lng);
        newMarker.setController(this);
        mainApp.markers.put(id, newMarker);
    }

    /**
     * Get Marker from markersMap and set new latLng for them.
     * @param id Marker's id
     * @param lat latitiude
     * @param lng longitude
     */
    public void setMarkerLatLng(String id, double lat, double lng){
        Markers marker = mainApp.markers.get(id);
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
        mainApp.markers.get(id).setName(crossName);
        return crossName;
    }

    /**
     * Estabilish new one or two way connection between Markers.
     * @param mode connect2w for two way connection, other string for one way
     * @param markerOne (if one way: from) marker's id
     * @param markerTwo (if one way: to) marker's id
     */
    public void connectMarkers(String mode, String markerOne, String markerTwo){
        // Add connection from markerOne to markerTwo
        mainApp.markers.get(markerOne).addConnection(markerTwo);
        // If mode is "cinnect2w" adds connection backwards (mTwo-mOne)
        if(mode.equals("connect2w")){
            mainApp.markers.get(markerTwo).addConnection(markerOne);
        }
    }
}