package test;

import manager.InMemoryTaskManager;
import org.junit.jupiter.api.Test;
import task.Task;

import java.time.LocalDateTime;
import java.util.List;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    public InMemoryTaskManagerTest() {
        super();
        taskManager = new InMemoryTaskManager();
    }

    @Test
    void shouldReturnPrioritizedTasks() {
        Task task1 = new Task("task1", "description", LocalDateTime.now(), 1);
        Task task2 = new Task("task2", "description", LocalDateTime.now().minusMinutes(2), 1);
        Task task3 = new Task("task3", "description", LocalDateTime.now().plusMinutes(4), 1);
        taskManager.addNewTask(task1);
        taskManager.addNewTask(task2);
        taskManager.addNewTask(task3);
        System.out.println(taskManager.getPrioritizedTasks());
        TreeSet<Task> tempSet = taskManager.getPrioritizedTasks();

        assertEquals(3, taskManager.getPrioritizedTasks().size(), "Не все задачи были добавлены в приоритет");
        assertEquals(task2, tempSet.first(), "Задачи не сортируются по приоритету");
    }

    @Test
    void shouldPrintHistory() {
        Task task = new Task("task", "description", LocalDateTime.now(), 1);
        taskManager.addNewTask(task);
        taskId = task.getId();
        taskManager.getTaskById(taskId);
        List<Task> history = taskManager.getHistory();
        assertEquals(1, history.size(), "Данные истории не были получены");
    }

    @Test
    void shouldPrintEmptyHistory() {
        Task task = new Task("task", "description", LocalDateTime.now(), 1);
        taskManager.addNewTask(task);
        taskId = task.getId();
        taskManager.getTaskById(taskId);
        taskManager.deleteTask(taskId);
        List<Task> history = taskManager.getHistory();
        assertEquals(0, history.size(), "История не пустая");
    }
}
