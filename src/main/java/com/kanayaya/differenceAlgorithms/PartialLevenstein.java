package com.kanayaya.differenceAlgorithms;

import java.util.ArrayList;

public class PartialLevenstein implements PartialDifferencer {
    private final ArrayList<ArrayList<Integer>> dp;
    private final String value;



    public PartialLevenstein(String s) {
        this.value = s;
        dp = new ArrayList<>();
        dp.add(new ArrayList<>(s.length() + 1));
        for (int j = 0; j <= s.length(); j++) {
            dp.get(0).add(j);
        }
    }

    @Override
    public int increaseDifference(String piece) {
        int prevLastIndex = dp.size() - 1;
        for (int i = 1; i <= piece.length(); i++) {
            dp.add(new ArrayList<>(value.length() + 1));
            dp.get(prevLastIndex + i).add(i);
            for (int j = 1; j <= value.length(); j++) {
                calculateDistance(piece, prevLastIndex, i, j);
            }
        }
        int coordinate = Math.min(prevLastIndex + piece.length(), value.length());
        return dp.get(coordinate)
                .get(coordinate);
    }


    @Override
    public int decreaseDifference(String piece) {
        int length = piece.length();
        return decreaseDifference(length);
    }

    private Integer decreaseDifference(int length) {
        for (int i = 1; i <= length; i++) {
            dp.remove(dp.size() - 1);
        }
        int coordinate = Math.min(dp.size()-1, value.length());
        return dp.get(coordinate).get(coordinate);
    }

    @Override
    public int getMaxDistance() {
        return dp.get(dp.size()-1).get(dp.get(0).size()-1);
    }

    @Override
    public int distanceNow() {
        int coordinate = Math.min(dp.size()-1 , dp.get(0).size()-1);
        return dp.get(coordinate)
                .get(coordinate);
    }


    @Override
    public int remainingSize() {
        return value.length() - (dp.size()-1);
    }

    @Override
    public boolean checkAndIncrease(String piece, int distance) {
        int prevLastIndex = dp.size() - 1;
        for (int i = 1; i <= piece.length(); i++) {
            dp.add(new ArrayList<>(value.length() + 1));
            dp.get(prevLastIndex + i).add(i);
            for (int j = 1; j <= value.length(); j++) {
                calculateDistance(piece, prevLastIndex, i, j);
                int coordinate = Math.min(prevLastIndex + i - 1, value.length());
                if (dp.get(coordinate).get(coordinate) > distance) {
                    decreaseDifference(i);
                    return false;
                }
            }
        }
        int coordinate = Math.min(prevLastIndex + piece.length(), value.length());
        return dp.get(coordinate)
                .get(coordinate) <= distance;
    }

    private void calculateDistance(String piece, int prevLastIndex, int i, int j) {
        int cost = piece.charAt(i - 1) == value.charAt(j - 1) ? 0 : 1;
        dp.get(prevLastIndex + i)
                .add(Math.min(
                        dp.get(prevLastIndex + i - 1).get(j - 1) + cost,
                        Math.min(
                                dp.get(prevLastIndex + i - 1).get(j),
                                dp.get(prevLastIndex + i).get(j - 1)) + 1));
    }
}
