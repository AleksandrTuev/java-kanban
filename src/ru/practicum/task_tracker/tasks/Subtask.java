package tasks;

import enums.TaskType;
import enums.Status;

import java.time.LocalDateTime;

public class Subtask extends Task{
    private final int epicId;

    public Subtask(String name, String description, int epicId, LocalDateTime startTime, long duration) {
        super(name, description, startTime, duration);
        this.epicId = epicId;
    }

    //конструктор для проверки метода обновления подзадачи. аргументы - id, name, description, status, epicId
    public Subtask(Integer id, String name, String description, Status status,
                   int epicId, LocalDateTime startTime, long duration) {
        super(id, name, description, status, startTime, duration);
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
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                ", starTime='" + startTime + '\'' +
                ", duration='" + duration + '\'' +
                '}';
    }
}
