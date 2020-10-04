package buzz.programmers;

import org.javatuples.Pair;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicReference;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.stream.IntStream.range;

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

        last.next = newNode;
        newNode.prev = last;
        last = newNode;
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
        first.prev = newNode;
        newNode.next = first;
        first = newNode;
        ++size;
    }

    @Override
    public void addLast(final T element) {
        add(element);
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

        final AtomicReference<Node> current = new AtomicReference<>(first);
        range(0, index).forEach(ignored -> current.set(current.get().next));

        return current.get().data;
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
    public T remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException(index);
        }

        if (size == 1) {
            final T data = getFirst();
            clear();
            return data;
        }

        final AtomicReference<Node> current = new AtomicReference<>(first);
        range(0, index).forEach(ignored -> current.set(current.get().next));
        final Node currentNode = current.get();

        final T data = currentNode.data;
        if (currentNode == first) {
            first = currentNode.next;
            first.prev = null;
        } else if (currentNode == last) {
            last = currentNode.prev;
            last.next = null;
        } else {
            currentNode.prev.next = currentNode.next;
            currentNode.next.prev = currentNode.prev;
        }
        --size;
        return data;
    }

    @Override
    public boolean remove(final T element) {
        final AtomicReference<Pair<Integer, Node>> current = new AtomicReference<>(Pair.with(0, first));
        range(0, size)
                .takeWhile(ignored -> !current.get().getValue1().data.equals(element))
                .forEach(index -> current.set(Pair.with(index + 1, current.get().getValue1().next)));

        final Node currentNode = current.get().getValue1();
        if (nonNull(currentNode) && currentNode.data.equals(element)) {
            remove(current.get().getValue0());
            return true;
        }
        return false;
    }

    @Override
    public boolean contains(final Object o) {
        final AtomicReference<Node> current = new AtomicReference<>(first);
        range(0, size)
                .takeWhile(ignored -> !current.get().data.equals(o))
                .forEach(ignored -> current.set(current.get().next));
        return nonNull(current.get()) && o.equals(current.get().data);
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
