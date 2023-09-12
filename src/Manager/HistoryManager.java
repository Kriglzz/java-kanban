package Manager;

import Task.Task;

import java.util.List;

public interface HistoryManager {

    void addToHistory(Task task);

    List<Task> getHistory();
}
