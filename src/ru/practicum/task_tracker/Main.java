import enums.Status;
import manager.Managers;
import manager.TaskManager;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) {
        TaskManager taskManager = Managers.getDefault();

        System.out.println("---------");
        System.out.println("Добавляем Task 1 [15.01.24 / 16:30 - 16:40]");
        //id=1
        Task task1 = new Task("Task 1", "Description/Task 1",
                LocalDateTime.of(2024, 1, 15,16,30), 10);
        int taskId1 = taskManager.addNewTask(task1);
        System.out.println(task1);

        System.out.println("---------");
        System.out.println("Добавляем Task 2 [15.01.24 / 17:30 - 17:45]");
        //id=2
        Task task2 = new Task("Task 2", "Description/Task 2",
                LocalDateTime.of(2024, 1, 15,17,30), 15);
        int taskId2 = taskManager.addNewTask(task2);
        System.out.println(task2);

        System.out.println("---------");
        System.out.println("Проверка Set");
        System.out.println(taskManager.getPrioritizedTasks());

        System.out.println("---------");
        System.out.println("Обновляем Task 1 [15.01.24 / 18:00 - 18:10]");
        task1 = new Task(taskId1, "UpdateTask 1", "UpdateDescription/Task 1", Status.IN_PROGRESS,
                LocalDateTime.of(2024, 1, 15,18,0), 10);
        System.out.println(task1);
        taskManager.updateTask(task1);

        System.out.println("---------");
        System.out.println("Проверка Set");
        System.out.println(taskManager.getPrioritizedTasks());

        System.out.println("---------");
        System.out.println("Обновляем Task 1 [15.01.24 / 18:00 - 18:20]");
        task1 = new Task(taskId1, "UpdateTask 1", "UpdateDescription/Task 1", Status.IN_PROGRESS,
                LocalDateTime.of(2024, 1, 15,18,0), 20);
        System.out.println(task1);
        taskManager.updateTask(task1);

        System.out.println("---------");
        System.out.println("Проверка Set");
        System.out.println(taskManager.getPrioritizedTasks());

        System.out.println("---------");
        System.out.println("Обновляем Task 2 [15.01.24 / 18:10 - 18:25]");
        task2 = new Task(taskId2, "Task 2", "Description/Task 2", Status.IN_PROGRESS,
                LocalDateTime.of(2024, 1, 15,18,10), 15);
        System.out.println(task2);
        taskManager.updateTask(task2);

        System.out.println("---------");
        System.out.println("Проверка Set");
        System.out.println(taskManager.getPrioritizedTasks());

        System.out.println("---------");
        System.out.println("---------");
        System.out.println("Добавляем Epic1");
        //id=3
        Epic epic1 = new Epic("Epic 1", "Description 1");
        int epicId1 = taskManager.addNewEpic(epic1);

        System.out.println("---------");
        System.out.println("Добавляем Subtask11 (Epic1) [16.01.24 / 10:30 - 10:45]");
        //id=4
        Subtask subtask11 = new Subtask("Subtask 11", "Description 11/Epic 1", epicId1,
                LocalDateTime.of(2024, 1, 16,10,30), 15);
        int subtaskId11 = taskManager.addNewSubtask(subtask11);
        System.out.println(subtask11);

        System.out.println("---------");
        System.out.println("Добавляем Subtask12 (Epic1) [16.01.24 / 10:50 - 11:05]");
        //id=5
        Subtask subtask12 = new Subtask("Subtask 12", "Description 12/Epic 1", epicId1,
                LocalDateTime.of(2024, 1, 16,10,50), 15);
        int subtaskId12 = taskManager.addNewSubtask(subtask12);
        System.out.println(subtask12);

        System.out.println("---------");
        System.out.println("Добавляем Subtask13 (Epic1) [16.01.24 / 11:10 - 11:25]");
        //id=6
        Subtask subtask13 = new Subtask("Subtask 13", "Description 13/Epic 1", epicId1,
                LocalDateTime.of(2024, 1, 16,11,10), 15);
        int subtaskId13 = taskManager.addNewSubtask(subtask13);
        System.out.println(subtask13);

        System.out.println("---------");
        System.out.println("Обновляем Subtask11 (Epic1) [16.01.24 / 10:40 - 10:50]");
        subtask11 = new Subtask(subtaskId11, "UpdateSubtask 11", "UpdateDescription 11/Epic 1",
                Status.IN_PROGRESS, epicId1, LocalDateTime.of(2024, 1, 16,10,40), 10);
        System.out.println(subtask11);
        taskManager.updateSubtask(subtask11);

        System.out.println("---------");
        System.out.println("Обновляем Subtask12 (Epic1) [16.01.24 / 12:50 - 13:05]");
        subtask12 = new Subtask(subtaskId12, "UpdateSubtask 12", "UpdateDescription 12/Epic 1",
                Status.IN_PROGRESS, epicId1, LocalDateTime.of(2024, 1, 16,12,50), 15);
        System.out.println(subtask12);
        taskManager.updateSubtask(subtask12);

        System.out.println("---------");
        System.out.println("Проверка Set");
        System.out.println(taskManager.getPrioritizedTasks());

        System.out.println("---------");
        System.out.println("Обновляем Subtask13 (Epic1) [15.01.24 / 18:00 - 18:15]");
        subtask13 = new Subtask(subtaskId13, "UpdateSubtask 13", "UpdateDescription 13/Epic 1",
                Status.IN_PROGRESS, epicId1, LocalDateTime.of(2024, 1, 15,18,0), 15);
        System.out.println(subtask13);
        taskManager.updateSubtask(subtask13);

        System.out.println("---------");
        System.out.println("Проверка Set");
        System.out.println(taskManager.getPrioritizedTasks());

        System.out.println("---------");
        System.out.println("Обновляем Subtask11 (Epic1) [16.01.24 / 10:40 - 10:50]");
        subtask11 = new Subtask(subtaskId11, "UpdateSubtask 11", "UpdateDescription 11/Epic 1",
                Status.DONE, epicId1, LocalDateTime.of(2024, 1, 16,10,40), 10);
        System.out.println(subtask11);
        taskManager.updateSubtask(subtask11);

        System.out.println("---------");
        System.out.println("Обновляем Subtask12 (Epic1) [16.01.24 / 12:50 - 13:05]");
        subtask12 = new Subtask(subtaskId12, "UpdateSubtask 12", "UpdateDescription 12/Epic 1",
                Status.DONE, epicId1, LocalDateTime.of(2024, 1, 16,12,50), 15);
        System.out.println(subtask12);
        taskManager.updateSubtask(subtask12);

        System.out.println("---------");
        System.out.println("Обновляем Subtask13 (Epic1) [16.01.24 / 18:00 - 18:15]");
        subtask13 = new Subtask(subtaskId13, "UpdateSubtask 13", "UpdateDescription 13/Epic 1",
                Status.IN_PROGRESS, epicId1, LocalDateTime.of(2024, 1, 16,18,0), 15);
        System.out.println(subtask13);
        taskManager.updateSubtask(subtask13);

        System.out.println("---------");
        System.out.println("Проверка Set");
        System.out.println(taskManager.getPrioritizedTasks());
        System.out.println("---------");
        System.out.println("Статус Epic - " + taskManager.getEpicToId(epicId1).getStatus());

        System.out.println("---------");
        System.out.println("Добавляем Task 3 [LocalDateTime = null]");
        //id=7
        Task task3 = new Task("Task 3", "Description/Task 3",
                null, 0);
        taskManager.addNewTask(task3);
        System.out.println(task3);

        System.out.println("---------");
        System.out.println("Проверка Set");
        System.out.println(taskManager.getPrioritizedTasks());

        System.out.println("---------");
        System.out.println("Добавляем Task 4 [LocalDateTime = null]");
        //id=8
        Task task4 = new Task("Task 4", "Description/Task 4",
                null, 0);
        taskManager.addNewTask(task4);
        System.out.println(task4);

        System.out.println("---------");
        System.out.println("Проверка Set");
        System.out.println(taskManager.getPrioritizedTasks());

        System.out.println("---------");
        System.out.println("Параметры Epic1:");
        System.out.println("Время начала: " + epic1.getStartTime());
        System.out.println("Длительность в минутах: " + epic1.getDuration());
        System.out.println("Время окончания: " + epic1.getEndTime());
    }
}
