package deque;

public class ArrayDeque<T> {
    T[] items;
    int headIndex;
    int size;

    public ArrayDeque(){
        items = (T[]) new Object[100];
        size = 0;
        headIndex = 33;
    }

    private void resize(int theSize){
        T[] arr = (T[]) new Object[theSize];
        int newHeadSize = theSize / 3;
        System.arraycopy(items, headIndex, arr, newHeadSize, size);
        items = arr;
    }

    public void addFirst(T item){
        if (headIndex - 1 < 0){
            resize(size * 2);
        }
        items[headIndex-1] = item;
        size++;
        headIndex--;
    }

    public void addLast(T item){
        if (headIndex + size >= 100){
            resize(size * 2);
        }
        items[headIndex+size] = item;
        size++;
    }

    public boolean isEmpty(){
        return size == 0;
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
            // resize
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
            // resize
            return theItem;
        }
    }

    public T get(int index){
        return items[headIndex + index];
    }
}
