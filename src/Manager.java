import java.util.Scanner;
import java.util.TreeMap;

public class Manager {
    protected TreeMap<Integer, Task> tasksList;
    Scanner scanner = new Scanner(System.in);

    public Manager() {
        this.tasksList = new TreeMap<>();
    }

    public void printTasksList() { //получение списка всех задач
        StringBuilder sbNewTasks = new StringBuilder();
        StringBuilder sbProgressTasks = new StringBuilder();
        StringBuilder sbDoneTasks = new StringBuilder();

        if (!tasksList.isEmpty()) {
            for (Integer key : tasksList.keySet()) { //перебираем все дерево задач
                Task value = tasksList.get(key);

                if (value.getStatus() == TaskStatus.New) { //можем через == так как это ENUM
                    sbNewTasks.append("\tНаименование задачи - " + value.getTitle() +
                            "\n \t   Описание: " + value.getDescription() + "\n");

                } else if (value.getStatus() == TaskStatus.InProgress) {
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

    public void deleteAllTasks() {
        tasksList.clear();
    }

    public void printOneTask() {
        System.out.println("Введите номер задачи для отображения");
        int taskUniqueNumber = scanner.nextInt();

        Task currentTask = tasksList.get(taskUniqueNumber);
        System.out.println("Наименование задачи - " + currentTask.getTitle() +
                "\n \t   Описание: " + currentTask.getDescription());
    }

    public void addNewTask() {
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
                case 1:
                    Task newTask = new Task(taskTitle, taskDescription);
                    tasksList.put(newTask.getUniqueNumber(), newTask);

                case 2:
                    AdvancedTask newAdvTask = new AdvancedTask(taskTitle, taskDescription);
                    tasksList.put(newAdvTask.getUniqueNumber(), newAdvTask);

                case 3:
                    System.out.println("Введите наименование продвинутой задачи, для которой предназначена это подзадача");
                    String advancedTaskTitle = scanner.next();

                    SubTask newSubTask = new SubTask(taskTitle, taskDescription, advancedTaskTitle);
                    tasksList.put(newSubTask.getUniqueNumber(), newSubTask);

                default:
                    System.out.println("Такого типа задач не существует");
            }
        } catch (Exception e) {
            System.out.println("Введите пожалуйста цифру");
        }
    }

    public void changeTask() {
        StringBuilder sbShortTasksInfo = new StringBuilder();

        for (Integer key : tasksList.keySet()) {
            Task value = tasksList.get(key);
            if (value.getStatus() != TaskStatus.Done) {
                sbShortTasksInfo.append("Статус задачи - " + value.getStatus() +
                        " , название задачи - " + value.getTitle() + " , уникальный номер задачи - " +
                        value.uniqueNumber + "\n");
            }
        }
        System.out.println(sbShortTasksInfo + "\n Введите уникальный номер задачи для ее изменения: ");
        int taskNumberForChange = scanner.nextInt();

        Task taskForChange = tasksList.get(taskNumberForChange);
        System.out.println("Вы выбрали задачу - " + taskForChange.getTitle() + " она типа - " + taskForChange.getStatus());

        while (true) {
            System.out.println("""
                    Введите номер того операции
                    1.Смена названия
                    2.Смена описания
                    3.Смена статуса
                    4.Выйти из меню и сохранить изменения""");

            try {
                int selectionNumber = scanner.nextInt();

                switch (selectionNumber) {
                    case 1:
                        System.out.println("Введите новое название: ");
                        String newName = scanner.next();
                        taskForChange.setTitle(newName);

                    case 2:
                        System.out.println("Введите новое описание: ");
                        String newDescription = scanner.nextLine();
                        taskForChange.setDescription(newDescription);

                    case 3:
                        System.out.println("Введите новый статус InProgress / Done :");
                        String newStatus = scanner.next();

                        if (newStatus.equals("InProgress")) {
                            taskForChange.setStatus(TaskStatus.InProgress);

                        } else if (newStatus.equals("Done")) {
                            taskForChange.setStatus(TaskStatus.Done);

                        } else {
                            System.out.println("Неверный статус");
                        }

                    case 4:
                        tasksList.put(taskNumberForChange, taskForChange);
                        System.out.println("Изменения сохранены");
                }
            } catch (Exception e) {
                System.out.println("Неверный ввод");
            }
        }
    }

    public void deleteTask() {
        StringBuilder sbShortTasksInfo = new StringBuilder();

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
