import client.KVServer;
import com.google.gson.Gson;
import manager.HistoryManager;
import manager.Managers;
import manager.TaskManager;
import server.HttpTaskManager;
import task.Epic;
import task.SubTask;
import task.Task;
import manager.FileBackedTasksManager;

import java.io.IOException;
import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) throws IOException {

        KVServer kvServer = new KVServer();
        Gson gson = new Gson();
        kvServer.start();
        TaskManager taskManager = Managers.getDefault();
        //FileBackedTasksManager.testSprint(taskManager);  // тестовые данные для ФЗ 8-го спринта
        Task task = new Task("task", "description", LocalDateTime.now(), 1);
        Epic epic = new Epic("epicTemp", "description", LocalDateTime.now().plusMinutes(3), 1);
        SubTask subTask = new SubTask("subTask","description", epic.getId(), LocalDateTime.now().plusMinutes(5), 1);
        HttpTaskManager taskManagerNew = new HttpTaskManager("http://localhost:8085/");
        taskManagerNew.serverLoad();
        HistoryManager historyManager = Managers.getDefaultHistoryManager();
        System.out.println(gson.toJson(task));
        System.out.println(gson.toJson(epic));
        System.out.println(gson.toJson(subTask));

        /*System.out.println("начало проверки");
        for (int i = 1; i < 20; i++) {
            taskManager.addNewTask(new Task("name" + i, "desc" + i, LocalDateTime.of(2023, 2, 11, 1, 1), 1));
        }
        for (int i = 5; i < 18; i++) {
            taskManager.getTaskById(i);
        }
        var e = taskManager.getHistory();
        System.out.println(e);
        System.out.println("конец проверки");*/
        /*Task task1 = new Task("задача", "описание");
        Task task2 = new Task("з2", "о2");
        Task task3 = new Task("Изменено", "Изменено");
        Epic epic1 = new Epic("epic1", "descr1");
        SubTask subTask1 = new SubTask("subtask1", "descr1", 0);
        SubTask subTask2 = new SubTask("subtask2", "descr2", 0);
        SubTask subTask3 = new SubTask("subtask3", "descr3", 0);

        Epic epic2 = new Epic("epic2", "descr2");
        Epic epic3 = new Epic("epic3", "descr3");

        taskManager.addNewTask(task1);
        taskManager.getTaskById(1);
        taskManager.addNewTask(task2);
        taskManager.getTaskById(2);
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


        System.out.println("Проверка работы Task.Task");
        System.out.println(task1);
        System.out.println("========");
        System.out.println(taskManager.getAllTask());
        System.out.println("ИСТОРИЯ");
        System.out.println(historyManager.getHistory());
        System.out.println("========");
        System.out.println(taskManager.getTaskById(2));
        System.out.println(historyManager.getHistory());

        System.out.println("========");
        taskManager.deleteTask(3);
        System.out.println(taskManager.getAllTask());
        System.out.println("========");
        taskManager.deleteAllTask();
        System.out.println(taskManager.getAllTask());
        System.out.println("ИСТОРИЯ");
        System.out.println(historyManager.getHistory());
        System.out.println("Проверка работы Task.Epic");
        System.out.println(epic1);
        System.out.println("========");
        System.out.println(taskManager.getAllEpic());
        System.out.println("ИСТОРИЯ");
        System.out.println(historyManager.getHistory());
        System.out.println("========");
        System.out.println(taskManager.getEpicById(4));
        System.out.println("ИСТОРИЯ");
        System.out.println(historyManager.getHistory());
        taskManager.deleteEpic(4);
        System.out.println("ИСТОРИЯ");
        System.out.println(historyManager.getHistory());
        System.out.println("========");
        taskManager.deleteEpic(5);
        System.out.println(taskManager.getAllEpic());
        System.out.println("========");
        System.out.println(taskManager.getAllSubTask());
        System.out.println(taskManager.getEpicSubTaskIds(4));
        subTask1.setStatus(Status.IN_PROGRESS);
        subTask1.setDescription("ЭТО ИЗМЕНЕННОЕ ОПИСАНИЕ");
        subTask1.setName("ЭТО ИЗМЕНЕННОЕ НАЗВАНИЕ");
        System.out.println("========");
        System.out.println(taskManager.getHistory());*/

    }
}
