package manager;

import task.Epic;
import task.SubTask;
import task.Task;
import task.TaskTypes;

import java.io.File;
import java.util.List;
import java.util.LinkedList;

public class CSVFormatter extends InMemoryTaskManager{
    public CSVFormatter() {
        super();
    }
    public static Task fromString(String line) {
        String[] linePart = line.split(",");

        Task task;
        if (TaskTypes.valueOf(linePart[1]).equals(TaskTypes.TASK)) {
            task = new Task(linePart[2].trim(),
                    linePart[4].trim());
        } else if (TaskTypes.valueOf(linePart[1]).equals(TaskTypes.SUBTASK)) {
            task = new SubTask(linePart[2].trim(),
                    linePart[4].trim(),
                    Integer.parseInt(linePart[5]));
        } else {
            task = new Epic(linePart[2].trim(),
                    linePart[4].trim());
        }
        return task;
    }
    public static List<Integer> historyFromString(String line) {
        List<Integer> history = new LinkedList<>();
        String[] linePart = line.split(",");
        for (String string : linePart) {
            history.add(Integer.parseInt(string));
        }
        return history;
    }

}
