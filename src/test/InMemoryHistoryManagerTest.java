package test;

import manager.HistoryManager;
import manager.InMemoryHistoryManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.Task;

import java.time.LocalDateTime;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class InMemoryHistoryManagerTest {
    private static HistoryManager historyManager;
    private static Task task;
    int taskId;
    @BeforeEach
    public void beforeEach() {
        historyManager = new InMemoryHistoryManager();
        task = new Task("task","description", LocalDateTime.now(), 1);
        taskId=task.getId();
    }
    @Test
    void shouldAddToHistoryAndGetHistory(){
        historyManager.addToHistory(task);
        List<Task> history = historyManager.getHistory();
        assertEquals(1, history.size(), "Задача не добавляется в историю или ее данные не были получены");
    }
    @Test
    void shouldRemoveFromHistory(){
        historyManager.addToHistory(task);
        historyManager.remove(taskId);
        List<Task> history = historyManager.getHistory();
        assertEquals(0, history.size(), "Задача не убирается из историю");
    }
}
