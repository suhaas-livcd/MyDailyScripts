package application;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class MainController implements Initializable {

    @FXML
    private ScrollPane scrlPane_AppLog;

    @FXML
    private TextFlow txtFlow_AppLog;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        // TODO Auto-generated method stub
        recordAppLog("Controller :: initialize");

        System.out.println("Date is :: "+Utils.getCurrentDate());
        System.out.println("Logged");
    }

    public void recordAppLog(String appLog) {
        Platform.runLater(() -> {
            Text text = new Text(appLog + "\n > ");
            txtFlow_AppLog.getChildren().addListener((ListChangeListener<Node>) ((change) -> {
                txtFlow_AppLog.requestLayout();
                scrlPane_AppLog.setVvalue(1.0);
            }));
            txtFlow_AppLog.getChildren().add(text);
        });
    }
}
