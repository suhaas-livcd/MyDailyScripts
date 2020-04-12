package bean;

import java.util.ArrayList;
import java.util.List;

public class LetterBean {
    private String textbox_WordOfDay;
    private String textbox_Diary;

    private List<TaskBean> tasks_List = new ArrayList<TaskBean>();

    public List<TaskBean> getTasks_List() {
        return tasks_List;
    }

    public void setTasks_List(List<TaskBean> tasks_List) {
        this.tasks_List = tasks_List;
    }

    public String getTextbox_WordOfDay() {
        return textbox_WordOfDay;
    }

    public void setTextbox_WordOfDay(String textbox_WordOfDay) {
        this.textbox_WordOfDay = textbox_WordOfDay;
    }

    public String getTextbox_Diary() {
        return textbox_Diary;
    }

    public void setTextbox_Diary(String textbox_Diary) {
        this.textbox_Diary = textbox_Diary;
    }
}
