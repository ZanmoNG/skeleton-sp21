package deque;

import afu.org.checkerframework.checker.oigj.qual.O;

import java.util.Iterator;

public class ArrayDeque<T> implements Deque<T>, Iterable<T> {
    T[] items;
    int headIndex;
    int size;

    public ArrayDeque(){
        items = (T[]) new Object[100];
        size = 0;
        headIndex = 0;
    }

    public int getHeadIndex(){
        return headIndex;
    }

    private void resize(int theSize){
        T[] arr = (T[]) new Object[theSize];
        int newHeadIndex = theSize / 3;
        System.arraycopy(items, headIndex, arr, newHeadIndex, size);
        items = arr;
        headIndex = newHeadIndex;
    }

    public void addFirst(T item){
        if (headIndex - 1 <= 0){
            resize(items.length * 2);
        }
        items[headIndex - 1] = item;
        size++;
        headIndex--;
    }

    public void addLast(T item){
        if (headIndex + size >= items.length){
            resize(items.length* 2);
        }
        items[headIndex+size] = item;
        size++;
    }

    public int size(){
        return size;
    }

    public void printDeque(){
        for (int i = headIndex; i < headIndex + size; i++){
            System.out.print(items[i] + " ");
        }
        System.out.println();
    }

    public T removeFirst(){
        if (size == 0) {
            return null;
        }
        else {
            T theItem = items[headIndex];
            items[headIndex] = null;
            headIndex++;
            size--;
            if (size >= 400 && size < items.length / 4){
                resize(items.length / 2);
            }
            return theItem;
        }
    }

    public T removeLast(){
        if (size == 0) {
            return null;
        }
        else {
            T theItem = items[headIndex + size - 1];
            items[headIndex + size - 1] = null;
            size--;
            if (size >= 400 && size < items.length / 4){
                resize(items.length / 2);
            }
            return theItem;
        }
    }

    public T get(int index){
        return items[headIndex + index];
    }

    private class ADIterator implements Iterator<T> {
        private int i;

        public ADIterator() {
            i = 0;
        }

        @Override
        public boolean hasNext() {
            return i < size;
        }

        @Override
        public T next() {
            T item = items[headIndex + i];
            i++;
            return item;
        }
    }

    @Override
    public Iterator<T> iterator() {
        return new ADIterator();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }else if (o == null) {
            return false;
        }else if (o.getClass() != this.getClass()) {
            return false;
        }else if (((ArrayDeque<?>) o).size() != size) {
            return false;
        }
        for (int i = 0; i < size; i++) {
            if (((ArrayDeque<?>) o).get(i) != get(i))
                return false;
        }
        return true;
    }
}
