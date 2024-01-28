package server;

import adapter.LocalDateTimeAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import enums.TaskType;
import manager.FileBackedTaskManager;
import manager.KVClient;
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

//    public HttpTaskManager(int port){
//        this(port, false);
//    }

//    public HttpTaskManager(int port, boolean load){
//        super(null);
////        gson = Managers.getGson();
//        gson = new Gson();
////        client = new KVClient(port);
//        client = new KVClient();
//        if (load) {
//            load();
//        }
//    }
    public HttpTaskManager(){
        super(null);
        gson = Managers.getGson();
//        gson = new Gson();
//        client = new KVClient(port);
        client = new KVClient();
//        flag = false;
//        if (load) {
        load();  //____________________________________ВРЕМЕННО НЕ ПОДГРУЖАЕМ ДАННЫЕ
//        }
    }


    public static void main(String[] args) {

    }

    protected void addTasks (List<? extends Task> tasks) { //Добавление в наши мапы
        if (tasks == null){ //призапуска программы (пытается подкачать null данные)
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
//                prioritizedTasks.put(task.getStartTime(), task); //У меня TreeSet
            } else if (type == TaskType.SUBTASK) {
                subtasks.put(id, (Subtask) task);

                setPrioritizedTasks(task);
//                prioritizedTasks.put(task.getStartTime(), task); //У меня TreeSet
            } else if (type == TaskType.EPIC) {
                epics.put(id, (Epic) task);
            }
        }
    }

    private void load() { //метод восстановления с сервера
        if (!flag) {
            return;
        }
        ArrayList<Task> tasks = gson.fromJson(client.load("tasks"), new TypeToken<ArrayList<Task>>() {
        }.getType());
        addTasks(tasks);
//        if (tasks != null){
//            addTasks(tasks);
//        }

        ArrayList<Epic> epics = gson.fromJson(client.load("epics"), new TypeToken<ArrayList<Epic>>() {
        }.getType());
        addTasks(epics);
//        if (epics != null){
//            addTasks(epics);
//        }

        ArrayList<Subtask> subtasks = gson.fromJson(client.load("subtasks"), new TypeToken<ArrayList<Subtask>>() {
        }.getType());
        addTasks(subtasks);
//        if (subtasks != null){
//            addTasks(subtasks);
//        }

        List<Integer> history = gson.fromJson(client.load("history"), new TypeToken<ArrayList<Integer>>() {
        }.getType());
        if (history != null) {
            for (Integer id : history) {
                if (getTasks().containsKey(id)) {
                    historyManager.addTask(getTaskToId(id));
                }
                if (getEpics().containsKey(id)) {
                    historyManager.addTask(getEpicToId(id));
//                setHistory(getEpics().get(id));
                }
                if (getSubtasks().containsKey(id)) {
                    historyManager.addTask(getSubtaskToId(id));
//                setHistory(getSubtasks().get(id));
                }
            }
        }
        flag = true;
    }

//        }

//        for (Integer taskId : history) {
//            historyManager.add(findTask(taskId));
//        }
//        for (Integer id : history) {
//            if (getTasks().containsKey(id)){
//                historyManager.addTask(getTaskToId(id));
//            }
//            if (getEpics().containsKey(id)){
//                historyManager.addTask(getEpicToId(id));
////                setHistory(getEpics().get(id));
//            }
//            if (getSubtasks().containsKey(id)){
//                historyManager.addTask(getSubtaskToId(id));
////                setHistory(getSubtasks().get(id));
//            }
//        }
//    }

    @Override
    protected void save(){ //сохранение на KVServer /по логике вещей должен использоваться этот save а не тот что в FileBacked
        String jsonTasks = gson.toJson(new ArrayList<>(tasks.values()));
//        String jsonTasks = new Gson().toJson(new ArrayList<>(tasks.values()));
        client.put("tasks", jsonTasks);
        String jsonSubtasks = gson.toJson(new ArrayList<>(subtasks.values()));
        client.put("subtasks", jsonSubtasks);
        String jsonEpics = gson.toJson(new ArrayList<>(epics.values()));
        client.put("epics", jsonEpics);

        String jsonHistory = gson.toJson(historyManager.getHistory().stream().map(Task::getId).collect(Collectors.toList()));
        client.put("history", jsonHistory);
    }

//    @Override
//    public HashMap<Integer, Task> getTasks() {
//        return super.getTasks();
//    }
//
//    @Override
//    public HashMap<Integer, Epic> getEpics() {
//        return super.getEpics();
//    }
//
//    @Override
//    public HashMap<Integer, Subtask> getSubtasks() {
//        return super.getSubtasks();
//    }
//
//    @Override
//    public void deleteAllTasks() {
//        super.deleteAllTasks();
//        save();
//    }
//
//    @Override
//    public void deleteAllEpics() {
//        super.deleteAllEpics();
//        save();
//    }
//
//    @Override
//    public void deleteAllSubtasks() {
//        super.deleteAllSubtasks();
//        save();
//    }
//
//    @Override
//    public Task getTaskToId(int id) {
//        Task task = super.getTaskToId(id);
//        save();
//        return task;
//    }
//
//    @Override
//    public Epic getEpicToId(int id) {
//        Epic epic = super.getEpicToId(id);
//        save();
//        return epic;
//    }
//
//    @Override
//    public Subtask getSubtaskToId(int id) {
//        Subtask subtask = super.getSubtaskToId(id);
//        save();
//        return subtask;
//    }
//
//    @Override
//    public int addNewTask(Task task) {
//        int taskId = super.addNewTask(task);
//        save();
//        return taskId;
//    }
//
//    @Override
//    public int addNewEpic(Epic epic) {
//        int epicId = super.addNewEpic(epic);
//        save();
//        return epicId;
//    }
//
//    @Override
//    public Integer addNewSubtask(Subtask subtask) {
//        Integer subtaskId = super.addNewSubtask(subtask);
//        save();
//        return subtaskId;
//    }
//
//    @Override
//    public void updateTask(Task task) {
//        super.updateTask(task);
//        save();
//    }
//
//    @Override
//    public void updateEpic(Epic epic) {
//        super.updateEpic(epic);
//        save();
//    }
//
//    @Override
//    public void updateSubtask(Subtask subtask) {
//        super.updateSubtask(subtask);
//        save();
//    }
//
//    @Override
//    public void removeTask(int taskId) {
//        super.removeTask(taskId);
//        save();
//    }
//
//    @Override
//    public void removeEpic(int epicId) {
//        super.removeEpic(epicId);
//        save();
//    }
//
//    @Override
//    public void removeSubtask(int subtaskId) {
//        super.removeSubtask(subtaskId);
//        save();
//    }
//
//    @Override
//    public List<Task> getHistory() {
//        return super.getHistory();
//    }
//
//    @Override
//    public Set<Task> getPrioritizedTasks() {
//        return super.getPrioritizedTasks();
//    }
//
//    @Override
//    public boolean setPrioritizedTasks(Task task) {
//        return super.setPrioritizedTasks(task);
//    }
//
//    @Override
//    public boolean overlaps(Task task1, Task task2) {
//        return super.overlaps(task1, task2);
//    }
}
