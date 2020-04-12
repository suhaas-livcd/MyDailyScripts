package application;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PushCommand;
import org.eclipse.jgit.api.Status;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.NoFilepatternException;
import org.eclipse.jgit.errors.RepositoryNotFoundException;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.transport.PushResult;
import org.eclipse.jgit.transport.RefSpec;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import bean.LetterBean;
import bean.TaskBean;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.FontSmoothingType;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.scene.web.HTMLEditor;
import javafx.util.Pair;
import manager.FilesManager;
import manager.GitManager;
import utils.Define;
import utils.Utils;

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
    private Button btn_LogSave, btn_LogMaxView, btn_TasksSave, btn_LogCommit, btn_TasksLoad;

    @FXML
    private CheckBox cb_task00, cb_task01, cb_task02, cb_task03, cb_task04, cb_task05, cb_task06, cb_task07, cb_task08,
            cb_task09;

    @FXML
    private ChoiceBox<Integer> cb_pr_task00, cb_pr_task01, cb_pr_task02, cb_pr_task03, cb_pr_task04, cb_pr_task05,
            cb_pr_task06, cb_pr_task07, cb_pr_task08, cb_pr_task09;

    @FXML
    private TextField txtFld_task0, txtFld_task1, txtFld_task2, txtFld_task3, txtFld_task4, txtFld_task5, txtFld_task6,
            txtFld_task7, txtFld_task8, txtFld_task9;

    @FXML
    private ScrollPane scrlPane_AppLog;

    @FXML
    private TextFlow txtFlow_AppLog;

    private LetterBean mLetterBean = null;
    List<CheckBox> tasks_checkBoxGroup = new ArrayList<CheckBox>();
    List<ChoiceBox<Integer>> tasks_choiceBoxPriority = new ArrayList<ChoiceBox<Integer>>();
    List<TextField> tasks_txtFields = new ArrayList<TextField>();

    private LetterBean getLetterBean() {
        if (mLetterBean == null) {
            return new LetterBean();
        }
        return mLetterBean;
    }

    EventHandler<MouseEvent> txtFieldMouseClkEvnt = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            if (event.getSource() instanceof TextField) {
                TextField textField = (TextField) event.getSource();
                if (textField.isFocused()) {
                    textField.setStyle(Define.CSS_ENABLE_TXT_OPACITY);
                } else {
                    textField.setStyle(Define.CSS_DISABLE_TXT_OPACITY);
                }
                textField.focusedProperty().addListener((obs, oldVal, newVal) -> {
                    if (textField.isFocused()) {
                        textField.setStyle(Define.CSS_ENABLE_TXT_OPACITY);
                    } else {
                        textField.setStyle(Define.CSS_DISABLE_TXT_OPACITY);

                    }
                });
            }
        }
    };

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        recordAppLog("Controller :: initialize", Level.INFO);
        mLetterBean = getLetterBean();
        disableTextFields();
        initListener();
        createtasksGroups();
        addChoiceBoxPriorityNumber();
        // Create empty task file.
        btn_TasksLoad.fire();
    }

    private void disableTextFields() {
        recordAppLog("Listner :: disableTextFields", Level.ALL);
        txtFld_task0.setStyle(Define.CSS_DISABLE_TXT_OPACITY);
        txtFld_task1.setStyle(Define.CSS_DISABLE_TXT_OPACITY);
        txtFld_task2.setStyle(Define.CSS_DISABLE_TXT_OPACITY);
        txtFld_task3.setStyle(Define.CSS_DISABLE_TXT_OPACITY);
        txtFld_task4.setStyle(Define.CSS_DISABLE_TXT_OPACITY);
        txtFld_task5.setStyle(Define.CSS_DISABLE_TXT_OPACITY);
        txtFld_task6.setStyle(Define.CSS_DISABLE_TXT_OPACITY);
        txtFld_task7.setStyle(Define.CSS_DISABLE_TXT_OPACITY);
        txtFld_task8.setStyle(Define.CSS_DISABLE_TXT_OPACITY);
        txtFld_task9.setStyle(Define.CSS_DISABLE_TXT_OPACITY);

    }

    private void createtasksGroups() {
        tasks_checkBoxGroup.addAll(Arrays.asList(cb_task00, cb_task01, cb_task02, cb_task03, cb_task04, cb_task05,
                cb_task06, cb_task07, cb_task08, cb_task09));

        tasks_choiceBoxPriority.addAll(Arrays.asList(cb_pr_task00, cb_pr_task01, cb_pr_task02, cb_pr_task03,
                cb_pr_task04, cb_pr_task05, cb_pr_task06, cb_pr_task07, cb_pr_task08, cb_pr_task09));

        tasks_txtFields.addAll(Arrays.asList(txtFld_task0, txtFld_task1, txtFld_task2, txtFld_task3, txtFld_task4,
                txtFld_task5, txtFld_task6, txtFld_task7, txtFld_task8, txtFld_task9));

        // Create Total tasks
        for (int i = 0; i < Define.TOTAL_TASKS; i++) {
            TaskBean taskBean = new TaskBean();
            mLetterBean.getTasks_List().add(taskBean);
        }
    }

    public void addChoiceBoxPriorityNumber() {
        int defaultPriority = Define.defaultPriority;
        // string array
        Integer[] priorityNumbers = Define.priorityNumbers;
        cb_pr_task00.getItems().addAll(priorityNumbers);
        cb_pr_task01.getItems().addAll(priorityNumbers);
        cb_pr_task02.getItems().addAll(priorityNumbers);
        cb_pr_task03.getItems().addAll(priorityNumbers);
        cb_pr_task04.getItems().addAll(priorityNumbers);
        cb_pr_task05.getItems().addAll(priorityNumbers);
        cb_pr_task06.getItems().addAll(priorityNumbers);
        cb_pr_task07.getItems().addAll(priorityNumbers);
        cb_pr_task08.getItems().addAll(priorityNumbers);
        cb_pr_task09.getItems().addAll(priorityNumbers);
        cb_pr_task00.setValue(defaultPriority);
        cb_pr_task01.setValue(defaultPriority);
        cb_pr_task02.setValue(defaultPriority);
        cb_pr_task03.setValue(defaultPriority);
        cb_pr_task04.setValue(defaultPriority);
        cb_pr_task05.setValue(defaultPriority);
        cb_pr_task06.setValue(defaultPriority);
        cb_pr_task07.setValue(defaultPriority);
        cb_pr_task08.setValue(defaultPriority);
        cb_pr_task09.setValue(defaultPriority);
    }

    public void initListener() {
        recordAppLog("Listner :: initListener", Level.ALL);

        // Text-fields listener
        txtFld_task0.setOnMouseClicked(txtFieldMouseClkEvnt);
        txtFld_task1.setOnMouseClicked(txtFieldMouseClkEvnt);
        txtFld_task2.setOnMouseClicked(txtFieldMouseClkEvnt);
        txtFld_task3.setOnMouseClicked(txtFieldMouseClkEvnt);
        txtFld_task4.setOnMouseClicked(txtFieldMouseClkEvnt);
        txtFld_task5.setOnMouseClicked(txtFieldMouseClkEvnt);
        txtFld_task6.setOnMouseClicked(txtFieldMouseClkEvnt);
        txtFld_task7.setOnMouseClicked(txtFieldMouseClkEvnt);
        txtFld_task8.setOnMouseClicked(txtFieldMouseClkEvnt);
        txtFld_task9.setOnMouseClicked(txtFieldMouseClkEvnt);
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
    void btn_LogCommitClicked(ActionEvent event) {
        recordAppLog("Clicked :: btn_LogCommitClicked", Level.ALL);

        Alert alert = new Alert(AlertType.CONFIRMATION, "Are you sure you want to commit and push " + " ?",
                ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
        alert.showAndWait();
        if (alert.getResult() == ButtonType.YES) {
            // check if the repo is correct

            File file = FilesManager.openDir_FilesToCommit();

            // Checking ROOT :: "DIARY"
            String PATH = file.getAbsolutePath();
            String directoryName = PATH.concat("\\diary\\");

            File directory = new File(directoryName);
            FilesManager.createDir_IfNotExist(directory);

            Date date = new Date();
            System.out.println("Date is :: " + Utils.getCurrentDate(date));
            String YEAR = Utils.getCurrentYear(date);
            String MONTH = Utils.getCurrentMonth(date);
            String FILE_NAME = Utils.getCurrentDate(date) + ".json";
            String COMMIT_MSG = FILE_NAME;

            // Checking DIR :: "YEAR"
            directoryName = directoryName.concat("\\" + YEAR + "\\");

            directory = new File(directoryName);
            FilesManager.createDir_IfNotExist(directory);

            // Checking FILE :: "MONTH"
            directoryName = directoryName.concat("\\" + MONTH + "\\");
            directory = new File(directoryName);
            FilesManager.createDir_IfNotExist(directory);

            // Checking DIR :: "FILE"
            directoryName = directoryName.concat("\\" + FILE_NAME);
            directory = new File(directoryName);

            // Setting the Subject and the content
            COMMIT_MSG = txtarea_LogSub.getText();
            mLetterBean.setTextbox_WordOfDay(COMMIT_MSG);
            mLetterBean.setTextbox_Diary(txtarea_LogContent.getHtmlText());
            FilesManager.createJsonFile(directory, mLetterBean);

            // Open an existing repository
            Utils.getPrefs().put(Define.GNRL_PRF_GITLASTREPO, file.getAbsolutePath());
            try {
                Git git = Git.open(file);
                git.add().addFilepattern(".").call();
                Status gitStatus = git.status().call();
                System.out.println(" Git status :: " + gitStatus.getAdded());
                RevCommit revCommit = git.commit().setMessage(COMMIT_MSG).call();
                System.out.println("Commit Status  :: " + revCommit.getFullMessage());
                Pair<String, String> usernamePassword = GitManager.customDialog();
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
    void btn_LogMaxViewClicked(ActionEvent event) {
        recordAppLog("Clicked :: btn_LogMaxViewClicked", Level.ALL);

        Alert alert = new Alert(AlertType.CONFIRMATION, "Delete " + " ?", ButtonType.YES, ButtonType.NO,
                ButtonType.CANCEL);
        alert.showAndWait();

        if (alert.getResult() == ButtonType.YES) {
            // do stuff
        }

    }

    @FXML
    void btn_LogSaveClick(ActionEvent event) {
        recordAppLog("Clicked :: btn_LogSaveClick", Level.ALL);
    }

    @FXML
    void btn_TasksSaveClicked(ActionEvent event) {
        recordAppLog("Clicked :: btn_TasksSaveClicked", Level.ALL);
        for (int i = 0; i < mLetterBean.getTasks_List().size(); i++) {
            boolean isChecked = tasks_checkBoxGroup.get(i).isSelected();
            int priorityValue = tasks_choiceBoxPriority.get(i).getValue();
            String taskTitle = tasks_txtFields.get(i).getText();
            mLetterBean.getTasks_List().get(i).setTaskFinished(isChecked);
            mLetterBean.getTasks_List().get(i).setTaskTitle(taskTitle);
            mLetterBean.getTasks_List().get(i).setTaskPriority(priorityValue);
        }

        String filePath = Define.TEMP_TASKS_FILE_PATH;
        FilesManager.saveTask_TempFile(new File(filePath), mLetterBean);
    }

    @FXML
    void btn_TasksLoadClicked(ActionEvent event) {
        recordAppLog("Clicked :: btn_TasksLoadClicked", Level.ALL);
        String filePath = Define.TEMP_TASKS_FILE_PATH;
        LetterBean mTempFileData = FilesManager.readGSON_Tasks(new File(filePath));
        if (mTempFileData != null) {
            for (int i = 0; i < mTempFileData.getTasks_List().size(); i++) {
                TaskBean taskItem = mTempFileData.getTasks_List().get(i);
                boolean isChecked = taskItem.isTaskFinished();
                int priorityValue = taskItem.getTaskPriority();
                String taskTitle = taskItem.getTaskTitle();
                tasks_checkBoxGroup.get(i).setSelected(isChecked);
                tasks_choiceBoxPriority.get(i).setValue(priorityValue);
                tasks_txtFields.get(i).setText(taskTitle);
            }
        }
    }
}
