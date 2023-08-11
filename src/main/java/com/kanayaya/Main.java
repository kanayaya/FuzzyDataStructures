package com.kanayaya;

import com.kanayaya.differenceAlgorithms.PartialLevenstein;
import com.kanayaya.structures.FuzzyMap;
import com.kanayaya.structures.FuzzyTrie;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        FuzzyTrie trie = new FuzzyTrie();
        trie.addAll("префикс", "префиксный", "префиксное", "дерево", "префиксное деревj", "тестирование", "тестирование префиксного дерева",
                "префикс", "префиксный", "префиксное", "дерево", "префиксное дерево", "тестирование", "тестирование префиксного дерева", "алгоритм", "структура", "данные", "поиск", "поиск по префиксу", "префиксный поиск", "быстрый поиск", "быстрый поиск по префиксу", "эффективность", "эффективность поиска");
        PartialLevenstein levenshtein = new PartialLevenstein("гибралтар");

        System.out.println(levenshtein.increaseDifference("а"));
        System.out.println(levenshtein.increaseDifference("бра"));
        System.out.println(levenshtein.increaseDifference("кадабра"));
        System.out.println(levenshtein.getMaxDistance());
        System.out.println(levenshtein.decreaseDifference("кадабра"));
        System.out.println(levenshtein.decreaseDifference("бра"));

        System.out.println(trie.fuzzyGetFirst(new PartialLevenstein("прbфекс"), 2));
        System.out.println(trie.fuzzyGetAll(new PartialLevenstein("префиксное дерево"), 10));
        System.out.println(trie.fuzzyGetClosest(new PartialLevenstein("префе дерево"), 10));

        System.out.println(trie);

        Random r = new Random();

        FuzzyMap<Object> ntrie = new FuzzyMap<>();
        for (int i = 0; i < 5_000_000; i++) {
            ntrie.put(randomString(r), new Object());
        }
        System.out.println("putted");

        System.out.println(ntrie);
        String rs = randomString(r);
        System.out.println(rs);
        System.out.println(ntrie.fuzzyGetClosest(new PartialLevenstein(rs), rs.length()));

    }

    private static String randomString(Random r) {
        byte[] bytes = new byte[r.nextInt(20)];
        r.nextBytes(bytes);
        for (int i = 0; i < bytes.length; i++) {
            byte b = bytes[i];
            if (b<0) bytes[i] += 128;
            if (b<33) bytes[i] += 32;
        }
        return new String(bytes, StandardCharsets.US_ASCII);
    }
}