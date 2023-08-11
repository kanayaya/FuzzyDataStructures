package com.kanayaya.structures;

import com.kanayaya.differenceAlgorithms.PartialDifferencer;
import com.kanayaya.differenceAlgorithms.PartialLevenstein;

import java.util.*;

public class FuzzyMap<V> implements Map<String, V> {
    private final List<ValueNode<V>> nodes = new ArrayList<>();
    private int size = 0;

    public V fuzzyGetFirst(PartialLevenstein differencer, int maxDistance) {
        for (ValueNode<V> node: nodes) {
            V s = node.fuzzyGetFirst(differencer, maxDistance);
            if (s != null) return s;
        }
        return null;
    }
    public List<V> fuzzyGetAll(PartialLevenstein differencer, int maxDistance) {
        List<V> result = new ArrayList<>();
        for (ValueNode<V> node: nodes) {
            List<V> strings = node.fuzzyGetAll(differencer, maxDistance);
            if (strings != null) result.addAll(strings);
        }
        return result;
    }
    public V fuzzyGetClosest(PartialDifferencer differencer, int maxDistance) {
        Pair<V> closest = new Pair<>(null, Integer.MAX_VALUE);
        for (ValueNode<V> node: nodes) {
            Pair<V> result = node.fuzzyGetClosest(differencer, maxDistance);
            if (result != null) {
                if (result.distance() < closest.distance()) closest = result;
            }
        }
        return closest.value() == null? null : closest.value();
    }



    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean containsKey(Object key) {
        if (! (key instanceof String)) {
            return false;
        }
        for (ValueNode<V> node : nodes) {
            String k = (String) key;
            if (node.key.charAt(0) == k.charAt(0)) {
                if (node.fuzzyGetFirst(new PartialLevenstein(k), 0) != null) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean containsValue(Object value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public V get(Object key) {
        if (! (key instanceof String)) {
            return null;
        }
        for (ValueNode<V> node : nodes) {
            String k = (String) key;
            if (node.key.charAt(0) == k.charAt(0)) {
                V v = node.fuzzyGetFirst(new PartialLevenstein(k), 0);
                if (v != null) {
                    return v;
                }
            }
        }
        return null;
    }

    @Override
    public V put(String key, V value) {
        for (ValueNode<V> node : nodes) {
            if (node.key.charAt(0) == key.charAt(0)) {
                V add = node.add(key, value);
                if (add == null) size++;
                return add;
            }
        }
        return null;
    }

    @Override
    public V remove(Object key) {
        if (! (key instanceof String)) return null;
        for (ValueNode<V> node: nodes) {
            String k = (String) key;
            if (node.key.charAt(0) == k.charAt(0)) {
                V remove = node.remove(k);
                if (remove != null) size--;
                return remove;
            }
        }
        return null;
    }

    @Override
    public void putAll(Map<? extends String, ? extends V> m) {
        Set<? extends String> ks = m.keySet();
        for (String k : ks) {
            put(k, m.get(k));
        }
    }

    @Override
    public void clear() {
        nodes.clear();
        size = 0;
    }

    @Override
    public Set<String> keySet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Collection<V> values() {
        List<V> result = new ArrayList<>();
        nodes.forEach(x-> x.forEach(result::add));
        return result;
    }

    @Override
    public Set<Entry<String, V>> entrySet() {
        throw new UnsupportedOperationException();
    }
}
