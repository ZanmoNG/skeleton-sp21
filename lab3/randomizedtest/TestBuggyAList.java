package randomizedtest;

import edu.princeton.cs.algs4.StdRandom;
import net.sf.saxon.type.StringConverter;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by hug.
 */
public class TestBuggyAList {

    @Test
    public void testThreeAddThreeRemove(){
        int[] arr = {4, 5, 6};

        AListNoResizing<Integer> cL = new AListNoResizing<Integer>();
        BuggyAList<Integer> bL = new BuggyAList<Integer>();

        for (int j : arr) {
            cL.addLast(j);
            bL.addLast(j);
        }
        assertEquals(cL.size(), bL.size());
        assertEquals(cL.removeLast(), bL.removeLast());
        assertEquals(cL.removeLast(), bL.removeLast());
        assertEquals(cL.removeLast(), bL.removeLast());
    }

    @Test
    public void randomizedTest() {
        BuggyAList<Integer> L = new BuggyAList<>();
        AListNoResizing<Integer> correct = new AListNoResizing<>();

        int N = 5000;
        for (int i = 0; i < N; i += 1) {
            int operationNumber = StdRandom.uniform(0, 4);
            if (operationNumber == 0) {
                // addLast
                int randVal = StdRandom.uniform(0, 100);
                L.addLast(randVal);
                correct.addLast(randVal);
            } else if (operationNumber == 1) {
                // size
                int size = L.size();
                int size2 = correct.size();
                assertEquals(size, size2);
            } else if (L.size() > 0 && operationNumber == 2) {
                // getLast
                int item = L.getLast();
                int item2 = L.getLast();
                assertEquals(item, item2);
            } else if (L.size() > 0 && operationNumber == 3) {
                // removeLast
                int item = L.removeLast();
                int item2 = correct.removeLast();
                assertEquals(item, item2);
            }
        }
    }
}
