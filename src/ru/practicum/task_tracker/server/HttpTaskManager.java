package server;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import enums.TaskType;
import manager.FileBackedTaskManager;
import manager.Managers;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.*;
import java.util.stream.Collectors;

public class HttpTaskManager extends FileBackedTaskManager {
    private final Gson gson;
    private final KVClient client;
    private boolean flag;

    public HttpTaskManager(){
        super(null);
        gson = Managers.getGson();
        client = new KVClient();
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
        if (!flag) {
            return;
        }
        ArrayList<Task> tasks = gson.fromJson(client.load("tasks"), new TypeToken<ArrayList<Task>>() {
        }.getType());
        addTasks(tasks);

        ArrayList<Epic> epics = gson.fromJson(client.load("epics"), new TypeToken<ArrayList<Epic>>() {
        }.getType());
        addTasks(epics);

        ArrayList<Subtask> subtasks = gson.fromJson(client.load("subtasks"), new TypeToken<ArrayList<Subtask>>() {
        }.getType());
        addTasks(subtasks);

        List<Integer> history = gson.fromJson(client.load("history"), new TypeToken<ArrayList<Integer>>() {
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
        flag = true;
    }

    @Override
    protected void save(){
        String jsonTasks = gson.toJson(new ArrayList<>(tasks.values()));
        client.put("tasks", jsonTasks);
        String jsonSubtasks = gson.toJson(new ArrayList<>(subtasks.values()));
        client.put("subtasks", jsonSubtasks);
        String jsonEpics = gson.toJson(new ArrayList<>(epics.values()));
        client.put("epics", jsonEpics);
        String jsonHistory = gson.toJson(historyManager.getHistory().stream().map(Task::getId).collect(Collectors.toList()));
        client.put("history", jsonHistory);
    }
}
