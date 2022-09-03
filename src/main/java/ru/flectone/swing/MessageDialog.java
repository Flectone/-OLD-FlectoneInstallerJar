package ru.flectone.swing;

import ru.flectone.utils.UtilsSystem;

import javax.swing.*;

public class MessageDialog extends JOptionPane {

    public MessageDialog(String message, String messageTitle, int typeMessage){
        showMessageDialog(null, message, UtilsSystem.getLocaleString("message." + messageTitle +".title"), typeMessage);
    }

    public MessageDialog(String message, String replaceMessage, String messageTitle, int typeMessage){
        //Replace custom %file_path%
        message = message.replace("%file_path%", "\"" + replaceMessage + "\"");
        showMessageDialog(null, message, UtilsSystem.getLocaleString("message." + messageTitle +".title"), typeMessage);
    }
}
