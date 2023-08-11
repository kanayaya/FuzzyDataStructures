package com.kanayaya.structures;

record Pair<T>(T value, int distance) implements Comparable<Pair<T>> {
    @Override
    public int compareTo(Pair<T> tPair) {
        return distance - tPair.distance;
    }
}
