package task;

public class SubTask extends Task {

    protected TaskTypes taskType;
    private int epicId;

    public SubTask(String name, String description, int epicId) {
        super(name, description);
        this.taskType = TaskTypes.SUBTASK;
        this.epicId = epicId;
    }

    public TaskTypes getTaskType() {
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
