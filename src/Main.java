public class Main {
    public static void main(String[] args) {
        InMemoryTaskManager manager = new InMemoryTaskManager();

        manager.addNewAdvancedTask("поспать", "везде");
        manager.addNewSubTask("в машине", "неудобно сидя", 0);
        manager.addNewSubTask("в постели", "как надо", 0);

        System.out.println(manager.contentFromAdvancedTask(0));
    }
}