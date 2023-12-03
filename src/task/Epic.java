package task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Epic extends Task {

    private ArrayList<Integer> subTaskIds = new ArrayList<>();

    public Epic(String name, String description, LocalDateTime startTime, int duration) {
        super(name, description, startTime, duration);
        this.taskType = TaskType.EPIC;
    }

    public TaskType getTaskType() {
        return taskType;
    }
    public ArrayList<Integer> getSubTaskIds() {
        return subTaskIds;
    }

    public void setSubTaskId(ArrayList<Integer> subTaskIds) {
        this.subTaskIds = subTaskIds;
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
        return  id + ", " +
                taskType + ", " +
                name + ", " +
                description + ", " +
                status + ", " +
                startTimeString + ", " +
                durationString;
    }
}
