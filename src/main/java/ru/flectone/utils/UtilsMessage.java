package ru.flectone.utils;

import javax.swing.*;

public class UtilsMessage {

    public static void showInformation(String text){
        JOptionPane.showMessageDialog(null, text, UtilsSystem.getLocaleString("installer.title"), JOptionPane.INFORMATION_MESSAGE);
    }

    //Show error message for user
    public static void showErrorMessage(String text, String replace){
        //Replace custom %file_path%
        text = text.replace("%file_path%", "\"" + replace + "\"");
        JOptionPane.showMessageDialog(null, text, UtilsSystem.getLocaleString("error.title"), JOptionPane.ERROR_MESSAGE);
    }

    //Show warn message for user
    public static void showWarnMessage(String text){
        JOptionPane.showMessageDialog(null, text, UtilsSystem.getLocaleString("warn.title"), JOptionPane.WARNING_MESSAGE);
    }
}
