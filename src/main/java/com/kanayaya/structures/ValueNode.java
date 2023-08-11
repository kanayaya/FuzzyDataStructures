package com.kanayaya.structures;

import java.util.List;

public class ValueNode<V> extends Node<V> {
    private V value;

    public ValueNode(String key, boolean hasValue, List<Node<V>> children, V value) {
        super(key, hasValue, children);
        this.value = value;
    }

    @Override
    protected V getValue() {
        return value;
    }

    @Override
    protected void setValue(V value) {
        this.value = value;
    }

    @Override
    protected Node<V> getNewChild(String key, V value, boolean hasValue, List<Node<V>> children) {
        return new ValueNode<>(key, hasValue, children, value);
    }
}
