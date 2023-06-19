package com.kanayaya;

import com.kanayaya.differenceAlgorithms.PartialLevenstein;
import com.kanayaya.structures.FuzzyTrie;

public class Main {
    public static void main(String[] args) {
        FuzzyTrie trie = new FuzzyTrie();
        trie.addAll("префикс", "префиксный", "префиксное", "дерево", "префиксное деревj", "тестирование", "тестирование префиксного дерева",
                "префикс", "префиксный", "префиксное", "дерево", "префиксное дерево", "тестирование", "тестирование префиксного дерева", "алгоритм", "структура", "данные", "поиск", "поиск по префиксу", "префиксный поиск", "быстрый поиск", "быстрый поиск по префиксу", "эффективность", "эффективность поиска");
        System.out.println(trie);


        PartialLevenstein levenshtein = new PartialLevenstein("гибралтар");

        System.out.println(levenshtein.increaseDifference("а"));
        System.out.println(levenshtein.increaseDifference("бра"));
        System.out.println(levenshtein.increaseDifference("кадабра"));
        System.out.println(levenshtein.getMaxDistance());
        System.out.println(levenshtein.decreaseDifference("кадабра"));
        System.out.println(levenshtein.decreaseDifference("бра"));

        System.out.println(trie.fuzFindFirst(new PartialLevenstein("прbфекс"), 2));
        System.out.println(trie.fuzFindAll(new PartialLevenstein("префиксное дерево"), 10));
        System.out.println(trie.fuzFindClosest(new PartialLevenstein("префекс"), 2));
    }
}