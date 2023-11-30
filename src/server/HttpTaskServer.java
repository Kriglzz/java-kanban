package server;

import client.KVServer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import manager.Managers;
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
import java.time.LocalDateTime;
import java.util.Optional;


public class HttpTaskServer implements HttpHandler {
    private final static Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private final static Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new DateTimeAdapter())
            .create();
    private final HttpTaskManager httpTaskManager = new HttpTaskManager("http://localhost:8080/");
    protected static TaskManager taskManager = Managers.getDefault();

    private static void writeResponse(HttpExchange exchange, String responseString, int responseCode) throws IOException {
        if (responseString.isBlank()) {
            exchange.sendResponseHeaders(responseCode, 0);
        } else {
            byte[] bytes = responseString.getBytes(DEFAULT_CHARSET);
            exchange.sendResponseHeaders(responseCode, bytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(bytes);
            }
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
            case ADD_TASK: {
                addTask(httpExchange);
            }
            case GET_TASK_BY_ID: {
                getTask(httpExchange);
            }
            case UPDATE_TASK: {
                updateTask(httpExchange);
            }
            case DELETE_TASK_BY_ID: {
                deleteTask(httpExchange);
            }
            case DELETE_ALL_TASKS: {
                deleteAllTasks(httpExchange);
            }
            //EPIC
            case GET_ALL_EPICS: {
                getAllEpics(httpExchange);
            }
            case ADD_EPIC: {
                addEpic(httpExchange);
            }
            case GET_EPIC_BY_ID: {
                getEpic(httpExchange);
            }
            case UPDATE_EPIC: {
                updateEpic(httpExchange);
            }
            case DELETE_EPIC_BY_ID: {
                deleteEpic(httpExchange);
            }
            case DELETE_ALL_EPICS: {
                deleteAllEpics(httpExchange);
            }
            //SUBTASK
            case GET_ALL_SUBTASKS: {
                getAllSubTasks(httpExchange);
            }
            case ADD_SUBTASK: {
                addSubTask(httpExchange);
            }
            case GET_SUBTASK_BY_ID: {
                getSubTask(httpExchange);
            }
            case UPDATE_SUBTASK: {
                updateSubTask(httpExchange);
            }
            case DELETE_SUBTASK_BY_ID: {
                deleteSubTask(httpExchange);
            }
            case DELETE_ALL_SUBTASKS: {
                deleteAllSubTasks(httpExchange);
            }
/*            case GET_PRIORITIZED_TASKS: {
                getPrioritizedTask(httpExchange);
            }*/
            default:
                writeResponse(httpExchange, "Ошибка. Выбранный endpoint не реализован", 404);
        }
    }

    private Endpoint getEndpoint(String requestPath, String requestMethod, HttpExchange httpExchange) {
        String[] path = requestPath.split("/");
        switch (requestMethod) {
            case "GET":
                switch (path[1]) {
                    case "tasks":  //GET/tasks
                        if (path[2].equals("task")) {//GET/tasks/task
                            return Endpoint.GET_TASK_BY_ID;
                        } else if (path[2].isEmpty()) { //GET/tasks
                            return Endpoint.GET_ALL_TASKS;
                        } else if (path[2].equals("history")) { //GET/tasks/history
                            return Endpoint.GET_HISTORY;
                        }

                    case "epics":  //GET/tasks
                        if (path[2].equals("epic")) {//GET/epics/epic
                            return Endpoint.GET_EPIC_BY_ID;
                        } else if (path[2].isEmpty()) { //GET/epics
                            return Endpoint.GET_ALL_EPICS;
                        } else if (path[2].equals("history")) { //GET/epics/history
                            return Endpoint.GET_HISTORY;
                        }

                    case "subtasks":  //GET/subtasks
                        if (path[2].equals("epic")) {//GET/subtasks/subtask
                            return Endpoint.GET_SUBTASK_BY_ID;
                        } else if (path[2].isEmpty()) { //GET/epics
                            return Endpoint.GET_ALL_SUBTASKS;
                        } else if (path[2].equals("history")) { //GET/subtasks/history
                            return Endpoint.GET_HISTORY;
                        }

                }
                break;
            case "DELETE":
                switch (path[1]) {
                    case "tasks":  //DELETE/tasks
                        if (path[2].equals("task")) {//DELETE/tasks/task
                            return Endpoint.DELETE_TASK_BY_ID;
                        } else if (path[2].isEmpty()) { //DELETE/tasks
                            return Endpoint.DELETE_ALL_TASKS;
                        }

                    case "epics":  //DELETE/tasks
                        if (path[2].equals("epic")) {//DELETE/epics/epic
                            return Endpoint.DELETE_EPIC_BY_ID;
                        } else if (path[2].isEmpty()) { //DELETE/epics
                            return Endpoint.DELETE_ALL_EPICS;
                        }

                    case "subtasks":  //DELETE/subtasks
                        if (path[2].equals("subtask")) {//DELETE/subtasks/subtask
                            return Endpoint.DELETE_SUBTASK_BY_ID;
                        } else if (path[2].isEmpty()) { //DELETE/subtasks
                            return Endpoint.DELETE_ALL_SUBTASKS;
                        }

                }
                break;
            case "POST":
                switch (path[1]) {
                    case "tasks":  //POST/tasks
                        if (path[2].equals("task")) {//POST/tasks/task
                            return Endpoint.UPDATE_TASK;
                        }

                    case "epics":  //POST/tasks
                        if (path[2].equals("epic")) {//POST/epics/epic
                            return Endpoint.UPDATE_EPIC;
                        }

                    case "subtasks":  //POST/subtasks
                        if (path[2].equals("subtask")) {//POST/subtasks/subtask
                            return Endpoint.UPDATE_SUBTASK;
                        }

                }
            default:
                return Endpoint.UNKNOWN;
        }
        return Endpoint.UNKNOWN;
    }

    // TASK
    private void getAllTasks(HttpExchange exchange) throws IOException {
        httpTaskManager.serverLoad();
        writeResponse(exchange, gson.toJson(taskManager.getAllTask()), 200);
    }

    private void addTask(HttpExchange exchange) throws IOException {
        httpTaskManager.serverLoad();
        InputStream inputStream = exchange.getRequestBody();
        String body = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
        Task task = gson.fromJson(body, Task.class);
        taskManager.addNewTask(task);
        writeResponse(exchange, "Задача " + task.getName() + " добавлена!", 200);
    }

    private void getTask(HttpExchange exchange) throws IOException {
        httpTaskManager.serverLoad();
        Optional<String> path = Optional.ofNullable(exchange.getRequestURI().getQuery());
        if (path.isPresent()) {
            int id = Integer.parseInt(path.get().split("=")[1]);
            writeResponse(exchange, gson.toJson(taskManager.getTaskById(id)), 200);
        }
    }

    private void deleteTask(HttpExchange exchange) throws IOException {
        httpTaskManager.serverLoad();
        Optional<String> path = Optional.ofNullable(exchange.getRequestURI().getQuery());
        if (path.isPresent()) {
            int id = Integer.parseInt(path.get().split("=")[1]);
            taskManager.deleteTask(id);
            writeResponse(exchange, "Задача была удалена.", 200);
        }
    }

    private void deleteAllTasks(HttpExchange exchange) throws IOException {
        httpTaskManager.serverLoad();
        taskManager.deleteAllTask();
        writeResponse(exchange, "Все задачи были удалены.", 200);
    }

    private void updateTask(HttpExchange exchange) throws IOException {
        httpTaskManager.serverLoad();
        InputStream inputStream = exchange.getRequestBody();
        String body = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
        Task task = gson.fromJson(body, Task.class);
        taskManager.updateTask(task);
        writeResponse(exchange, "Задача " + task.getName() + " обновлена!", 200);
    }

    //epic
    private void getAllEpics(HttpExchange exchange) throws IOException {
        httpTaskManager.serverLoad();
        writeResponse(exchange, gson.toJson(taskManager.getAllEpic()), 200);
    }

    private void addEpic(HttpExchange exchange) throws IOException {
        httpTaskManager.serverLoad();
        InputStream inputStream = exchange.getRequestBody();
        String body = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
        Epic epic = gson.fromJson(body, Epic.class);
        taskManager.addNewTask(epic);
        writeResponse(exchange, "Задача " + epic.getName() + " добавлена!", 200);
    }

    private void getEpic(HttpExchange exchange) throws IOException {
        httpTaskManager.serverLoad();
        Optional<String> path = Optional.ofNullable(exchange.getRequestURI().getQuery());
        if (path.isPresent()) {
            int id = Integer.parseInt(path.get().split("=")[1]);
            writeResponse(exchange, gson.toJson(taskManager.getEpicById(id)), 200);
        }
    }

    private void deleteEpic(HttpExchange exchange) throws IOException {
        httpTaskManager.serverLoad();
        Optional<String> path = Optional.ofNullable(exchange.getRequestURI().getQuery());
        if (path.isPresent()) {
            int id = Integer.parseInt(path.get().split("=")[1]);
            taskManager.deleteEpic(id);
            writeResponse(exchange, "Задача была удалена.", 200);
        }
    }

    private void deleteAllEpics(HttpExchange exchange) throws IOException {
        httpTaskManager.serverLoad();
        taskManager.deleteAllTask();
        writeResponse(exchange, "Все задачи были удалены.", 200);
    }

    private void updateEpic(HttpExchange exchange) throws IOException {
        httpTaskManager.serverLoad();
        InputStream inputStream = exchange.getRequestBody();
        String body = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
        Epic epic = gson.fromJson(body, Epic.class);
        taskManager.updateTask(epic);
        writeResponse(exchange, "Задача " + epic.getName() + " обновлена!", 200);
    }

    //SUBTASK
    private void getAllSubTasks(HttpExchange exchange) throws IOException {
        httpTaskManager.serverLoad();
        writeResponse(exchange, gson.toJson(taskManager.getAllTask()), 200);
    }

    private void addSubTask(HttpExchange exchange) throws IOException {
        httpTaskManager.serverLoad();
        InputStream inputStream = exchange.getRequestBody();
        String body = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
        SubTask subTask = gson.fromJson(body, SubTask.class);
        taskManager.addNewTask(subTask);
        writeResponse(exchange, "Задача " + subTask.getName() + " добавлена!", 200);
    }

    private void getSubTask(HttpExchange exchange) throws IOException {
        httpTaskManager.serverLoad();
        Optional<String> path = Optional.ofNullable(exchange.getRequestURI().getQuery());
        if (path.isPresent()) {
            int id = Integer.parseInt(path.get().split("=")[1]);
            writeResponse(exchange, gson.toJson(taskManager.getSubTaskById(id)), 200);
        }
    }

    private void deleteSubTask(HttpExchange exchange) throws IOException {
        httpTaskManager.serverLoad();
        Optional<String> path = Optional.ofNullable(exchange.getRequestURI().getQuery());
        if (path.isPresent()) {
            int id = Integer.parseInt(path.get().split("=")[1]);
            taskManager.deleteSubTask(id);
            writeResponse(exchange, "Задача была удалена.", 200);
        }
    }

    private void deleteAllSubTasks(HttpExchange exchange) throws IOException {
        taskManager.deleteAllTask();
        writeResponse(exchange, "Все задачи были удалены.", 200);
    }

    private void updateSubTask(HttpExchange exchange) throws IOException {
        httpTaskManager.serverLoad();
        InputStream inputStream = exchange.getRequestBody();
        String body = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
        SubTask subTask = gson.fromJson(body, SubTask.class);
        taskManager.updateTask(subTask);
        writeResponse(exchange, "Задача " + subTask.getName() + " обновлена!", 200);
    }

    /*    private void getPrioritizedTask(HttpExchange httpExchange) throws IOException {
            httpTaskManager.serverLoad();
            String json = gson.toJson(taskManager.getPrioritizedTasks());
                writeResponse(httpExchange, json, 200);
        }*/
    public static void main(String[] args) throws IOException {
        try {
            KVServer kvServer = new KVServer();
            kvServer.start();
            System.out.println("KVServer запущен");
            HttpServer server = HttpServer.create(new InetSocketAddress(8081), 0);

            server.createContext("/GET/tasks/task", new HttpTaskServer());
            server.createContext("/subtasks/subtask", new HttpTaskServer());
            server.createContext("/epics/epic", new HttpTaskServer());

            server.start();
            System.out.println("Основной? сервер запущен");

        } catch (RuntimeException e) {
            e.printStackTrace();
            System.out.println("Ошибка запуска сервера.");
        }
    }
}
