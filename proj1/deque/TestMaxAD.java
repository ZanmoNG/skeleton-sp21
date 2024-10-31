package deque;
import org.junit.Test;

import java.util.Comparator;

import static org.junit.Assert.*;

public class TestMaxAD {
    private static class Dog {
        String name;
        int weight;

        public Dog(String theName, int theWeight) {
            name = theName;
            weight = theWeight;
        }

        private static class NameComparator implements Comparator<Dog> {
            public int compare(Dog a, Dog b) {
                return a.name.compareTo(b.name);
            }
        }

        public static Comparator<Dog> GetNameComparator() {
            return new NameComparator();
        }

        private static class WeightComparator implements Comparator<Dog> {
            public int compare(Dog a, Dog b) {
                return a.weight - b.weight;
            }
        }

        public static Comparator<Dog> GetWeightComparator() {
            return new WeightComparator();
        }

        public String toString() {
            return name;
        }
    }

    @Test
    public void NameComparatorTest() {
        MaxArrayDeque<Dog> madd =
                new MaxArrayDeque<>(Dog.GetNameComparator());

        Dog expect = new Dog("Huicr", 14);
        Dog except2 = new Dog("Ellin", 23);

        madd.addFirst(expect);
        madd.addFirst(new Dog("Atusa", 11));
        madd.addFirst(new Dog("Clima", 15));
        madd.addFirst(new Dog("Billy", 20));
        madd.addFirst(except2);
        madd.addFirst(new Dog("Duufu", 9));



        Dog result = madd.max();
        assertEquals(result, expect);


    }

    @Test
    public void WeightComparatorTest() {
        MaxArrayDeque<Dog> madd =
                new MaxArrayDeque<>(Dog.GetWeightComparator());

        Dog except = new Dog("Huicr", 14);
        Dog except2 = new Dog("Ellin", 23);

        madd.addFirst(new Dog("Atusa", 11));
        madd.addFirst(new Dog("Clima", 15));
        madd.addFirst(new Dog("Billy", 20));
        madd.addFirst(except2);
        madd.addFirst(except);
        madd.addFirst(new Dog("Duufu", 9));

        Dog result2 = madd.max(Dog.GetWeightComparator());
        assertEquals(result2, except2);
    }





}
