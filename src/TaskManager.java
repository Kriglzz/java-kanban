import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private HashMap<Integer, Task> taskList = new HashMap<>();
    private HashMap<Integer, Epic> epicList = new HashMap<>();
    private HashMap<Integer, SubTask> subTaskList = new HashMap<>();

    public void addNewTask(Task task) {
        taskList.put(task.getId(), task);
    }

    public void updateTask(Task task) {
        int id = task.getId();
        Task savedTask = taskList.get(id);
        if (savedTask == null) {
            return;
        }
        taskList.put(id, task);
    }

    public void deleteTask(int taskId) {
        if (taskList.containsKey(taskId)) {
            taskList.remove(taskId);
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
        epicList.put(epic.getId(), epic);
    }

    public void updateEpic(Epic epic) {
        int id = epic.getId();
        Epic savedEpic = epicList.get(id);
        if (savedEpic == null) {
            return;
        }
        epicList.put(id, epic);
    }

    public void deleteEpic(int epicId) {
        if (epicList.containsKey(epicId)) {
            epicList.remove(epicId);
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

    public ArrayList<Integer> getEpicSubTaskIds(int desiredId) {
        if (epicList.get(desiredId) != null) {
            return epicList.get(desiredId).getSubTaskIds();
        } else {
            return null;
        }
    }

    public void addNewSubTask(SubTask subTask) {
        subTaskList.put(subTask.getId(), subTask);
        epicList.get(subTask.getEpicId()).getSubTaskIds().add(subTask.getId());
        checkEpicStatus(subTask.getEpicId());
    }

    public void updateSubTask(SubTask subTask) {
        int id = subTask.getId();
        SubTask savedSubTask = subTaskList.get(id);
        if (savedSubTask == null) {
            return;
        }
        taskList.put(id, subTask);
        checkEpicStatus(subTask.getId());
    }

    public void deleteSubTask(int subTaskId) {
        if (subTaskList.containsKey(subTaskId)) {
            subTaskList.remove(subTaskId);
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

    private void checkEpicStatus(int checkId) {
        if (epicList.get(checkId).subTaskIds != null && epicList.get(checkId).subTaskIds.isEmpty()) {
            epicList.get(checkId).status = "NEW";
        } else {
            boolean checkNew = false;
            boolean checkDone = false;
            for (SubTask taskId : subTaskList.values()) {
                if (!taskId.status.equals("NEW") && !taskId.status.equals("DONE") && taskId.getEpicId() == checkId) {
                    epicList.get(checkId).status = "IN_PROGRESS";
                    break;
                } else if (taskId.status.equals("NEW") && taskId.getEpicId() == checkId && checkDone) {
                    epicList.get(checkId).status = "IN_PROGRESS";
                    break;
                } else if (taskId.status.equals("NEW") && taskId.getEpicId() == checkId && !checkDone) {
                    checkNew = true;
                    break;
                } else if (taskId.status.equals("DONE") && taskId.getEpicId() == checkId) {
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
