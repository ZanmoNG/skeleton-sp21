package deque;

import java.util.Comparator;

public class MaxArrayDeque<T> extends ArrayDeque<T>{

    private Comparator<T> cp;

    public MaxArrayDeque(Comparator<T> c) {
        cp = c;
    }

    public T max() {
        return max(cp);
    }

    public T max(Comparator<T> c) {
        if (size == 0)
            return null;
        int maxdex = 0;
        if (size == 1)
            return get(maxdex);
        for (int i = 0; i < size(); i++) {
            T a = get(maxdex);
            T b = get(i);
            if (c.compare(a, b) < 0) {
                maxdex = i;
            }
        }
        return get(maxdex);
    }


}
