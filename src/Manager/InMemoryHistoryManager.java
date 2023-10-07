package Manager;

import Task.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    static final int MAX_HISTORY_SIZE = 10;
    private Node<Task> head;
    private Node<Task> tail;
    private ArrayList<Task> taskHistory = new ArrayList<>();
    private LinkedList<Task> customLinkedList = new LinkedList<>();
    private Map<Integer, Node<Task>> linkedListHashTable = new HashMap();

    private void linkLast(Task task) {

        final Node<Task> newNode = new Node<>(task, null, tail, null, null);
        if (newNode.next != null) {
            newNode.next.prev = newNode;
        }
        if (tail == null) {
            head = newNode;
        } else {
            tail.next = newNode;
        }
        tail = newNode;
    }

    private void getTasks() {
        taskHistory.addAll(customLinkedList);
    }

    private void removeNode(Node<Task> node) {
        if (node == null) {
            return;
        }
        if (node == head) {
            head = node.next;
        }
        if (node == tail) {
            tail = node.prev;
        }
        if (node.prev != null) {
            node.prev.next = node.next;
        }
        if (node.next != null) {
            node.next.prev = node.prev;
        }
    }


    @Override
    public void addToHistory(Task task) {

        if (linkedListHashTable.size() < MAX_HISTORY_SIZE) {
            linkLast(task);
            getTasks();
            linkedListHashTable.put(task.getId(), tail);
            customLinkedList.add(task);
        } else {
            removeNode(head);
            linkLast(task);
            getTasks();
            linkedListHashTable.put(task.getId(), tail);
            customLinkedList.add(task);
        }
    }

    @Override
    public void remove(int id) {
        if (linkedListHashTable.containsKey(id)) {
            removeNode(linkedListHashTable.get(id));
            linkedListHashTable.remove(id);
        }
    }

    @Override
    public List<Task> getHistory() {
        getTasks();
        return taskHistory;
    }
}
