package deque;

public class ArrayDeque<T> {
    T[] items;
    int headIndex;
    int size;

    public ArrayDeque(){
        items = (T[]) new Object[100];
        size = 0;
        headIndex = 0;
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
        items[headIndex-1] = item;
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
}
