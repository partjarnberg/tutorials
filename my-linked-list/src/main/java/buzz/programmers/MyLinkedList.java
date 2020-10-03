package buzz.programmers;

public interface MyLinkedList<T> extends Iterable<T> {
    /**
     * Appends the specified element to the end of this list.
     * @param element element to be appended to this list
     * @return true if this list changed as a result of the call
     */
    boolean add(T element);

    /**
     * Inserts the specified element at the specified position in this list.
     * @param index index at which the specified element is to be inserted
     * @param element element to be inserted
     */
    void add(int index, T element);

    /**
     * Inserts the specified element at the beginning of this list.
     * @param element the element to add
     */
    void addFirst(T element);

    /**
     * Appends the specified element to the end of this list.
     * @param element the element to add
     */
    void addLast(T element);

    /**
     * Removes all of the elements from this list.
     */
    void clear();

    /**
     * Returns the element at the specified position in this list.
     * @param index index of the element to return
     * @return
     */
    T get(int index);

    /**
     * Returns the first element in this list.
     * @return the first element in this list
     */
    T getFirst();

    /**
     * Returns the last element in this list.
     * @return the last element in this list
     */
    T getLast();

    /**
     * Removes the element at the specified position in this list. Shifts any subsequent elements to the left (subtracts one from their indices). Returns the element that was removed from the list.
     * @param index the index of the element to be removed
     * @return the element previously at the specified position
     */
    T remove(int index);

    /**
     * Removes the first occurrence of the specified element from this list, if it is present. If this list does not contain the element, it is unchanged. More formally, removes the element with the lowest index i such that (o==null ? get(i)==null : o.equals(get(i))) (if such an element exists). Returns true if this list contained the specified element (or equivalently, if this list changed as a result of the call).
     * @param element element to be removed from this list, if present
     * @return true if this list contained the specified element
     */
    boolean remove(T element);

    /**
     * Returns true if this list contains the specified element. More formally, returns true if and only if this list contains at least one element e such that (o==null ? e==null : o.equals(e)).
     * @param o element whose presence in this list is to be tested
     * @return true if this list contains the specified element
     */
    boolean contains(final Object o);

    /**
     * Returns the number of elements in this list.
     * @return the number of elements in this list
     */
    int size();
}
