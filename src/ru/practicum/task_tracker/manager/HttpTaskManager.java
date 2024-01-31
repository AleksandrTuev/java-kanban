package manager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import enums.TaskType;
import server.KVClient;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.*;
import java.util.stream.Collectors;

public class HttpTaskManager extends FileBackedTaskManager {
    private final Gson gson;
    private final KVClient kvClient;

    public HttpTaskManager(){
        super(null);
        gson = Managers.getGson();
        kvClient = new KVClient();
        load();
    }

    protected void addTasks (List<? extends Task> tasks) {
        if (tasks == null){
            return;
        }
        for (Task task : tasks) {
            final int id = task.getId();
            if (id > generateId) {
                generateId = id;
            }
            TaskType type = task.getType();
            if (type == TaskType.TASK) {
                this.tasks.put(id, task);
                setPrioritizedTasks(task);
            } else if (type == TaskType.SUBTASK) {
                subtasks.put(id, (Subtask) task);
                setPrioritizedTasks(task);
            } else if (type == TaskType.EPIC) {
                epics.put(id, (Epic) task);
            }
        }
    }

    private void load() {
        String tasksStr = kvClient.load("tasks");
        if (tasksStr != null){
            ArrayList<Task> tasks = gson.fromJson(tasksStr, new TypeToken<ArrayList<Task>>() {
            }.getType());
            addTasks(tasks);
        }

        String epicsStr = kvClient.load("epics");
        if (epicsStr != null){
            ArrayList<Epic> epics = gson.fromJson(epicsStr, new TypeToken<ArrayList<Epic>>() {
            }.getType());
            addTasks(epics);
        }

        String subtasksStr = kvClient.load("subtasks");
        if (subtasksStr != null) {
            ArrayList<Subtask> subtasks = gson.fromJson(subtasksStr, new TypeToken<ArrayList<Subtask>>() {
            }.getType());
            addTasks(subtasks);
        }

        String historyStr = kvClient.load("history");
        if (historyStr != null){
            List<Integer> history = gson.fromJson(historyStr, new TypeToken<ArrayList<Integer>>() {
            }.getType());
            if (history != null) {
                for (Integer id : history) {
                    if (getTasks().containsKey(id)) {
                        historyManager.addTask(getTaskToId(id));
                    }
                    if (getEpics().containsKey(id)) {
                        historyManager.addTask(getEpicToId(id));
                    }
                    if (getSubtasks().containsKey(id)) {
                        historyManager.addTask(getSubtaskToId(id));
                    }
                }
            }
        }
    }

    @Override
    protected void save(){
        String jsonTasks = gson.toJson(new ArrayList<>(tasks.values()));
        kvClient.put("tasks", jsonTasks);
        String jsonSubtasks = gson.toJson(new ArrayList<>(subtasks.values()));
        kvClient.put("subtasks", jsonSubtasks);
        String jsonEpics = gson.toJson(new ArrayList<>(epics.values()));
        kvClient.put("epics", jsonEpics);
        String jsonHistory = gson.toJson(historyManager.getHistory().stream().map(Task::getId).collect(Collectors.toList()));
        kvClient.put("history", jsonHistory);
    }
}
