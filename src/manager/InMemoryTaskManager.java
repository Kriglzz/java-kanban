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
    public void addNewTask(Task task) throws IllegalStateException{
        try {
            checkStartTimeAndEndTime(task);
        } catch (IllegalStateException e) {
            throw e;
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
    public void addNewEpicTask(Epic epic) throws IllegalStateException{
        try {
            checkStartTimeAndEndTime(epic);
        } catch (IllegalStateException e) {
            throw e;
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
        checkEpicStatus(id);
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
    public void addNewSubTask(SubTask subTask) throws IllegalStateException{
        try {
            checkStartTimeAndEndTime(subTask);
        } catch (IllegalStateException e) {
            throw e;
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
            checkEpicStatus(subTaskList.get(subTaskId).getEpicId());
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
            boolean hasNew = false;
            boolean hasInProgress = false;
            boolean hasDone = false;

            for (SubTask subTaskTemp : subTaskList.values()) {
                if (subTaskTemp.getEpicId() == checkId) {
                    if (subTaskTemp.getStatus().equals(Status.NEW)) {
                        hasNew = true;
                    } else if (subTaskTemp.getStatus().equals(Status.IN_PROGRESS)) {
                        hasInProgress = true;
                        break;
                    } else if (subTaskTemp.getStatus().equals(Status.DONE)) {
                        hasDone = true;
                        if (hasNew) {
                            break; // Прерываем цикл, если уже есть задача со статусом NEW или IN_PROGRESS
                        }
                    }
                }
            }

            if (hasInProgress|| hasNew && hasDone) {
                epicList.get(checkId).setStatus(Status.IN_PROGRESS);
            } else if (hasNew) {
                epicList.get(checkId).setStatus(Status.NEW);
            } else if (hasDone) {
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
    private void checkStartTimeAndEndTime(Task task) {
        LocalDateTime taskStartTime = task.getStartTime();
        LocalDateTime taskEndTime = task.getEndTime();

        Optional<Task> overlappingTask = getPrioritizedTasks().stream()
                .filter(existingTask -> {
                    LocalDateTime existingStartTime = existingTask.getStartTime();
                    LocalDateTime existingEndTime = existingTask.getEndTime();

                    if (existingStartTime != null && existingEndTime != null && taskStartTime != null && taskEndTime != null) {
                        return (taskStartTime.isBefore(existingEndTime) && taskEndTime.isAfter(existingStartTime));
                    }
                    return false;
                })
                .findFirst();

        if (overlappingTask.isPresent()) {
            throw new IllegalStateException("Ошибка. Временной интервал задачи пересекается с уже существующей задачей.\n"
                    + overlappingTask.get().getName() + " - " + overlappingTask.get().getStartTime() + " - " + overlappingTask.get().getEndTime());
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
