package buzz.programmers;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

public class MyLinkedListImpl<T> implements MyLinkedList<T> {

    private Node first = null;
    private Node last = null;
    private int size = 0;

    class Node {
        private final T data;
        private Node next;
        private Node prev;

        private Node(final T data) {
            this.data = data;
        }
    }

    @Override
    public boolean add(final T element) {
        final Node newNode = new Node(element);

        if (isNull(first)) {
            first = last = newNode;
            ++size;
            return true;
        }

        Node current = first;
        while (nonNull(current.next)) {
            current = current.next;
        }

        last = current.next = newNode;
        newNode.prev = current;
        ++size;
        return true;
    }

    @Override
    public void add(int index, final T element) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException(index);
        }

        if (index == 0) {
            addFirst(element);
            return;
        }

        if (index == size) {
            addLast(element);
            return;
        }

        Node current = first.next;
        for (int i = 1; i < index; i++) {
            current = current.next;
        }
        final Node newNode = new Node(element);
        newNode.prev = current.prev;
        newNode.next = current;
        current.prev = newNode;
        ++size;
    }

    @Override
    public void addFirst(final T element) {
        if(0 == size) {
            add(element);
            return;
        }
        final Node newNode = new Node(element);
        newNode.next = first;
        first = newNode;
        ++size;
    }

    @Override
    public void addLast(final T element) {
        if(0 == size) {
            add(element);
            return;
        }
        final Node newNode = new Node(element);
        last.next = newNode;
        last = newNode;
        ++size;
    }

    @Override
    public void clear() {
        first = last = null;
        size = 0;
    }

    @Override
    public T get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException(index);
        }

        if (0 == index) {
            return first.data;
        }

        if (size-1 == index) {
            return last.data;
        }

        Node current = first;
        for (int i = 1; i <= index; i++) {
            current = current.next;
        }
        return current.data;
    }

    @Override
    public T getFirst() {
        return nonNull(first) ? first.data : null;
    }

    @Override
    public T getLast() {
        return nonNull(last) ? last.data : null;
    }

    @Override
    public boolean contains(final Object o) {
        Node current = first;
        for (int i = 0; i < size; i++) {
            if(current.data.equals(o)) {
                return true;
            }
            current = current.next;
        }
        return false;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<>() {
            Node current = first;

            @Override
            public boolean hasNext() {
                return nonNull(current);
            }

            @Override
            public T next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                final T data = current.data;
                current = current.next;
                return data;
            }
        };
    }
}
