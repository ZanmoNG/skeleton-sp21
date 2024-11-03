public class Sort {
    /** Sorts strings destructively  */
    public static void sort(String[] x){
        sort(x, 0);
    }

    /** sort x from start to end */
    private static void sort(String[] x, int start) {
        // find the smallest
        // move to the front
        // repeat in the rest N-1
        if (start == x.length){
            return;
        }
        int smallest = findSmallest(x, start);
        swap(x, smallest, start);
        sort(x, start + 1);
    }

    /** Returns the smallest string in x.
     *  @source  Got help from 菜鸟教程 */
    public static int findSmallest(String[] x, int start){
        int index = start;
        for (int i = start; i < x.length; i += 1){
            int cmp = x[index].compareTo(x[i]);
            if (cmp > 0){
                index = i;
            }
        }
        return index;
    }

    /** Swap the ath and bth element of x */
    public static void swap(String[] x, int a, int b){
        String tmp = x[a];
        x[a] = x[b];
        x[b] = tmp;
     }

}
