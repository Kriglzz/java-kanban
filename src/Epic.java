import java.util.ArrayList;

public class Epic extends Task {

    protected ArrayList<Integer> subTaskIds = new ArrayList<>();

    public Epic(String name, String description, String status) {
        super(name, description, status);
    }

    public ArrayList<Integer> getSubTaskIds() {
        return subTaskIds;
    }

    public void setSubTaskId(ArrayList<Integer> subTaskIds) {
        this.subTaskIds = subTaskIds;
    }
}
