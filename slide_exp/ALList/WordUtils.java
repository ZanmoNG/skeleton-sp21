public class WordUtils {



    public static String longest(AList<String> s) {
        if (s.size == 0)
            return null;
        String max = s.get(0);
        for (int i = 1; i < s.size(); i++) {
            String tmp = s.get(i);
            if (max.length() < tmp.length()) {
                max = tmp;
            }
        }
        return max;

    }
}
