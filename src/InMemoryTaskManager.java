import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;

public class InMemoryTaskManager implements TaskManager {
    protected TreeMap<Integer, Task> tasksList; //список всех задач
    protected List<Task> searchHistory; //список просмотра

    public InMemoryTaskManager() {
        this.tasksList = new TreeMap<>();
        this.searchHistory = new LinkedList<>();
    }

    @Override
    public String printTasksList() { //получение списка всех задач
        StringBuilder sbNewTasks = new StringBuilder();
        StringBuilder sbProgressTasks = new StringBuilder();
        StringBuilder sbDoneTasks = new StringBuilder();

        if (!tasksList.isEmpty()) {
            for (Integer key : tasksList.keySet()) { //перебираем все дерево задач
                Task value = tasksList.get(key);

                if (value.getStatus() == TaskStatus.NEW) { //можем через == так как это ENUM
                    sbNewTasks.append("\tНаименование задачи - " + value.getTitle() +
                            " ее id - " + value.getUniqueNumber() +
                            "\n \t   Описание: " + value.getDescription() + "\n");

                } else if (value.getStatus() == TaskStatus.IN_PROGRESS) {
                    sbProgressTasks.append("\tНаименование задачи - " + value.getTitle() +
                            " ее id - " + value.getUniqueNumber() +
                            "\n \t   Описание: " + value.getDescription() + "\n");

                } else {
                    sbDoneTasks.append("\tНаименование задачи - " + value.getTitle() +
                            " ее id - " + value.getUniqueNumber() +
                            "\n \t   Описание: " + value.getDescription() + "\n");
                }
            }

            String totalExistingTasksList = "";
            if (!sbNewTasks.isEmpty()) { //избегаем строк со значением null для каждого вида задач
                totalExistingTasksList += "Новые задачи: \n" + sbNewTasks;
            }
            if (!sbProgressTasks.isEmpty()) {
                totalExistingTasksList += "Задачи в процессе: \n" + sbProgressTasks;
            }
            if (!sbDoneTasks.isEmpty()) {
                totalExistingTasksList += "Завершенные задачи: \n" + sbDoneTasks;
            }

            return totalExistingTasksList;

        } else {
            return "Вы не сохраняли ни одной задачи";
        }
    }

    @Override
    public boolean deleteAllTasks() {
        tasksList.clear();
        return true;
    }

    @Override
    public String printOneTask(int SearchingId) { //метод вывода одной задачи по ее id

        Task currentTask = tasksList.get(SearchingId);

        if (searchHistory.size() <= 10) { //если список не более 10 элементов
            searchHistory.add(currentTask);
        } else {
            searchHistory.remove(0); //если уже заполнен, то стираем первый элемент и добавляем новый
            searchHistory.add(currentTask);
        }

        return "Наименование задачи - " + currentTask.getTitle() +
                "\n \t   Описание: " + currentTask.getDescription();
    }

    @Override
    public boolean addNewTask(String taskTitle, String taskDescription) { //добавление новой задачи

        Task newTask = new Task(taskTitle, taskDescription);
        tasksList.put(newTask.getUniqueNumber(), newTask);
        return true;
    }

    public boolean addNewAdvancedTask(String taskTitle, String taskDescription) { //создание продвинутой задачи

        AdvancedTask newAdvTask = new AdvancedTask(taskTitle, taskDescription);
        tasksList.put(newAdvTask.getUniqueNumber(), newAdvTask);
        return true;
    }

    public boolean addNewSubTask(String taskTitle, String taskDescription, int advancedTaskId) {
        //создание подзадачи для определенной продвинутой задачи
        for (int currentTaskId : tasksList.keySet()) { //проверяем есть ли такая задача и продвинутая ли она
            Task currentTask = tasksList.get(currentTaskId);

            if (currentTaskId == advancedTaskId && currentTask.getType() == TaskType.ADVANCED) {
                SubTask newSubTask = new SubTask(taskTitle, taskDescription, advancedTaskId);
                tasksList.put(newSubTask.getUniqueNumber(), newSubTask);

                AdvancedTask advTask = (AdvancedTask) tasksList.get(advancedTaskId); //Добавляем в адванс нашу саб задачу
                advTask.setSubTasksNumbers(advancedTaskId);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean changeTaskTitle(int taskIdToChange, String newTitle) { //изменение уже существующей задачи

        for (int currentTaskId : tasksList.keySet()) { //проверяем есть ли такая задача

            if (currentTaskId == taskIdToChange) {
                tasksList.get(taskIdToChange).setTitle(newTitle);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean changeTaskDescription(int taskIdToChange, String newDescription) {
        for (int currentTaskId : tasksList.keySet()) { //проверяем есть ли такая задача

            if (currentTaskId == taskIdToChange) {
                tasksList.get(taskIdToChange).setDescription(newDescription);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean changeTaskStatus(int taskIdToChange, TaskStatus newStatus) {
        for (int currentTaskId : tasksList.keySet()) { //проверяем есть ли такая задача

            if (currentTaskId == taskIdToChange) {
                tasksList.get(taskIdToChange).setStatus(newStatus);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean deleteTask(int taskIdToDelete) { //удаление задачи
        for (int currentTaskId : tasksList.keySet()) { //проверяем есть ли такая задача

            if (currentTaskId == taskIdToDelete) {
                tasksList.remove(taskIdToDelete);
                return true;
            }
        }
        return false;
    }

    @Override
    public String contentFromAdvancedTask(int advancedTaskId) { //список подзадач из одной продвинутой задачи по нашему выбору

        StringBuilder sbShortTasksInfo = new StringBuilder();

        for (int currentTaskId : tasksList.keySet()) { //проверяем есть ли такая задача и продвинутая ли она
            Task currentTask = tasksList.get(currentTaskId);

            if (currentTaskId == advancedTaskId && currentTask.getType() == TaskType.ADVANCED) {
                AdvancedTask advCurrentTask = (AdvancedTask) currentTask; //тут мы уже уверены, что она продвинутая

                sbShortTasksInfo.append("В этом списке содержатся подзадачи: \n");
                ArrayList<Integer> numbersOfSubTasks = advCurrentTask.getSubTasksNumbers(); //достаем номера нужных нам саб задач

                for (Integer numbersOfSubTask : numbersOfSubTasks) { //проходимся по нашему списку
                    Task task = tasksList.get(numbersOfSubTask);
                    sbShortTasksInfo.append(task.getTitle() + "\n");
                }

                return sbShortTasksInfo.toString();
            }
        }
        return "нет данных по этой задаче";
    }

    @Override
    public String history() { //вывод списка истории просмотров
        StringBuilder sbHistory = new StringBuilder();

        if (searchHistory.isEmpty()) {
            return "Еще не было ни одного поиска";
        }

        for (Task task : searchHistory) {
            sbHistory.append("Задача - " + task.getTitle() + ", id задачи - " + task.getUniqueNumber() +
                    ", типа - " + task.getType() + "\n");
        }
        return sbHistory.toString();
    }
}

