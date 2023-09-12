package Manager;

import Task.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    static final int MAX_HISTORY_SIZE = 10;
    private ArrayList<Task> taskHistory = new ArrayList<>();

    @Override
    public void addToHistory(Task task) {
        if (taskHistory.size() < MAX_HISTORY_SIZE) {
            taskHistory.add(task);
        } else {
            taskHistory.remove(0);
            taskHistory.add(task);
        }
    }

    @Override
    public List<Task> getHistory() {
        return taskHistory;
    }
}
