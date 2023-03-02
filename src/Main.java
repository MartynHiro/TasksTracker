import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Manager manager = new Manager();
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("""
                    Добрый день, выберите номер команды:
                    1.Получение списка всех задач
                    2.Удаление всех задач
                    3.Вывод задачи по ее идентификатору
                    4.Создание задачи
                    5.Обновление данных существующей задачи
                    6.Удаление задачи по идентификатору
                    7.Получение списка подзадач определенной продвинутой задачи
                    8.Завершение работы
                    """);
            try {
                int selectedNumber = scanner.nextInt();

                if (selectedNumber == 8) {
                    break;
                }

                switch (selectedNumber) {
                    case 1 -> manager.printTasksList();

                    case 2 -> manager.deleteAllTasks();

                    case 3 -> manager.printOneTask();

                    case 4 -> manager.addNewTask();

                    case 5 -> manager.changeTask();

                    case 6 -> manager.deleteTask();

                    case 7 -> manager.printContentFromAdvancedTask();

                    default -> System.out.println("Такого действия не существует");
                }

            } catch (Exception e) {
                System.out.println("Неверный ввод");
            }
        }
    }
}