package com.example.naocontroller;

public class StatsManager {
    private final static String TAG = StatsManager.class.getSimpleName();

    private StatsManager () {}

    private static short nARPaintingsDescribed = 0;
    private static short nPaintingsDescribed = 0;
    private static short nPaintingsRecognised = 0;
    //private static byte NAOBatteryPercentage = 100;

    public static void increaseARPaintings() {nARPaintingsDescribed ++;}

    public static short getNARPaintings() {return nARPaintingsDescribed;}

    public static void resetARPaintings() {nARPaintingsDescribed = 0;}

    public static void increaseNormalPaintings() {nPaintingsDescribed ++;}

    public static short getNNormalPaintings() {return nPaintingsDescribed;}

    public static void resetNormalPaintings() {nPaintingsDescribed = 0;}

    public static int getTotNOfPaintingsDescribed() {
        return nPaintingsDescribed + nARPaintingsDescribed;
    }

    public static void increaseNPaintingsRecognised () {nPaintingsRecognised++;}

    public static short getNPaintingsRecognised () {return nPaintingsRecognised;}

    public static void resetNPaintingsRecognised () {nPaintingsRecognised = 0;}

    /*public static void setNAOBatteryPercentage(byte batteryPercentage) {
        NAOBatteryPercentage = batteryPercentage;
    }

    public static byte getNAOBatteryPercentage() {return NAOBatteryPercentage;}*/
}
