public class SLList {

    public static class IntNode {
        public int item;
        public IntNode next;

        public IntNode(int f, IntNode n){
            item = f;
            next = n;
        }
    }

    private IntNode first;
    private int size;

    public SLList() {
        first = new IntNode(0, null);
        size = 0;
    }

    public void addFirst(int x){
        IntNode p = first;
        p.next = new IntNode (x, p.next);
        size += 1;
    }

    public void addLast(int x) {
        // 使用递归有一些微妙, 所以需要私有helper函数，见下size()方法
        IntNode p = first;

        /* Advance p to the end of the list. */
        while (p.next != null) {
            p = p.next;
        }

        p.next = new IntNode(x, null);
        size += 1;
    }

    public int getFirst() {
        return first.item;
    }

    private static int old_size(IntNode n) {
        if (n.next == null) {
            return 1;
        }
        else {
            return 1 + old_size(n.next);
        }
    }

    public void deleteFirst()
    {
        IntNode p = first;
        if (p.next != null)
        {
            p.next = p.next.next;
            size--;
        }
    }

    public int old_size() {
        return old_size(first);
    }

    public int size() {
        return size;
    }



}
