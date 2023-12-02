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
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;

public class HttpTaskManagerTest extends TaskManagerTest<HttpTaskManager> {

    KVServer kvServer;
    Gson gson;
    HttpTaskManager httpTaskManager;
    
    @Override
    @BeforeEach
    public void BeforeEach() throws IOException {
        kvServer = new KVServer();
        kvServer.start();
        gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new DateTimeAdapter())
                .create();
        httpTaskManager = new HttpTaskManager("http://localhost:8078/");
    }

    @AfterEach
    public void afterEach() {
        kvServer.stop();
    }
    @Test
    public void testSave() throws URISyntaxException, IOException, InterruptedException {
        Task task = new Task("task", "description", LocalDateTime.now(), 1);
        Epic epic = new Epic("epicTemp", "description", LocalDateTime.now().plusMinutes(3), 1);
        SubTask subTask = new SubTask("subTask","description", epic.getId(), LocalDateTime.now().plusMinutes(5), 1);

        httpTaskManager.addNewTask(task);
        httpTaskManager.addNewEpicTask(epic);
        httpTaskManager.addNewSubTask(subTask);

        httpTaskManager.getTaskById(3);
        httpTaskManager.getSubTaskById(2);

        httpTaskManager.serverLoad();

        assertArrayEquals(httpTaskManager.getAllTask().toArray(), httpTaskManager.getAllTask().toArray());
        assertArrayEquals(httpTaskManager.getHistory().toArray(), httpTaskManager.getHistory().toArray());
    }
    /*@Test
    public void createTasks() {
        Task task = new Task("task","description", LocalDateTime.now(), 1);
        httpTaskManager.addNewTask(task);
        httpTaskManager.save();
        httpTaskManager.serverLoad();
        Task loadedTask = httpTaskManager.getTaskById(1);
        assertEquals(task, loadedTask, "Задача не находится на сервере.");
    }*/
}
