import client.KVServer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import manager.HistoryManager;
import manager.Managers;
import manager.TaskManager;
import server.DateTimeAdapter;
import server.DurationAdapter;
import server.HttpTaskManager;
import server.HttpTaskServer;
import com.sun.net.httpserver.HttpServer;
import task.Epic;
import task.SubTask;
import task.Task;
import manager.FileBackedTasksManager;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) throws IOException {

        KVServer kvServer = new KVServer();
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new DateTimeAdapter())
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .create();
        kvServer.start();
        Task task = new Task("task", "description", LocalDateTime.now(), 1);
        Epic epic = new Epic("epic", "description", LocalDateTime.now().plusMinutes(3), 1);
        SubTask subTask1 = new SubTask("subTask1", "description", epic.getId(), LocalDateTime.now().plusMinutes(5), 1);
        SubTask subTask2 = new SubTask("subTask2", "description", epic.getId(), LocalDateTime.now().plusMinutes(7), 1);
        HttpTaskManager taskManagerNew = new HttpTaskManager("http://localhost:8078/");

        try {
            HttpServer httpServer = HttpServer.create();
            httpServer.bind(new InetSocketAddress(8080), 0);
            httpServer.createContext("/tasks", new HttpTaskServer());
            httpServer.createContext("/tasks/history", new HttpTaskServer());
            httpServer.createContext("/tasks/task", new HttpTaskServer());
            httpServer.createContext("/epics/epic", new HttpTaskServer());
            httpServer.start();
            System.out.println("Сервер на порту 8080 запущен");
        } catch (RuntimeException e) {
            e.printStackTrace();
            System.out.println("Сервер не запускается");
        }

        taskManagerNew.addNewTask(task);
        taskManagerNew.addNewEpicTask(epic);
        taskManagerNew.addNewSubTask(subTask1);
        taskManagerNew.addNewSubTask(subTask2);

        taskManagerNew.getTaskById(1);
        taskManagerNew.getEpicById(2);
        taskManagerNew.getSubTaskById(3);
        taskManagerNew.getSubTaskById(4);

        taskManagerNew.serverLoad();


        System.out.println(gson.toJson(task));
        System.out.println(gson.toJson(epic));
        System.out.println(gson.toJson(subTask1));
        System.out.println(gson.toJson(subTask2));
    }
}
