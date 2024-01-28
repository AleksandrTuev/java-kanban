package server;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import manager.Managers;
import manager.TaskManager;
import server.handlers.*;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.net.InetSocketAddress;

import java.io.IOException;
import java.io.PipedOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;

public class HttpTaskServer{
//    private static final String TASK = "task";
//    private static final String SUBTASK = "subtask";
//    private static final String EPIC = "epic";

    public static final int PORT = 8080;
    private final HttpServer server;
    private final Gson gson;
    private final TaskManager taskManager;

    public HttpTaskServer() throws IOException {
        taskManager = Managers.getDefault();
        gson = Managers.getGson();
//        gson = new Gson();
        server = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
//        server.createContext("/tasks/task", new TaskHandler());
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

    public void TaskHandler(HttpExchange exchange) throws IOException {
        try {
            String method = exchange.getRequestMethod();
            //        String path = extractPath(exchange);
            String query = extractQuery(exchange);

            switch (method) {
                case "GET":
                    if (query == null) {
                        String gsonStr = gson.toJson(taskManager.getTasks()); //getTasks()
//                        System.out.println("@@@@@@@TEST@@@@@@@");//test
//                        System.out.println(gsonStr); //test
//                        String gsonStr = new Gson().toJson(taskManager.getTasks()); //getTasks()
                        sendText(exchange, 200, gsonStr); //200
                    } else {
                        int key = Integer.parseInt(query.substring(4));

                        if (taskManager.getTaskToId(key) != null) {
                            String gsonStr = gson.toJson(taskManager.getTaskToId(key));
//                            String gsonStr = new Gson().toJson(taskManager.getTaskToId(key));
                            sendText(exchange, 200, gsonStr); //200
                        } else {
                            sendTextToError(exchange, 404, "task с таким id не найден");
                        }
                        //try catch exception
                        //можно выкинуть исключение то что в запросе нет параметра id 400
                        //если формат id указан неверно 400
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
                        //                    if (task != null) { //перепроверить. Для начала пробежался по tasks и проверил id на наличие
                        if (taskManager.getTaskToId(id) != null) {//если task находится в map то update
                            taskManager.updateTask(task);
                            sendText(exchange, 201, "task c id " + id + " обновлена");
                        } else {
                            taskManager.addNewTask(task);
                            //прописать информацию если task не создана
                            if (taskManager.getTaskToId(task.getId()) != null){
                                sendText(exchange, 201, "task c id " + id + " создана");
                            } else {
//                                for (Task prioritizedTask : taskManager.getPrioritizedTasks()) {
//                                    taskManager.overlaps()
//                                }
                                sendText(exchange, 400, "task c id " + id + " не создана из-за пересечения по времени с " + findOtherTask(task));
                            }
//                            sendText(exchange, 201, "task c " + id + " создана");
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
                        //try catch exception
                        //можно выкинуть исключение то что в запросе нет параметра id 400
                        //если формат id указан неверно 400
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

    public void SubtaskHandler(HttpExchange exchange) throws IOException {
        try {
            String method = exchange.getRequestMethod();
//        String path = extractPath(exchange);
            String query = extractQuery(exchange);
            switch (method){
                case "GET":
                    if (query == null){
                        String gsonStr = gson.toJson(taskManager.getSubtasks()); //getTasks()
                        sendText(exchange, 200, gsonStr);
                    } else {
                        int key = Integer.parseInt(query.substring(4));

                        if (taskManager.getSubtaskToId(key) != null){
                            String gsonStr = gson.toJson(taskManager.getSubtaskToId(key));
                            sendText(exchange, 200, gsonStr);
                        } else {
                            sendTextToError(exchange, 404, "subtask с таким id не найден");
                        }
                        //try catch exception
                        //можно выкинуть исключение то что в запросе нет параметра id 400
                        //если формат id указан неверно 400
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
//                    if (task != null) { //перепроверить. Для начала пробежался по tasks и проверил id на наличие
                        if (taskManager.getSubtaskToId(id) != null) {//если task находится в map то update
                            taskManager.updateSubtask(task);
                            sendText(exchange, 201, "subtask c id " + id + " обновлена");
                        } else {
                            taskManager.addNewSubtask(task);
                            if (taskManager.getSubtaskToId(task.getId()) != null){
                                sendText(exchange, 201, "subtask c id " + id + " создана");
                            } else {
                                sendText(exchange, 400, "subtask c id " + id + " не создана из-за пересечения по времени с " + findOtherTask(task));
                            }
//                            sendText(exchange, 201, "subtask c " + id + " создана");
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
                        //try catch exception
                        //можно выкинуть исключение то что в запросе нет параметра id 400
                        //если формат id указан неверно 400
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

    public void EpicHandler(HttpExchange exchange) throws IOException {
        try {
            String method = exchange.getRequestMethod();
//        String path = extractPath(exchange);
            String query = extractQuery(exchange);

            switch (method){
                case "GET":
                    if (query == null){
                        String gsonStr = gson.toJson(taskManager.getEpics()); //getTasks()
                        sendText(exchange, 200, gsonStr);
                    } else {
                        int key = Integer.parseInt(query.substring(4));

                        if (taskManager.getEpicToId(key) != null){
                            String gsonStr = gson.toJson(taskManager.getEpicToId(key));
                            sendText(exchange, 200, gsonStr);
                        } else {
                            sendTextToError(exchange, 404, "epic с таким id не найден");
                        }
                        //try catch exception
                        //можно выкинуть исключение то что в запросе нет параметра id 400
                        //если формат id указан неверно 400
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
//                    if (task != null) { //перепроверить. Для начала пробежался по tasks и проверил id на наличие
                        if (taskManager.getEpicToId(id) != null) {//если task находится в map то update
                            taskManager.updateEpic(task);
                            sendText(exchange, 201, "epic c id " + id + " обновлен");
                        } else {
                            taskManager.addNewEpic(task);
                            if (taskManager.getEpicToId(task.getId()) != null){
                                sendText(exchange, 201, "subtask c id " + id + " создана");
                            } else {
                                sendText(exchange, 400, "subtask c id " + id + " не создана из-за пересечения по времени с " + findOtherTask(task));
                            }
//                            sendText(exchange, 201, "epic c " + id + " создан");
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
                        //try catch exception
                        //можно выкинуть исключение то что в запросе нет параметра id 400
                        //если формат id указан неверно 400
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

    public void HistoryHandler(HttpExchange exchange) throws IOException {
        try {
            String method = exchange.getRequestMethod();
//        String path = extractPath(exchange);
//        String query = extractQuery(exchange);

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

    public void TasksHandler(HttpExchange exchange) throws IOException {
        try {
            String method = exchange.getRequestMethod();
//        String path = extractPath(exchange);
//        String query = extractQuery(exchange);

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

    protected void sendText(HttpExchange exchange, int code, String text) throws IOException { //отправка ответа (заголовок, статус код, "тело")
        //Лучше передавать Task task и здесть проверять на null -> exchange.sendResponseHeaders(404, 0);
        byte[] response = text.getBytes(UTF_8);
        exchange.getResponseHeaders().add("Content-Type", "application/json"); //для теста убрано
        exchange.sendResponseHeaders(code, response.length);
        exchange.getResponseBody().write(response);
    }

    protected void sendTextToError(HttpExchange exchange, int code, String text) throws IOException { //отправка ответа (заголовок, статус код, "тело")
        //Лучше передавать Task task и здесть проверять на null -> exchange.sendResponseHeaders(404, 0);
        byte[] response = text.getBytes(UTF_8);
        exchange.sendResponseHeaders(code, response.length);
        exchange.getResponseBody().write(response);
    }

    protected String readText(HttpExchange exchange) throws IOException{
        return new String(exchange.getRequestBody().readAllBytes(), UTF_8);
    }

    protected String extractPath(HttpExchange exchange){ //возвращает путь
        return exchange.getRequestURI().getPath();
    }

    protected String extractQuery(HttpExchange exchange){ //возвращает параметры
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
}
