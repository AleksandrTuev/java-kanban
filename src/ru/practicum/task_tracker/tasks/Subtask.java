package ru.practicum.task_tracker.tasks;

import ru.practicum.task_tracker.manager.task_type.TaskType;
import ru.practicum.task_tracker.task_status.Status;

public class Subtask extends Task{
    //Семён привет!
    //Если использовать private int epicId, то измение id эпика возможно
    //Чтобы избежить этого лучше воспользоваться модификатором final и зафиксировать полученное значение
    private final int epicId;

    public Subtask(String name, String description, int epicId) {
        super(name, description);
        this.epicId = epicId;
    }

    //конструктор для проверки метода обновления подзадачи. аргументы - id, name, description, status, epicId
    public Subtask(Integer id, String name, String description, Status status, int epicId) {
        super(id, name, description, status);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    @Override
    public TaskType getType(){
        return TaskType.SUBTASK;
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "id='" + id + '\'' +
                ", epicId='" + epicId + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
