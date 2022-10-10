package ru.flectone.components;

import ru.flectone.FSystem;
import ru.flectone.Utils;

public class FSwing {

    private static int tabbedPaneAlign = 1;

    public static int getTabbedPaneAlign(){
        return tabbedPaneAlign;
    }

    public static void setTabbedPaneAlign(int tabbedPaneAlign){
        FSwing.tabbedPaneAlign = tabbedPaneAlign;
    }

    private static String selectedLanguage = FSystem.getSystemLocale();

    public static void setSelectedLanguage(String selectedLanguage) {
        FSwing.selectedLanguage = selectedLanguage;
    }

    public static String getSelectedLanguage() {
        return selectedLanguage;
    }

    private static String selectedTheme = "dark";

    public static void setSelectedTheme(String selectedTheme) {
        FSwing.selectedTheme = selectedTheme;
    }

    public static String getSelectedTheme() {
        return selectedTheme;
    }

    private static boolean showWarns = true;

    public static void setShowWarns(boolean showWarns) {
        FSwing.showWarns = showWarns;
    }

    public static boolean getShowWarns() {
       return showWarns;
    }

    public static String[] getTabSequence() {
        if(Utils.getString("tab.Sequence").equals(Utils.getString(""))) return Utils.getString("tabs").split(" ");
        return Utils.getString("tab.Sequence").split(" ");
    }
}
