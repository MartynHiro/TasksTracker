public class SubTask extends Task {
    protected int advancedTaskNumber; //к какой суперзадаче она принадлежит
    protected TaskType type = TaskType.SUB;

    public SubTask(String title, String description, int advancedTaskNumber) {
        super(title, description);
        this.advancedTaskNumber = advancedTaskNumber;
    }

    public void setStatus(TaskStatus newStatus) { //обновление статуса
        this.status = newStatus;
    }

    public TaskType getType() {
        return type;
    }
}
