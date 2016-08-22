package elevatorsystem.gui.util;

import java.util.ArrayList;

/**
 *
 * @author merve
 * @param <E>
 */
public class Queue<E> {

    private final ArrayList<E> list;

    public Queue() {
        list = new ArrayList<>();
    }

    public boolean isEmpty() {
        return list.isEmpty();
    }
    
    public E peek(){
        return list.get(0);
    }

    /**
     * Adds an element to the stack.
     *
     * @param item
     */
    public void put(E item) {
        list.add(item);
    }

    /**
     * Removes and returns the last element.
     *
     * @return
     */
    public E get() {
        E item = null;
        if (!isEmpty()) {
            item = list.get(0);
            for (int i = 1; i < getSize(); i++) {
                E e = list.get(i);
                list.add(i - 1, item);
            }
        }
        return item;
    }

    /**
     * Empties the queue.
     */
    public void removeAll() {
        list.clear();
    }

    /**
     * Returns the amount of elements.
     *
     * @return
     */
    public int getSize() {
        return list.size();
    }

}
