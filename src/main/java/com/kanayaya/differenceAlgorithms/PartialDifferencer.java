package com.kanayaya.differenceAlgorithms;

public interface PartialDifferencer {
    int increaseDifference(String piece);

    int decreaseDifference(String piece);

    int getMaxDistance();

    int distance(String piece);
    int remainingSize();
}
