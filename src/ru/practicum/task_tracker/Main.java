package ru.practicum.task_tracker;

import ru.practicum.task_tracker.manager.Managers;
import ru.practicum.task_tracker.manager.TaskManager;
import ru.practicum.task_tracker.tasks.Epic;
import ru.practicum.task_tracker.tasks.Subtask;
import ru.practicum.task_tracker.tasks.Task;

public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = Managers.getDefault();
        //Тестирование работы программы согласно тех. задания

        Task task1 = new Task("Task 1", "Description 1");
        int taskId1 = taskManager.addNewTask(task1);
        Task task2 = new Task("Task 2", "Description 2");
        int taskId2 = taskManager.addNewTask(task2);

        Epic epic1 = new Epic("Epic 1", "Description 1");
        int epicId1 = taskManager.addNewEpic(epic1);
        Subtask subtask11 = new Subtask("Subtask 11", "Description 11/Epic 1", epicId1);
        int subtaskId11 = taskManager.addNewSubtask(subtask11);
        Subtask subtask12 = new Subtask("Subtask 12", "Description 12/Epic 1", epicId1);
        int subtaskId12 = taskManager.addNewSubtask(subtask12);
        Subtask subtask13 = new Subtask("Subtask 13", "Description 13/Epic 1", epicId1);
        int subtaskId13 = taskManager.addNewSubtask(subtask13);

        Epic epic2 = new Epic("Epic 2", "Description 2");
        int epicId2 = taskManager.addNewEpic(epic2);

        //Вызов тасок, епиков и сабтасок по id
        System.out.println("-----");
        taskManager.getTaskToId(1);
        System.out.println("Вызов истории");
        System.out.println(taskManager.getHistory());
        taskManager.getEpicToId(7);
        System.out.println("Вызов истории");
        System.out.println(taskManager.getHistory());
        taskManager.getTaskToId(1);
        System.out.println("Вызов истории");
        System.out.println(taskManager.getHistory());
        taskManager.getEpicToId(3);
        System.out.println("Вызов истории");
        System.out.println(taskManager.getHistory());
        taskManager.getSubtaskToId(4);
        taskManager.getSubtaskToId(5);
        System.out.println("Вызов истории");
        System.out.println(taskManager.getHistory());
        taskManager.getEpicToId(7);
        System.out.println("Вызов истории");
        System.out.println(taskManager.getHistory());
        taskManager.getSubtaskToId(6);
        System.out.println("Вызов истории");
        System.out.println(taskManager.getHistory());

        System.out.println("Удаление таски 1(id1)");
        taskManager.removeTask(taskId1);
        System.out.println("Вызов истории");
        System.out.println(taskManager.getHistory());
        System.out.println("Удаление Эпика 1(id3)");
        taskManager.removeEpic(epicId1);
        System.out.println("Вызов истории");
        System.out.println(taskManager.getHistory());
    }
}
