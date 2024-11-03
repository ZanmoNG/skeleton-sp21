public class Main {

    public static void main(String[] args) {
        System.out.println(1);
        IntList t = new IntList(3, null);
        t = new IntList(2, t);
        t = new IntList(1, t);
        t = new IntList(1, t);
        System.out.println(1);
        t.addAdjacent();
        System.out.println(t.iterativeSize());
    }
}
