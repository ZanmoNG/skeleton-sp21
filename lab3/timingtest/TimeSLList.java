package timingtest;
import edu.princeton.cs.algs4.Stopwatch;

/**
 * Created by hug.
 */
public class TimeSLList {
    private static void printTimingTable(AList<Integer> Ns, AList<Double> times, AList<Integer> opCounts) {
        System.out.printf("%12s %12s %12s %12s\n", "N", "time (s)", "# ops", "microsec/op");
        System.out.printf("------------------------------------------------------------\n");
        for (int i = 0; i < Ns.size(); i += 1) {
            int N = Ns.get(i);
            double time = times.get(i);
            int opCount = opCounts.get(i);
            double timePerOp = time / opCount * 1e6;
            System.out.printf("%12d %12.2f %12d %12.2f\n", N, time, opCount, timePerOp);
        }
    }



    public static void main(String[] args) {
        timeGetLast();
    }

    public static void createNewNs(AList<Integer> a){
        int[] arr = {1000, 2000, 4000, 8000, 16000, 32000, 64000, 128000};
        for (int j : arr) {
            a.addLast(j);
        }
    }

    public static void exams(AList<Integer> Ns, AList<Double> secs, AList<Integer> ops){
        for (int i = 0; i < Ns.size(); i++) {

            int loopTimes = Ns.get(i);

            SLList<Integer> sl = new SLList<Integer>();
            for (int j = 0; j < loopTimes; j++) {
                sl.addLast(j);
            }

            Stopwatch sw = new Stopwatch();
            int count = 0;

            for (int j = 0; j < 10000; j++) {
                sl.getLast();
                count++;
            }

            double timeInSeconds = sw.elapsedTime();
            secs.addLast(timeInSeconds);
            ops.addLast(count);
        }
    }

    public static void timeGetLast() {
        AList<Integer> Ns = new AList<Integer>();
        AList<Double> secs = new AList<Double>();
        AList<Integer> ops = new AList<Integer>();

        createNewNs(Ns);

        exams(Ns, secs, ops);

        printTimingTable(Ns, secs, ops);
    }

}
