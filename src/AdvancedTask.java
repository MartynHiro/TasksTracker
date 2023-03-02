import java.util.ArrayList;

public class AdvancedTask extends Task {
    protected ArrayList<SubTask> subTasks;

    public AdvancedTask(String title, String description) {
        super(title, description);
        subTasks = new ArrayList<>();
    }

    public void setSubTasks(SubTask sub) { //добавление подзадачи
        subTasks.add(sub);
    }

    public void setStatus(TaskStatus newStatus) { //обновление статуса
        this.status = newStatus;
    }
}
