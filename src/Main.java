public class Main {
    public static void main(String[] args) {
        InMemoryTaskManager manager = new InMemoryTaskManager();

        manager.addNewTask("попить", "водички");
        manager.addNewTask("поесть", "бургер");

        manager.addNewAdvancedTask("Большая стирка", "нужно постирать все белье");
        manager.addNewSubTask("трусы", "5 штук", 2);
        manager.addNewSubTask("носки", "3 пары", 2);
        manager.addNewSubTask("футболка", "10 штук", 2);

        System.out.println(manager.printTasksList());
    }
}