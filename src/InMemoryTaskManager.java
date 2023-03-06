import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    protected TreeMap<Integer, Task> tasksList; //список всех задач
    protected List<Task> searchHistory; //список просмотра
    Scanner scanner = new Scanner(System.in);

    public InMemoryTaskManager() {
        this.tasksList = new TreeMap<>();
        this.searchHistory = new LinkedList<>();
    }

    @Override
    public void printTasksList() { //получение списка всех задач
        StringBuilder sbNewTasks = new StringBuilder();
        StringBuilder sbProgressTasks = new StringBuilder();
        StringBuilder sbDoneTasks = new StringBuilder();

        if (!tasksList.isEmpty()) {
            for (Integer key : tasksList.keySet()) { //перебираем все дерево задач
                Task value = tasksList.get(key);

                if (value.getStatus() == TaskStatus.NEW) { //можем через == так как это ENUM
                    sbNewTasks.append("\tНаименование задачи - " + value.getTitle() +
                            "\n \t   Описание: " + value.getDescription() + "\n");

                } else if (value.getStatus() == TaskStatus.IN_PROGRESS) {
                    sbProgressTasks.append("\tНаименование задачи - " + value.getTitle() +
                            "\n \t   Описание: " + value.getDescription() + "\n");

                } else {
                    sbDoneTasks.append("\tНаименование задачи - " + value.getTitle() +
                            "\n \t   Описание: " + value.getDescription() + "\n");
                }
            }

            System.out.println("Новые задачи: \n" + sbNewTasks +
                    "Задачи в процессе: \n" + sbProgressTasks +
                    "Завершенные задачи: \n" + sbDoneTasks);

        } else {
            System.out.println("Вы не сохраняли ни одной задачи");
        }
    }

    @Override
    public void deleteAllTasks() {
        tasksList.clear();
    }

    @Override
    public void printOneTask() { //метод вывода одной задачи по ее id
        StringBuilder sbShortTasksInfo = new StringBuilder();

        for (Integer key : tasksList.keySet()) { //сборка списка задач для вывода
            Task value = tasksList.get(key);
            sbShortTasksInfo.append("Статус задачи - " + value.getStatus() +
                    " , название задачи - " + value.getTitle() + " , уникальный номер задачи - " +
                    value.uniqueNumber + "\n");
        }
        if (sbShortTasksInfo.isEmpty()) {
            System.out.println("Вы еще ничего не сохраняли");
        } else {
            System.out.println(sbShortTasksInfo);   //выводим список и предлагаем какую задачу вывести

            System.out.println("Введите номер задачи для отображения");
            int taskUniqueNumber = scanner.nextInt();

            Task currentTask = tasksList.get(taskUniqueNumber);
            System.out.println("Наименование задачи - " + currentTask.getTitle() +
                    "\n \t   Описание: " + currentTask.getDescription());

            if (searchHistory.size() <= 10) { //если список не более 10 элементов
                searchHistory.add(tasksList.get(taskUniqueNumber));
            } else {
                searchHistory.remove(0); //если уже заполнен, то стираем первый элемент и добавляем новый
                searchHistory.add(tasksList.get(taskUniqueNumber));
            }
        }
    }

    @Override
    public void addNewTask() { //добавление новой задачи
        System.out.println("""
                Введите номер типа задачи:
                1.Task
                2.AdvancedTask
                3.SubTask""");
        try {
            int taskType = scanner.nextInt();

            System.out.println("Введите наименование задачи: ");
            String taskTitle = scanner.next();

            System.out.println("Введите описание: ");
            String taskDescription = scanner.nextLine();

            switch (taskType) {
                case 1 -> { //создание обычной задачи
                    Task newTask = new Task(taskTitle, taskDescription);
                    tasksList.put(newTask.getUniqueNumber(), newTask);
                }
                case 2 -> { //создание продвинутой задачи
                    AdvancedTask newAdvTask = new AdvancedTask(taskTitle, taskDescription);
                    tasksList.put(newAdvTask.getUniqueNumber(), newAdvTask);
                }
                case 3 -> { //добавление подзадачи к уже существующей продвинутой
                    StringBuilder sbShortTasksInfo = new StringBuilder();
                    for (Integer key : tasksList.keySet()) {
                        Task value = tasksList.get(key);

                        if (value.getIdentifier() == TaskIdentifier.ADVANCED) { //выводим список только адванс задач
                            sbShortTasksInfo.append("Статус задачи - " + value.getStatus() +
                                    " , название задачи - " + value.getTitle() + " , уникальный номер задачи - " +
                                    value.uniqueNumber + "\n");
                        }
                    }
                    if (sbShortTasksInfo.isEmpty()) {
                        System.out.println("Продвинутых задач еще не было создано");

                    } else {
                        System.out.println(sbShortTasksInfo);

                        System.out.println("Введите номер продвинутой задачи, для которой предназначена эта подзадача");
                        int advancedTaskNumber = scanner.nextInt();

                        SubTask newSubTask = new SubTask(taskTitle, taskDescription, advancedTaskNumber);
                        tasksList.put(newSubTask.getUniqueNumber(), newSubTask);

                        AdvancedTask advTask = (AdvancedTask) tasksList.get(advancedTaskNumber); //Добавляем в адванс нашу саб задачу
                        advTask.setSubTasksNumbers(advancedTaskNumber);
                        tasksList.put(advTask.getUniqueNumber(), advTask); //перезаписываем ее обратно
                    }
                }
                default -> System.out.println("Такого типа задач не существует");
            }
        } catch (Exception e) {
            System.out.println("Введите пожалуйста цифру");
        }
    }

    @Override
    public void changeTask() { //изменение уже существующей задачи
        StringBuilder sbShortTasksInfo = new StringBuilder();

        if (tasksList.isEmpty()) {
            System.out.println("Ни одной задачи еще не было добавлено");

        } else {
            for (Integer key : tasksList.keySet()) { //вывод списка доступных задач
                Task value = tasksList.get(key);
                if (value.getStatus() != TaskStatus.DONE) { //со статусов *завершены* не выводим
                    sbShortTasksInfo.append("Статус задачи - " + value.getStatus() +
                            " , название задачи - " + value.getTitle() + " , уникальный номер задачи - " +
                            value.uniqueNumber + "\n");
                }
            }
            System.out.println(sbShortTasksInfo + "\n Введите уникальный номер задачи для ее изменения: ");
            try {
                int taskNumberForChange = scanner.nextInt();

                Task taskForChange = tasksList.get(taskNumberForChange);
                System.out.println("Вы выбрали задачу - " + taskForChange.getTitle() + " она типа - " + taskForChange.getStatus());

                while (true) {  //меню изменения выбранной нами задачи
                    System.out.println("""
                            Введите номер того операции
                            1.Смена названия
                            2.Смена описания
                            3.Смена статуса
                            4.Выйти из меню и сохранить изменения""");

                    try {
                        int selectionNumber = scanner.nextInt();

                        switch (selectionNumber) {
                            case 1 -> {
                                System.out.println("Введите новое название: ");
                                String newName = scanner.next();
                                taskForChange.setTitle(newName);
                            }
                            case 2 -> {
                                System.out.println("Введите новое описание: ");
                                String newDescription = scanner.nextLine();
                                taskForChange.setDescription(newDescription);
                            }
                            case 3 -> {
                                System.out.println("Введите новый статус InProgress / Done :");
                                String newStatus = scanner.next();
                                if (newStatus.equals("InProgress")) {
                                    taskForChange.setStatus(TaskStatus.IN_PROGRESS);

                                } else if (newStatus.equals("Done")) {
                                    taskForChange.setStatus(TaskStatus.DONE);

                                } else {
                                    System.out.println("Неверный статус");
                                }
                            }
                            case 4 -> {
                                tasksList.put(taskNumberForChange, taskForChange);
                                System.out.println("Изменения сохранены");
                            }
                            default -> System.out.println("Такой операции не существует");
                        }
                    } catch (Exception e) {
                        System.out.println("Неверный ввод");
                    }
                }
            } catch (Exception e) {
                System.out.println("Неверный ввод");
            }
        }
    }

    @Override
    public void deleteTask() { //удаление задачи
        StringBuilder sbShortTasksInfo = new StringBuilder();

        if (tasksList.isEmpty()) {
            System.out.println("Нет задач для удаления");

        } else {
            for (Integer key : tasksList.keySet()) {
                Task value = tasksList.get(key);

                sbShortTasksInfo.append("Статус задачи - " + value.getStatus() +
                        " , название задачи - " + value.getTitle() + " , уникальный номер задачи - " +
                        value.uniqueNumber + "\n");
            }

            System.out.println(sbShortTasksInfo + "\n Введите уникальный номер задачи для ее удаления из списка: ");
            int taskNumberForChange = scanner.nextInt();

            tasksList.remove(taskNumberForChange);
        }
    }

    @Override
    public void printContentFromAdvancedTask() { //вывод списка подзадач из одной продвинутой задачи по нашему выбору
        StringBuilder sbShortTasksInfo = new StringBuilder();

        if (tasksList.isEmpty()) {
            System.out.println("Вы еще не добавляли задач");

        } else {
            for (Integer key : tasksList.keySet()) {
                Task value = tasksList.get(key);

                if (value.getIdentifier() == TaskIdentifier.ADVANCED) {    //выводим список только адванс задач
                    sbShortTasksInfo.append("Статус задачи - " + value.getStatus() +
                            " , название задачи - " + value.getTitle() + " , уникальный номер задачи - " +
                            value.uniqueNumber + "\n");
                }
            }

            if (sbShortTasksInfo.isEmpty()) {
                System.out.println("Вы еще не добавляли продвинутых задач");

            } else {
                System.out.println(sbShortTasksInfo);

                System.out.println("Выберите номер задачи, содержание которой вы хотите просмотреть: ");

                try {
                    int selectedNumber = scanner.nextInt();

                    AdvancedTask selectedTask = (AdvancedTask) tasksList.get(selectedNumber); //
                    System.out.println("В этом списке содержатся подзадачи: \n");
                    ArrayList<Integer> numbersOfSubTasks = selectedTask.getSubTasksNumbers(); //достаем номера нужных нам саб задач

                    for (Integer numbersOfSubTask : numbersOfSubTasks) { //проходимся по нашему списку
                        Task task = tasksList.get(numbersOfSubTask);
                        System.out.println(task.getTitle());
                    }
                } catch (Exception e) {
                    System.out.println("Неверный ввод");
                }
            }
        }
    }

    @Override
    public void history() { //вывод списка истории просмотров
        StringBuilder sbHistory = new StringBuilder();

        if (searchHistory.isEmpty()) {
            System.out.println("Еще не было ни одного поиска");
        } else {
            for (Task task : searchHistory) {
                sbHistory.append("Задача - " + task.getTitle() + ", id задачи - " + task.getUniqueNumber() +
                        ", типа - " + task.getIdentifier() + "\n");
            }
        }
    }
}

