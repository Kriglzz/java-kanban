import java.util.ArrayList;
import java.util.HashMap;

public class InMemoryTaskManager implements TaskManager {
    private HashMap<Integer, Task> taskList = new HashMap<>();
    private HashMap<Integer, Epic> epicList = new HashMap<>();
    private HashMap<Integer, SubTask> subTaskList = new HashMap<>();
    @Override
    public void addNewTask(Task task) {
        taskList.put(task.getId(), task);
    }

    @Override
    public void updateTask(Task task) {
        int id = task.getId();
        Task savedTask = taskList.get(id);
        if (savedTask == null) {
            return;
        }
        taskList.put(id, task);
    }

    @Override
    public void deleteTask(int taskId) {
        if (taskList.containsKey(taskId)) {
            taskList.remove(taskId);
        }
    }

    @Override
    public void deleteAllTask() {
        taskList.clear();
    }

    @Override
    public Task getTask(int taskId) {
        return taskList.get(taskId);
    }

    @Override
    public ArrayList<Task> getAllTask() {
        return new ArrayList<>(taskList.values());
    }

    @Override
    public void addNewEpicTask(Epic epic) {
        epicList.put(epic.getId(), epic);
    }

    @Override
    public void updateEpic(Epic epic) {
        int id = epic.getId();
        Epic savedEpic = epicList.get(id);
        if (savedEpic == null) {
            return;
        }
        epicList.put(id, epic);
    }

    @Override
    public void deleteEpic(int epicId) {
        if (epicList.containsKey(epicId)) {
            epicList.remove(epicId);
        }
    }

    @Override
    public void deleteAllEpic() {
        epicList.clear();
    }

    @Override
    public Task getEpic(int taskId) {
        return epicList.get(taskId);
    }

    @Override
    public ArrayList<Epic> getAllEpic() {
        return new ArrayList<>(epicList.values());
    }

    @Override
    public ArrayList<Integer> getEpicSubTaskIds(int desiredId) {
        if (epicList.get(desiredId) != null) {
            return epicList.get(desiredId).getSubTaskIds();
        } else {
            return null;
        }
    }

    @Override
    public void addNewSubTask(SubTask subTask) {
        subTaskList.put(subTask.getId(), subTask);
        epicList.get(subTask.getEpicId()).getSubTaskIds().add(subTask.getId());
        checkEpicStatus(subTask.getEpicId());
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        int id = subTask.getId();
        SubTask savedSubTask = subTaskList.get(id);
        if (savedSubTask == null) {
            return;
        }
        taskList.put(id, subTask);
        checkEpicStatus(subTask.getId());
    }

    @Override
    public void deleteSubTask(int subTaskId) {
        if (subTaskList.containsKey(subTaskId)) {
            subTaskList.remove(subTaskId);
        }
    }

    @Override
    public void deleteAllSubTask() {
        subTaskList.clear();
    }

    @Override
    public Task getSubTask(int taskId) {
        return subTaskList.get(taskId);
    }

    @Override
    public ArrayList<SubTask> getAllSubTask() {
        return new ArrayList<>(subTaskList.values());
    }

    @Override
    public void checkEpicStatus(int checkId) {
        if (epicList.get(checkId).subTaskIds != null && epicList.get(checkId).subTaskIds.isEmpty()) {
            epicList.get(checkId).status = Status.NEW;
        } else {
            boolean checkNew = false;
            boolean checkDone = false;
            for (SubTask taskId : subTaskList.values()) {
                if (!taskId.status.equals(Status.NEW) && !taskId.status.equals(Status.DONE) && taskId.getEpicId() == checkId) {
                    epicList.get(checkId).status = Status.IN_PROGRESS;
                    break;
                } else if (taskId.status.equals(Status.NEW) && taskId.getEpicId() == checkId && checkDone) {
                    epicList.get(checkId).status = Status.IN_PROGRESS;
                    break;
                } else if (taskId.status.equals(Status.NEW) && taskId.getEpicId() == checkId && !checkDone) {
                    checkNew = true;
                    break;
                } else if (taskId.status.equals(Status.DONE) && taskId.getEpicId() == checkId) {
                    if (checkNew) {
                        epicList.get(checkId).status = Status.IN_PROGRESS;
                        break;
                    } else {
                        checkDone = true;
                    }
                }
            }
            if (checkNew) {
                epicList.get(checkId).status = Status.NEW;
            } else if (checkDone) {
                epicList.get(checkId).status = Status.DONE;
            }
        }
    }
}