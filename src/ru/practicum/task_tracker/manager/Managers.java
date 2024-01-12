package ru.practicum.task_tracker.manager;

public class Managers {

    public static TaskManager getDefault(){
        return new FileBackedTaskManager();
    }

    public static HistoryManager getDefaultHistory(){
        return new InMemoryHistoryManager();
    }
}
