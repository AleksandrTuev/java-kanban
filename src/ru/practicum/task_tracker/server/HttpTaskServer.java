package server;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import manager.Managers;
import manager.TaskManager;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.net.InetSocketAddress;

import java.io.IOException;

import static java.nio.charset.StandardCharsets.UTF_8;

public class HttpTaskServer{
    public static final int PORT = 8080;
    private final HttpServer server;
    private final Gson gson;
    private final TaskManager taskManager;

    public HttpTaskServer() throws IOException {
        this(Managers.getDefault());
    }

    public HttpTaskServer(TaskManager taskManager) throws IOException {
        this.taskManager = taskManager;
        gson = Managers.getGson();
        server = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
        server.createContext("/tasks/task", this::TaskHandler);
        server.createContext("/tasks/subtask", this::SubtaskHandler);
        server.createContext("/tasks/epic", this::EpicHandler);
        server.createContext("/tasks/history", this::HistoryHandler);
        server.createContext("/tasks", this::TasksHandler);
    }

    public void start() {
        System.out.println("Запускаем HttpTaskServer сервер на порту " + PORT);
        System.out.println("Открой в браузере http://localhost:" + PORT + "/");
        server.start();
    }

    public void stop() {
        server.stop(1);
    }

    public void TaskHandler(HttpExchange exchange) {
        try {
            String method = exchange.getRequestMethod();
            String query = extractQuery(exchange);

            switch (method) {
                case "GET":
                    if (query == null) {
                        String gsonStr = gson.toJson(taskManager.getTasks());
                        sendText(exchange, 200, gsonStr);
                    } else {
                        //проверка формата переданного в параметрах id
                        if (!checkingThePassedParameter(exchange, query.substring(3))){
                            return;
                        }

                        int key = Integer.parseInt(query.substring(3));

                        if (taskManager.getTaskToId(key) != null) {
                            String gsonStr = gson.toJson(taskManager.getTaskToId(key));
                            sendText(exchange, 200, gsonStr);
                        } else {
                            sendTextToError(exchange, 404, "task с таким id не найден");
                        }
                    }
                    break;
                case "POST":
                    String bodyRequest = readText(exchange);
                    if (bodyRequest.isEmpty()) {
                        sendTextToError(exchange, 400, "информация не передана");
                        return;
                    }
                    try {
                        Task task = gson.fromJson(bodyRequest, Task.class);
                        Integer id = task.getId();
                        if (taskManager.getTaskToId(id) != null) {//если task находится в map то update
                            taskManager.updateTask(task);
                            sendText(exchange, 201, "task c id " + id + " обновлена");
                        } else {
                            taskManager.addNewTask(task);
                            //прописать информацию если task не создана
                            if (taskManager.getTaskToId(task.getId()) != null){
                                sendText(exchange, 201, "task c id " + id + " создана");
                            } else {
                                sendTextToError(exchange, 400, "task c id " + id
                                        + " не создана из-за пересечения по времени с " + findOtherTask(task));
                            }
                        }
                    } catch (JsonSyntaxException exception) {
                        sendTextToError(exchange, 400, "Неверный формат json");
                    }
                    break;
                case "DELETE":
                    if (query == null) {
                        taskManager.deleteAllTasks();
                        sendText(exchange, 200, "Все tasks удалены");
                    } else {
                        int key = Integer.parseInt(query.substring(4));
                        taskManager.removeTask(key);
                        sendText(exchange, 200, "task c id " + key + " удалена");
                    }
                    break;
                default:
                    sendText(exchange, 400, "http-метод не определён");
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        } finally {
            exchange.close();
        }
    }

    public void SubtaskHandler(HttpExchange exchange) {
        try {
            String method = exchange.getRequestMethod();
            String query = extractQuery(exchange);
            switch (method){
                case "GET":
                    if (query == null){
                        String gsonStr = gson.toJson(taskManager.getSubtasks());
                        sendText(exchange, 200, gsonStr);
                    } else {
                        //проверка формата переданного в параметрах id
                        if (!checkingThePassedParameter(exchange, query.substring(3))){
                            return;
                        }

                        int key = Integer.parseInt(query.substring(3));

                        if (taskManager.getSubtaskToId(key) != null){
                            String gsonStr = gson.toJson(taskManager.getSubtaskToId(key));
                            sendText(exchange, 200, gsonStr);
                        } else {
                            sendTextToError(exchange, 404, "subtask с таким id не найден");
                        }
                    }
                    break;
                case "POST":
                    String bodyRequest = readText(exchange);
                    if (bodyRequest.isEmpty()){
                        sendTextToError(exchange,400, "информация не передана");
                        return;
                    }
                    try {
                        Subtask task = gson.fromJson(bodyRequest, Subtask.class);
                        Integer id = task.getId();
                        if (taskManager.getSubtaskToId(id) != null) {//если task находится в map то update
                            taskManager.updateSubtask(task);
                            sendText(exchange, 201, "subtask c id " + id + " обновлена");
                        } else {
                            taskManager.addNewSubtask(task);
                            if (taskManager.getSubtaskToId(task.getId()) != null){
                                sendText(exchange, 201, "subtask c id " + id + " создана");
                            } else {
                                sendTextToError(exchange, 400, "subtask c id " + id
                                        + " не создана из-за пересечения по времени с " + findOtherTask(task));
                            }
                        }
                    } catch (JsonSyntaxException exception) {
                        sendTextToError(exchange, 400, "Неверный формат json");
                    }
                    break;
                case "DELETE":
                    if (query == null){
                        taskManager.deleteAllSubtasks();
                        sendText(exchange, 200, "Все subtasks удалены");
                    } else {
                        int key = Integer.parseInt(query.substring(4));
                        taskManager.removeSubtask(key);
                        sendText(exchange, 200, "subtask c id " + key + " удалена");
                    }
                    break;
                default:
                    sendText(exchange, 400, "http-метод не определён");
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        } finally {
            exchange.close();
        }
    }

    public void EpicHandler(HttpExchange exchange) {
        try {
            String method = exchange.getRequestMethod();
            String query = extractQuery(exchange);

            switch (method){
                case "GET":
                    if (query == null){
                        String gsonStr = gson.toJson(taskManager.getEpics());
                        sendText(exchange, 200, gsonStr);
                    } else {
                        //проверка формата переданного в параметрах id
                        if (!checkingThePassedParameter(exchange, query.substring(3))){
                            return;
                        }

                        int key = Integer.parseInt(query.substring(3));

                        if (taskManager.getEpicToId(key) != null){
                            String gsonStr = gson.toJson(taskManager.getEpicToId(key));
                            sendText(exchange, 200, gsonStr);
                        } else {
                            sendTextToError(exchange, 404, "epic с таким id не найден");
                        }
                    }
                    break;
                case "POST":
                    String bodyRequest = readText(exchange);
                    if (bodyRequest.isEmpty()){
                        sendTextToError(exchange,400, "информация не передана");
                        return;
                    }
                    try {
                        Epic task = gson.fromJson(bodyRequest, Epic.class);
                        Integer id = task.getId();
                        if (taskManager.getEpicToId(id) != null) {//если task находится в map то update
                            taskManager.updateEpic(task);
                            sendText(exchange, 201, "epic c id " + id + " обновлен");
                        } else {
                            taskManager.addNewEpic(task);
                            if (taskManager.getEpicToId(task.getId()) != null){
                                sendText(exchange, 201, "subtask c id " + id + " создана");
                            } else {
                                sendTextToError(exchange, 400, "subtask c id " + id
                                        + " не создана из-за пересечения по времени с " + findOtherTask(task));
                            }
                        }
                    } catch (JsonSyntaxException exception) {
                        sendTextToError(exchange, 400, "Неверный формат json");
                    }
                    break;
                case "DELETE":
                    if (query == null){
                        taskManager.deleteAllEpics();
                        sendText(exchange, 200, "Все epics удалены");
                    } else {
                        int key = Integer.parseInt(query.substring(4));
                        taskManager.removeEpic(key);
                        sendText(exchange, 200, "epic c id " + key + " удален");
                    }
                    break;
                default:
                    sendText(exchange, 400, "http-метод не определён");
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        } finally {
            exchange.close();
        }
    }

    public void HistoryHandler(HttpExchange exchange) {
        try {
            String method = exchange.getRequestMethod();

            switch (method){
                case "GET":
                    String gsonStr = gson.toJson(taskManager.getHistory()); //getTasks()
                    sendText(exchange, 200, gsonStr);
                    break;
                default:
                    sendText(exchange, 400, "http-метод не определён");
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        } finally {
            exchange.close();
        }
    }

    public void TasksHandler(HttpExchange exchange) {
        try {
            String method = exchange.getRequestMethod();
            switch (method){
                case "GET":
                    String gsonStr = gson.toJson(taskManager.getPrioritizedTasks()); //getTasks()
                    sendText(exchange, 200, gsonStr);
                    break;
                default:
                    sendText(exchange, 400, "http-метод не определён");
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        } finally {
            exchange.close();
        }
    }

    protected void sendText(HttpExchange exchange, int code, String text) throws IOException {
        byte[] response = text.getBytes(UTF_8);
        exchange.getResponseHeaders().add("Content-Type", "application/json"); //для теста убрано
        exchange.sendResponseHeaders(code, response.length);
        exchange.getResponseBody().write(response);
        exchange.close();
    }

    protected void sendTextToError(HttpExchange exchange, int code, String text) throws IOException {
        byte[] response = text.getBytes(UTF_8);
        exchange.sendResponseHeaders(code, response.length);
        exchange.getResponseBody().write(response);
        exchange.close();
    }

    protected String readText(HttpExchange exchange) throws IOException{
        return new String(exchange.getRequestBody().readAllBytes(), UTF_8);
    }

    protected String extractPath(HttpExchange exchange){ //возвращает путь
        return exchange.getRequestURI().getPath();
    }

    protected String extractQuery(HttpExchange exchange){
        return exchange.getRequestURI().getQuery();
    }

    private String findOtherTask(Task task) {
        String text = null;
        for (Task other : taskManager.getPrioritizedTasks()) {
            if (taskManager.overlaps(task, other)){
                if (task.getId() != other.getId()){
                    text = other.getName()+ " c id=" + other.getId() + ".";
                }
            }
        }
        return text;
    }

    private boolean checkingThePassedParameter (HttpExchange exchange, String text) throws IOException {
        try {
            Integer.parseInt(text);
            return true;
        } catch (NumberFormatException exception) {
            sendTextToError(exchange, 400, "формат id указан неверно");
            return false;
        }
    }
}