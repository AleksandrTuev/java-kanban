package ru.practicum.task_tracker.manager;

import java.io.File;

public class Managers {
    public static TaskManager getDefault(File file){
        return new FileBackedTaskManager(file);
    }

    public static HistoryManager getDefaultHistory(){
        return new InMemoryHistoryManager();
    }
}
