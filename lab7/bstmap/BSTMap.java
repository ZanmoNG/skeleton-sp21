package bstmap;

import java.util.Iterator;
import java.util.Set;
import java.util.ArrayList;
import java.util.LinkedList;

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
        private LinkedList<K> ll = new LinkedList<>();

        private void LinkedListBuilder(BSTNode t) {
            if (t != null) {
                LinkedListBuilder(t.left);
                ll.addLast(t.key);
                LinkedListBuilder(t.right);
            }
        }

        public KeyIterator() {
            isize = 0;
            LinkedListBuilder(sentinel.left);
        }

        @Override
        public boolean hasNext() {
            return isize < size();
        }

        @Override
        public K next() {
            if (hasNext()) {
                K item = ll.get(isize);
                isize += 1;
                return item;
            }
            return null;
        }
    }

    @Override
    public Iterator<K> iterator() {
        return new KeyIterator();
    }

}
