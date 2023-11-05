package task;

import java.util.ArrayList;

public class Epic extends Task {

    protected TaskTypes taskType;
    private ArrayList<Integer> subTaskIds = new ArrayList<>();

    public Epic(String name, String description) {
        super(name, description);
        this.taskType = TaskTypes.EPIC;
    }

    public TaskTypes getTaskType() {
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
        return "EpicTask{" +
                ", id='" + id + '\'' +
                ", Type='" + taskType + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
