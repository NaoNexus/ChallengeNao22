package com.example.naocontroller;

public class StatsManager {
    private final static String TAG = StatsManager.class.getSimpleName();

    private StatsManager () {}

    private static short nARPaintingsDescribed = 0;
    private static short nPaintingsDescribed = 0;
    private static short nPaintingsRecognised = 0;

    public static void increaseARPaintings() {nARPaintingsDescribed ++;}

    public static short getARPaintings() {return nARPaintingsDescribed;}

    public static void resetARPaintings() {nARPaintingsDescribed = 0;}

    public static void increaseNormalPaintings() {nPaintingsDescribed ++;}

    public static short getNormalPaintings() {return nPaintingsDescribed;}

    public static void resetNormalPaintings() {nPaintingsDescribed = 0;}

    public static int getTotPaintingsDescribed() {
        return nPaintingsDescribed + nARPaintingsDescribed;
    }

    public static void increasePaintingsRecognised() {nPaintingsRecognised++;}

    public static short getPaintingsRecognised() {return nPaintingsRecognised;}

    public static void resetPaintingsRecognised() {nPaintingsRecognised = 0;}
}
