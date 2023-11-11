package task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Task {
    private static int count = 0;
    protected int id;
    protected TaskType taskType;
    protected String name;
    protected String description;
    protected Status status;
    protected LocalDateTime startTime;
    protected Duration duration;


    public Task(String name, String description, LocalDateTime startTime, int duration) {
        this.id = generateId();
        this.taskType = TaskType.TASK;
        this.name = name;
        this.description = description;
        this.status = Status.NEW;
        this.startTime = startTime;
        this.duration = Duration.ofMinutes(duration);

    }

    Integer generateId() {
        return ++count;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }
    public TaskType getTaskType() {
        return taskType;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public LocalDateTime getEndTime() {
        if (startTime == null) return null;
        else return startTime.plus(duration);
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
                status+ ", " +
                startTimeString + ", " +
                durationString;
    }

}
