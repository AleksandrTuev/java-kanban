package tasks;

import enums.Status;
import manager.Managers;
import manager.TaskManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Map;

class EpicTest {
    TaskManager taskManager;

    @BeforeEach
    public void beforeEach(){
        taskManager = Managers.getDefault();
        Epic epic1 = new Epic("Epic 1", "Description 1"); //id = 1
        int epicId1 = taskManager.addNewEpic(epic1);
        Subtask subtask11 = new Subtask("Subtask 11", "Description 11/Epic 1", epicId1); //id = 2
        int subtaskId11 = taskManager.addNewSubtask(subtask11);
        Subtask subtask12 = new Subtask("Subtask 12", "Description 12/Epic 1", epicId1); //id = 3
        int subtaskId12 = taskManager.addNewSubtask(subtask12);
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
    }

    @Test
    public void shouldStatusDoneForAllSubtasks(){
        taskManager.getSubtaskToId(2).setStatus(Status.DONE);
        taskManager.getSubtaskToId(3).setStatus(Status.DONE);

        boolean result = true;

        Map<Integer, Subtask> subtasks = taskManager.getSubtasks();
        for (Integer id: subtasks.keySet()) {
            if (subtasks.get(id).getStatus() != Status.DONE){
                result = false;
            }
        }

        Assertions.assertTrue(result);
    }

    @Test
    public void shouldStatusNewAndDoneForSubtasks(){
        taskManager.getSubtaskToId(2).setStatus(Status.DONE);

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
    }

    @Test
    public void shouldStatusInProgressForAllSubtasks(){
        taskManager.getSubtaskToId(2).setStatus(Status.IN_PROGRESS);
        taskManager.getSubtaskToId(3).setStatus(Status.IN_PROGRESS);

        boolean result = true;

        Map<Integer, Subtask> subtasks = taskManager.getSubtasks();
        for (Integer id: subtasks.keySet()) {
            if (subtasks.get(id).getStatus() != Status.IN_PROGRESS){
                result = false;
            }
        }

        Assertions.assertTrue(result);
    }
}