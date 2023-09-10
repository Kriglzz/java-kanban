import java.util.List;

public interface HistoryManager {
    static final int MAX_HISTORY_SIZE = 10;

    void addToHistory(Task task);

    List<Task> getHistory();
}
