public class LinkedListDeque<T> {
    public class LinkedListNode{
        LinkedListNode prev;
        T item;
        LinkedListNode next;

        public LinkedListNode(LinkedListNode p, T x, LinkedListNode n){
            prev = p;
            item = x;
            next = n;
        }
    }

    private LinkedListNode headSentinel;
    private LinkedListNode tailSentinel;
    private int size;

    /** constructor */
    public LinkedListDeque(){
        headSentinel = new LinkedListNode(null, null, null);
        tailSentinel = new LinkedListNode(headSentinel, null, null);
        headSentinel.next = tailSentinel;
        size = 0;
    }

    public void addFirst(T item){
        headSentinel.next = new LinkedListNode(headSentinel, item, headSentinel.next);
        headSentinel.next.next.prev = headSentinel.next;
        size++;
    }

    public void addLast(T item){
        tailSentinel.prev = new LinkedListNode(tailSentinel.prev, item, tailSentinel);
        tailSentinel.prev.prev.next = tailSentinel.prev;
        size++;
    }

    public boolean isEmpty(){
        return size == 0;
    }

    public int size(){
        return size;
    }

    public void printDeque(){
        LinkedListNode p = headSentinel.next;
        while (p != tailSentinel){
            System.out.print(p.item + " ");
            p = p.next;
        }
        System.out.println();
    }

    public T removeLast(){
        if (size == 0)
            return null;
        else {
            T theItem = tailSentinel.prev.item;
            tailSentinel.prev = tailSentinel.prev.prev;
            tailSentinel.prev.next = tailSentinel;
            size--;
            return theItem;
        }
    }

    public T removeFirst(){
        if (size == 0)
            return null;
        else {
            T theItem = headSentinel.next.item;
            headSentinel.next = headSentinel.next.next;
            headSentinel.next.prev = headSentinel;
            size--;
            return theItem;
        }
    }

    public T get(int index){
        LinkedListNode p = headSentinel.next;
        int i;
        for (i = 0; i < size && i < index; i++){
            p = p.next;
        }
        if (i == size)
            return null;
        else
            return p.item;
    }

    private T getRecursiveHelper(int index, LinkedListNode p) {
        if (index == 0)
            return p.item;
        else if (p.next == tailSentinel) {
            return null;
        } else {
            return getRecursiveHelper(index--, p.next);
        }
    }

    public T getRecursive(int index) {
        return getRecursiveHelper(index, headSentinel.next);
    }
}