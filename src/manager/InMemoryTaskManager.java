package manager;

import task.Epic;
import task.Status;
import task.SubTask;
import task.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    protected HashMap<Integer, Task> taskList = new HashMap<>();
    protected HashMap<Integer, Epic> epicList = new HashMap<>();
    protected HashMap<Integer, SubTask> subTaskList = new HashMap<>();
    protected HistoryManager historyManager = Managers.getDefaultHistoryManager();
    protected final TreeSet<Task> prioritizedTasks = new TreeSet<>(new TimeComparator());


    @Override
    public void addNewTask(Task task) {
        try {
            checkStartTime(task);
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
            return;
        }
        taskList.put(task.getId(), task);
        prioritizedTasks.add(task);

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
            historyManager.remove(taskId);
        }
    }

    @Override
    public void deleteAllTask() {
        taskList.clear();
    }

    @Override
    public Task getTaskById(int taskId) {
        historyManager.addToHistory(taskList.get(taskId));
        return taskList.get(taskId);
    }

    @Override
    public ArrayList<Task> getAllTask() {
        return new ArrayList<>(taskList.values());
    }

    @Override
    public void addNewEpicTask(Epic epic) {
        try {
            checkStartTime(epic);
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
            return;
        }
        epicList.put(epic.getId(), epic);
        calculateEpicTime(epic.getId());
        prioritizedTasks.add(epic);
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
            historyManager.remove(epicId);
        }
    }

    @Override
    public void deleteAllEpic() {
        epicList.clear();
    }

    @Override
    public Epic getEpicById(int taskId) {
        historyManager.addToHistory(epicList.get(taskId));
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
        try {
            checkStartTime(subTask);
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
            return;
        }
        subTaskList.put(subTask.getId(), subTask);
        epicList.get(subTask.getEpicId()).getSubTaskIds().add(subTask.getId());
        checkEpicStatus(subTask.getEpicId());
        calculateEpicTime(subTask.getEpicId());
        prioritizedTasks.add(subTask);
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
            calculateEpicTime(subTaskList.get(subTaskId).getEpicId());
            historyManager.remove(subTaskId);
        }
    }

    @Override
    public void deleteAllSubTask() {
        subTaskList.clear();
    }

    @Override
    public SubTask getSubTaskById(int taskId) {
        historyManager.addToHistory(subTaskList.get(taskId));
        return subTaskList.get(taskId);
    }

    @Override
    public ArrayList<SubTask> getAllSubTask() {
        return new ArrayList<>(subTaskList.values());
    }

    private void checkEpicStatus(int checkId) {
        if (epicList.get(checkId).getSubTaskIds() != null && epicList.get(checkId).getSubTaskIds().isEmpty()) {
            epicList.get(checkId).setStatus(Status.NEW);
        } else {
            boolean checkNew = false;
            boolean checkDone = false;
            for (SubTask taskId : subTaskList.values()) {
                if (!taskId.getStatus().equals(Status.NEW) && !taskId.getStatus().equals(Status.DONE) && taskId.getEpicId() == checkId) {
                    epicList.get(checkId).setStatus(Status.IN_PROGRESS);
                    break;
                } else if (taskId.getStatus().equals(Status.NEW) && taskId.getEpicId() == checkId && checkDone) {
                    epicList.get(checkId).setStatus(Status.IN_PROGRESS);
                    break;
                } else if (taskId.getStatus().equals(Status.NEW) && taskId.getEpicId() == checkId && !checkDone) {
                    checkNew = true;
                    break;
                } else if (taskId.getStatus().equals(Status.DONE) && taskId.getEpicId() == checkId) {
                    if (checkNew) {
                        epicList.get(checkId).setStatus(Status.IN_PROGRESS);
                        break;
                    } else {
                        checkDone = true;
                    }
                }
            }
            if (checkNew) {
                epicList.get(checkId).setStatus(Status.NEW);
            } else if (checkDone) {
                epicList.get(checkId).setStatus(Status.DONE);
            }
        }
    }
    private void calculateEpicTime(Integer epicId) {
        Epic epic = epicList.get(epicId);
        ArrayList<Integer> subTaskIds = epic.getSubTaskIds();
        if (subTaskIds.isEmpty()) {
            epic.setDuration(Duration.ofMinutes(0));
            epic.setStartTime(LocalDateTime.now());
        }
        if (!subTaskIds.isEmpty() && !subTaskList.isEmpty()) {
            Duration epicDuration = Duration.ofMinutes(0);
            LocalDateTime startTimeFirst = LocalDateTime.now();
            for (int id: subTaskIds) {
                epicDuration = epicDuration.plus(subTaskList.get(id).getDuration());
                epicDuration = epicDuration.plus(subTaskList.get(id).getDuration());
                if (subTaskList.get(id).getStartTime().isBefore(startTimeFirst)) {
                    startTimeFirst = subTaskList.get(id).getStartTime();
                }
            }
            epic.setDuration(epicDuration);
            epic.setStartTime(startTimeFirst);
        }
    }
    public void checkStartTime(Task task) {
        Optional<Task> check = getPrioritizedTasks().stream()
                .filter(time -> {
                    LocalDateTime startTime = time.getStartTime();
                    return startTime != null && startTime.equals(task.getStartTime());
                })
                .findFirst();
        if (check.isPresent()) {
            throw new IllegalStateException("Ошибка. Время начала задачи совпадает с уже существующей задачей.\n"
                    + check.get().getName() + " - " + check.get().getStartTime());
        }
    }
    public TreeSet<Task> getPrioritizedTasks() {
        return prioritizedTasks;
    }
    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }
}
