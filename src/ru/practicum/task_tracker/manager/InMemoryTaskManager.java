package ru.practicum.task_tracker.manager;

import ru.practicum.task_tracker.task_status.Status;
import ru.practicum.task_tracker.tasks.Epic;
import ru.practicum.task_tracker.tasks.Subtask;
import ru.practicum.task_tracker.tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private final HistoryManager historyManager = Managers.getDefaultHistory();
    private int generateId = 0;

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
        for (Integer integer : epics.keySet()) { //вытягиваем список id подзадач
            Epic epic = epics.get(integer);
            ArrayList<Integer> subtaskIds = epic.getSubtaskIds();
            subtaskIds.clear(); // очищаем список id подзадач
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
        tasks.put(task.getId(), task);
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
        subtasks.put(subtask.getId(), subtask); //подзадача добавляется в мапу
        epic.addSubtaskId(subtask.getId()); //id подзадачи добавляется в список эпика
        updateEpicStatus(subtask.getEpicId()); //обновление статуса эпика
        return subtask.getId();
    }

    @Override
    public void updateTask(Task task) { // обновление мапы - Tasks
        Task savedTask = tasks.get(task.getId());
        if (savedTask == null) {
            return;
        }
        tasks.put(task.getId(), task);
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
        subtasks.put(subtask.getId(), subtask);
        updateEpicStatus(subtask.getEpicId());
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
            if (subtaskIds.get(id) == subtaskId) {
                idIndex = id;
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
            epic.setStatus(Status.IN_PROGRESS);
        }
        epic.setStatus(status);
    }
}
