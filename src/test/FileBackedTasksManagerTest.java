package test;

import manager.FileBackedTasksManager;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {

    File file = new File(".\\", "saveTasks2.csv");

    public FileBackedTasksManagerTest() {
        super();
        this.taskManager = new FileBackedTasksManager(file);
    }

    @Test
    void shouldSave() {
        taskManager.getTaskById(1);
        taskManager.getEpicById(3);
        taskManager.save();
        taskManager.loadFromFile(file);

        assertEquals(2, taskManager.getAllTask().size());
        assertEquals(3, taskManager.getAllSubTask().size());
        assertEquals(2, taskManager.getHistory().size());
    }

    @Test
    void shouldSaveWithoutTasks() throws IOException {
        taskManager.deleteAllTask();
        taskManager.save();
        taskManager.loadFromFile(file);
        assertEquals(0, taskManager.getAllTask().size());
        assertEquals(3, taskManager.getAllSubTask().size());
        assertEquals(0, taskManager.getHistory().size());
    }

    @Test
    void shouldSaveWithoutSubtasks() {
        taskManager.deleteAllSubTask();
        taskManager.save();
        taskManager.loadFromFile(file);
        assertEquals(2, taskManager.getAllTask().size());
        assertEquals(0, taskManager.getAllSubTask().size());
        assertEquals(0, taskManager.getHistory().size());
    }
}