package ru.practicum.task_tracker.manager;

import ru.practicum.task_tracker.tasks.Epic;
import ru.practicum.task_tracker.tasks.Subtask;
import ru.practicum.task_tracker.tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskTracker {
    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, Epic> epics = new HashMap<>();
    private HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private int generateId = 0;

    public HashMap<Integer, Task> getTasks() {
        return tasks;
    }

    public void setTasks(HashMap<Integer, Task> tasks) {
        this.tasks = tasks;
    }

    public HashMap<Integer, Epic> getEpics() {
        return epics;
    }

    public void setEpics(HashMap<Integer, Epic> epics) {
        this.epics = epics;
    }

    public HashMap<Integer, Subtask> getSubtasks() {
        return subtasks;
    }

    public void setSubtasks(HashMap<Integer, Subtask> subtasks) {
        this.subtasks = subtasks;
    }

    private int generateId() {
        return ++generateId; // метод по генерации id
    }

    public void deleteAllTasks() { //удаление задач
        tasks.clear();
    }

    public void deleteAllEpics() { //удаление эпиков
        subtasks.clear(); //при удалении эпика удаляются и подзадачи
        epics.clear();
    }

    public void deleteAllSubtasks() { //удаление подзадач
        for (Integer integer : epics.keySet()) { //вытягиваем список id подзад
            Epic epic = epics.get(integer);
            ArrayList<Integer> subtaskIds = epic.getSubtaskIds();
            subtaskIds.clear(); // очищаем список id подзадач
        }
        subtasks.clear();
    }

    public Task getTaskToId(int id) { //получение задачи по id
        return tasks.get(id);
    }

    public Epic getEpicToId(int id) { //получение эпика по id
        return epics.get(id);
    }

    public Subtask getSubtaskToId(int id) { //получение подзадачи по id
        return subtasks.get(id);
    }

    public int addNewTask(Task task) { //создание задачи
        task.setId(generateId());
        tasks.put(task.getId(), task);
        return task.getId();
    }

    public int addNewEpic(Epic epic) { //создание эпика
        epic.setId(generateId());
        epics.put(epic.getId(), epic);
        return epic.getId();
    }

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

    public void updateTask(Task task) { // обновление мапы - Tasks
        Task savedTask = tasks.get(task.getId());
        if (savedTask == null) {
            return;
        }
        tasks.put(task.getId(), task);
    }

    public void updateEpic(Epic epic) { // обновление мапы - Epics
        Epic saveEpic = epics.get(epic.getId());
        if (saveEpic == null) {
            return;
        }
        saveEpic.setName(epic.getName());
        saveEpic.setDescription(epic.getDescription());
    }

    public void updateSubtask(Subtask subtask) { // обновление мапы - Subtasks
        Subtask saveSubtask = subtasks.get(subtask.getId());
        if (saveSubtask == null) {
            return;
        }
        subtasks.put(subtask.getId(), subtask);
        updateEpicStatus(subtask.getEpicId());
    }

    public void removeTask(int taskId) { //удаление задачи
        tasks.remove(taskId);
    }

    public void removeEpic(int epicId) { //удаление эпика
        Epic epic = epics.get(epicId);
        ArrayList<Integer> subtaskIds = epic.getSubtaskIds();
        for (Integer subtaskId : subtaskIds) {
            subtasks.remove(subtaskId);
        }
        epics.remove(epicId);
    }

    public void removeSubtask(int subtaskId) { //удаление сабтаски
        Subtask subtask = subtasks.get(subtaskId);
        Epic epic = epics.get(subtask.getEpicId());
        ArrayList<Integer> subtaskIds = epic.getSubtaskIds();
        int idIndex = 0;
        for (int i = 0; i < subtaskIds.size(); i++) {
            if (subtaskIds.get(i) == subtaskId) {
                idIndex = i;
            }
        }
        subtaskIds.remove(idIndex); //удаляем id подзадачи из списка эпика
        subtasks.remove(subtask.getId()); //удаляем подзадачу из мапы subtasks
        updateEpicStatus(subtask.getEpicId());
    }

    private void updateEpicStatus(int epicId) {
        Epic epic = epics.get(epicId);
        ArrayList<Integer> subtaskIds = epic.getSubtaskIds();
        if (subtaskIds.isEmpty()) {
            epic.setStatus("NEW");
            return;
        }

        String status = null;
        for (Integer subtaskId : subtaskIds) {
            Subtask subtask = subtasks.get(subtaskId);
            if (status == null) {
                status = subtask.getStatus();
                continue;
            }
            if (status.equals(subtask.getStatus())
                    && !status.equals("IN_PROGRESS")) {
                continue;
            }
            epic.setStatus("IN_PROGRESS");
        }
        epic.setStatus(status);
    }
}
