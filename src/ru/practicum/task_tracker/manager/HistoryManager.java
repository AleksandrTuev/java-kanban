package ru.practicum.task_tracker.manager;

import ru.practicum.task_tracker.tasks.Task;

import java.util.List;

public interface HistoryManager {
    void addTask(Task task);

    List<Task> getHistory();
}
