package application;

import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
            VBox root = (VBox) FXMLLoader.load(getClass().getResource("MainUI.fxml"));
            Scene scene = new Scene(root, 1000, 700);
            scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
            setApplicationIcon(primaryStage);
            primaryStage.setTitle(" Shelly - Make Your Day Count");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void setApplicationIcon(Stage primaryStage) {
        try {
            String appIconPath = "app_icon_calender.png";
            Image image = new Image(getClass().getResource(appIconPath).toExternalForm());
            primaryStage.getIcons().add(image);
        } catch (Exception e) {
            Logger.getLogger(Main.class.getSimpleName()).log(Level.SEVERE, " setApplicationIcon ::" + e.getMessage());
            e.printStackTrace();
        }
    }
}
