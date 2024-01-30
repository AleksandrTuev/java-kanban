package server;

import com.google.gson.*;
import manager.Managers;
import org.junit.jupiter.api.*;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class HttpTaskServerTest {
    private static KVServer kvServer;
    private static HttpTaskServer httpTaskServer;
    private static final Gson gson = Managers.getGson();
    private static final String TASK_BASE_URL = "http://localhost:8080/tasks/task/";
    private static final String EPIC_BASE_URL = "http://localhost:8080/tasks/epic/";
    private static final String SUBTASK_BASE_URL = "http://localhost:8080/tasks/subtask/";

    @BeforeAll
    static void startServer(){
        try {
            kvServer = new KVServer();
            kvServer.start();
            httpTaskServer = new HttpTaskServer();
            httpTaskServer.start();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    @AfterAll
    static void stopServer(){
        kvServer.stop();
        httpTaskServer.stop();
    }

    @BeforeEach
    void resetServer(){
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(TASK_BASE_URL);
        try {
            HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
            client.send(request, HttpResponse.BodyHandlers.ofString());
            url = URI.create(EPIC_BASE_URL);
            request = HttpRequest.newBuilder().uri(url).DELETE().build();
            client.send(request, HttpResponse.BodyHandlers.ofString());
            url = URI.create(SUBTASK_BASE_URL);
            request = HttpRequest.newBuilder().uri(url).DELETE().build();
            client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException exception) {
            exception.printStackTrace();
        }
    }

    @Test
    void TestGetTask() {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(TASK_BASE_URL);
        Task task1 = new Task("Task1", "Description/Task1",
                LocalDateTime.of(2024, 1, 15,21,30), 10);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(task1)))
                .build();

        try {
            client.send(request, HttpResponse.BodyHandlers.ofString());
            request = HttpRequest.newBuilder().uri(url).GET().build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());
            JsonArray array = JsonParser.parseString(response.body()).getAsJsonArray();
            assertEquals(1, array.size());
        } catch (IOException | InterruptedException exception) {
            exception.printStackTrace();
        }
    }

    @Test
    void TestGetEpic() {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(EPIC_BASE_URL);
        Epic epic2 = new Epic("Epic 2", "Description 2");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(epic2)))
                .build();

        try {
            client.send(request, HttpResponse.BodyHandlers.ofString());
            request = HttpRequest.newBuilder().uri(url).GET().build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());
            JsonArray array = JsonParser.parseString(response.body()).getAsJsonArray();
            assertEquals(1, array.size());
        } catch (IOException | InterruptedException exception) {
            exception.printStackTrace();
        }
    }

    @Test
    void TestGetSubtask() {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(EPIC_BASE_URL);
        Epic epic2 = new Epic("Epic 2", "Description 2");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(epic2)))
                .build();

        try {
            HttpResponse<String> postResponse = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(201, postResponse.statusCode(),"POST");
            if (postResponse.statusCode() == 201){
                int epicId2 = Integer.parseInt(postResponse.body());
                epic2.setId(epicId2);
                Subtask subtask21 = new Subtask("Subtask 21", "Description 21/Epic 2", epicId2,
                        LocalDateTime.of(2024, 1, 15,7,30), 60);
                url = URI.create(SUBTASK_BASE_URL);

                request = HttpRequest.newBuilder()
                        .uri(url)
                        .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(subtask21)))
                        .build();
                client.send(request, HttpResponse.BodyHandlers.ofString());
                request = HttpRequest.newBuilder().uri(url).GET().build();
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                assertEquals(200, response.statusCode());
                JsonArray array = JsonParser.parseString(response.body()).getAsJsonArray();
                assertEquals(1, array.size());
            }
        } catch (IOException | InterruptedException exception) {
            exception.printStackTrace();
        }
    }
}