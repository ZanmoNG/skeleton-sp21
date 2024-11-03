public class AList<T> implements List<T>{
    static int MAX = 100;
    T [] items;
    int size;
    static double RFACTOR = 1.5;
    double ratio;

    public AList(){
        items = (T[]) new Object[AList.MAX];
        size = 0;
    }

    /** change the length to theSize*/
    public void resize(int theSize){
        T[] arr = (T[]) new Object[theSize];
        System.arraycopy(items, 0, arr, 0, size);
        items = arr;
    }

    @Override
    public void addLast(T x){
        if (size >= MAX){
            resize((int) (size * RFACTOR));
        }
        items[size] = x;
        size += 1;
    }

    public T getLast(){
        return items[size-1];
    }

    public T get(int index){
        return items[index];
    }

    public int size(){
        return size;
    }

    public T removeLast(){
        T x = getLast();
        size--;
        return x;
    }




}







