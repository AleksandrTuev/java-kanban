package manager;

import com.google.gson.Gson;
import org.junit.jupiter.api.*;
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

class HttpTaskManagerTest extends TaskManagerTest<HttpTaskManager> {
    private KVServer kvServer;
    private HttpTaskServer httpTaskServer;
    private Gson gson;

    @BeforeEach
    public void setUp() throws IOException {
        kvServer = Managers.getDefaultKVServer();
        taskManager = new HttpTaskManager();
        httpTaskServer = new HttpTaskServer(taskManager);
        httpTaskServer.start();
        gson = Managers.getGson();
    }

    @AfterEach
    public void stopServer(){
        httpTaskServer.stop();
        kvServer.stop();
    }

    @Test
    public void testLoadTasks(){
        TaskManager newTaskManager = Managers.getDefault();
        Assertions.assertEquals(taskManager.getTasks(), newTaskManager.getTasks());
    }

    @Test
    void testLoadSubtasks(){
        TaskManager newTaskManager = Managers.getDefault();
        Assertions.assertEquals(taskManager.getSubtasks(), newTaskManager.getSubtasks());
    }

    @Test
    void testLoadEpics(){
        TaskManager newTaskManager = Managers.getDefault();
        Assertions.assertEquals(taskManager.getEpics(), newTaskManager.getEpics());
    }

    @Test
    void testLoadHistory(){
        TaskManager newTaskManager = Managers.getDefault();
        Assertions.assertEquals(taskManager.getHistory(), newTaskManager.getHistory());
    }

    @Test
    void testSaveTask(){
        Task task1 = new Task("Task1", "Description/Task1",
                LocalDateTime.of(2024, 1, 15,21,30), 10);
        int taskId1 = taskManager.addNewTask(task1);
        Assertions.assertEquals(task1, taskManager.getTaskToId(taskId1));
    }

    @Test
    void testSaveEpicAndSubtask(){
        Epic epic1 = new Epic("Epic 1", "Description 1");
        int epicId1 = taskManager.addNewEpic(epic1);
        Subtask subtask11 = new Subtask("Subtask 11", "Description 11/Epic 1", epicId1,
                LocalDateTime.of(2024, 1, 16,10,30), 15);
        int subtaskId1 = taskManager.addNewSubtask(subtask11);
        Assertions.assertEquals(epic1, taskManager.getEpicToId(epicId1));
        Assertions.assertEquals(subtask11, taskManager.getSubtaskToId(subtaskId1));
    }

    @Test
    void testGetTask() throws IOException, InterruptedException {
        Task task1 = new Task("Task1", "Description/Task1",
                LocalDateTime.of(2024, 1, 15, 21, 30), 10);
        taskManager.addNewTask(task1);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println(response.body());
        Assertions.assertEquals(200, response.statusCode());
    }

    @Test
    void testGetTaskToId() throws IOException, InterruptedException {
        Task task1 = new Task("Task1", "Description/Task1",
                LocalDateTime.of(2024, 1, 15, 21, 30), 10);
        int taskId1 = taskManager.addNewTask(task1);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task?id=1");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println(response.body());
        Assertions.assertEquals(200, response.statusCode());
        Assertions.assertEquals(taskId1, (gson.fromJson(response.body(), Task.class)).getId());
    }

    @Test
    void testGetEpic() throws IOException, InterruptedException {
        Epic epic1 = new Epic("Epic 1", "Description 1");
        taskManager.addNewEpic(epic1);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/epic");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println(response.body());
        Assertions.assertEquals(200, response.statusCode());
    }

    @Test
    void testGetEpicToId() throws IOException, InterruptedException {
        Epic epic1 = new Epic("Epic 1", "Description 1");
        int epicId1 = taskManager.addNewEpic(epic1);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/epic?id=1");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println(response.body());
        Assertions.assertEquals(200, response.statusCode());
        Assertions.assertEquals(epicId1, (gson.fromJson(response.body(), Epic.class)).getId());
    }

    @Test
    void testGetSubtask() throws IOException, InterruptedException {
        Epic epic1 = new Epic("Epic 1", "Description 1");
        int epicId1 = taskManager.addNewEpic(epic1);
        Subtask subtask11 = new Subtask("Subtask 11", "Description 11/Epic 1", epicId1,
                LocalDateTime.of(2024, 1, 16,10,30), 15);
        taskManager.addNewSubtask(subtask11);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/subtask");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println(response.body());
        Assertions.assertEquals(200, response.statusCode());
    }

    @Test
    void testGetSubtaskToId() throws IOException, InterruptedException {
        Epic epic1 = new Epic("Epic 1", "Description 1");
        int epicId1 = taskManager.addNewEpic(epic1);
        Subtask subtask11 = new Subtask("Subtask 11", "Description 11/Epic 1", epicId1,
                LocalDateTime.of(2024, 1, 16,10,30), 15);
        int subtaskId11 = taskManager.addNewSubtask(subtask11);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/subtask?id=2");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println(response.body());
        Assertions.assertEquals(200, response.statusCode());
        Assertions.assertEquals(subtaskId11, (gson.fromJson(response.body(), Subtask.class)).getId());
    }
}