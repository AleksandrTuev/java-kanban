package manager;

import enums.Status;
import enums.TaskType;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    protected final HashMap<Integer, Task> tasks = new HashMap<>();
    protected final HashMap<Integer, Epic> epics = new HashMap<>();
    protected final HashMap<Integer, Subtask> subtasks = new HashMap<>();
    protected final HistoryManager historyManager = Managers.getDefaultHistory();
    protected final Set<Task> prioritizedTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime,
        Comparator.nullsLast(Comparator.naturalOrder())).thenComparing(Task::getId));
    protected int generateId = 0;

    @Override
    public Set<Task> getPrioritizedTasks(){
        return prioritizedTasks;
    }

    public boolean setPrioritizedTasks(Task task){
        for (Task other : getPrioritizedTasks()) {
            if (overlaps(task, other)){
                if (task.getId() != other.getId()){
                    System.out.println(task.getName() + " c id=" + task.getId() + " невозможно добавить. Имеется пересечение по времени c " + other.getName()+ " c id=" + other.getId() + ".");
                    return false;
                }
            }
        }
        if (task.getType().equals(TaskType.TASK)){
            if (tasks.get(task.getId()) != null){
                getPrioritizedTasks().remove(tasks.get(task.getId())); //удаление task с тем же id, но со старым состоянием
            }
        }
        if (task.getType().equals(TaskType.SUBTASK)){
            if (subtasks.get(task.getId()) != null){
                getPrioritizedTasks().remove(subtasks.get(task.getId())); //удаление subtask с тем же id, но со старым состоянием
            }
        }
        if (task.getType().equals(TaskType.EPIC)){
            return false;
        }
        getPrioritizedTasks().add(task);
        return true;
    }

    private void updateEpicTime(int epicId){
        LocalDateTime startTime = null;
        LocalDateTime endTime = null;
        
        Epic epic = epics.get(epicId);

        if (epic.getSubtaskIds().isEmpty()){ // обнуляем поля у Epic, если subtasksIds пуст
            epic.setStartTime(null);
            epic.setEndTime(null);
            epic.setDuration(0);
            return;
        }

        for (Integer subtaskId : subtasks.keySet()) {
            if (epic.getId() == subtasks.get(subtaskId).getEpicId()){
                if (startTime == null){
                    startTime = subtasks.get(subtaskId).getStartTime();
                    endTime = subtasks.get(subtaskId).getEndTime();
                    continue;
                }
                if (!startTime.isBefore(subtasks.get(subtaskId).getStartTime())){
                    startTime = subtasks.get(subtaskId).getStartTime();
                }
                if (!endTime.isAfter(subtasks.get(subtaskId).getEndTime())){
                    endTime = subtasks.get(subtaskId).getEndTime();
                }
            }
        }
        if ((startTime != null) && (endTime != null)){
            epic.setStartTime(startTime);
            epic.setEndTime(endTime);
            epic.setDuration(Duration.between(startTime, endTime).toMinutes());
        }
    }

    @Override
    public boolean overlaps(Task task1, Task task2){
        if (task1.getStartTime() == null){
            return false;
        }
        if (!task1.getStartTime().isAfter(task2.getStartTime())){
            return !(task1.getEndTime().isBefore(task2.getStartTime()));
        }else{
            return !(task2.getEndTime().isBefore(task1.getStartTime()));
        }
    }

    @Override
    public HashMap<Integer, Task> getTasks() {
        return tasks;
    }

    @Override
    public HashMap<Integer, Epic> getEpics() {
        return epics;
    }

    @Override
    public HashMap<Integer, Subtask> getSubtasks() {
        return subtasks;
    }

    private int generateId() {
        return ++generateId; // метод по генерации id
    }

    @Override
    public void deleteAllTasks() { //удаление задач
        tasks.clear();
    }

    @Override
    public void deleteAllEpics() { //удаление эпиков
        subtasks.clear(); //при удалении эпика удаляются и подзадачи
        epics.clear();
    }

    @Override
    public void deleteAllSubtasks() { //удаление подзадач
        for (Integer epicId : epics.keySet()) { //вытягиваем список id подзадач
            Epic epic = epics.get(epicId);
            ArrayList<Integer> subtaskIds = epic.getSubtaskIds();
            subtaskIds.clear(); // очищаем список id подзадач
            updateEpicTime(epicId); //обновление времени у Epic
        }
        subtasks.clear();
    }

    @Override
    public Task getTaskToId(int id) { //получение задачи по id
        historyManager.addTask(tasks.get(id)); //запись в список историй
        return tasks.get(id);
    }

    @Override
    public Epic getEpicToId(int id) { //получение эпика по id
        historyManager.addTask(epics.get(id)); //запись в список историй
        return epics.get(id);
    }

    @Override
    public Subtask getSubtaskToId(int id) { //получение подзадачи по id
        historyManager.addTask(subtasks.get(id)); //запись в список историй
        return subtasks.get(id);
    }

    @Override
    public int addNewTask(Task task) { //создание задачи
        task.setId(generateId());
        if (setPrioritizedTasks(task)){
            tasks.put(task.getId(), task);
        }
        return task.getId();
    }

    @Override
    public int addNewEpic(Epic epic) { //создание эпика
        epic.setId(generateId());
        epics.put(epic.getId(), epic);
        return epic.getId();
    }

    @Override
    public Integer addNewSubtask(Subtask subtask) { //создание подзадачи
        Epic epic = epics.get(subtask.getEpicId());
        if (epic == null) { //Проверка на существование эпика, при создании подзадачи
            return null;
        }
        subtask.setId(generateId()); //генерируется id подзадачи и присваивается ей
        if (setPrioritizedTasks(subtask)){
            subtasks.put(subtask.getId(), subtask); //подзадача добавляется в мапу
            epic.addSubtaskId(subtask.getId()); //id подзадачи добавляется в список эпика
            updateEpicStatus(subtask.getEpicId()); //обновление статуса эпика
        }
        return subtask.getId();
    }

    @Override
    public void updateTask(Task task) { // обновление мапы - Tasks
        Task savedTask = tasks.get(task.getId());
        if (savedTask == null) {
            return;
        }
        if (setPrioritizedTasks(task)){ //Если пересечений нет, то task запишется в мапу (исключение пересечение со своим временем)
            tasks.put(task.getId(), task);
        }
    }

    @Override
    public void updateEpic(Epic epic) { // обновление мапы - Epics
        Epic saveEpic = epics.get(epic.getId());
        if (saveEpic == null) {
            return;
        }
        saveEpic.setName(epic.getName());
        saveEpic.setDescription(epic.getDescription());
    }

    @Override
    public void updateSubtask(Subtask subtask) { // обновление мапы - Subtasks
        Subtask saveSubtask = subtasks.get(subtask.getId());
        if (saveSubtask == null) {
            return;
        }
        if (setPrioritizedTasks(subtask)){ //Если пересечений нет, то task запишется в мапу (исключение пересечение со своим временем)
            subtasks.put(subtask.getId(), subtask);
            updateEpicStatus(subtask.getEpicId());
        }
    }

    @Override
    public void removeTask(int taskId) { //удаление задачи
        tasks.remove(taskId);
        historyManager.remove(taskId); //удаление из истории
    }

    @Override
    public void removeEpic(int epicId) { //удаление эпика
        Epic epic = epics.get(epicId);
        ArrayList<Integer> subtaskIds = epic.getSubtaskIds();
        for (Integer subtaskId : subtaskIds) {
            subtasks.remove(subtaskId);
            historyManager.remove(subtaskId); //удаление из истории
        }
        epics.remove(epicId);
        historyManager.remove(epicId); //удаление из истории
    }

    @Override
    public void removeSubtask(int subtaskId) { //удаление сабтаски
        Subtask subtask = subtasks.get(subtaskId);
        Epic epic = epics.get(subtask.getEpicId());
        ArrayList<Integer> subtaskIds = epic.getSubtaskIds();
        int idIndex = 0;
        for (Integer id : subtaskIds) {
            if (id == subtaskId) {
                idIndex = subtaskIds.indexOf(id);
            }
        }
        subtaskIds.remove(idIndex); //удаляем id подзадачи из списка эпика
        subtasks.remove(subtask.getId()); //удаляем подзадачу из мапы subtasks
        historyManager.remove(subtaskId); //удаление из истории
        updateEpicStatus(subtask.getEpicId());
    }

    @Override
    public List<Task> getHistory(){
        return historyManager.getHistory();
    }

    private void updateEpicStatus(int epicId) {
        Epic epic = epics.get(epicId);
        ArrayList<Integer> subtaskIds = epic.getSubtaskIds();
        updateEpicTime(epicId); //обновление времени у Epic
        if (subtaskIds.isEmpty()) {
            epic.setStatus(Status.NEW);
            return;
        }

        Status status = null;
        for (Integer subtaskId : subtaskIds) {
            Subtask subtask = subtasks.get(subtaskId);
            if (status == null) {
                status = subtask.getStatus();
                continue;
            }
            if ((subtask.getStatus() == status)
                    && !status.equals(Status.IN_PROGRESS)) {
                continue;
            }
            status = Status.IN_PROGRESS;
        }
        epic.setStatus(status);
    }
}
