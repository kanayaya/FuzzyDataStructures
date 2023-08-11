package com.kanayaya.structures;

import com.kanayaya.differenceAlgorithms.PartialDifferencer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

abstract class Node<V> implements Iterable<V> {
    protected String key;
    protected boolean hasValue;
    protected List<Node<V>> children;

    protected Node(String key, boolean hasValue, List<Node<V>> children) {
        this.key = key;
        this.hasValue = hasValue;
        this.children = children;
    }

    /**
     * @return returns number of nodes that have values
     * including itself if it has
     */
    public int size() {
        int sum = children.stream().mapToInt(Node::size).sum();
        return sum + (hasValue? 1 : 0);
    }


    /**
     * @return returns previous value or null
     */
    public V add(String key, V value) {
        if (this.key.equals(key)) {
            hasValue = true;
            V result = getValue();
            setValue(value);
            return result;
        }
        int index = 1;
        for (; index < key.length() && index < this.key.length(); index++) {
            if (this.key.charAt(index) != key.charAt(index)) break;
        }
        String stripped = key.substring(index);
        if (index == this.key.length()) {
            for (int i = 0; i < children.size(); i++) {
                Node<V> child = children.get(i);
                if (child.key.charAt(0) > stripped.charAt(0)) {
                    children.add(i, getNewChild(stripped, value, true, new ArrayList<>()));
                    return null;
                } else if (child.key.charAt(0) == stripped.charAt(0)) {
                    return child.add(stripped, value);
                } else if (i == children.size() - 1) {
                    children.add(getNewChild(stripped, value, true, new ArrayList<>()));
                    return null;
                }
            }
            if (children.isEmpty()) {
                children.add(getNewChild(stripped, value, true, new ArrayList<>()));
            }
        }
        else {
            Node<V> firstChild = getNewChild(this.key.substring(index), getValue(), hasValue, children);
            children = new ArrayList<>();
            children.add(firstChild);
            if (index == key.length()) {
                this.hasValue = true;
                this.key = key;
                V result = getValue();
                setValue(value);
                return result;
            } else {
                this.key = key.substring(0, index);
                setValue(null);
                hasValue = false;
                Node<V> secondChild = getNewChild(key.substring(index), value, true, new ArrayList<>());
                children.add(secondChild.key.charAt(0) < firstChild.key.charAt(0)? 0 : 1,
                        secondChild);
            }
        }
        return null;
    }
    /**
     * @return returns previous value or null
     */
    public V remove(String key) {
        if (key.length()< this.key.length()) return null;
        V result = null;
        if (key.equals(this.key)) {
            if (hasValue) {
                result = getValue();
                hasValue = false;
            }
        } else if (key.substring(0, this.key.length()).equals(this.key)) {
            for (int i = 0; i < children.size(); i++) {
                Node<V> child = children.get(i);
                V remove = child.remove(key.substring(this.key.length()));
                if (remove != null) {
                    if (child.size() == 0) children.remove(child);
                    result = remove;
                }
            }
        }
        return result;
    }

    /**
     * @param differencer - instance of PartialDifferencer interface
     * @param distance - number representing the distance between two words, will be
     *                 used to compare with value's key
     * @return returns first value that matches its key with differencer. Returns null if no match
     */
    public V fuzzyGetFirst(PartialDifferencer differencer, int distance) {
        if (differencer.checkAndIncrease(key, distance)) {
            V result = null;
            if (hasValue && differencer.getMaxDistance() <= distance) {
                result = getValue();
            } else {
                for (Node<V> child: children) {
                    result = child.fuzzyGetFirst(differencer, distance - differencer.distanceNow());
                    if (result != null) break;
                }
            }
            differencer.decreaseDifference(key);
            return result;
        }
        return null;
    }

    public List<V> fuzzyGetAll(PartialDifferencer differencer, int distance) {
        if (differencer.checkAndIncrease(key, distance)) {
            List<V> result = new ArrayList<>();
            if (hasValue && differencer.getMaxDistance() <= distance) result.add(getValue());
            for (Node<V> child: children) {
                List<V> vs = child.fuzzyGetAll(differencer, distance - differencer.distanceNow());
                if (vs != null) result.addAll(vs);
            }
            differencer.decreaseDifference(key);
            return result;
        }
        return null;
    }

    public Pair<V> fuzzyGetClosest(PartialDifferencer differencer, int distance) {
        if (differencer.checkAndIncrease(key, distance)) {
            int itsDistance = differencer.getMaxDistance();
            Pair<V> result = itsDistance <= distance? new Pair<>(getValue(), itsDistance): null;
            for (Node<V> child: children) {
                Pair<V> childPair = child.fuzzyGetClosest(differencer, distance - differencer.distanceNow());
                if (childPair != null) {
                    if (result == null || childPair.distance() < result.distance()) {
                        result = childPair;
                    }
                }
            }
            differencer.decreaseDifference(key);
            return result;
        }
        return null;
    }

    @Override
    public Iterator<V> iterator() {
        return new NodeIterator();
    }
    protected class NodeIterator implements Iterator<V> {
        private V next;
        private Iterator<V> childIterator;
        private int childIndex = 0;
        public NodeIterator() {
            if (! children.isEmpty()) {
                childIterator = children.get(0).iterator();
            }
            if (hasValue) next = getValue();
            else {
                next = childIterator.next();  // it could not produce NPE because if node doesn't have value children are never empty
            }
        }

        @Override
        public boolean hasNext() {
            return next != null;
        }

        @Override
        public V next() {
            V result = next;
            if (children.isEmpty()) {
                next = null;
                return result;
            }
            if (childIterator.hasNext()) next = childIterator.next();
            else if (childIndex<children.size()-1) {
                childIndex++;
                childIterator = children.get(childIndex).iterator();
                next = childIterator.next();
            } else next = null;
            return result;
        }
    }

    protected abstract V getValue();
    protected abstract void setValue(V value);

    /**
     * @param key key of new child
     * @param value value of new child
     * @param hasValue should be true if value is not null
     * @param children children of new child
     * @return new instance of class extending {@link Node}
     */
    protected abstract Node<V> getNewChild(String key, V value, boolean hasValue, List<Node<V>> children);
}
