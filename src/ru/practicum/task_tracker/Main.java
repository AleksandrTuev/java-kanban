package ru.practicum.task_tracker;

import ru.practicum.task_tracker.manager.FileBackedTaskManager;
import ru.practicum.task_tracker.manager.Managers;
import ru.practicum.task_tracker.manager.TaskManager;
import ru.practicum.task_tracker.tasks.Epic;
import ru.practicum.task_tracker.tasks.Subtask;
import ru.practicum.task_tracker.tasks.Task;

import java.io.File;

public class Main {
    public static void main(String[] args) {

        TaskManager taskManager = Managers.getDefault();

        //id=1
        Task task1 = new Task("Task 1", "Description 1");
        int taskId1 = taskManager.addNewTask(task1);
        //id=2
        Task task2 = new Task("Task 2", "Description 2");
        int taskId2 = taskManager.addNewTask(task2);

        //id=3
        Epic epic1 = new Epic("Epic 1", "Description 1");
        int epicId1 = taskManager.addNewEpic(epic1);
        //id=4
        Subtask subtask11 = new Subtask("Subtask 11", "Description 11/Epic 1", epicId1);
        int subtaskId11 = taskManager.addNewSubtask(subtask11);
        //id=5
        Subtask subtask12 = new Subtask("Subtask 12", "Description 12/Epic 1", epicId1);
        int subtaskId12 = taskManager.addNewSubtask(subtask12);
        //id=6
        Subtask subtask13 = new Subtask("Subtask 13", "Description 13/Epic 1", epicId1);
        int subtaskId13 = taskManager.addNewSubtask(subtask13);

        //id=7
        Epic epic2 = new Epic("Epic 2", "Description 2");
        int epicId2 = taskManager.addNewEpic(epic2);
        //id=8
        Subtask subtask21 = new Subtask("Subtask 21", "Description 21/Epic 2", epicId2);
        int subtaskId21 = taskManager.addNewSubtask(subtask21);
        //id=9
        Subtask subtask22 = new Subtask("Subtask 22", "Description 22/Epic 2", epicId2);
        int subtaskId22 = taskManager.addNewSubtask(subtask22);

        //id=10
        Task task3 = new Task("Task 3", "Description 3");
        int taskId3 = taskManager.addNewTask(task3);

        //Вызов тасок, епиков и сабтасок по id
        taskManager.getTaskToId(1);
        taskManager.getTaskToId(10);
        taskManager.getEpicToId(7);
        taskManager.getTaskToId(1);
        taskManager.getTaskToId(10);
        taskManager.getEpicToId(3);
        taskManager.getSubtaskToId(6);

        TaskManager restoredFromFile = FileBackedTaskManager.loadFromFile(new File("data.csv"));
        System.out.println("-----------------------");
        System.out.println("-------Проверка-------");
        System.out.println("-------tasks-------");
        System.out.println(taskManager.getTasks());
        System.out.println(restoredFromFile.getTasks());
        System.out.println("-------epics-------");
        System.out.println(taskManager.getEpics());
        System.out.println(restoredFromFile.getEpics());
        System.out.println("-------subtasks-------");
        System.out.println(taskManager.getSubtasks());
        System.out.println(restoredFromFile.getSubtasks());
        System.out.println("-------history-------");
        System.out.println(taskManager.getHistory());
        System.out.println(restoredFromFile.getHistory());
        System.out.println("-----------------------");

        System.out.print("Сравнение мап tasks - ");
        if (taskManager.getTasks().equals(restoredFromFile.getTasks())){
            System.out.println("true");
        } else {
            System.out.println("false");
        }

        System.out.print("Сравнение мап epics - ");
        if (taskManager.getEpics().equals(restoredFromFile.getEpics())){
            System.out.println("true");
        } else {
            System.out.println("false");
        }

        System.out.print("Сравнение мап subtasks - ");
        if (taskManager.getSubtasks().equals(restoredFromFile.getSubtasks())){
            System.out.println("true");
        } else {
            System.out.println("false");
        }

        System.out.print("Сравнивание списков history - ");
        if (taskManager.getHistory().equals(restoredFromFile.getHistory())){
            System.out.println("true");
        } else {
            System.out.println("false");
        }
    }
}
