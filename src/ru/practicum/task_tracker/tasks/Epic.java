package ru.practicum.task_tracker.tasks;

import ru.practicum.task_tracker.enums.TaskType;

import java.util.ArrayList;

public class Epic extends Task{
    private ArrayList<Integer> subtaskIds;

    public Epic(String name, String description) {
        super(name, description);
        subtaskIds = new ArrayList<>();
    }

    public void setSubtaskIds(ArrayList<Integer> subtaskIds) {
        this.subtaskIds = subtaskIds;
    }

    public ArrayList<Integer> getSubtaskIds() {
        return subtaskIds;
    }

    public void addSubtaskId(int subtaskId){
        subtaskIds.add(subtaskId);
    }

    @Override
    public TaskType getType(){
        return TaskType.EPIC;
    }

    @Override
    public String toString() {
        return "Epic{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", subtaskIds.size()='" + subtaskIds.size() + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
