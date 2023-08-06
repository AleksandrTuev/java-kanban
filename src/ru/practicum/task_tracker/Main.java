package ru.practicum.task_tracker;

import ru.practicum.task_tracker.manager.TaskTracker;
import ru.practicum.task_tracker.task_status.Status;
import ru.practicum.task_tracker.tasks.Epic;
import ru.practicum.task_tracker.tasks.Subtask;
import ru.practicum.task_tracker.tasks.Task;

public class Main {

    public static void main(String[] args) {
        TaskTracker taskTracker = new TaskTracker();
/*
Задание по тестированию
- Создайте 2 задачи, один эпик с 2 подзадачами, а другой эпик с 1 подзадачей.
- Распечатайте списки эпиков, задач и подзадач, через System.out.println(..)
- Измените статусы созданных объектов, распечатайте. Проверьте, что статус задачи и подзадачи сохранился, а статус эпика рассчитался по статусам подзадач.
- И, наконец, попробуйте удалить одну из задач и один из эпиков.
*/
//      После проверки метод main будет очищен

        Task task1 = new Task("Task 1", "Description 1");
        int taskId1 = taskTracker.addNewTask(task1);
        Task task2 = new Task("Task 2", "Description 2");
        int taskId2 = taskTracker.addNewTask(task2);

        Epic epic1 = new Epic("Epic 1", "Description 1");
        int epicId1 = taskTracker.addNewEpic(epic1);
        Subtask subtask11 = new Subtask("Subtask 11", "Description 11/Epic 1", epicId1);
        int subtaskId11 = taskTracker.addNewSubtask(subtask11);
        Subtask subtask12 = new Subtask("Subtask 12", "Description 12/Epic 1", epicId1);
        int subtaskId12 = taskTracker.addNewSubtask(subtask12);

        Epic epic2 = new Epic("Epic 2", "Description 2");
        int epicId2 = taskTracker.addNewEpic(epic2);
        Subtask subtask21 = new Subtask("Subtask 21", "Description 21/Epic 2", epicId2);
        int subtaskId21 = taskTracker.addNewSubtask(subtask21);

        System.out.println(taskTracker.getTasks());
        System.out.println(taskTracker.getEpics());
        System.out.println(taskTracker.getSubtasks());

        System.out.println("Update Task2");
        taskTracker.updateTask(new Task(2, "Task 2", "Description 2", Status.IN_PROGRESS));
        System.out.println(taskTracker.getTasks());
        System.out.println("Update Subtask11");
        taskTracker.updateSubtask(new Subtask(4, "Subtask 11", "Description 11/Epic 1", Status.IN_PROGRESS, epicId1));
        System.out.println(taskTracker.getSubtasks());
        System.out.println("New status Epic1");
        System.out.println(taskTracker.getEpics());
        System.out.println("---");
        System.out.println("Delete Task2");
        taskTracker.removeTask(taskId2);
        System.out.println(taskTracker.getTasks());
        System.out.println("Delete all Task");
        taskTracker.deleteAllTasks();
        System.out.println(taskTracker.getTasks());
        System.out.println("---");
        System.out.println("Delete Epic2");
        taskTracker.removeEpic(epicId2);
        System.out.println(taskTracker.getEpics());
        System.out.println("Delete Subtask11");
        taskTracker.removeSubtask(subtaskId11);
        System.out.println(taskTracker.getEpics());
        System.out.println(taskTracker.getSubtasks());


    }

}
