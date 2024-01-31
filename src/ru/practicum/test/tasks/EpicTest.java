package tasks;

import enums.Status;
import manager.Managers;
import manager.TaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.HttpTaskServer;
import server.KVServer;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

class EpicTest {
    TaskManager taskManager;

    private KVServer kvServer;
    private HttpTaskServer httpTaskServer;

    @BeforeEach
    public void beforeEach() throws IOException {
        kvServer = Managers.getDefaultKVServer();
        taskManager = Managers.getDefault();
        httpTaskServer = new HttpTaskServer(taskManager);
        httpTaskServer.start();

        Epic epic1 = new Epic("Epic 1", "Description 1"); //id = 1
        int epicId1 = taskManager.addNewEpic(epic1);
        Subtask subtask11 = new Subtask("Subtask 11", "Description 11/Epic 1", epicId1,
                LocalDateTime.of(2024,1,10,11,15),30); //id = 2
        taskManager.addNewSubtask(subtask11);
        Subtask subtask12 = new Subtask("Subtask 12", "Description 12/Epic 1", epicId1,
                LocalDateTime.of(2024,1,10,12,15),30);  //id = 3
        taskManager.addNewSubtask(subtask12);
    }

    @AfterEach
    public void stopServer(){
        httpTaskServer.stop();
        kvServer.stop();
    }

    @Test
    public void shouldListSubtaskEmpty(){
        Epic epic = new Epic("Epic 1", "Description 1");

        List<Integer> subtaskIds = epic.getSubtaskIds();

        Assertions.assertTrue(subtaskIds.isEmpty());
    }

    @Test
    public void shouldStatusNewForAllSubtasks(){
        boolean result = true;

        Map<Integer, Subtask> subtasks = taskManager.getSubtasks();
        for (Integer id: subtasks.keySet()) {
            if (subtasks.get(id).getStatus() != Status.NEW){
                result = false;
            }
        }

        Assertions.assertTrue(result);
        Assertions.assertEquals("NEW", taskManager.getEpicToId(1).getStatus().toString());
    }

    @Test
    public void shouldStatusDoneForAllSubtasks(){
        taskManager.getSubtaskToId(2).setStatus(Status.DONE);
        taskManager.updateSubtask(taskManager.getSubtaskToId(2));
        taskManager.getSubtaskToId(3).setStatus(Status.DONE);
        taskManager.updateSubtask(taskManager.getSubtaskToId(3));


        boolean result = true;

        Map<Integer, Subtask> subtasks = taskManager.getSubtasks();
        for (Integer id: subtasks.keySet()) {
            if (subtasks.get(id).getStatus() != Status.DONE){
                result = false;
            }
        }

        Assertions.assertTrue(result);
        Assertions.assertEquals("DONE", taskManager.getEpicToId(1).getStatus().toString());
    }

    @Test
    public void shouldStatusNewAndDoneForSubtasks(){
        taskManager.getSubtaskToId(2).setStatus(Status.DONE);
        taskManager.updateSubtask(taskManager.getSubtaskToId(2));

        boolean result1 = false;
        boolean result2 = false;

        Map<Integer, Subtask> subtasks = taskManager.getSubtasks();
        for (Integer id: subtasks.keySet()) {
            if (subtasks.get(id).getStatus() == Status.NEW){
                result1 = true;
            }
            if (subtasks.get(id).getStatus() == Status.DONE){
                result2 = true;
            }
        }

        Assertions.assertTrue(result1 && result2);
        Assertions.assertEquals("IN_PROGRESS", taskManager.getEpicToId(1).getStatus().toString());
    }

    @Test
    public void shouldStatusInProgressForAllSubtasks(){
        taskManager.getSubtaskToId(2).setStatus(Status.IN_PROGRESS);
        taskManager.updateSubtask(taskManager.getSubtaskToId(2));
        taskManager.getSubtaskToId(3).setStatus(Status.IN_PROGRESS);
        taskManager.updateSubtask(taskManager.getSubtaskToId(3));

        boolean result = true;

        Map<Integer, Subtask> subtasks = taskManager.getSubtasks();
        for (Integer id: subtasks.keySet()) {
            if (subtasks.get(id).getStatus() != Status.IN_PROGRESS){
                result = false;
            }
        }

        Assertions.assertTrue(result);
        Assertions.assertEquals("IN_PROGRESS", taskManager.getEpicToId(1).getStatus().toString());
    }
}