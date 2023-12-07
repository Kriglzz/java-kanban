package test;

import manager.FileBackedTasksManager;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.SubTask;
import task.Task;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {

    protected FileBackedTasksManagerTest() {
        super();
        taskManager = new FileBackedTasksManager(new File("src/test/TEST.csv"));
    }


    @Test
    void shouldSaveAndShouldLoad() {
        Task task = new Task("loadedTask", "description", LocalDateTime.now(), 1);
        Epic epicTemp = new Epic("loadedEpic", "description", LocalDateTime.now().plusMinutes(1), 1);
        SubTask subTask = new SubTask("loadedSubTask", "description", epicTemp.getId(), LocalDateTime.now().plusMinutes(2), 1);
        taskManager.addNewTask(task);
        taskManager.addNewEpicTask(epicTemp);
        taskManager.addNewSubTask(subTask);
        taskId = task.getId();
        epicId = epicTemp.getId();
        subTaskId = subTask.getId();
        taskManager.getTaskById(taskId);
        taskManager.getEpicById(epicId);
        taskManager.getSubTaskById(subTaskId);
        taskManager.save();
        taskManager.loadFromFile(new File("src/test/TEST.csv"));

        assertEquals(1, taskManager.getAllTask().size());
        assertEquals(1, taskManager.getAllSubTask().size());
        assertEquals(3, taskManager.getHistory().size());
    }

    @Test
    void shouldSaveWithoutTasks() throws IOException {
        Task task = new Task("task", "description", LocalDateTime.now(), 1);
        Epic epicTemp = new Epic("epic", "description", LocalDateTime.now().plusMinutes(1), 1);
        SubTask subTask = new SubTask("subTask", "description", epicTemp.getId(), LocalDateTime.now().plusMinutes(2), 1);
        taskManager.addNewTask(task);
        taskManager.addNewEpicTask(epicTemp);
        taskManager.addNewSubTask(subTask);
        taskManager.deleteAllTask();
        taskManager.save();
        taskManager.loadFromFile(new File("src/test/TEST.csv"));
        assertEquals(0, taskManager.getAllTask().size());
        assertEquals(1, taskManager.getAllSubTask().size());
        assertEquals(0, taskManager.getHistory().size());
    }

    @Test
    void shouldSaveWithoutSubtasks() {
        Task task = new Task("task", "description", LocalDateTime.now(), 1);
        Epic epicTemp = new Epic("epic", "description", LocalDateTime.now().plusMinutes(1), 1);
        SubTask subTask = new SubTask("subTask", "description", epicTemp.getId(), LocalDateTime.now().plusMinutes(2), 1);
        taskManager.addNewTask(task);
        taskManager.addNewEpicTask(epicTemp);
        taskManager.addNewSubTask(subTask);
        taskManager.deleteAllSubTask();
        taskManager.save();
        taskManager.loadFromFile(new File("src/test/TEST.csv"));
        assertEquals(1, taskManager.getAllTask().size());
        assertEquals(0, taskManager.getAllSubTask().size());
        assertEquals(0, taskManager.getHistory().size());
    }
}