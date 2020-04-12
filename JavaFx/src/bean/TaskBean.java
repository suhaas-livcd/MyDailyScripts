package bean;

public class TaskBean {
    private boolean isTaskFinished;
    private String TaskTitle;
    private int TaskPriority;

    public boolean isTaskFinished() {
        return isTaskFinished;
    }

    public void setTaskFinished(boolean isTaskFinished) {
        this.isTaskFinished = isTaskFinished;
    }

    public String getTaskTitle() {
        return TaskTitle;
    }

    public void setTaskTitle(String taskTitle) {
        TaskTitle = taskTitle;
    }

    public int getTaskPriority() {
        return TaskPriority;
    }

    public void setTaskPriority(int taskPriority) {
        TaskPriority = taskPriority;
    }

}
