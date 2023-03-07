public class Task {
    protected String title;
    protected String description;
    protected int uniqueNumber; //уникальный номер задачи, по которому можно будет ее искать
    protected static int totalTasksNumber = 0;
    protected TaskStatus status;

    protected TaskType type;

    public Task(String title, String description) {
        this.title = title;
        this.description = description;
        this.status = TaskStatus.NEW;
        uniqueNumber = totalTasksNumber++;
    }

    public String getTitle() {
        return title;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public String getDescription() {
        return description;
    }

    public int getUniqueNumber() {
        return uniqueNumber;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public void setType(TaskType type) {
        this.type = type;
    }

    public TaskType getType() {
        return type;
    }
}
