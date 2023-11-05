package manager;

import task.Epic;
import task.SubTask;
import task.Task;
import task.TaskTypes;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.LinkedList;


public class FileBackedTasksManager extends InMemoryTaskManager {
    private final File file;
    public static void main(String[] args){

        FileBackedTasksManager fileManager = new FileBackedTasksManager(new File("saveTasks2.csv"));
        fileManager.addNewTask(new Task("task1", "Купить автомобиль"));
        fileManager.addNewEpicTask(new Epic("new Epic1", "Новый Эпик"));
        fileManager.addNewSubTask(new SubTask("New Subtask", "Подзадача", 2));
        fileManager.addNewSubTask(new SubTask("New Subtask2", "Подзадача2", 2));
        fileManager.getTaskById(1);
        fileManager.getEpicById(2);
        fileManager.getSubTaskById(3);
        System.out.println(fileManager.getAllTask());
        System.out.println(fileManager.getAllEpic());
        System.out.println(fileManager.getAllSubTask());
        System.out.println(fileManager.getHistory());
        System.out.println("\n\n" + "new" + "\n\n");
        FileBackedTasksManager fileBackedTasksManager = loadFromFile(new File("saveTasks2.csv"));
        System.out.println(fileManager.getAllTask());
        System.out.println(fileManager.getAllEpic());
        System.out.println(fileManager.getAllSubTask());
        System.out.println(fileBackedTasksManager.getHistory());
    }

    public FileBackedTasksManager(File file) {
        super();
        this.file = file;
    }

    public static FileBackedTasksManager loadFromFile(File file) {
        FileBackedTasksManager read = new FileBackedTasksManager(file);
        LinkedList<String> tasksFromFile = new LinkedList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            while (reader.ready()) {
                String lines = reader.readLine();
                tasksFromFile.add(lines);
            }
            if (tasksFromFile.size() != 2) {
                tasksFromFile.removeFirst();
                if (tasksFromFile.size() > 2) {
                    for (int i = 0; i < tasksFromFile.size() - 2; i++) {
                        Task task = CSVFormatter.fromString(tasksFromFile.get(i));
                        tasksToHash(task);
                    }
                }
                if (!tasksFromFile.getLast().equals("")) {
                    List<Integer> history = CSVFormatter.historyFromString(tasksFromFile.getLast());
                    for (Integer id : history) {
                        historyToHash(id);
                    }
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Файл отсутствует в системе.");
        }
        return read;
    }

    public void historyToHash(int id) {
        if (taskList.containsKey(id)) {
            historyManager.addToHistory(taskList.get(id));
        } else if (subTaskList.containsKey(id)) {
            historyManager.addToHistory(subTaskList.get(id));
        } else if (epicList.containsKey(id)) {
            historyManager.addToHistory(epicList.get(id));
        }
    }

    public void tasksToHash(Task task) {
        switch (task.getTaskType()) {
            case SUBTASK:
                subTaskList.put(task.getId(), (SubTask) task);
                break;
            case EPIC:
                epicList.put(task.getId(), (Epic) task);
                break;
            case TASK:
                taskList.put(task.getId(), task);
                break;
        }
    }

    private void save() {
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
