package deque;

import java.util.Iterator;

public class LinkedListDeque<T> implements Deque<T>, Iterable<T> {
    private class LinkedListNode {
        LinkedListNode prev;
        T item;
        LinkedListNode next;

        LinkedListNode(LinkedListNode p, T x, LinkedListNode n) {
            prev = p;
            item = x;
            next = n;
        }
    }

    private LinkedListNode headSentinel;
    private LinkedListNode tailSentinel;
    private int size;

    /** constructor */
    public LinkedListDeque() {
        headSentinel = new LinkedListNode(null, null, null);
        tailSentinel = new LinkedListNode(headSentinel, null, null);
        headSentinel.next = tailSentinel;
        size = 0;
    }

    public void addFirst(T item) {
        headSentinel.next = new LinkedListNode(headSentinel, item, headSentinel.next);
        headSentinel.next.next.prev = headSentinel.next;
        size++;
    }

    public void addLast(T item) {
        tailSentinel.prev = new LinkedListNode(tailSentinel.prev, item, tailSentinel);
        tailSentinel.prev.prev.next = tailSentinel.prev;
        size++;
    }

    public int size() {
        return size;
    }

    public void printDeque() {
        LinkedListNode p = headSentinel.next;
        while (p != tailSentinel) {
            System.out.print(p.item + " ");
            p = p.next;
        }
        System.out.println();
    }

    public T removeLast() {
        if (size == 0) {
            return null;
        } else {
            T theItem = tailSentinel.prev.item;
            tailSentinel.prev = tailSentinel.prev.prev;
            tailSentinel.prev.next = tailSentinel;
            size--;
            return theItem;
        }
    }

    public T removeFirst() {
        if (size == 0) {
            return null;
        } else {
            T theItem = headSentinel.next.item;
            headSentinel.next = headSentinel.next.next;
            headSentinel.next.prev = headSentinel;
            size--;
            return theItem;
        }
    }

    public T get(int index) {
        LinkedListNode p = headSentinel.next;
        int i;
        for (i = 0; i < size && i < index; i++) {
            p = p.next;
        }
        if (i == size) {
            return null;
        } else {
            return p.item;
        }
    }

    private T getRecursiveHelper(int index, LinkedListNode p) {
        if (index == 0) {
            return p.item;
        } else {
            return getRecursiveHelper(index--, p.next);
        }
    }

    public T getRecursive(int index) {
        if (index >= size) {
            return null;
        }
        return getRecursiveHelper(index, headSentinel.next);
    }

    private class LLDIterator implements Iterator<T> {
        private LinkedListNode p;

        LLDIterator() {
            p = headSentinel.next;
        }

        @Override
        public boolean hasNext() {
            return p != tailSentinel;
        }

        @Override
        public T next() {
            T theItem = p.item;
            p = p.next;
            return theItem;
        }
    }

    public Iterator<T> iterator() {
        return new LLDIterator();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o == null) {
            return false;
        } else if (o instanceof Deque) {
            return false;
        } else if (((Deque<?>) o).size() != size) {
            return false;
        }
        for (int i = 0; i < size; i++) {
            if (!((Deque<?>) o).get(i).equals(get(i))) {
                return false;
            }
        }
        return true;
    }
}
