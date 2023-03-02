public class SubTask extends Task {
    protected String advancedTaskTitle; //к какой суперзадаче она принадлежит
    public SubTask(String title, String description, String advancedTaskTitle) {
        super(title, description);
        this.advancedTaskTitle = advancedTaskTitle;
    }

    public void setStatus(TaskStatus newStatus) { //обновление статуса
        this.status = newStatus;
    }
}
