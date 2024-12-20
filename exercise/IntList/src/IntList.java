public class IntList {
    public int first;
    public IntList rest;

    public IntList(int f, IntList r) {
        first = f;
        rest = r;
    }

    public void addFirst(int x) {
        IntList t = new IntList(first, rest);
        first = x;
        rest = t;
    }

    /** Return the size of the list using no recursion! */
    public int iterativeSize() {
        IntList p = this;
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


}