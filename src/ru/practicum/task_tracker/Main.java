import com.google.gson.Gson;
import enums.Status;
import manager.Managers;
import manager.TaskManager;
import server.HttpTaskServer;
import server.KVServer;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) throws IOException {
//        KVServer kvServer = Managers.getDefaultKVServer();
//        HttpTaskServer httpTaskServer = new HttpTaskServer();
//        httpTaskServer.start();
        Gson gson = Managers.getGson();
        TaskManager taskManager = Managers.getDefault();

        //id=1
        Task task1 = new Task("Task 1", "Description/Task 1",
                LocalDateTime.of(2024, 1, 15,16,30), 10);
        int taskId1 = taskManager.addNewTask(task1);
        //id=2
        Task task2 = new Task("Task 2", "Description/Task 2",
                LocalDateTime.of(2024, 1, 16,18,0), 15);
        int taskId2 = taskManager.addNewTask(task2);
        //id=3
        Epic epic1 = new Epic("Epic 1", "Description 1");
        int epicId1 = taskManager.addNewEpic(epic1);
        //id=4
        Subtask subtask11 = new Subtask("Subtask 11", "Description 11/Epic 1", epicId1,
                LocalDateTime.of(2024, 1, 16,10,30), 15);
        int subtaskId11 = taskManager.addNewSubtask(subtask11);
        //id=5
        Subtask subtask12 = new Subtask("Subtask 12", "Description 12/Epic 1", epicId1,
                LocalDateTime.of(2024, 1, 16,10,50), 15);
        int subtaskId12 = taskManager.addNewSubtask(subtask12);

        System.out.println(gson.toJson(taskManager.getTaskToId(1))); //test

        System.out.println("---Печать задач отсортированных по приоритету---");
        System.out.println(taskManager.getPrioritizedTasks());
        System.out.println(gson.toJson(taskManager.getPrioritizedTasks()));
        System.out.println("------------------------------------------------");
        taskManager.getEpicToId(epicId1);
        taskManager.getTaskToId(taskId2);
        taskManager.getSubtaskToId(subtaskId12);
        taskManager.getTaskToId(taskId1);
        taskManager.getSubtaskToId(subtaskId12);
        System.out.println("-------------------Печать tasks-----------------");
        System.out.println(gson.toJson(taskManager.getTasks()));
        System.out.println("--------------------Печать epics----------------");
        System.out.println(gson.toJson(taskManager.getEpics()));
        System.out.println("------------------Печать subtasks---------------");
        System.out.println(gson.toJson(taskManager.getSubtasks()));
        System.out.println("------------------------------------------------");

        System.out.println("-----------------История просмотра--------------");
        System.out.println(taskManager.getHistory());
        System.out.println("------------------------------------------------");
//
//        System.out.println("---------");
//        System.out.println("Проверка Set");
//        System.out.println(taskManager.getPrioritizedTasks());
//
//        System.out.println("---------");
//        System.out.println("Добавляем Task 4 [LocalDateTime = null]");
//        //id=8
//        Task task4 = new Task("Task 4", "Description/Task 4",
//                null, 0);
//        taskManager.addNewTask(task4);
//        System.out.println(task4);
//
//        System.out.println("---------");
//        System.out.println("Проверка Set");
//        System.out.println(taskManager.getPrioritizedTasks());
//
//        System.out.println("---------");
//        System.out.println("Параметры Epic1:");
//        System.out.println("Время начала: " + epic1.getStartTime());
//        System.out.println("Длительность в минутах: " + epic1.getDuration());
//        System.out.println("Время окончания: " + epic1.getEndTime());
//        System.out.println("Заход в тест1");
//        try {
//            System.out.println("Заход в тест2");
//            HttpClient client = HttpClient.newHttpClient();
//            URI url = URI.create("http://localhost:8080/tasks/task/");
//            HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
//            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
//            System.out.println(response.body());
////            String a = response.body();
//        } catch (IOException | InterruptedException exception) {
//            exception.printStackTrace();
//        }

    }
}
