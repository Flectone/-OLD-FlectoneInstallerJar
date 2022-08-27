package ru.flectone.utils;

public class UtilsOS {

    //Get system OC and minecraft folder path
    public static final String systemOc = System.getProperty("os.name").toLowerCase();

    //Get system locale
    public static String systemLocale = System.getProperty("user.language");


    public static String getSystemLocale() {
        return systemLocale;
    }

    public static void setSystemLocale(String systemLocale) {
        UtilsOS.systemLocale = systemLocale;
    }
}
