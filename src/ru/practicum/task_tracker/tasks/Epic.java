package tasks;

import enums.TaskType;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Epic extends Task{
    private ArrayList<Integer> subtaskIds;
    private LocalDateTime endTime;

    public Epic(String name, String description) {
        super(name, description, null, 0);
        subtaskIds = new ArrayList<>();
    }

    @Override
    public LocalDateTime getEndTime(){
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime){
        this.endTime = endTime;
    }

    @Override
    public void setStartTime(LocalDateTime startTime){
        super.setStartTime(startTime);
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
