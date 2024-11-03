public class MyIntList {
    public int first;
    public MyIntList rest;

    public MyIntList(int f, MyIntList r) {
        first = f;
        rest = r;
    }

    public void addFirst(int x) {
        MyIntList t = new MyIntList(first, rest);
        first = x;
        rest = t;
    }

    /** Return the size of the list using no recursion! */
    public int iterativeSize() {
        MyIntList p = this;
        int totalSize = 0;
        while (p != null) {
            totalSize += 1;
            p = p.rest;
        }
        return totalSize;
    }

    public void addAdjacent() {
        if (rest != null ) {
            if (first == rest.first) {
                first = 2 * first;
                rest = rest.rest;
            }
            else {
                rest.addAdjacent();
            }
            this.addAdjacent();
        }
    }

    public static void main(String[] args) {
        System.out.println(1);
        MyIntList t = new MyIntList(3, null);
        t = new MyIntList(2, t);
        t = new MyIntList(1, t);
        t = new MyIntList(1, t);
        System.out.println(1);
        t.addAdjacent();
        System.out.println(t.iterativeSize());
    }
}
