package com.kanayaya.differenceAlgorithms;

public interface PartialDifferencer {
    int increaseDifference(String piece);

    int decreaseDifference(String piece);

    int getMaxDistance();
    int distanceNow();
    int remainingSize();
    boolean checkAndIncrease(String piece, int distance);
}
