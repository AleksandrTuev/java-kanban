package ru.practicum.task_tracker;

import ru.practicum.task_tracker.manager.Managers;
import ru.practicum.task_tracker.manager.TaskManager;
import ru.practicum.task_tracker.task_status.Status;
import ru.practicum.task_tracker.tasks.Epic;
import ru.practicum.task_tracker.tasks.Subtask;
import ru.practicum.task_tracker.tasks.Task;

public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = Managers.getDefault();

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

        Epic epic2 = new Epic("Epic 2", "Description 2");
        int epicId2 = taskManager.addNewEpic(epic2);
        Subtask subtask21 = new Subtask("Subtask 21", "Description 21/Epic 2", epicId2);
        int subtaskId21 = taskManager.addNewSubtask(subtask21);

        System.out.println(taskManager.getTasks());
        System.out.println(taskManager.getEpics());
        System.out.println(taskManager.getSubtasks());

        //Вызов тасок, епиков и сабтасок по id
        System.out.println("-----");
        taskManager.getTaskToId(1);
        taskManager.getTaskToId(2);
        taskManager.getEpicToId(3);
        taskManager.getSubtaskToId(4);
        taskManager.getSubtaskToId(5);
        System.out.println("Вызов истории");
        System.out.println(taskManager.getHistory());
        System.out.println("-----");

        System.out.println("Update Task2");
        taskManager.updateTask(new Task(2, "Task 2", "Description 2", Status.IN_PROGRESS));
        System.out.println(taskManager.getTasks());
        System.out.println("Update Subtask11");
        taskManager.updateSubtask(new Subtask(4, "Subtask 11", "Description 11/Epic 1", Status.IN_PROGRESS, epicId1));
        System.out.println(taskManager.getSubtasks());
        System.out.println("New status Epic1");
        System.out.println(taskManager.getEpics());
        System.out.println("---");
        System.out.println("Delete Task2");
        taskManager.removeTask(taskId2);
        System.out.println(taskManager.getTasks());
        System.out.println("Delete all Task");
        taskManager.deleteAllTasks();
        System.out.println(taskManager.getTasks());
        System.out.println("---");
        System.out.println("Delete Epic2");
        taskManager.removeEpic(epicId2);
        System.out.println(taskManager.getEpics());
        System.out.println("Delete Subtask11");
        taskManager.removeSubtask(subtaskId11);
        System.out.println(taskManager.getEpics());
        System.out.println(taskManager.getSubtasks());
    }

}
