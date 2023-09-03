import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private HashMap<Integer, Task> taskList = new HashMap<>();
    private HashMap<Integer, Epic> epicList = new HashMap<>();
    private HashMap<Integer, SubTask> subTaskList = new HashMap<>();
    private int id = 1;


    public void addNewTask(Task task) {
        task.setTaskId(id);
        taskList.put(task.getTaskId(), task);
        id++;
    }

    public void updateTask(Task task) {
        taskList.get(task.getTaskId()).setName(task.getName());
        taskList.get(task.getTaskId()).setDescription(task.getDescription());
        taskList.get(task.getTaskId()).setStatus(task.getStatus());
    }

    public void deleteTask(int desiredId) {
        if (taskList.containsKey(desiredId)) {
            taskList.remove(desiredId);
        }
    }

    public void deleteAllTask() {
        taskList.clear();
    }

    public Task getTask(int taskId) {
        return taskList.get(taskId);
    }

    public ArrayList<Task> getAllTask() {
        return new ArrayList<>(taskList.values());
    }

    public void addNewEpicTask(Epic epic) {
        epic.setTaskId(id);
        epic.setStatus("NEW");
        epicList.put(epic.getTaskId(), epic);
        id++;
    }

    public void updateEpic(Epic epic) {
        epicList.get(epic.getTaskId()).setName(epic.getName());
        epicList.get(epic.getTaskId()).setDescription(epic.getDescription());
    }

    public void deleteEpic(int desiredId) {
        if (epicList.containsKey(desiredId)) {
            epicList.remove(desiredId);
        }
    }

    public void deleteAllEpic() {
        epicList.clear();
    }

    public Task getEpic(int taskId) {
        return epicList.get(taskId);
    }

    public ArrayList<Epic> getAllEpic() {
        return new ArrayList<>(epicList.values());
    }

    public ArrayList<Integer> getEpicSubTaskIds(int id) {
        if (epicList.get(id) != null) {
            return epicList.get(id).getSubTaskIds();
        } else {
            return null;
        }
    }

    public void addNewSubTask(SubTask subTask) {
        subTask.setTaskId(id);
        subTaskList.put(subTask.getTaskId(), subTask);
        epicList.get(subTask.getEpicId()).getSubTaskIds().add(subTask.getTaskId());
        checkEpicStatus(subTask.getEpicId());
        id++;
    }

    public void updateSubTask(SubTask subTask) {
        subTaskList.get(subTask.getTaskId()).setName(subTask.getName());
        subTaskList.get(subTask.getTaskId()).setDescription(subTask.getDescription());
        subTaskList.get(subTask.getTaskId()).setStatus(subTask.getStatus());
        checkEpicStatus(subTask.getTaskId());
    }

    public void deleteSubTask(int desiredId) {
        if (subTaskList.containsKey(desiredId)) {
            subTaskList.remove(desiredId);
        }
    }

    public void deleteAllSubTask() {
        subTaskList.clear();
    }

    public Task getSubTask(int taskId) {
        return subTaskList.get(taskId);
    }

    public ArrayList<SubTask> getAllSubTask() {
        return new ArrayList<>(subTaskList.values());
    }

    public void checkEpicStatus(int checkId) {
        if (epicList.get(checkId).subTaskIds != null && epicList.get(checkId).subTaskIds.isEmpty()) {
            epicList.get(checkId).status = "NEW";
        } else {
            boolean checkNew = false;
            boolean checkDone = false;
            for (SubTask taskId : subTaskList.values()) {
                if (!taskId.status.equals("NEW") && !taskId.status.equals("DONE") && taskId.epicId == checkId) {
                    epicList.get(checkId).status = "IN_PROGRESS";
                    break;
                } else if (taskId.status.equals("NEW") && taskId.epicId == checkId) {
                    if (checkDone) {
                        epicList.get(checkId).status = "IN_PROGRESS";
                        break;
                    } else {
                        checkNew = true;
                    }
                } else if (taskId.status.equals("DONE") && taskId.epicId == checkId) {
                    if (checkNew) {
                        epicList.get(checkId).status = "IN_PROGRESS";
                        break;
                    } else {
                        checkDone = true;
                    }
                }
            }
            if (checkNew) {
                epicList.get(checkId).status = "NEW";
            } else if (checkDone) {
                epicList.get(checkId).status = "DONE";
            }
        }
    }

}
