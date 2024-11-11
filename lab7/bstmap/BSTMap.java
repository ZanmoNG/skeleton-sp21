package bstmap;

import java.util.Iterator;
import java.util.Set;
import java.util.ArrayList;

public class BSTMap<K extends Comparable<K>, V> implements Map61B<K, V>{

    private class BSTNode {
        private K key;
        private V value;

        private BSTNode left;
        private BSTNode right;

        public BSTNode (K theKey, V theValue) {
            key = theKey;
            value = theValue;

            left = null;
            right = null;
        }
    }

    private BSTNode sentinel;
    private int size;

    public BSTMap() {
        sentinel = new BSTNode(null, null);
        size = 0;
    }

    private void deleteAll(BSTNode p) {
        if (p != null) {
            deleteAll(p.left);
            deleteAll(p.right);
            p.left = null;
            p.right = null;
        }
    }

    @Override
    public void clear() {
        deleteAll(sentinel);
        size = 0;
    }

    /** return the node with the right key
     *  null if not found
     * @param t
     * @param key
     * @return
     */
    private BSTNode find(BSTNode t, K key){
        if (t == null || t.key.compareTo(key) == 0) {
            return t;
        } else if (key.compareTo(t.key) < 0) {
            return find(t.left, key);
        } else if (key.compareTo(t.key) > 0) {
            return find(t.right, key);
        } else {
            System.out.println("sth wrong in find");
            return null;
        }
    }

    @Override
    public V get(K key){
        K k = (K) key;
        BSTNode p = find(sentinel.left, k);
        if (p == null) {
            return null;
        } else {
            return p.value;
        }
    }

    @Override
    public boolean containsKey(K key) {
        K k = (K) key;
        BSTNode p = find(sentinel.left, k);
        return p != null;
    }

    @Override
    public int size(){
        return size;
    }


    private void insert(BSTNode p, K key, V value) {
        BSTNode next;
        if (key.compareTo(p.key) < 0) {

        }
    }

    private BSTNode put(BSTNode t, K key, V value) {
        if (t == null) {
            size += 1;
            return new BSTNode(key, value);
        } else if (key.compareTo(t.key) < 0) {
            t.left = put(t.left, key, value);
        } else if (key.compareTo(t.key) > 0) {
            t.right = put(t.right, key, value);
        } else {
            t.value = value;
        }
        return t;
    }

    @Override
    public void put(K key, V value){
        sentinel.left = put(sentinel.left, key, value);
    }

    public Set<K> keySet(){
        throw new UnsupportedOperationException("not yet");
    }

//    /** return the next bigger node of the key
//     *  works only in case that t has 2 children */
//    private BSTNode nextNode(BSTNode t) {
//        t = t.right;
//        while (t.left != null) {
//            t = t.left;
//        }
//        return t;
//    }

//    /** find the parent of t, return sentinel if t is root */
//    private BSTNode findParent(BSTNode t, BSTNode p) {
//        if (p.left == t) {
//            return p;
//        } else if (p.right == t) {
//            return p;
//        } else if (t.key.compareTo(p.key) < 0) {
//            return findParent(t, p.left);
//        } else if (t.key.compareTo(p.key) > 0) {
//            return findParent(t, p.right);
//        } else {
//            System.out.println("sth wrong in findParent");
//            return null;
//        }
//    }

    private BSTNode Min(BSTNode t) {
        if (t == null) {
            return null;
        } else if (t.left == null) {
            return t;
        } else {
            return Min(t.left);
        }
    }

    private BSTNode deleteMin(BSTNode t) {
        if (t == null) {
            return null;
        } else if (t.left == null) {
            BSTNode x = t.right;
            t = null;
            return x;
        } else {
            t.left = deleteMin(t.left);
            return t;
        }
    }

    private BSTNode delete(BSTNode t, K key) {
        if (t == null) {
            return null;
        }
        int cmp = key.compareTo(t.key);
        if (cmp < 0) {
            t.left = delete(t.left, key);
            return t;
        } else if (cmp > 0) {
            t.right = delete(t.right, key);
            return t;
        } else {
            size -= 1;
            if (t.left == null) {
                return  t.right;
            }
            if (t.right == null) {
                return t.left;
            }
            BSTNode p = Min(t.right);
            p.right = deleteMin(t.right);
            p.left = t.left;
            return p;
        }
    }
    
    @Override
    public V remove(K key){
        V theItem = get(key);
        sentinel.left = delete(sentinel.left, key);
        return theItem;
    }

    public V remove(K key, V value){
        throw new UnsupportedOperationException("not yet");
    }

    private class KeyIterator implements Iterator<K> {
        private int isize;
        private ArrayList<K> al = new ArrayList<>();

        private void ArrayListBuilder(BSTNode t) {
            if (t != null) {
                ArrayListBuilder(t.left);
                al.addLast(t.key);
                ArrayListBuilder(t.right);
            }
        }

        public KeyIterator() {
            isize = 0;
            ArrayListBuilder(sentinel.left);
        }

        @Override
        public boolean hasNext() {
            return isize < size();
        }

        @Override
        public K next() {
            K item = al.get(isize);
            isize += 1;
            return item;
        }
    }

    @Override
    public Iterator<K> iterator() {
        return new KeyIterator();
    }

}
