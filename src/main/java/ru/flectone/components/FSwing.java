package ru.flectone.components;

import ru.flectone.FSystem;
import ru.flectone.Utils;

import java.awt.*;

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

    public static boolean isShowWarns() {
       return showWarns;
    }

    public static String[] getTabSequence() {
        if(Utils.getString("tab.Sequence").equals(Utils.getString(""))) return Utils.getString("tabs").split(" ");

        if(Utils.getString("tab.Sequence").split(" ").length != 9) return Utils.getString("tabs").split(" ");

        return Utils.getString("tab.Sequence").split(" ");
    }

    private static boolean updateProgram = false;

    public static void setUpdateProgram(boolean updateProgram) {
        FSwing.updateProgram = updateProgram;
    }

    public static boolean isUpdateProgram() {
        return updateProgram;
    }

    private static Dimension frameSize = new Dimension(1000, 550);

    public static void setFrameSize(Dimension frameSize) {
        FSwing.frameSize = frameSize;
    }

    public static Dimension getFrameSize() {
        return frameSize;
    }
}
