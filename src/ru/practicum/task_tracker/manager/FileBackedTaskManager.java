package manager;

import exception.ManagerSaveException;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private final File file;

    FileBackedTaskManager(){
        file = new File("data.csv");
    };
    FileBackedTaskManager(File file){
        this.file = file;
    };

    public static FileBackedTaskManager loadFromFile(File file){
        FileBackedTaskManager newTaskManager = new FileBackedTaskManager(file);
        List<String> lines = new ArrayList<>();

        try(BufferedReader reader = new BufferedReader(new FileReader(file))){
            Task task;

            while (reader.ready()){
                String line = reader.readLine();
                lines.add(line);
            }

            for (int i = 1; i < lines.size(); i++) {
                if (lines.get(i).isEmpty()){

                    //проверка в файле наличия строки просмотра тасок
                    if (lines.size() == i + 1){
                        break;
                    }

                    List<Integer> ids = CSVFormatter.historyFromString(lines.get(i+1));
                    for (Integer id : ids) {
                        if (newTaskManager.getTasks().containsKey(id)){
                            newTaskManager.setHistory(newTaskManager.getTasks().get(id));
                        }
                        if (newTaskManager.getEpics().containsKey(id)){
                            newTaskManager.setHistory(newTaskManager.getEpics().get(id));
                        }
                        if (newTaskManager.getSubtasks().containsKey(id)){
                            newTaskManager.setHistory(newTaskManager.getSubtasks().get(id));
                        }
                    }
                    break;
                } else {
                    task = CSVFormatter.fromString(lines.get(i));
                    switch (task.getType()){
                        case EPIC:
                            newTaskManager.addEpic((Epic) task, newTaskManager);
                            break;
                        case SUBTASK:
                            newTaskManager.addSubtask((Subtask) task, newTaskManager);
                            Epic epic = newTaskManager.getEpics().get(((Subtask) task).getEpicId());
                            epic.addSubtaskId(task.getId());
                            break;
                        case TASK:
                            newTaskManager.addTask(task, newTaskManager);
                            break;
                    }
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        return newTaskManager;
    }

    public void save(){
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))){

            writer.write("id,type,name,status,description,startTime,duration,epic\n");

            for (Integer taskId : getTasks().keySet()) {
                Task task = getTasks().get(taskId);
                writer.write(CSVFormatter.toString(task) + "\n");
            }

            for (Integer epicId : getEpics().keySet()) {
                Epic task = getEpics().get(epicId);
                writer.write(CSVFormatter.toString(task) + "\n");
            }

            for (Integer subtaskId : getSubtasks().keySet()) {
                Subtask task = getSubtasks().get(subtaskId);
                writer.write(CSVFormatter.toString(task) + "\n");
            }
            writer.write("\n");
            writer.write(CSVFormatter.historyToString(historyManager));

        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при сохранении в файл", e);
        }

    }

    //метод по восстановлению информации по таске из файла
    private void addTask(Task task, FileBackedTaskManager newTaskManager){
        newTaskManager.tasks.put(task.getId(), task);
        maxId(task.getId());
    }

    private void addEpic(Epic epic, FileBackedTaskManager newTaskManager){
        newTaskManager.epics.put(epic.getId(), epic);
        maxId(epic.getId());
    }

    private void addSubtask(Subtask subtask, FileBackedTaskManager newTaskManager){
        newTaskManager.subtasks.put(subtask.getId(), subtask);
        maxId(subtask.getId());
    }

    private void maxId(int id){
        if (this.generateId < id){
            this.generateId = id;
        }
    }

    private void setHistory(Task task){
        historyManager.addTask(task);
    }

    @Override
    public HashMap<Integer, Task> getTasks() {
        return super.getTasks();
    }

    @Override
    public HashMap<Integer, Epic> getEpics() {
        return super.getEpics();
    }

    @Override
    public HashMap<Integer, Subtask> getSubtasks() {
        return super.getSubtasks();
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }

    @Override
    public void deleteAllEpics() {
        super.deleteAllEpics();
        save();
    }

    @Override
    public void deleteAllSubtasks() {
        super.deleteAllSubtasks();
        save();
    }

    @Override
    public Task getTaskToId(int id) {
        Task task = super.getTaskToId(id);
        save();
        return task;
    }

    @Override
    public Epic getEpicToId(int id) {
        Epic epic = super.getEpicToId(id);
        save();
        return epic;
    }

    @Override
    public Subtask getSubtaskToId(int id) {
        Subtask subtask = super.getSubtaskToId(id);
        save();
        return subtask;
    }

    @Override
    public int addNewTask(Task task) {
        int taskId = super.addNewTask(task);
        save();
        return taskId;
    }

    @Override
    public int addNewEpic(Epic epic) {
        int epicId = super.addNewEpic(epic);
        save();
        return epicId;
    }

    @Override
    public Integer addNewSubtask(Subtask subtask) {
        Integer subtaskId = super.addNewSubtask(subtask);
        save();
        return subtaskId;
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void removeTask(int taskId) {
        super.removeTask(taskId);
        save();
    }

    @Override
    public void removeEpic(int epicId) {
        super.removeEpic(epicId);
        save();
    }

    @Override
    public void removeSubtask(int subtaskId) {
        super.removeSubtask(subtaskId);
        save();
    }

    @Override
    public List<Task> getHistory() {
        return super.getHistory();
    }
}
