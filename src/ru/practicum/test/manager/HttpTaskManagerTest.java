package manager;

import org.junit.jupiter.api.*;
import server.HttpTaskServer;
import server.KVServer;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.IOException;
import java.time.LocalDateTime;

class HttpTaskManagerTest extends TaskManagerTest<HttpTaskManager> {
    private KVServer kvServer;
    private HttpTaskServer httpTaskServer;

    @BeforeEach
    public void setUp() throws IOException {
        kvServer = Managers.getDefaultKVServer();
        httpTaskServer = new HttpTaskServer();
        httpTaskServer.start();
        taskManager = new HttpTaskManager();
    }

    @AfterEach
    public void stopServer(){
        httpTaskServer.stop();
        kvServer.stop();
    }

    //___information___//
    //Task, subtask и epic создаются в TaskManagerTest
    @Test
    public void testLoadTasks(){
        HttpTaskManager newTaskManager = new HttpTaskManager();
//        TaskManager newTaskManager = Managers.getDefault();
        Assertions.assertEquals(taskManager.getTasks(), newTaskManager.getTasks());
    }

    @Test
    void testLoadSubtasks(){
        HttpTaskManager newTaskManager = new HttpTaskManager();
//        TaskManager newTaskManager = Managers.getDefault();
        Assertions.assertEquals(taskManager.getSubtasks(), newTaskManager.getSubtasks());
    }

    @Test
    void testLoadEpics(){
        HttpTaskManager newTaskManager = new HttpTaskManager();
//        TaskManager newTaskManager = Managers.getDefault();
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
}