package com.example.zakhar.kursach2020.classes;

import java.util.HashMap;
import java.util.Map;

public class FingerBoard {
    private static final int distanceFromE1 = -24;
    private static final int distanceFromA = -12;
    private static final int distanceFromD = -7;
    private static final int distanceFromG = -2;
    private static final int distanceFromB = 2;
    private static final int distanceFromE2 = 7;

    public static Map<Integer, Integer> getNoteOnFingerBoard(Integer tones) {
        Map<Integer, Integer> map = new HashMap<>();
        checkAndAdd(distanceFromE1, 0, tones, map);
        checkAndAdd(distanceFromA, 1, tones, map);
        checkAndAdd(distanceFromD, 2, tones, map);
        checkAndAdd(distanceFromG, 3, tones, map);
        checkAndAdd(distanceFromB, 4, tones, map);
        checkAndAdd(distanceFromE2, 5, tones, map);
        return map;
    }

    private static void checkAndAdd(int distance, int string, int tones, Map<Integer, Integer> map) {
        if (tones>=distance&&tones<=distance+12) {
            map.put(string, tones - distance);
        } else {
            map.put(string, null);
        }
    }
}