public interface List<T> {
    public void addFirst(T x);
    public void addLast(T x);
    public T getFirst();
    public T getLast();
    public T removeLast();
    public T get(int i);
    public T insert(T x, int position);
    public int size();
    default public void print() {
        for (int i = 0; i < size(); i += 1) {
            System.out.print(get(i) + " ");
        }
        System.out.println();
    } // for SLList, it is not the most efficient way to do that.
    
}
