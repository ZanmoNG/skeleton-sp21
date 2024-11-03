public class Main {
    public static void main(String[] args){
        IntList t = new IntList(3, null);
        t = new IntList(2, t);
        t = new IntList(1, t);
        t = new IntList(1, t);

        t.addLastWithSquare(7);
        System.out.println(t.size());
    }
}