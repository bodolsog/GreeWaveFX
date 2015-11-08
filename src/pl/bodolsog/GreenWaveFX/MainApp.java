package pl.bodolsog.GreenWaveFX;/**
 * Created by bodolsog on 07.11.15.
 */

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {

        final FXMLLoader loader = new FXMLLoader(getClass().getResource("view/MapView.fxml"));
        final Parent root = (Parent) loader.load();

        //TODO: Load controller

        Scene scene = new Scene(root, 1000, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
