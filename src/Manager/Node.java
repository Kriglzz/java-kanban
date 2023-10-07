package Manager;

public class Node<Task> {
    public Task data;
    public Node<Task> next;
    public Node<Task> prev;
    public Node<Task> first;
    public Node<Task> last;

    public Node(Task data,Node<Task> next, Node<Task> prev, Node<Task> first, Node<Task> last) {
        this.data = data;
        this.next = next;
        this.prev = prev;
        this.first = first;
        this.last = last;
    }
}
