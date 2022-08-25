package ru.flectone.utils;

public class UtilsOS {

    //Get system OC and minecraft folder path
    private static final String systemOc = System.getProperty("os.name").toLowerCase();

    static final boolean isUnix = (systemOc.contains("nix") || systemOc.contains("nux") || systemOc.contains("aix"));

    static final boolean isMac = (systemOc.contains("mac"));

    static final boolean isWindows = (systemOc.contains("win"));

    public static String systemSlash;

    //Get system locale
    public static String systemLocale = System.getProperty("user.language");

    public static void setSystemSlash(){
        if(isWindows){
            systemSlash = "\\";
        } else {
            systemSlash = "/";
        }
    }

    //Windows slash is "\\" but another OS is "/"
    public static String getSystemSlash(){
        return systemSlash;
    }

    public static String getSystemLocale() {
        return systemLocale;
    }

    public static void setSystemLocale(String systemLocale) {
        UtilsOS.systemLocale = systemLocale;
    }
}
