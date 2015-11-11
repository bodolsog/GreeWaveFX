package pl.bodolsog.GreenWaveFX.view;

import javafx.fxml.FXML;
import javafx.scene.control.Accordion;
import pl.bodolsog.GreenWaveFX.MainApp;

/**
 * Created by bodolsog on 11.11.15.
 */
public class MarkersListController {

    // Reference to main app.
    private MainApp mainApp;

    @FXML
    private Accordion markersList;

    @FXML
    private void initialize(){

    }

    /**
     * Is called by the main application to give a reference back to itself.
     *
     * @param mainApp
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
}
