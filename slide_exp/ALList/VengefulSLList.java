public class VengefulSLList<T> extends LinkedListDeque<T> {
    private LinkedListDeque<T> deletedItems;

    public VengefulSLList() {
        deletedItems = new LinkedListDeque<T>();
    }

    @Override
    public T removeLast() {
        T x = super.removeLast();
        deletedItems.addLast(x);
        return x;
    }

    public void printLostItems() {
        deletedItems.printDeque();
    }
}
