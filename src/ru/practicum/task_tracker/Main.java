import manager.Managers;
import manager.TaskManager;
import server.HttpTaskServer;
import server.KVServer;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.IOException;
import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) throws IOException {
        KVServer kvServer = Managers.getDefaultKVServer();
        TaskManager taskManager = Managers.getDefault();
        HttpTaskServer server = new HttpTaskServer(taskManager);
        server.start();

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
        taskManager.getEpicToId(epicId1);
        taskManager.getTaskToId(taskId2);
        taskManager.getSubtaskToId(subtaskId12);
        taskManager.getTaskToId(taskId1);
        taskManager.getSubtaskToId(subtaskId12);

        System.out.println("----------------Создание нового объекта------------");
        TaskManager newTaskManager = Managers.getDefault();
        System.out.println("----------------------Сравнение--------------------");
        System.out.println("-----------------История просмотра №1--------------");
        System.out.println(taskManager.getHistory());
        System.out.println("-----------------История просмотра №2--------------");
        System.out.println(newTaskManager.getHistory());
        System.out.println("----------------Все tasks объекта №1---------------");
        System.out.println(taskManager.getTasks());
        System.out.println("----------------Все tasks объекта №2---------------");
        System.out.println(newTaskManager.getTasks());
        System.out.println("---------------Все subtasks объекта №1--------------");
        System.out.println(taskManager.getSubtasks());
        System.out.println("---------------Все subtasks объекта №2--------------");
        System.out.println(newTaskManager.getSubtasks());
        System.out.println("----------------Все epics объекта №1---------------");
        System.out.println(taskManager.getEpics());
        System.out.println("----------------Все epics объекта №2---------------");
        System.out.println(newTaskManager.getEpics());
        System.out.println("--------Список задач по приоритету (объект №1)-------");
        System.out.println(taskManager.getPrioritizedTasks());
        System.out.println("--------Список задач по приоритету (объект №2)-------");
        System.out.println(newTaskManager.getPrioritizedTasks());
        kvServer.stop();
        server.stop();
    }
}
