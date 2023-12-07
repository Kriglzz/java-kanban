package manager;

import task.Task;
import task.Epic;
import task.SubTask;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.LinkedList;


public class FileBackedTasksManager extends InMemoryTaskManager {
    protected File file;
    public static void main(String[] args){

        FileBackedTasksManager fileManager = new FileBackedTasksManager(new File("saveTasks.csv"));
        fileManager.addNewTask(new Task("task1", "Купить автомобиль", LocalDateTime.of(2023, 1, 10, 1, 1), 1));
        fileManager.addNewEpicTask(new Epic("new Epic1", "Новый Эпик", LocalDateTime.of(2023, 2, 11, 1, 1), 1));
        fileManager.addNewSubTask(new SubTask("New Subtask", "Подзадача", 2, LocalDateTime.of(2023, 3, 11, 1, 1), 1));
        fileManager.addNewSubTask(new SubTask("New Subtask2", "Подзадача2", 2, LocalDateTime.of(2023, 3, 12, 1, 1), 1));
        fileManager.getTaskById(1);
        fileManager.getEpicById(2);
        fileManager.getSubTaskById(3);
        System.out.println(fileManager.getAllTask());
        System.out.println(fileManager.getAllEpic());
        System.out.println(fileManager.getAllSubTask());
        System.out.println(fileManager.getHistory());
        System.out.println("\n\n" + "new" + "\n\n");
        FileBackedTasksManager fileBackedTasksManager = loadFromFile(new File("saveTasks.csv"));
        System.out.println(fileBackedTasksManager.getAllTask());
        System.out.println(fileBackedTasksManager.getAllEpic());
        System.out.println(fileBackedTasksManager.getAllSubTask());
        System.out.println(fileBackedTasksManager.getHistory());
    }

    public FileBackedTasksManager(File file) {
        super();
        this.file = file;
    }

    public FileBackedTasksManager() {
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
                        tasksToHash(task, read);
                    }
                }
                if (!tasksFromFile.getLast().equals("")) {
                    List<Integer> history = CSVFormatter.historyFromString(tasksFromFile.getLast());
                    for (Integer id : history) {
                        historyToHash(id, read);
                    }
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Файл отсутствует в системе.");
        }
        return read;
    }

    static void historyToHash(int id, FileBackedTasksManager manager) {
        if ( manager.taskList.containsKey(id)) {
            manager.historyManager.addToHistory( manager.taskList.get(id));
        } else if ( manager.subTaskList.containsKey(id)) {
            manager.historyManager.addToHistory( manager.subTaskList.get(id));
        } else if ( manager.epicList.containsKey(id)) {
            manager.historyManager.addToHistory( manager.epicList.get(id));
        }
    }

    static void tasksToHash(Task task, FileBackedTasksManager manager) {
        switch (task.getTaskType()) {
            case SUBTASK:
                manager.subTaskList.put(task.getId(), (SubTask) task);
                break;
            case EPIC:
                manager.epicList.put(task.getId(), (Epic) task);
                break;
            case TASK:
                manager.taskList.put(task.getId(), task);
                break;

        }
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
    public Epic getEpicById(int taskId) {
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
    public SubTask getSubTaskById(int taskId) {
        historyManager.addToHistory(subTaskList.get(taskId));
        save();
        return subTaskList.get(taskId);
    }

}

