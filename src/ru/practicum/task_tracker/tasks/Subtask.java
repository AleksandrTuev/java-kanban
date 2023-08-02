package ru.practicum.task_tracker.tasks;

public class Subtask extends Task{
    private int epicId;
    public Subtask(String name, String description, String status, int epicId) {
        super(name, description, status);
        this.epicId = epicId;
    }

    //конструктор для проверки метода обновления подзадачи. аргументы - id, name, description, status, epicId
    public Subtask(Integer id, String name, String description, String status, int epicId) {
        super(id, name, description, status);
        this.epicId = epicId;
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

    public int getEpicId() {
        return epicId;
    }
}
