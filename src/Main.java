public class Main {
    public static void main(String[] args) {
        InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();
        InMemoryHistoryManager inMemoryHistoryManager = new InMemoryHistoryManager();

        Task task1 = new Task("задача", "описание");
        Task task2 = new Task("з2", "о2");
        Task task3 = new Task("Изменено", "Изменено");
        Epic epic1 = new Epic("epic1", "descr1");
        SubTask subTask1 = new SubTask("subtask1", "descr1", 0);
        SubTask subTask2 = new SubTask("subtask2", "descr2", 0);
        SubTask subTask3 = new SubTask("subtask3", "descr3", 0);

        Epic epic2 = new Epic("epic2", "descr2");
        Epic epic3 = new Epic("epic3", "descr3");

        inMemoryTaskManager.addNewTask(task1);
        inMemoryHistoryManager.addToHistory(task1);
        inMemoryTaskManager.addNewTask(task2);
        inMemoryHistoryManager.addToHistory(task2);
        inMemoryTaskManager.addNewTask(task3);
        inMemoryHistoryManager.addToHistory(task3);

        inMemoryTaskManager.addNewEpicTask(epic1);
        inMemoryHistoryManager.addToHistory(epic1);

        subTask1.setEpicId(epic1.getId());
        inMemoryTaskManager.addNewSubTask(subTask1);
        inMemoryHistoryManager.addToHistory(subTask1);
        subTask2.setEpicId(epic1.getId());
        inMemoryTaskManager.addNewSubTask(subTask2);
        inMemoryHistoryManager.addToHistory(subTask2);

        inMemoryTaskManager.addNewEpicTask(epic2);

        subTask3.setEpicId(epic2.getId());
        inMemoryTaskManager.addNewSubTask(subTask3);
        inMemoryHistoryManager.addToHistory(subTask3);

        inMemoryTaskManager.addNewEpicTask(epic3);
        inMemoryHistoryManager.addToHistory(epic3);


        System.out.println("Проверка работы Task");
        System.out.println(task1);
        System.out.println("========");
        System.out.println(inMemoryTaskManager.getAllTask());
        System.out.println("========");
        System.out.println(inMemoryTaskManager.getTask(2));
        System.out.println("========");
        inMemoryTaskManager.deleteTask(3);
        System.out.println(inMemoryTaskManager.getAllTask());
        System.out.println("========");
        inMemoryTaskManager.deleteAllTask();
        System.out.println(inMemoryTaskManager.getAllTask());
        System.out.println("Проверка работы Epic");
        System.out.println(epic1);
        System.out.println("========");
        System.out.println(inMemoryTaskManager.getAllEpic());
        System.out.println("========");
        System.out.println(inMemoryTaskManager.getEpic(4));
        System.out.println("========");
        inMemoryTaskManager.deleteEpic(5);
        System.out.println(inMemoryTaskManager.getAllEpic());
        System.out.println("========");
        System.out.println(inMemoryTaskManager.getAllSubTask());
        System.out.println(inMemoryTaskManager.getEpicSubTaskIds(4));
        subTask1.setStatus(Status.IN_PROGRESS);
        subTask1.setDescription("ЭТО ИЗМЕНЕННОЕ ОПИСАНИЕ");
        subTask1.setName("ЭТО ИЗМЕНЕННОЕ НАЗВАНИЕ");
        System.out.println(inMemoryHistoryManager.getHistory());

    }
}
