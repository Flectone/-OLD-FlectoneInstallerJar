package ru.flectone.utils;

import javax.swing.*;

public class UtilsMessage {

    public static void showInformation(String text){
        JOptionPane.showMessageDialog(null, text, UtilsSystem.getLocaleString("message.install.title"), JOptionPane.INFORMATION_MESSAGE);
    }

    //Show error message for user
    public static void showErrorMessage(String text, String replace){
        //Replace custom %file_path%
        text = text.replace("%file_path%", "\"" + replace + "\"");
        JOptionPane.showMessageDialog(null, text, UtilsSystem.getLocaleString("message.error.title"), JOptionPane.ERROR_MESSAGE);
    }

    //Show warn message for user
    public static void showWarnMessage(String text){
        JOptionPane.showMessageDialog(null, text, UtilsSystem.getLocaleString("message.warn.title"), JOptionPane.WARNING_MESSAGE);
    }
}
