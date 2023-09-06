public class Main {
    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();

        Task task1 = new Task("задача", "описание");
        Task task2 = new Task("з2", "о2");
        Task task3 = new Task("Изменено", "Изменено");
        Epic epic1 = new Epic("epic1", "descr1");
        SubTask subTask1 = new SubTask("subtask1", "descr1", 0);
        SubTask subTask2 = new SubTask("subtask2", "descr2", 0);
        SubTask subTask3 = new SubTask("subtask3", "descr3", 0);

        Epic epic2 = new Epic("epic2", "descr2");
        Epic epic3 = new Epic("epic3", "descr3");

        taskManager.addNewTask(task1);
        taskManager.addNewTask(task2);
        taskManager.addNewTask(task3);

        taskManager.addNewEpicTask(epic1);

        subTask1.setEpicId(epic1.getId());
        taskManager.addNewSubTask(subTask1);
        subTask2.setEpicId(epic1.getId());
        taskManager.addNewSubTask(subTask2);

        taskManager.addNewEpicTask(epic2);

        subTask3.setEpicId(epic2.getId());
        taskManager.addNewSubTask(subTask3);

        taskManager.addNewEpicTask(epic3);


        System.out.println("Проверка работы Task");
        System.out.println(task1);
        System.out.println("========");
        System.out.println(taskManager.getAllTask());
        System.out.println("========");
        System.out.println(taskManager.getTask(2));
        System.out.println("========");
        taskManager.deleteTask(3);
        System.out.println(taskManager.getAllTask());
        System.out.println("========");
        taskManager.deleteAllTask();
        System.out.println(taskManager.getAllTask());
        System.out.println("Проверка работы Epic");
        System.out.println(epic1);
        System.out.println("========");
        System.out.println(taskManager.getAllEpic());
        System.out.println("========");
        System.out.println(taskManager.getEpic(4));
        System.out.println("========");
        taskManager.deleteEpic(5);
        System.out.println(taskManager.getAllEpic());
        System.out.println("========");
        System.out.println(taskManager.getAllSubTask());
        System.out.println(taskManager.getEpicSubTaskIds(4));
    }
}
