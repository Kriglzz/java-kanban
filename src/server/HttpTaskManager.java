package server;

import com.google.gson.*;
import manager.*;
import client.*;
import task.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class HttpTaskManager extends FileBackedTasksManager {
    final KVTaskClient client;

    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new DateTimeAdapter())
            .registerTypeAdapter(Duration.class, new DurationAdapter())
            .create();


    public HttpTaskManager(String URL) {
        super(null);
        client = new KVTaskClient(URL);
    }

    @Override
    public void save() {
        if (!getAllTask().isEmpty()) {
            client.put("tasks", gson.toJson(getAllTask().toArray()));
        }
        if (!getAllEpic().isEmpty()) {
            client.put("epics", gson.toJson(getAllEpic().toArray()));
        }
        if (!getAllSubTask().isEmpty()) {
            client.put("subtasks", gson.toJson(getAllSubTask().toArray()));
        }
        if (!getPrioritizedTasks().isEmpty()){
            client.put("prioritizedTasks", gson.toJson(getPrioritizedTasks().toArray()));
        }

        List<Task> history = new ArrayList<>(historyManager.getHistory());

        client.put("history", gson.toJson(history));
    }

    public void serverLoad() {
        Optional<String> tasks = Optional.ofNullable(client.load("tasks"));
        if (tasks.isPresent()) {
            JsonElement jsonTasks = JsonParser.parseString(tasks.get());
            if (!jsonTasks.isJsonNull()) {
                JsonArray jsonArrayTasks = jsonTasks.getAsJsonArray();
                for (JsonElement jsonTask : jsonArrayTasks) {
                    Task task = gson.fromJson(jsonTask, Task.class);
                    taskList.put(task.getId(), task);
                }
            }
        }
        Optional<String> epics = Optional.ofNullable(client.load("epics"));
        if (epics.isPresent()) {
            JsonElement jsonEpics = JsonParser.parseString(epics.get());
            if (!jsonEpics.isJsonNull()) {
                JsonArray jsonArrayEpics = jsonEpics.getAsJsonArray();
                for (JsonElement jsonEpic : jsonArrayEpics) {
                    Epic epic = gson.fromJson(jsonEpic, Epic.class);
                    epicList.put(epic.getId(), epic);
                }
            }
        }
        Optional<String> subTasks = Optional.ofNullable(client.load("subtasks"));
        if (subTasks.isPresent()) {
            JsonElement jsonSubtasks = JsonParser.parseString(subTasks.get());
            if (!jsonSubtasks.isJsonNull()) {
                JsonArray jsonArraySubtasks = jsonSubtasks.getAsJsonArray();
                for (JsonElement jsonSubtask : jsonArraySubtasks) {
                    SubTask subtask = gson.fromJson(jsonSubtask, SubTask.class);
                    subTaskList.put(subtask.getId(), subtask);
                }
            }
        }
        Optional<String> history = Optional.ofNullable(client.load("history"));
        if (history.isPresent()) {
            JsonElement jsonHistoryList = JsonParser.parseString(history.get());
            if (!jsonHistoryList.isJsonNull()) {
                JsonArray jsonHistoryArray = jsonHistoryList.getAsJsonArray();
                for (JsonElement jsonArrayElement : jsonHistoryArray) {
                    Task task = gson.fromJson(jsonArrayElement, Task.class);
                    if (!(task == null)) {
                        historyManager.addToHistory(task);
                    }
                }
            }
        }
        Optional<String> prioritizedTask = Optional.ofNullable(client.load("prioritizedTasks"));
        if (prioritizedTask.isPresent()) {
            JsonElement jsonPrioritizedTasks = JsonParser.parseString(prioritizedTask.get());
            if (!jsonPrioritizedTasks.isJsonNull()) {
                JsonArray jsonArrayPrioritizedTasks = jsonPrioritizedTasks.getAsJsonArray();
                for (JsonElement jsonPrioritizedTask : jsonArrayPrioritizedTasks) {
                    Task task = gson.fromJson(jsonPrioritizedTask, SubTask.class);
                    prioritizedTasks.add(task);
                }
            }
        }
    }
}
