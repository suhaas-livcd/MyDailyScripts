package manager;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;

import application.MainController;
import bean.LetterBean;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import utils.Define;
import utils.Utils;

public class FilesManager {

    public static File openDir_FilesToCommit() {
        DirectoryChooser dir_chooser = new DirectoryChooser();
        String lastUsedDir = Utils.getPrefs().get(Define.GNRL_PRF_GITLASTREPO, System.getProperty("user.home"));
        dir_chooser.setInitialDirectory(new File(lastUsedDir));
        File file = dir_chooser.showDialog(new Stage());
        return file;
    }

    public static void createDir_IfNotExist(File directory) {
        if (!directory.exists()) {
            boolean isDirCreated = directory.mkdir();
            System.out.println("CREATED :: " + isDirCreated + " - " + directory.getAbsolutePath());
        } else {
            System.out.println("FOUND :: " + directory.getAbsolutePath());
        }
    }

    public static void createJsonFile(File directory, LetterBean mLetterBean) {
        if (!directory.exists()) {
            System.out.println("CREATED :: ");
            try {
                // Creating content

                Gson gson = new GsonBuilder().disableHtmlEscaping().create();
                System.out.println(gson.toJson(mLetterBean));
                FileWriter fileWriter = new FileWriter(directory.getAbsolutePath());
                gson.toJson(mLetterBean, fileWriter);
                fileWriter.flush();
                fileWriter.close();
                System.out.println(mLetterBean.getTextbox_WordOfDay() + "\n " + mLetterBean.getTextbox_Diary());
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        } else {
            // Not possible, because timestamp would vary,
            System.out.println("FOUND :: " + directory.getAbsolutePath());
        }

    }

    public static void saveTask_TempFile(File directory, LetterBean mLetterBean) {

        System.out.println("FOUND :: " + directory.getAbsolutePath());
        System.out.println("CREATED :: ");
        try {
            Gson gson = new GsonBuilder().disableHtmlEscaping().create();
            System.out.println(gson.toJson(mLetterBean));
            FileWriter fileWriter = new FileWriter(directory.getAbsolutePath(), false);
            gson.toJson(mLetterBean, fileWriter);
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

    }

    public static LetterBean readGSON_Tasks(File directory) {

        try {
            Gson gson = new Gson();
            JsonReader reader = new JsonReader(new FileReader(directory));
            LetterBean data = gson.fromJson(reader, LetterBean.class); // contains the whole reviews list
            return data;
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            createEmptyFileWithTempData();
        }
        return null;
    }

    public static void createEmptyFileWithTempData() {
        try {
            File file = new File(Define.TEMP_TASKS_FILE_PATH);
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
