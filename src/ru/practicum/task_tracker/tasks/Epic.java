package tasks;

import enums.TaskType;
import manager.InMemoryTaskManager;
import manager.TaskManager;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Epic extends Task{
    private ArrayList<Integer> subtaskIds;
    private LocalDateTime endTime;
    public Epic(String name, String description) {
        super(name, description, null, 0);
        subtaskIds = new ArrayList<>();
    }

//    public Epic(Integer id, String name, String description) {
//        super(id, name, description);
//        subtaskIds = new ArrayList<>();
//    }

    @Override
    public LocalDateTime getEndTime(){
        TaskManager taskManager = new InMemoryTaskManager();
        LocalDateTime startTime = null;
        LocalDateTime endTime = null;

        for (Integer subtaskId : subtaskIds) {
            if (startTime == null){
                startTime = taskManager.getSubtasks().get(subtaskId).getStartTime();
                endTime = taskManager.getSubtasks().get(subtaskId).getEndTime();
                continue;
            }
            if (!startTime.isBefore(taskManager.getSubtasks().get(subtaskId).getStartTime())){
                startTime = taskManager.getSubtasks().get(subtaskId).getStartTime();
            }
            if (!endTime.isAfter(taskManager.getSubtasks().get(subtaskId).getEndTime())){
                endTime = taskManager.getSubtasks().get(subtaskId).getEndTime();
            }
        }
        setStartTime(startTime);
        return endTime;
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
