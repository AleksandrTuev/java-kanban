package ru.practicum.task_tracker.manager;

import ru.practicum.task_tracker.tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {
    private final Map<Integer, Node> nodeMap = new HashMap<>();
    private Node first;
    private Node last;

    private ArrayList<Task> getTasks(){
        ArrayList<Task> tasks = new ArrayList<>();

        Node node = first;
        while (node != null){
            tasks.add(node.task);
            node = node.next;
        }
        return tasks;
    }

    private void removeNode(Node node) {
        //основная логика по удалению, т.е. переключение ссылок и т.д.
        if (node.prev != null){
            //нода не первая
            node.prev.next = node.next;
            if (node.next == null){
                //удаляемая нода последняя
                last = node.prev;
            } else {
                //если нода нвходится в середине
                node.next.prev = node.prev;
            }
        } else {
            //нода первая
            first = node.next;
            if (first == null){
                last = null;
                //была одна нода и мы её удалили
            } else {
                //не одна
                first.prev = null;
            }
        }
    }

    private void linkLast(Task task){
        Node oldLast = last;
        Node newNode = new Node(oldLast, task, null);
        last = newNode;

        if(oldLast == null){
            first = newNode;
        } else {
            oldLast.next =newNode;
        }
    }

    @Override
    public void addTask(Task task) {
        if (task == null){
            return;
        }
        int id = task.getId();
        remove(id);
        linkLast(task);
        nodeMap.put(id,last);
    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }

    @Override
    public void remove(int id) {
        Node node = nodeMap.remove(id);
        if (node == null){
            return;
        }
        removeNode(node);
    }

    public static class Node {
        private Task task;
        private Node prev;
        private Node next;

        public Node(Node prev, Task task, Node next) {
            this.prev = prev;
            this.task = task;
            this.next = next;
        }
    }
}
