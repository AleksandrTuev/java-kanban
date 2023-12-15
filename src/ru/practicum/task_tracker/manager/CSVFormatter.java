package ru.practicum.task_tracker.manager;

import ru.practicum.task_tracker.manager.task_type.TaskType;
import ru.practicum.task_tracker.task_status.Status;
import ru.practicum.task_tracker.tasks.Epic;
import ru.practicum.task_tracker.tasks.Subtask;
import ru.practicum.task_tracker.tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class CSVFormatter {

    private CSVFormatter(){}

    private static String getParentEpicId(Task task){
        String taskStr;
        if (task instanceof Subtask){
            taskStr = Integer.toString(((Subtask) task).getEpicId());
        } else {
            taskStr = "";
        }
        return taskStr;
    }

    private static TaskType getType(Task task){
        if (task instanceof Subtask){
            return TaskType.SUBTASK;
        } else if (task instanceof Epic) {
            return TaskType.EPIC;
        } else {
            return TaskType.TASK;
        }
    }

    public static String toString(Task task){
        StringBuilder taskStr = new StringBuilder();

        taskStr.append(task.getId());
        taskStr.append(",");
        taskStr.append(getType(task));
        taskStr.append(",");
        taskStr.append(task.getName() );
        taskStr.append(",");
        taskStr.append(task.getStatus());
        taskStr.append(",");
        taskStr.append(task.getDescription());
        taskStr.append(",");
        taskStr.append(getParentEpicId(task));
        return taskStr.toString();
    }

    public static Task fromString(String taskStr){
        String[] arr = taskStr.split(",");

        switch (TaskType.valueOf(arr[1])){
            case EPIC:
                Epic epic = new Epic(arr[2], arr[4]);
                epic.setId(Integer.parseInt(arr[0]));
                epic.setStatus(Status.valueOf(arr[3]));
                return epic;
            case SUBTASK:
                Subtask subtask = new Subtask(arr[2], arr[4], Integer.parseInt(arr[5]));
                subtask.setId(Integer.parseInt(arr[0]));
                subtask.setStatus(Status.valueOf(arr[3]));
                return subtask;
            default:
                Task task = new Task(arr[2], arr[4]);
                task.setId(Integer.parseInt(arr[0]));
                task.setStatus(Status.valueOf(arr[3]));
                return task;
        }
    }

    public static String historyToString(HistoryManager historyManager){
        StringBuilder taskHistoryStr = new StringBuilder();
        for (int i = 0; i < historyManager.getHistory().size(); i++) {
            taskHistoryStr.append(historyManager.getHistory().get(i).getId());
            if (i == historyManager.getHistory().size() - 1){
                return taskHistoryStr.toString();
            } else {
                taskHistoryStr.append(",");
            }
        }
        return taskHistoryStr.toString();
    }

    public static List<Integer> historyFromString(String historyStr){
        List<Integer> ids = new ArrayList<>();
        String[] arr = historyStr.split(",");
        for (String id : arr) {
            ids.add(Integer.parseInt(id));
        }
        return ids;
    }
}
