package task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SubTask extends Task {

    private int epicId;

    public SubTask(String name, String description, int epicId, LocalDateTime startTime, int duration) {
        super(name, description, startTime, duration);
        this.taskType = TaskType.SUBTASK;
        this.epicId = epicId;
    }

    public TaskType getTaskType() {
        return taskType;
    }
    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    @Override
    public String toString() {
        String startTimeString;
        if (startTime != null) {
            startTimeString = startTime.format(DateTimeFormatter.ofPattern("yyy.MM.dd HH:mm"));
        } else {
            startTimeString = "null";
        }
        String durationString;
        if (duration != null) {
            durationString = String.valueOf(duration.toSeconds());
        } else {
            durationString = "null";
        }
        return id + ", " +
               taskType + ", " +
               name + ", " +
               description + ", " +
               status + ", " +
               epicId + ", " +
                startTimeString + ", " +
                durationString;
    }
}
