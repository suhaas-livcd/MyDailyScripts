package application;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {

    /**
     * To get the name of the OS, whether the user is running on a windows or a
     * Linux
     * 
     * @return
     */
    public static String getOSName() {
        return System.getProperty("os.name");
    }

    /**
     * To open the default text file using the common editor
     * 
     * @param file
     * @throws IOException
     */
    public static void openTextFileUsingDefaultEditor(File file) throws IOException {
        java.awt.Desktop.getDesktop().edit(file);
    }

    /**
     * Format :: 08_Mar_2020_19:24:29.json
     * 
     * @return
     */
    public static String getCurrentDate() {
        return new SimpleDateFormat("dd_MMM_yyyy_HH::ss").format(new Date());
    }
    
//    self.my_data['textbox_WordOfDay'] = self.textbox_WordOfDay.text()
//            self.my_data['textbox_Diary'] = self.textbox_Diary.toPlainText()
}
