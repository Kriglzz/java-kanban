package Manager;

import Task.Epic;
import Task.SubTask;
import Task.Task;

import java.util.ArrayList;
import java.util.List;

public interface TaskManager {

    void addNewTask(Task task);

    void updateTask(Task task);

    void deleteTask(int taskId);

    void deleteAllTask();

    Task getTask(int taskId);

    ArrayList<Task> getAllTask();

    void addNewEpicTask(Epic epic);

    void updateEpic(Epic epic);

    void deleteEpic(int epicId);

    void deleteAllEpic();

    Task getEpic(int taskId);

    ArrayList<Epic> getAllEpic();

    ArrayList<Integer> getEpicSubTaskIds(int desiredId);

    void addNewSubTask(SubTask subTask);

    void updateSubTask(SubTask subTask);

    void deleteSubTask(int subTaskId);

    void deleteAllSubTask();

    Task getSubTask(int taskId);

    ArrayList<SubTask> getAllSubTask();

    List<Task> getHistoryMethod();
}
