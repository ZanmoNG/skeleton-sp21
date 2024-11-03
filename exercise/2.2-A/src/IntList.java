public class IntList {
    public int first;
    public IntList rest;
    public IntList (int f, IntList r){
        first = f;
        rest = r;
    }

    public void addAdjacent(){
        if (rest != null){
            if (first == rest.first){
                first = 2 * first;
                rest = rest.rest;
                this.addAdjacent();
            }
            else{
                rest.addAdjacent();
            }
        }
    }

    public int size(){
        IntList p = this;
        int count = 1;
        while (p.rest != null){
            p = p.rest;
            count++;
        }
        return count;
    }

    public void addLastWithSquare(int x){
        IntList p = this;
        while (p.rest != null)
        {
            p.rest = new IntList(p.first * p.first, p.rest);
            p = p.rest.rest;
        }
        IntList p2 = new IntList(x, null);
        p.rest = new IntList(p.first * p.first, p2);
    }
}
