package task;

import java.util.ArrayList;

public class Epic extends Task {

    protected TaskType taskType;
    private ArrayList<Integer> subTaskIds = new ArrayList<>();

    public Epic(String name, String description) {
        super(name, description);
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
        return  id + ", " +
                taskType + ", " +
                name + ", " +
                description + ", " +
                status;
    }
}
