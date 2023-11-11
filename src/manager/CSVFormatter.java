package manager;

import task.Epic;
import task.SubTask;
import task.Task;
import task.TaskType;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.LinkedList;

public class CSVFormatter extends InMemoryTaskManager{
    public CSVFormatter() {
        super();
    }
    public static Task fromString(String line) {
        String[] linePart = line.split(", ");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyy.MM.dd HH:mm");
        Task task;

        if (TaskType.valueOf(linePart[1]).equals(TaskType.TASK)) {

            task = new Task(linePart[2].trim(),
                    linePart[3].trim(),
                    LocalDateTime.parse(linePart[5], formatter),
                    Integer.parseInt(linePart[6]));
            task.setId(Integer.parseInt(linePart[0]));
        } else if (TaskType.valueOf(linePart[1]).equals(TaskType.SUBTASK)) {
            task = new SubTask(linePart[2].trim(),
                    linePart[3].trim(),
                    Integer.parseInt(linePart[5]),
                    LocalDateTime.parse(linePart[6], formatter),
                    Integer.parseInt(linePart[7]));
            task.setId(Integer.parseInt(linePart[0]));
        } else {
            task = new Epic(linePart[2].trim(),
                    linePart[3].trim(),
                    LocalDateTime.parse(linePart[5], formatter),
                    Integer.parseInt(linePart[6]));
            task.setId(Integer.parseInt(linePart[0]));
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
