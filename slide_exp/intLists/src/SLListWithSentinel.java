/** An SLList is a list of integers, which hides the terrible truth
 * of the nakedness within. */
public class SLListWithSentinel {
    private static class IntNode {
        public int item;
        public IntNode next;

        public IntNode(int i, IntNode n) {
            item = i;
            next = n;
        }
    }

    /* The first item (if it exists) is at sentinel.next. */
    private IntNode sentinel;
    private int size;

    private static void lectureQuestion() {
        SLList L = new SLList();
        IntNode n = new IntNode(5, null);
    }

    /** Creates an empty SLList. */
    public SLListWithSentinel() {
        sentinel = new IntNode(63, null);
        size = 0;
    }

    public SLListWithSentinel(int x) {
        sentinel = new IntNode(63, null);
        sentinel.next = new IntNode(x, null);
        size = 1;
    }

    public SLListWithSentinel(int[] x)
    {
        sentinel = new IntNode(63, null);
        size = 0;
        int i = x.length - 1;
        for (; i >= 0; i--)
        {
            addFirst(x[i]);
        }
    }

    /** Adds x to the front of the list. */
    public void addFirst(int x) {
        sentinel.next = new IntNode(x, sentinel.next);
        size = size + 1;
    }

    /** Returns the first item in the list. */
    public int getFirst() {
        return sentinel.next.item;
    }

    public void deleteFirst()
    {
        if (sentinel.next != null)
        {
            sentinel.next = sentinel.next.next;
            size--;
        }
    }

    /** Adds x to the end of the list. */
    public void addLast(int x) {
        size = size + 1;

        IntNode p = sentinel;

        /* Advance p to the end of the list. */
        while (p.next != null) {
            p = p.next;
        }

        p.next = new IntNode(x, null);
    }

    /** Returns the size of the list. */
    public int size() {
        return size;
    }


}