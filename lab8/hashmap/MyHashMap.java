package hashmap;

import java.util.*;

/**
 *  A hash table-backed Map implementation. Provides amortized constant time
 *  access to elements via get(), remove(), and put() in the best case.
 *
 *  Assumes null keys will never be inserted, and does not resize down upon remove().
 *  @author YOUR NAME HERE
 */
public class MyHashMap<K, V> implements Map61B<K, V> {

    /**
     * Protected helper class to store key/value pairs
     * The protected qualifier allows subclass access
     */
    protected class Node {
        K key;
        V value;

        Node(K k, V v) {
            key = k;
            value = v;
        }
    }

    /* Instance Variables */
    private Collection<Node>[] buckets;
    // You should probably define some more!
    private int alive;
    private double MAXLOAD;

    /** Constructors */
    public MyHashMap() {
        buckets = createTable(16);
        MAXLOAD = 0.75;
    }

    public MyHashMap(int initialSize) {
        buckets = createTable(initialSize);
        MAXLOAD = 0.75;
    }

    /**
     * MyHashMap constructor that creates a backing array of initialSize.
     * The load factor (# items / # buckets) should always be <= loadFactor
     *
     * @param initialSize initial size of backing array
     * @param maxLoad maximum load factor
     */
    public MyHashMap(int initialSize, double maxLoad) {
        buckets = createTable(initialSize);
        MAXLOAD = maxLoad;
    }

    /**
     * Returns a new node to be placed in a hash table bucket
     */
    private Node createNode(K key, V value) {
        return new Node(key, value);
    }

    /**
     * Returns a data structure to be a hash table bucket
     *
     * The only requirements of a hash table bucket are that we can:
     *  1. Insert items (`add` method)
     *  2. Remove items (`remove` method)
     *  3. Iterate through items (`iterator` method)
     *
     * Each of these methods is supported by java.util.Collection,
     * Most data structures in Java inherit from Collection, so we
     * can use almost any data structure as our buckets.
     *
     * Override this method to use different data structures as
     * the underlying bucket type
     *
     * BE SURE TO CALL THIS FACTORY METHOD INSTEAD OF CREATING YOUR
     * OWN BUCKET DATA STRUCTURES WITH THE NEW OPERATOR!
     */
    protected Collection<Node> createBucket() {
        return new LinkedList<>();
    }

    /**
     * Returns a table to back our hash table. As per the comment
     * above, this table can be an array of Collection objects
     *
     * BE SURE TO CALL THIS FACTORY METHOD WHEN CREATING A TABLE SO
     * THAT ALL BUCKET TYPES ARE OF JAVA.UTIL.COLLECTION
     *
     * @param tableSize the size of the table to create
     */
    private Collection<Node>[] createTable(int tableSize) {
        Collection<Node>[] table = new Collection[tableSize];
        for (int i = 0; i < tableSize; i++) {
            table[i] = createBucket();
        }
        return table;
    }

    // TODO: Implement the methods of the Map61B Interface below
    // Your code won't compile until you do so!

    private int myhashcode(K key, int length) {
        return (key.hashCode() % length + length) % length;
    }

    @Override
    public void clear() {
        for (Collection<Node> c : buckets) {
            c.clear();
        }
        alive = 0;
    }

    @Override
    public boolean containsKey(K key) {
        int hashCode = myhashcode(key, buckets.length);
        Iterator<Node> iter = buckets[hashCode].iterator();
        while (iter.hasNext()) {
            if (key.equals(iter.next().key)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public V get(K key) {
        int hashCode = myhashcode(key, buckets.length);
        Iterator<Node> iter = buckets[hashCode].iterator();
        while (iter.hasNext()) {
            Node p = iter.next();
            if (key.equals(p.key)) {
                return p.value;
            }
        }
        return null;
    }

    @Override
    public int size() {
        return alive;
    }

    private void resize(int size) {
        Collection<Node>[] newBuckets = createTable(size);
        for (Collection<Node> bucket : buckets) {
            Iterator<Node> iter = bucket.iterator();
            while (iter.hasNext()) {
                Node p = iter.next();
                int hashcode = myhashcode(p.key, newBuckets.length);
                newBuckets[hashcode].add(p);
            }
        }
        buckets = newBuckets;
    }

    @Override
    public void put(K key, V value) {
        int hashcode = myhashcode(key, buckets.length);
        Iterator<Node> iter = buckets[hashcode].iterator();
        while (iter.hasNext()) {
            Node p = iter.next();
            if (p.key.equals(key)) {
                p.value = value;
                return;
            }
        }
        alive += 1;
        double load = ((double) alive) / buckets.length;
        if (load > MAXLOAD) {
            resize(buckets.length * 2);
        }
        hashcode = myhashcode(key, buckets.length);
        buckets[hashcode].add(createNode(key, value));
    }

    private void addKeys(Collection<Node>[] buckets, Set<K> set) {
        for (Collection<Node> bucket : buckets) {
            Iterator<Node> iter = bucket.iterator();
            while (iter.hasNext()) {
                set.add(iter.next().key);
            }
        }
    }

    @Override
    public Set<K> keySet() {
        HashSet<K> set = new HashSet<>();
        addKeys(buckets, set);
        return set;
    }

    @Override
    public V remove(K key) {
        int hashcode = myhashcode(key, buckets.length);
        Iterator<Node> iter = buckets[hashcode].iterator();
        Node p = null;
        while (iter.hasNext()) {
            Node tmp = iter.next();
            if (tmp.key.equals(key)) {
                p = tmp;
            }
        }
        if  (p == null) {
            return null;
        }
        V theVal = p.value;
        buckets[hashcode].remove(p);
        return theVal;
    }

    @Override
    public V remove(K key, V value) {
        int hashcode = myhashcode(key, buckets.length);
        Iterator<Node> iter = buckets[hashcode].iterator();
        Node p = null;
        while (iter.hasNext()) {
            Node tmp = iter.next();
            if (tmp.key.equals(key) && tmp.value.equals(value)) {
                p = tmp;
            }
        }
        if  (p == null) {
            return null;
        }
        V theVal = p.value;
        buckets[hashcode].remove(p);
        return theVal;
    }

    @Override
    public Iterator<K> iterator() {
        return keySet().iterator();
    }
}
