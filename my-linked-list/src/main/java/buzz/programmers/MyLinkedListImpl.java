package buzz.programmers;

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
        } else {
            last.next = newNode;
            newNode.prev = last;
            last = newNode;
        }
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

        final AtomicReference<Node> current = new AtomicReference<>(first);
        range(0, size)
            .mapToObj(ignore -> current.getAndSet(current.get().next))
            .skip(index).findFirst().ifPresent(node -> {
                final Node newNode = new Node(element);
                newNode.prev = node.prev;
                newNode.next = node;
                node.prev.next = newNode;
                node.prev = newNode;
                ++size;
            });
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

        final AtomicReference<Node> current = new AtomicReference<>(first);
        return range(0, size)
                .mapToObj(ignore -> current.getAndSet(current.get().next).data)
                .skip(index).findFirst().orElseThrow();
    }

    @Override
    public T getFirst() {
        if(isNull(first)) {
            throw new NoSuchElementException();
        }
        return first.data;
    }

    @Override
    public T getLast() {
        if(isNull(last)) {
            throw new NoSuchElementException();
        }
        return last.data;
    }

    @Override
    public T remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException(index);
        }

        final AtomicReference<Node> current = new AtomicReference<>(first);
        return range(0, size)
                .mapToObj(ignore -> current.getAndSet(current.get().next))
                .skip(index).findFirst()
                .map(remove -> {
                    final T data = remove.data;
                    if (size == 1) {
                        clear();
                        return data;
                    }
                    removeSingle(remove);
                    --size;
                    return data;
                }).orElseThrow();
    }

    @Override
    public boolean remove(final T element) {
        final AtomicReference<Node> current = new AtomicReference<>(first);
        return range(0, size)
                .mapToObj(ignore -> current.getAndSet(current.get().next))
                .filter(node -> node.data.equals(element))
                .findFirst().stream().anyMatch(remove -> {
                    if (size == 1) {
                        clear();
                    } else {
                        removeSingle(remove);
                        --size;
                    }
                    return true;
                });
    }

    @Override
    public boolean contains(final Object o) {
        final AtomicReference<Node> current = new AtomicReference<>(first);
        return range(0, size)
                .mapToObj(ignore -> current.getAndSet(current.get().next))
                .anyMatch(node -> node.data.equals(o));
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

    private void removeSingle(final Node remove) {
        if (remove == first) {
            first = remove.next;
            first.prev = null;
        } else if (remove == last) {
            last = remove.prev;
            last.next = null;
        } else {
            remove.prev.next = remove.next;
            remove.next.prev = remove.prev;
        }
    }
}
