public class RotatingList<T> extends LinkedListDeque<T>{

    public void RotatingRight() {
        T item = removeLast();
        addFirst(item);
    }

}
