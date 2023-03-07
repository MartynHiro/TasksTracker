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

            for (Integer taskId : tasksList.keySet()) { //перебираем все дерево задач
                Task currentTask = tasksList.get(taskId);

                if (currentTask.getType() == TaskType.COMMON) { //выделяем обычные задачи
                    TaskStatus currentTaskStatus = currentTask.getStatus();

                    if (currentTaskStatus == TaskStatus.NEW) { //делим на новые/в процессе/завершенные
                        sbNewTasks.append(addingInSb(currentTask));

                    } else if (currentTaskStatus == TaskStatus.IN_PROGRESS) {
                        sbProgressTasks.append(addingInSb(currentTask));

                    } else {
                        sbDoneTasks.append(addingInSb(currentTask));
                    }
                }

                if (currentTask.getType() == TaskType.ADVANCED) {
                    //теперь проходимся по продвинутым задачам отдельно так как их статус зависит от подзадач
                    AdvancedTask currentAdvTask = (AdvancedTask) currentTask; //уже уверены что она продвинутая

//                    ArrayList<Integer> subTasksIdInCurrentAdvTask = currentAdvTask.getSubTasksNumbers();

                    TaskStatus currentTaskStatus = null; //если останется null значит нет подзадач
                    //тут будем хранить итоговый статус продвинутой задачи исходя из статусов подзадач

                    StringBuilder sbSubTasksDescriptions = new StringBuilder();
                    sbSubTasksDescriptions.append("Эта продвинутая задача включает в себя\n");
                    //компонуем описание всех подзадач нашей продвинутой для вывода

                    for (int numberOfSubTask : currentAdvTask.getSubTasksNumbers()) { //проходимся по нашему списку
                        SubTask subTask = (SubTask) tasksList.get(numberOfSubTask); //уже уверены что они все подзадачи

                        if (currentTaskStatus == null) { //если это первая подзадача, то записываем ее статус
                            currentTaskStatus = subTask.getStatus();
                            //статус новой задачи будет присваиваться только здесь
                            sbSubTasksDescriptions.append(addingSubDescriptionForAdvTask(subTask));

                        } else if (subTask.getStatus() == TaskStatus.IN_PROGRESS) { //если в подзадаче статус в процессе
                            currentTaskStatus = TaskStatus.IN_PROGRESS;
                            //записываем всегда в приоритете такой статус так как если хоть одна подзадача *в процессе*
                            //то и вся продвинутая задача будет иметь такой статус
                            sbSubTasksDescriptions.append(addingSubDescriptionForAdvTask(subTask));

                        } else if (subTask.getStatus() == TaskStatus.DONE && currentTaskStatus != TaskStatus.IN_PROGRESS) {
                            currentTaskStatus = TaskStatus.DONE;
                            //статус завершена присваивается только если нет подзадач в процессе
                            sbSubTasksDescriptions.append(addingSubDescriptionForAdvTask(subTask));

                        } else {
                            //если статус менять не надо просто запоминаем описание нашей подзадачи
                            sbSubTasksDescriptions.append(addingSubDescriptionForAdvTask(subTask));
                        }
                    }

                    //теперь выяснив какой статус у продвинутой задачи, можем ее добавить в sb
                    if (currentTaskStatus == TaskStatus.NEW) { //делим на новые/в процессе/завершенные
                        sbNewTasks.append(addingInSb(currentTask) + sbSubTasksDescriptions);

                    } else if (currentTaskStatus == TaskStatus.IN_PROGRESS) {
                        sbProgressTasks.append(addingInSb(currentTask) + sbSubTasksDescriptions);

                    } else {
                        sbDoneTasks.append(addingInSb(currentTask) + sbSubTasksDescriptions);
                    }
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

    private String addingSubDescriptionForAdvTask(SubTask subTask) {
        return "Подпункт - " + subTask.getTitle()
                + "\n \t с описанием - " + subTask.getDescription() + "\n";

    }

    private String addingInSb(Task currentTask) {
        return "\tНаименование задачи - *" + currentTask.getTitle() +
                "* ,ее id - " + currentTask.getUniqueNumber() +
                "\n \t   Описание: " + currentTask.getDescription() + "\n";
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
                advTask.setSubTasksNumbers(newSubTask.getUniqueNumber());
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

