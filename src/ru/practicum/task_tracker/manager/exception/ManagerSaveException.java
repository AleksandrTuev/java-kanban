package ru.practicum.task_tracker.manager.exception;

import ru.practicum.task_tracker.manager.Managers;

import java.io.IOException;

public class ManagerSaveException extends RuntimeException {
    public ManagerSaveException(final String message){
        super(message);
    }
}
