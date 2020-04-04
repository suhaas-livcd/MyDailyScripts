package application;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.Iterator;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PushCommand;
import org.eclipse.jgit.api.Status;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.NoFilepatternException;
import org.eclipse.jgit.errors.RepositoryNotFoundException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.transport.PushResult;
import org.eclipse.jgit.transport.RefSpec;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.FontSmoothingType;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.scene.web.HTMLEditor;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.util.Pair;
import netscape.javascript.JSObject;

public class MainController implements Initializable {

    @FXML
    private Menu menu_File, menu_Themes, menu_Help;

    @FXML
    private MenuItem mitem_FileSettings, mitem_FileClose, mitem_HelpAbout;

    @FXML
    private RadioMenuItem mitem_ThemeLight, mitem_ThemeHacker;

    @FXML
    private ToggleGroup theme_group;

    @FXML
    private Tab tab_ViewLogger, tab_Data, tab_LogFile, tab_Tasks;

    @FXML
    private Label lbl_DaysLogged, lbl_DaysMissed, lbl_DaysLeft, lbl_DaysLeftInWeek, lbl_DaysLeftInMonth,
            lbl_DaysLeftInYear, lbl_MyAge, lbl_DateToday;

    @FXML
    private ProgressBar prgz_week, prgz_month, prgz_year;

    @FXML
    private TextArea txtarea_LogSub;

    @FXML
    private HTMLEditor txtarea_LogContent;

    @FXML
    private Button btn_LogSave, btn_LogMaxView, btn_TasksSave, btn_LogCommit;

    @FXML
    private CheckBox cb_task00, cb_task01, cb_task02, cb_task03, cb_task04, cb_task05, cb_task06, cb_task07, cb_task08,
            cb_task09;

    @FXML
    private ChoiceBox<?> cb_pr_task00, cb_pr_task01, cb_pr_task02, cb_pr_task03, cb_pr_task04, cb_pr_task05,
            cb_pr_task06, cb_pr_task07, cb_pr_task08, cb_pr_task09;

    @FXML
    private ScrollPane scrlPane_AppLog;

    @FXML
    private TextFlow txtFlow_AppLog;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        initListener();
        recordAppLog("Controller :: initialize", Level.INFO);
    }

    public void initListener() {
        recordAppLog("Listner :: initListener", Level.ALL);

    }

    public void recordAppLog(String appLog, Level logLevel) {
        Logger.getLogger(MainController.class.getSimpleName()).log(logLevel, appLog);
        Platform.runLater(() -> {
            Text text = new Text("> " + appLog + "\n");
            text.setStyle("-fx-font: 13 consolas;");
            text.setFontSmoothingType(FontSmoothingType.LCD);
            if (logLevel == Level.INFO) {
                text.setFill(Color.DARKBLUE);
            } else if (logLevel == Level.SEVERE || logLevel == Level.WARNING) {
                text.setFill(Color.DARKRED);
            } else {
                text.setFill(Color.DARKGRAY);
            }

            txtFlow_AppLog.getChildren().addListener((ListChangeListener<Node>) ((change) -> {
                txtFlow_AppLog.requestLayout();
                scrlPane_AppLog.setVvalue(1.0);
            }));
            txtFlow_AppLog.getChildren().add(text);
        });
    }

    @FXML
    void btn_LogCommitClicked(MouseEvent event) {
        recordAppLog("Clicked :: btn_LogCommitClicked", Level.ALL);

        Alert alert = new Alert(AlertType.CONFIRMATION, "Are you sure you want to commit and push " + " ?",
                ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
        alert.showAndWait();
        if (alert.getResult() == ButtonType.YES) {
            // check if the repo is correct

            // create a Directory chooser
            DirectoryChooser dir_chooser = new DirectoryChooser();
            String lastUsedDir = Utils.getPrefs().get(Define.GNRL_PRF_GITLASTREPO, System.getProperty("user.home"));
            dir_chooser.setInitialDirectory(new File(lastUsedDir));
            File file = dir_chooser.showDialog(new Stage());
            System.out.println(file.getAbsolutePath());
            String newPath = file.getAbsolutePath() + "\\diary\\";
            System.out.println(newPath);

            // Checking ROOT :: "DIARY"
            String PATH = file.getAbsolutePath() + "\\diary\\";
            String directoryName = PATH.concat("");
            File directory = new File(directoryName);
            if (!directory.exists()) {
                boolean isDirCreated = directory.mkdir();
                System.out.println("CREATED :: " + isDirCreated + " - " + directory.getAbsolutePath());
            } else {
                System.out.println("FOUND :: " + directory.getAbsolutePath());
            }

            Date date = new Date();
            System.out.println("Date is :: " + Utils.getCurrentDate(date));
            String YEAR = Utils.getCurrentYear(date);
            String MONTH = Utils.getCurrentMonth(date);
            String FILE_NAME = Utils.getCurrentDate(date) + ".json";
            String COMMIT_MSG = FILE_NAME;

            // Checking DIR :: "YEAR"
            directoryName = directoryName.concat("\\" + YEAR + "\\");
            directory = new File(directoryName);
            if (!directory.exists()) {
                boolean isDirCreated = directory.mkdir();
                System.out.println("CREATED :: " + isDirCreated + " - " + directory.getAbsolutePath());
            } else {
                System.out.println("FOUND :: " + directory.getAbsolutePath());
            }

            // Checking FILE :: "MONTH"
            directoryName = directoryName.concat("\\" + MONTH + "\\");
            directory = new File(directoryName);
            if (!directory.exists()) {
                boolean isDirCreated = directory.mkdir();
                System.out.println("CREATED :: " + isDirCreated + " - " + directory.getAbsolutePath());
            } else {
                System.out.println("FOUND :: " + directory.getAbsolutePath());
            }

            // Checking DIR :: "FILE"
            directoryName = directoryName.concat("\\" + FILE_NAME);
            directory = new File(directoryName);
            if (!directory.exists()) {
                System.out.println("CREATED :: ");
                try {
                    // Creating content
                    LetterBean letterBean = new LetterBean();
                    letterBean.setTextbox_WordOfDay(txtarea_LogSub.getText());
                    COMMIT_MSG = letterBean.getTextbox_WordOfDay();
                    letterBean.setTextbox_Diary(txtarea_LogContent.getHtmlText());

                    Gson gson = new GsonBuilder().disableHtmlEscaping().create();
                    System.out.println(gson.toJson(letterBean));
                    FileWriter fileWriter = new FileWriter(directory.getAbsolutePath());
                    gson.toJson(letterBean, fileWriter);
                    fileWriter.flush();
                    fileWriter.close();
                    System.out.println(letterBean.getTextbox_WordOfDay() + "\n " + letterBean.getTextbox_Diary());
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            } else {
                // Not possible, because timestamp would vary,
                System.out.println("FOUND :: " + directory.getAbsolutePath());
            }
            // Open an existing repository
            Utils.getPrefs().put(Define.GNRL_PRF_GITLASTREPO, file.getAbsolutePath());
            try {
//                Repository existingRepo = new FileRepositoryBuilder().setGitDir(file).readEnvironment().findGitDir().build();
                Git git = Git.open(file);
                // Check if diary/ folder exists

                git.add().addFilepattern(".").call();
                Status gitStatus = git.status().call();
                System.out.println(" Git status :: "+gitStatus.getAdded());
                // Commit to the file
                RevCommit revCommit = git.commit().setMessage(COMMIT_MSG).call();
                System.out.println("Commit Status  :: " + revCommit.getFullMessage());

                Pair<String, String> usernamePassword = customDialog();
                if (usernamePassword != null) {
                    // push to remote:
                    PushCommand pushCommand = git.push();
                    pushCommand.setCredentialsProvider(new UsernamePasswordCredentialsProvider(
                            usernamePassword.getKey(), usernamePassword.getValue()));
                    pushCommand.setRemote("origin");
                    pushCommand.setRefSpecs(new RefSpec("refs/heads/master:refs/heads/master"));
                    Iterable<PushResult> pushResult = pushCommand.call();
                    for (Iterator<PushResult> iterator = pushResult.iterator(); iterator.hasNext();) {
                        PushResult rev = iterator.next();
                        System.out.println(rev.getMessages());
                    }

                }

                // you can add more settings here if needed
//                Iterable<RevCommit> log = git.log().call();
//                for (Iterator<RevCommit> iterator = log.iterator(); iterator.hasNext();) {
//                    RevCommit rev = iterator.next();
//                    System.out.println(rev.getFullMessage());
//                }

            } catch (RepositoryNotFoundException e) {
                // TODO Show dialog not a valid git repo.
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NoFilepatternException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (GitAPIException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        System.out.println("Logged");

    }

    @FXML
    void btn_LogMaxViewClicked(MouseEvent event) {
        recordAppLog("Clicked :: btn_LogMaxViewClicked", Level.ALL);

        Alert alert = new Alert(AlertType.CONFIRMATION, "Delete " + " ?", ButtonType.YES, ButtonType.NO,
                ButtonType.CANCEL);
        alert.showAndWait();

        if (alert.getResult() == ButtonType.YES) {
            // do stuff
        }

    }

    @FXML
    void btn_LogSaveClick(MouseEvent event) {
        recordAppLog("Clicked :: btn_LogSaveClick", Level.ALL);
    }

    @FXML
    void btn_TasksSaveClicked(MouseEvent event) {
        recordAppLog("Clicked :: btn_TasksSaveClicked", Level.ALL);

    }

    private Pair<String, String> customDialog() {
        // Create the custom dialog.
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Login Dialog");
        dialog.setHeaderText("Look, a Custom Login Dialog");

        // Set the icon (must be included in the project).
//        dialog.setGraphic(new ImageView(this.getClass().getResource("login.png").toString()));

        // Set the button types.
        ButtonType loginButtonType = new ButtonType("Login", ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

        // Create the username and password labels and fields.
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField username = new TextField();
        username.setPromptText("Username");
        PasswordField password = new PasswordField();
        password.setPromptText("Password");

        grid.add(new Label("Username:"), 0, 0);
        grid.add(username, 1, 0);
        grid.add(new Label("Password:"), 0, 1);
        grid.add(password, 1, 1);

        // Enable/Disable login button depending on whether a username was entered.
        Node loginButton = dialog.getDialogPane().lookupButton(loginButtonType);
        loginButton.setDisable(true);

        // Do some validation (using the Java 8 lambda syntax).
        username.textProperty().addListener((observable, oldValue, newValue) -> {
            loginButton.setDisable(newValue.trim().isEmpty());
        });

        dialog.getDialogPane().setContent(grid);

        // Request focus on the username field by default.
        Platform.runLater(() -> username.requestFocus());

        // Convert the result to a username-password-pair when the login button is
        // clicked.
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == loginButtonType) {
                return new Pair<>(username.getText(), password.getText());
            }
            return null;
        });

        Optional<Pair<String, String>> result = dialog.showAndWait();

        result.ifPresent(usernamePassword -> {
            System.out.println("Username=" + usernamePassword.getKey() + ", Password=" + usernamePassword.getValue());
        });
        return result.get();
    }
}
