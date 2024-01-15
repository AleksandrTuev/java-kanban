package manager;

import enums.Status;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.time.LocalDateTime;

public abstract class TaskManagerTest<T extends TaskManager> {
    T taskManager;

    private void creatingTasks(){
        //id=1
        Task task1 = new Task("Task1", "Description/Task1",
                LocalDateTime.of(2024, 1, 15,21,30), 10);
        int taskId1 = taskManager.addNewTask(task1);
        //id=2
        Task task2 = new Task("Task2", "Description/Task2",
                LocalDateTime.of(2024, 1, 15,15,30), 15);
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
        //id=6
        Subtask subtask13 = new Subtask("Subtask 13", "Description 13/Epic 1", epicId1,
                LocalDateTime.of(2024, 1, 16,11,10), 15);
        int subtaskId13 = taskManager.addNewSubtask(subtask13);

        //id=7
        Epic epic2 = new Epic("Epic 2", "Description 2");
        int epicId2 = taskManager.addNewEpic(epic2);
        //id=8
        Subtask subtask21 = new Subtask("Subtask 21", "Description 21/Epic 2", epicId2,
                LocalDateTime.of(2024, 1, 15,7,30), 60);
        int subtaskId21 = taskManager.addNewSubtask(subtask21);
        //id=9
        Subtask subtask22 = new Subtask("Subtask 22", "Description 22/Epic 2", epicId2,
                LocalDateTime.of(2024, 1, 15,8,40), 60);
        int subtaskId22 = taskManager.addNewSubtask(subtask22);

        //id=10
        Task task3 = new Task("Task3", "Description/Task3",
                LocalDateTime.of(2024, 1, 17,21,15), 25);
        int taskId3 = taskManager.addNewTask(task3);

        //Просмотр задач
        taskManager.getTaskToId(1);
        taskManager.getTaskToId(2);
        taskManager.getEpicToId(3);
    }

    @Test
    public void testGetTasksAndEpicsAndSubtasks() {
        creatingTasks();
        Assertions.assertEquals(1, taskManager.getTasks().get(1).getId());
        Assertions.assertEquals(3, taskManager.getEpics().get(3).getId());
        Assertions.assertEquals(5, taskManager.getSubtasks().get(5).getId());
        Assertions.assertEquals(2, taskManager.getTaskToId(2).getId());
        Assertions.assertEquals(7, taskManager.getEpicToId(7).getId());
        Assertions.assertEquals(8, taskManager.getSubtaskToId(8).getId());

        Task task = new Task("NewTask", "NewDescription",
                LocalDateTime.of(2024,1,5,12,0),55);
        int taskId = taskManager.addNewTask(task);
        Assertions.assertEquals(task,taskManager.getTaskToId(taskId));
        Assertions.assertNotNull(taskManager.getTaskToId(1), "Задача не найдена");
    }

    @Test
    public void testDeleteAllTasksAndEpics(){
        creatingTasks();
        taskManager.deleteAllTasks();
        taskManager.deleteAllEpics();
        Assertions.assertTrue(taskManager.getTasks().isEmpty());
        Assertions.assertTrue(taskManager.getEpics().isEmpty());
    }

    @Test
    public void testDeleteAllSubtasks(){
        creatingTasks();
        taskManager.deleteAllSubtasks();
        Assertions.assertTrue(taskManager.getSubtasks().isEmpty());
        Assertions.assertTrue(taskManager.getEpicToId(3).getSubtaskIds().isEmpty());
        Assertions.assertTrue(taskManager.getEpicToId(7).getSubtaskIds().isEmpty());
    }

    @Test
    public void testRemoveTask(){
        creatingTasks();
        taskManager.removeTask(1);
        Assertions.assertFalse(taskManager.getTasks().containsKey(1));
    }

    @Test
    public void testRemoveEpic(){
        creatingTasks();
        taskManager.removeEpic(3);
        Assertions.assertFalse(taskManager.getEpics().containsKey(3));
    }

    @Test
    public void testRemoveSubtask(){
        creatingTasks();
        taskManager.removeSubtask(6);
        Assertions.assertFalse(taskManager.getSubtasks().containsKey(6));
    }

    @Test
    public void testHistory(){
        creatingTasks();
        Assertions.assertFalse(taskManager.getHistory().isEmpty());
    }

    @Test
    public void testUpdateTask(){
        creatingTasks();
        Task task = new Task(1, "Task", "Description", Status.IN_PROGRESS,
                LocalDateTime.of(2024, 1, 15,21,30), 10);
        taskManager.updateTask(task);
        Assertions.assertNotNull(taskManager.getTaskToId(1), "Задача не найдена");
        Assertions.assertEquals(task, taskManager.getTaskToId(1));
    }

    @Test
    public void testUpdateSubtask(){
        creatingTasks();
        Subtask subtask = new Subtask(4, "Subtask", "Description", Status.IN_PROGRESS, 3,
                LocalDateTime.of(2024, 1, 16,10,30), 15);
        taskManager.updateSubtask(subtask);
        taskManager.updateEpic(taskManager.getEpicToId(3));
        Assertions.assertNotNull(taskManager.getSubtaskToId(4), "Задача не найдена");
        Assertions.assertNotNull(taskManager.getEpicToId(3), "Задача не найдена");
        Assertions.assertEquals(Status.IN_PROGRESS, taskManager.getSubtaskToId(4).getStatus());
        Assertions.assertEquals(Status.IN_PROGRESS, taskManager.getEpicToId(3).getStatus());
    }

    @Test
    public void testGetIdEpicInSubtask(){
        creatingTasks();
        Assertions.assertEquals(7, taskManager.getSubtaskToId(8).getEpicId());
    }

    @Test
    public void testGetNonExistentTaskAndEpicAndSubtask(){
        creatingTasks();
        Assertions.assertNull(taskManager.getTasks().get(3));
        Assertions.assertNull(taskManager.getSubtasks().get(1));
        Assertions.assertNull(taskManager.getEpics().get(17));
    }

}
