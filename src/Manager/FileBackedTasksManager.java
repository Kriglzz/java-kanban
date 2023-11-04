package Manager;

import Task.Epic;
import Task.SubTask;
import Task.Task;
import Task.Types;
import Task.Status;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class FileBackedTasksManager extends InMemoryTaskManager {
    private File file;
    public static void main(String[] args) throws IOException {

        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(new File("example.csv"));

        Task task1 = new Task("task1", "desc1");
        fileBackedTasksManager.addNewTask(task1);
        fileBackedTasksManager.addNewTask(new Task("task1", "desc1"));

        Epic epic1 = new Epic("epic1", "desc1");
        fileBackedTasksManager.addNewEpicTask(epic1);

        SubTask subtask1 = new SubTask("subtask1", "desc1", 2);
        fileBackedTasksManager.addNewSubTask(subtask1);
        SubTask subtask2 = new SubTask("subtask2", "desc1", 2);
        fileBackedTasksManager.addNewSubTask(subtask2);

        fileBackedTasksManager.updateSubTask(subtask2);

        System.out.println("Tasks: " + fileBackedTasksManager.getAllTask());
        System.out.println("Epics: " + fileBackedTasksManager.getAllEpic());
        System.out.println("task1: " + fileBackedTasksManager.getTaskById(1));
        System.out.println("task2: " + fileBackedTasksManager.getTaskById(2));
    }

    public FileBackedTasksManager(File file) {
        super();
        this.file = file;
    }

    public static FileBackedTasksManager loadFromFile(File file) {
        FileBackedTasksManager read = new FileBackedTasksManager(file);
        ArrayList<String> tasksFromFile = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            while (reader.ready()) {
                String lines = reader.readLine();
                tasksFromFile.add(lines);
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Файл отсутствует в системе.");
        }
        return read;
    }

    public static Task fromString(String line) {
        String[] linePart = line.split(",");

        Task task;
        if (Types.valueOf(linePart[1]).equals(Types.TASK)) {
            task = new Task(linePart[2],
                    linePart[4]);

        } else if (Types.valueOf(linePart[1]).equals(Types.SUBTASK)) {
            task = new SubTask(linePart[2],
                    linePart[4],
                    Integer.parseInt(linePart[5]));
        } else {
            task = new Epic(linePart[2],
                    linePart[4]);
        }
        return task;
    }

    public void save() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("id,type,name,status,description,epic\n");

            for (Task task : taskList.values()) {
                writer.write(task.toString() + "\n");
            }

            for (SubTask subTask : subTaskList.values()) {
                writer.write(subTask.toString() + "\n");
            }

            for (Epic epic : epicList.values()) {
                writer.write(epic.toString() + "\n");
            }
            writer.write("\n" + historyToString(historyManager));
        } catch (IOException e) {
            throw new ManagerSaveException("Файл отсутствует в системе.");
        }
    }

    public String historyToString(HistoryManager manager) {
        List<String> result = new ArrayList<>();

        for (Task task : manager.getHistory()) {
            result.add(String.valueOf(task.getId()));
        }
        return String.join(",", result);
    }

    @Override
    public void addNewTask(Task task) {
        super.addNewTask(task);
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void deleteTask(int taskId) {
        super.deleteTask(taskId);
        save();
    }

    @Override
    public void deleteAllTask() {
        super.deleteAllTask();
        save();
    }

    @Override
    public Task getTaskById(int taskId) {
        historyManager.addToHistory(taskList.get(taskId));
        save();
        return taskList.get(taskId);

    }

    @Override
    public void addNewEpicTask(Epic epic) {
        super.addNewEpicTask(epic);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void deleteEpic(int epicId) {
        super.deleteEpic(epicId);
        save();
    }

    @Override
    public void deleteAllEpic() {
        super.deleteAllEpic();
        save();
    }

    @Override
    public Task getEpicById(int taskId) {
        historyManager.addToHistory(epicList.get(taskId));
        save();
        return epicList.get(taskId);
    }

    @Override
    public void addNewSubTask(SubTask subTask) {
        super.addNewSubTask(subTask);
        save();
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        super.updateSubTask(subTask);
        save();
    }

    @Override
    public void deleteSubTask(int subTaskId) {
        super.deleteSubTask(subTaskId);
        save();
    }

    @Override
    public void deleteAllSubTask() {
        super.deleteAllSubTask();
        save();
    }

    @Override
    public Task getSubTaskById(int taskId) {
        historyManager.addToHistory(subTaskList.get(taskId));
        save();
        return subTaskList.get(taskId);
    }
}
