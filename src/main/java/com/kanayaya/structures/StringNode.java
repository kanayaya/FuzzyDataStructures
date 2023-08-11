package com.kanayaya.structures;

import com.kanayaya.differenceAlgorithms.PartialDifferencer;

import java.util.*;
import java.util.stream.Collectors;

class StringNode extends Node<String> implements Comparable<StringNode> {
    private StringNode(String key, boolean hasValue, List<Node<String>> children) {
        super(key, hasValue, children);
    }
    public StringNode(String key) {
        this(key, true, new ArrayList<>());
    }

    @Override
    public Iterator<String> iterator() {
        return new stringNodeIterator();
    }
    private class stringNodeIterator extends NodeIterator {
        @Override
        public String next() {
            String next = super.next();
            return checkAndSum(next);
        }
    }

    @Override
    public String add(String key, String value) {
        String add = super.add(key, value);
        return checkAndSum(add);
    }

    @Override
    public String remove(String key) {
        String remove = super.remove(key);
        return checkAndSum(remove);
    }

    private String checkAndSum(String s) {
        if (s == this.key || s == null) return s; // should be "==", we are checking if it's the same object
        return this.key + s;
    }

    @Override
    public String fuzzyGetFirst(PartialDifferencer differencer, int distance) {
        String result = super.fuzzyGetFirst(differencer, distance);
        return checkAndSum(result);
    }

    @Override
    public List<String> fuzzyGetAll(PartialDifferencer differencer, int distance) {
        List<String> result = super.fuzzyGetAll(differencer, distance);
        return result == null? null : result.stream().map(this::checkAndSum).collect(Collectors.toList());
    }

    @Override
    public Pair<String> fuzzyGetClosest(PartialDifferencer differencer, int distance) {
        Pair<String> result = super.fuzzyGetClosest(differencer, distance);
        return result == null ? null : new Pair<>(checkAndSum(result.value()), result.distance());
    }

    @Override
    protected String getValue() {
        return key;
    }

    @Override
    protected void setValue(String value) {}

    @Override
    protected Node<String> getNewChild(String key, String value, boolean hasValue, List<Node<String>> children) {
        return new StringNode(key, hasValue,
                children);
    }

    @Override
    public int compareTo(StringNode node) {
        return this.key.charAt(0) - node.key.charAt(0);
    }
}
