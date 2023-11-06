package task;

public class SubTask extends Task {

    protected TaskType taskType;
    private int epicId;

    public SubTask(String name, String description, int epicId) {
        super(name, description);
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
        return "Task.SubTask{" +
                ", id='" + id + '\'' +
                ", Type='" + taskType + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                ", epicId='" + epicId + '\'' +
                '}';
    }
}
