package test;

import server.*;
import client.*;
import task.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;

public class HttpTaskManagerTest extends TaskManagerTest<HttpTaskManager> {

    private static KVServer kvServer;
    protected static Gson gson;
    private static HttpTaskManager httpTaskManager;

    HttpTaskManagerTest() {
        super();
    }

    @Override
    @BeforeEach
    public void BeforeEach() throws IOException {
        kvServer = new KVServer();
        kvServer.start();
        gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new DateTimeAdapter())
                .create();
        httpTaskManager = new HttpTaskManager("http://localhost:8078/");
        taskManager = new HttpTaskManager("http://localhost:8078/");
    }

    @AfterEach
    public void afterEach() {
        kvServer.stop();
    }

    @Test
    public void shouldSaveAndLoadFromServer() {
        Task task1 = new Task("task1", "description", LocalDateTime.now(), 1);
        Task task2 = new Task("task2", "description", LocalDateTime.now().plusMinutes(3), 1);
        Epic epic = new Epic("epicTemp", "description", LocalDateTime.now().plusMinutes(5), 1);
        SubTask subTask = new SubTask("subTask", "description", epic.getId(), LocalDateTime.now().plusMinutes(7), 1);

        httpTaskManager.addNewTask(task1);
        httpTaskManager.addNewTask(task2);
        httpTaskManager.addNewEpicTask(epic);
        httpTaskManager.addNewSubTask(subTask);
        taskId = task1.getId();
        httpTaskManager.getTaskById(taskId);
        taskId = task2.getId();
        httpTaskManager.getTaskById(taskId);
        epicId = epic.getId();
        httpTaskManager.getEpicById(epicId);
        subTaskId = subTask.getId();
        httpTaskManager.getSubTaskById(subTaskId);

        httpTaskManager.serverLoad();
        //Т.К. serverLoad ЗАМЕНЯЕТ при загрузке задачи, а историю он ДОБАВЛЯЕТ, значение истории дублируется
        assertEquals(2, Arrays.stream(httpTaskManager.getAllTask().toArray()).count(),
                "Количество изначальных задач не равно количеству загруженных");
        assertEquals(1, Arrays.stream(httpTaskManager.getAllEpic().toArray()).count(),
                "Количество изначальных задач не равно количеству загруженных");
        assertEquals(1, Arrays.stream(httpTaskManager.getAllSubTask().toArray()).count(),
                "Количество изначальных задач не равно количеству загруженных");
        assertEquals(8, Arrays.stream(httpTaskManager.getHistory().toArray()).count(),
                "История задач не совпадает");
    }
}
