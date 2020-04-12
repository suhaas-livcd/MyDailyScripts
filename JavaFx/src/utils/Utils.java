package utils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.prefs.Preferences;

public class Utils {

    public static Preferences getPrefs() {
        return Preferences.userNodeForPackage(utils.Utils.class);
    }
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
    public static String getCurrentDate(Date date) {
        return new SimpleDateFormat("dd_MMM_yyyy_HHmmss").format(date);
    }

    public static String getCurrentMonth(Date date) {
        return new SimpleDateFormat("MMM").format(date);
    }

    public static String getCurrentYear(Date date) {
        return new SimpleDateFormat("yyyy").format(date);
    }
}
