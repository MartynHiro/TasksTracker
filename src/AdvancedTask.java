import java.util.ArrayList;

public class AdvancedTask extends Task {
    protected ArrayList<Integer> subTasksNumbers;

    protected TaskType type = TaskType.ADVANCED;

    public AdvancedTask(String title, String description) {
        super(title, description);
        subTasksNumbers = new ArrayList<>();
    }

    public void setSubTasksNumbers(int subTasksNumbers) {
        this.subTasksNumbers.add(subTasksNumbers);
    }

    public void setStatus(TaskStatus newStatus) { //обновление статуса
        this.status = newStatus;
    }

    public ArrayList<Integer> getSubTasksNumbers() {
        return subTasksNumbers;
    }

    public TaskType getType() {
        return type;
    }
}
