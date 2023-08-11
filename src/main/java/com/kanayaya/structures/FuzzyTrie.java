package com.kanayaya.structures;

import com.kanayaya.differenceAlgorithms.PartialDifferencer;
import com.kanayaya.differenceAlgorithms.PartialLevenstein;

import java.util.*;

public class FuzzyTrie extends AbstractSet<String> {
    private final List<StringNode> nodes = new ArrayList<>();
    private int size;

    public FuzzyTrie() {

    }
    public FuzzyTrie(Collection<String> donor) {
        this.addAll(donor);
    }
    @Override
    public Iterator<String> iterator() {
        return new TrieIterator();
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean add(String s) {
        if (s.equals("")) {
            return false;
        }
        boolean added = false;
        if (nodes.isEmpty()) {
            nodes.add(new StringNode(s));
            added = true;
        } else for (int i = 0; i < nodes.size(); i++) {
            StringNode node = nodes.get(i);
            if (node.key.charAt(0) == s.charAt(0)) {
                added = node.add(s, s) == null;
                break;
            } else if (node.key.charAt(0) > s.charAt(0)) {
                nodes.add(i, new StringNode(s));
                added = true;
                break;
            } else if (i == nodes.size() - 1) {
                nodes.add(new StringNode(s));
                added = true;
                break;
            }
        }
        if (added) size++;
        return added;
    }

    @Override
    public boolean addAll(Collection<? extends String> c) {
        boolean added = false;
        for (String s : c) {
            if (add(s)) added = true;
        }
        return added;
    }

    public void addAll(String... c) {
        for (String s : c) {
            add(s);
        }
    }

    public String fuzzyGetFirst(PartialLevenstein differencer, int maxDistance) {
        for (StringNode node: nodes) {
            String s = node.fuzzyGetFirst(differencer, maxDistance);
            if (s != null) return s;
        }
        return null;
    }
    public List<String> fuzzyGetAll(PartialLevenstein differencer, int maxDistance) {
        List<String> result = new ArrayList<>();
        for (StringNode node: nodes) {
            List<String> strings = node.fuzzyGetAll(differencer, maxDistance);
            if (strings != null) result.addAll(strings);
        }
        return result;
    }
    public String fuzzyGetClosest(PartialDifferencer differencer, int maxDistance) {
        Pair<String> closest = new Pair<>("", Integer.MAX_VALUE);
        for (StringNode node: nodes) {
            Pair<String> result = node.fuzzyGetClosest(differencer, maxDistance);
            if (result != null) {
                if (result.distance() < closest.distance()) closest = result;
            }
        }
        return closest.value().equals("")? null : closest.value();
    }


    private class TrieIterator implements Iterator<String> {
        private String next = null;
        private Iterator<String> childIterator;
        private int childIndex = 0;
        public TrieIterator() {
            if ( ! nodes.isEmpty()) {
                childIterator = nodes.get(0).iterator();
                next = childIterator.next();
            }
        }
        @Override
        public boolean hasNext() {
            return next != null;
        }

        @Override
        public String next() {
            String result = next;
            if (nodes.isEmpty()) {
                return result;
            }
            if (childIterator.hasNext()) next = childIterator.next();
            else if (childIndex < nodes.size()-1) {
                childIndex++;
                childIterator = nodes.get(childIndex).iterator();
                next = childIterator.next();
            } else next = null;
            return result;
        }
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        nodes.forEach(node -> node.forEach(s -> {
            sb.append(s);
            sb.append(", ");
        }));
        sb.delete(sb.length()-2, sb.length());
        sb.append("]");
        return sb.toString();
    }
}

