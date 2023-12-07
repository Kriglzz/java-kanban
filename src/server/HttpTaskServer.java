package server;

import client.KVServer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import manager.TaskManager;
import task.Epic;
import task.SubTask;
import task.Task;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;


public class HttpTaskServer implements HttpHandler {
    private final static Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private final static Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new DateTimeAdapter())
            .registerTypeAdapter(Duration.class, new DurationAdapter())
            .create();
    private final HttpTaskManager httpTaskManager = new HttpTaskManager("http://localhost:8078/");
    protected static TaskManager taskManager;

    public HttpTaskServer() {
        taskManager = httpTaskManager;
    }

    private static void writeResponse(HttpExchange exchange, String responseString, int responseCode) throws IOException {
        byte[] bytes = responseString.getBytes(DEFAULT_CHARSET);
        exchange.sendResponseHeaders(responseCode, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
        exchange.close();
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        Endpoint endpoint = getEndpoint(httpExchange.getRequestURI().getPath(),
                httpExchange.getRequestMethod(), httpExchange);

        switch (endpoint) {
            //TASK
            case GET_ALL_TASKS: {
                getAllTasks(httpExchange);
            }
            case DELETE_ALL_TASKS: {
                deleteAllTasks(httpExchange);
            }
            case GET_HISTORY: {
                getHistory(httpExchange);
            }
            case GET_PRIORITIZED_TASKS: {
                getPrioritizedTask(httpExchange);
            }
            case ADD_TASK: {
                addOrUpdateTask(httpExchange);
            }
            case GET_TASK_BY_ID: {
                getTask(httpExchange);
            }

            case DELETE_TASK_BY_ID: {
                deleteTask(httpExchange);
            }
            //EPIC
            case ADD_EPIC: {
                addOrUpdateEpic(httpExchange);
            }
            case GET_EPIC_BY_ID: {
                getEpic(httpExchange);
            }

            case DELETE_EPIC_BY_ID: {
                deleteEpic(httpExchange);
            }
            //SUBTASK
            case ADD_SUBTASK: {
                addOrUpdateSubTask(httpExchange);
            }
            case GET_SUBTASK_BY_ID: {
                getSubTask(httpExchange);
            }

            case DELETE_SUBTASK_BY_ID: {
                deleteSubTask(httpExchange);
            }
            default:
                writeResponse(httpExchange, "Ошибка. Выбранный endpoint не реализован", 404);
        }
    }

    private Endpoint getEndpoint(String requestPath, String requestMethod, HttpExchange httpExchange) {
        String[] path = requestPath.split("/");
        String query = httpExchange.getRequestURI().getQuery();
        switch (requestMethod) {
            case "GET":
                if (path.length > 2) {
                    switch (path[2]) {
                        case "task":
                            if (path.length == 3 && (query == null || !query.contains("id"))) {
                                return Endpoint.GET_ALL_TASKS;
                            } else {
                                return Endpoint.GET_TASK_BY_ID;
                            }
                        case "epic":
                            return Endpoint.GET_EPIC_BY_ID;
                        case "subtask":
                            return Endpoint.GET_SUBTASK_BY_ID;
                        case "history":
                            return Endpoint.GET_HISTORY;
                    }
                    break;
                } else {
                    return Endpoint.GET_PRIORITIZED_TASKS;
                }
            case "POST":
                switch (path[2]) {
                    case "task":
                        return Endpoint.ADD_TASK;
                    case "epic":
                        return Endpoint.ADD_EPIC;
                    case "subtask":
                        return Endpoint.ADD_SUBTASK;
                }
                break;
            case "DELETE":
                switch (path[2]) {
                    case "task":
                        if (path.length == 3 && !query.contains("id")) {
                            return Endpoint.DELETE_ALL_TASKS;
                        } else {
                            return Endpoint.DELETE_TASK_BY_ID;
                        }
                    case "epic":
                        return Endpoint.DELETE_EPIC_BY_ID;
                    case "subtask":
                        return Endpoint.DELETE_SUBTASK_BY_ID;
                }
                break;
            default:
                return Endpoint.UNKNOWN;

        }


        return Endpoint.UNKNOWN;
    }

    // TASK
    private void getAllTasks(HttpExchange exchange) throws IOException {
        httpTaskManager.serverLoad();
        String jsonTasks = gson.toJson(httpTaskManager.getAllTask());
        String jsonEpicTasks = gson.toJson(httpTaskManager.getAllEpic());
        String jsonSubTasks = gson.toJson(httpTaskManager.getAllSubTask());
        httpTaskManager.save();
        writeResponse(exchange, jsonTasks + jsonEpicTasks + jsonSubTasks, 200);
    }

    private void deleteAllTasks(HttpExchange exchange) throws IOException {
        httpTaskManager.serverLoad();
        httpTaskManager.deleteAllTask();
        httpTaskManager.deleteAllSubTask();
        httpTaskManager.deleteAllEpic();
        writeResponse(exchange, "Все задачи были удалены.", 200);
        httpTaskManager.save();
    }

    private void getHistory(HttpExchange exchange) throws IOException {
        httpTaskManager.serverLoad();
        if (!httpTaskManager.getHistory().isEmpty()) {
            String json = gson.toJson(httpTaskManager.getHistory());
            if (!json.equals("[]")) {
                httpTaskManager.save();
                writeResponse(exchange, json, 200);
            } else {
                httpTaskManager.save();
                writeResponse(exchange, "Список истории пустой", 400);
            }
        }
    }

    private void getPrioritizedTask(HttpExchange httpExchange) throws IOException {
        httpTaskManager.serverLoad();
        String json = gson.toJson(httpTaskManager.getPrioritizedTasks());
        httpTaskManager.save();
        writeResponse(httpExchange, json, 200);
    }

    private void addOrUpdateTask(HttpExchange exchange) throws IOException {
        httpTaskManager.serverLoad();
        InputStream inputStream = exchange.getRequestBody();
        String body = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
        Task task = gson.fromJson(body, Task.class);
        int id = task.getId();
        try {
            httpTaskManager.getTaskById(id);
            httpTaskManager.updateTask(task);
            httpTaskManager.save();
            writeResponse(exchange, "Задача " + task.getName() + " обновлена!", 200);
        } catch (Exception e) {
            httpTaskManager.addNewTask(task);
            httpTaskManager.save();
            writeResponse(exchange, "Задача " + task.getName() + " добавлена!", 200);
        }
    }

    private void getTask(HttpExchange exchange) throws IOException {
        httpTaskManager.serverLoad();
        Optional<String> path = Optional.ofNullable(exchange.getRequestURI().getQuery());
        if (path.isPresent()) {
            int id = Integer.parseInt(path.get().split("=")[1]);
            httpTaskManager.save();
            writeResponse(exchange, gson.toJson(httpTaskManager.getTaskById(id)), 200);
        }

    }

    private void deleteTask(HttpExchange exchange) throws IOException {
        httpTaskManager.serverLoad();
        Optional<String> path = Optional.ofNullable(exchange.getRequestURI().getQuery());
        if (path.isPresent()) {
            int id = Integer.parseInt(path.get().split("=")[1]);
            httpTaskManager.deleteTask(id);
            httpTaskManager.save();
            writeResponse(exchange, "Задача была удалена.", 200);
        }
    }

    //epic

    private void addOrUpdateEpic(HttpExchange exchange) throws IOException {
        httpTaskManager.serverLoad();
        InputStream inputStream = exchange.getRequestBody();
        String body = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
        Epic epic = gson.fromJson(body, Epic.class);
        int id = epic.getId();
        try {
            httpTaskManager.getEpicById(id);
            httpTaskManager.updateEpic(epic);
            httpTaskManager.save();
            writeResponse(exchange, "Задача " + epic.getName() + " обновлена!", 200);
        } catch (Exception e) {
            httpTaskManager.addNewEpicTask(epic);
            httpTaskManager.save();
            writeResponse(exchange, "Задача " + epic.getName() + " добавлена!", 200);
        }
    }

    private void getEpic(HttpExchange exchange) throws IOException {
        httpTaskManager.serverLoad();
        Optional<String> path = Optional.ofNullable(exchange.getRequestURI().getQuery());
        if (path.isPresent()) {
            int id = Integer.parseInt(path.get().split("=")[1]);
            writeResponse(exchange, gson.toJson(httpTaskManager.getEpicById(id)), 200);
        }
        httpTaskManager.save();
    }

    private void deleteEpic(HttpExchange exchange) throws IOException {
        httpTaskManager.serverLoad();
        Optional<String> path = Optional.ofNullable(exchange.getRequestURI().getQuery());
        if (path.isPresent()) {
            int id = Integer.parseInt(path.get().split("=")[1]);
            httpTaskManager.deleteEpic(id);
            httpTaskManager.save();
            writeResponse(exchange, "Задача была удалена.", 200);
        }
    }

    //SUBTASK

    private void addOrUpdateSubTask(HttpExchange exchange) throws IOException {
        httpTaskManager.serverLoad();
        InputStream inputStream = exchange.getRequestBody();
        String body = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
        SubTask subTask = gson.fromJson(body, SubTask.class);
        int id = subTask.getId();
        try {
            httpTaskManager.getSubTaskById(id);
            httpTaskManager.updateTask(subTask);
            httpTaskManager.save();
            writeResponse(exchange, "Задача " + subTask.getName() + " обновлена!", 200);
        } catch (Exception e) {
            taskManager.addNewTask(subTask);
            httpTaskManager.save();
            writeResponse(exchange, "Задача " + subTask.getName() + " добавлена!", 200);
        }

    }

    private void getSubTask(HttpExchange exchange) throws IOException {
        httpTaskManager.serverLoad();
        Optional<String> path = Optional.ofNullable(exchange.getRequestURI().getQuery());
        if (path.isPresent()) {
            int id = Integer.parseInt(path.get().split("=")[1]);
            httpTaskManager.save();
            writeResponse(exchange, gson.toJson(httpTaskManager.getSubTaskById(id)), 200);
        }
    }

    private void deleteSubTask(HttpExchange exchange) throws IOException {
        httpTaskManager.serverLoad();
        Optional<String> path = Optional.ofNullable(exchange.getRequestURI().getQuery());
        if (path.isPresent()) {
            int id = Integer.parseInt(path.get().split("=")[1]);
            httpTaskManager.deleteSubTask(id);
            httpTaskManager.save();
            writeResponse(exchange, "Задача была удалена.", 200);
        }
    }

    public static void main(String[] args) throws IOException {
        KVServer kvServer = new KVServer();
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new DateTimeAdapter())
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .create();
        kvServer.start();
        Task task = new Task("task", "description", LocalDateTime.now(), 1);
        Epic epic = new Epic("epic", "description", LocalDateTime.now().plusMinutes(3), 1);
        SubTask subTask1 = new SubTask("subTask1", "description", epic.getId(), LocalDateTime.now().plusMinutes(5), 1);
        SubTask subTask2 = new SubTask("subTask2", "description", epic.getId(), LocalDateTime.now().plusMinutes(7), 1);
        HttpTaskManager taskManagerNew = new HttpTaskManager("http://localhost:8078/");

        try {
            HttpServer httpServer = HttpServer.create();
            httpServer.bind(new InetSocketAddress(8080), 0);
            httpServer.createContext("/tasks", new HttpTaskServer());
            httpServer.createContext("/tasks/history", new HttpTaskServer());
            httpServer.createContext("/tasks/task", new HttpTaskServer());
            httpServer.createContext("/epics/epic", new HttpTaskServer());
            httpServer.start();
            System.out.println("Сервер на порту 8080 запущен");
        } catch (RuntimeException e) {
            e.printStackTrace();
            System.out.println("Сервер не запускается");
        }

        taskManagerNew.addNewTask(task);
        taskManagerNew.addNewEpicTask(epic);
        taskManagerNew.addNewSubTask(subTask1);
        taskManagerNew.addNewSubTask(subTask2);

        taskManagerNew.getTaskById(1);
        taskManagerNew.getEpicById(2);
        taskManagerNew.getSubTaskById(3);
        taskManagerNew.getSubTaskById(4);

        taskManagerNew.serverLoad();


        System.out.println(gson.toJson(task));
        System.out.println(gson.toJson(epic));
        System.out.println(gson.toJson(subTask1));
        System.out.println(gson.toJson(subTask2));
    }
}
