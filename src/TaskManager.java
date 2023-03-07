public interface TaskManager {
    String printTasksList();

    boolean deleteAllTasks();

    String printOneTask(int SearchingId);

    boolean addNewTask(String taskTitle, String taskDescription);

    boolean addNewAdvancedTask(String taskTitle, String taskDescription);

    boolean addNewSubTask(String taskTitle, String taskDescription, int advancedTaskId);

    boolean changeTaskTitle(int taskIdToChange, String newTitle);

    boolean changeTaskDescription(int taskIdToChange, String newDescription);

    boolean changeTaskStatus(int taskIdToChange, TaskStatus newStatus);

    boolean deleteTask(int taskIdToDelete);

    String contentFromAdvancedTask(int advancedTaskId);

    String history();
}
