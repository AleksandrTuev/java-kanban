package manager;

import com.google.gson.Gson;
import org.junit.jupiter.api.*;
import server.HttpTaskManager;
import server.HttpTaskServer;
import server.KVServer;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.List;

class HttpTaskManagerTest extends TaskManagerTest<HttpTaskManager> {
    private KVServer kvServer;
    private TaskManager taskManager;
    private HttpTaskServer httpTaskServer;

    @BeforeEach
    public void createManager() throws IOException {
        kvServer = Managers.getDefaultKVServer();
        taskManager = new HttpTaskManager();
        httpTaskServer = new HttpTaskServer();
        httpTaskServer.start();
    }

    @AfterEach
    public void stopServer(){
        httpTaskServer.stop();
        kvServer.stop();
    }

    @Test
    public void testLoadTasks(){
        Task task1 = new Task("Task 1", "Description/Task 1",
                LocalDateTime.of(2024, 1, 15,16,30), 10);
        int taskId1 = taskManager.addNewTask(task1);
        Task task2 = new Task("Task 2", "Description/Task 2",
                LocalDateTime.of(2024, 1, 16,18,0), 15);
        int taskId2 = taskManager.addNewTask(task2);
        taskManager.getTaskToId(taskId1);
        taskManager.getTaskToId(taskId2);
        List<Task> history = taskManager.getHistory();
        Assertions.assertEquals(taskManager.getTasks().size(),history.size());
    }

    @Test
    public void testLoadEpicsAndSubtasks(){
        Epic epic1 = new Epic("Epic1", "Description/Epic1");
        int epicId1 = taskManager.addNewEpic(epic1);
        Epic epic2 = new Epic("Epic2", "Description/Epic2");
        taskManager.addNewEpic(epic2);
        Assertions.assertTrue(epic1.getSubtaskIds().isEmpty());
        Assertions.assertTrue(epic2.getSubtaskIds().isEmpty());

        Subtask subtask11 = new Subtask("Subtask11", "Description11/Epic1", epicId1,
                LocalDateTime.of(2024, 1, 16,10,30), 15);
        int subtaskId11 = taskManager.addNewSubtask(subtask11);
        Assertions.assertFalse(epic1.getSubtaskIds().isEmpty());
        Assertions.assertEquals(subtask11, taskManager.getSubtaskToId(subtaskId11));
        Assertions.assertEquals(taskManager.getEpicToId(epicId1).getStartTime(), taskManager.getSubtaskToId(subtaskId11).getStartTime());

        Subtask subtask12 = new Subtask("Subtask12", "Description12/Epic1", epicId1,
                LocalDateTime.of(2024, 1, 16,10,50), 15);
        int subtaskId12 = taskManager.addNewSubtask(subtask12);
        Assertions.assertEquals(2, taskManager.getEpicToId(epicId1).getSubtaskIds().size());
        Assertions.assertEquals(taskManager.getEpicToId(epicId1).getEndTime(), taskManager.getSubtaskToId(subtaskId12).getEndTime());
    }

    @Test
    void testGetTasks() throws IOException, InterruptedException {
        Task task1 = new Task("Task 1", "Description/Task 1",
                LocalDateTime.of(2024, 1, 15, 16, 30), 10);
        int taskId1 = taskManager.addNewTask(task1);

        System.out.println(taskManager.getTaskToId(taskId1));
        String json = new Gson().toJson(taskManager.getTaskToId(taskId1));
        System.out.println(json);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
        Assertions.assertEquals(200, response.statusCode());
    }

    @Test
    void testGetEpics() throws IOException, InterruptedException {
        Epic epic1 = new Epic("Epic1", "Description/Epic1");
        int epicId1 = taskManager.addNewEpic(epic1);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/epic");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(200, response.statusCode());
    }

    @Test
    void testGetSubtasks() throws IOException, InterruptedException {
        Epic epic1 = new Epic("Epic1", "Description/Epic1");
        int epicId1 = taskManager.addNewEpic(epic1);
        Subtask subtask11 = new Subtask("Subtask11", "Description11/Epic1", epicId1,
                LocalDateTime.of(2024, 1, 16,10,30), 15);
        int subtaskId11 = taskManager.addNewSubtask(subtask11);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/subtask");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(200, response.statusCode());
    }

    @Test
    void testGetTaskToId() throws IOException, InterruptedException {
        Task task1 = new Task("Task 1", "Description/Task 1",
                LocalDateTime.of(2024, 1, 15,16,30), 10);
        int taskId1 = taskManager.addNewTask(task1);
        Task task2 = new Task("Task 2", "Description/Task 2",
                LocalDateTime.of(2024, 1, 16,18,0), 15);
        int taskId2 = taskManager.addNewTask(task2);

//        System.out.println(taskManager.getTasks());

        System.out.println(taskManager.getTaskToId(1));
        System.out.println(taskManager.getTaskToId(2));
        System.out.println(taskManager.getTaskToId(3));

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task?id=1");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(200, response.statusCode());
        //Семён привет!
        //Вроде основной код реализовал. Запускал KVServer (через KVServerStarted) и HTTPTaskServer (через HTTPTaskServerStarted).
        //Код отрабатывает. Проверял через Insomnia.
        //Но тесты заваливаются. Не знаю куда копать.
        //
        //на 140 строке response.statusCode приходит верно (проверял, изменял), но вот тело response.body приходит пустое
        //Не знаю куда смотреть
    }
}