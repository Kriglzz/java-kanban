import java.util.ArrayList;

public class Epic extends Task {

    protected ArrayList<Integer> subTaskIds = new ArrayList<>();

    public Epic(String name, String description) {
        super(name, description);
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
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
