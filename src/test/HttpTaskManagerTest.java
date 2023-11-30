package test;
import server.*;
import client.*;
import manager.*;
import task.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.time.LocalDateTime;

public class HttpTaskManagerTest extends TaskManagerTest<HttpTaskManager> {

    KVServer kvServer;
    Gson gson;
    HttpTaskManager httpTaskManager;
    
    @BeforeEach
    public void beforeEach() throws IOException {
        kvServer = new KVServer();
        kvServer.start();
        gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new DateTimeAdapter())
                .create();
        taskManager = new HttpTaskManager("http://localhost:8082/");
    }

    @AfterEach
    public void afterEach() {
        kvServer.stop();
    }

    @Test
    public void createTasks() {
        Task task = new Task("task","description", LocalDateTime.now(), 1);
        taskManager.addNewTask(task);
        taskManager.save();
        taskManager.serverLoad();
        Task loadedTask = taskManager.getTaskById(1);
        assertEquals(task, loadedTask, "Задача не находится на сервере.");
    }
}
