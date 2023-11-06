package task;

public class Task {
    private static int count = 0;
    protected int id;
    protected TaskType taskType;
    protected String name;
    protected String description;
    protected Status status;

    public Task(String name, String description) {
        this.id = generateId();
        this.taskType = TaskType.TASK;
        this.name = name;
        this.description = description;
        this.status = Status.NEW;
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

    @Override
    public String toString() {
        return  id + ", " +
                taskType + ", " +
                name + ", " +
                description + ", " +
                status;
    }
}
