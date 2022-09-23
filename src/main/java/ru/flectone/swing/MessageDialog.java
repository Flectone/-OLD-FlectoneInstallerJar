package ru.flectone.swing;

import ru.flectone.components.FPanel;
import ru.flectone.utils.UtilsSystem;

import javax.swing.*;

public class MessageDialog extends JOptionPane {

    public MessageDialog(String message, String messageTitle, int typeMessage){
        showMessageDialog(null, message, UtilsSystem.getLocaleString("message." + messageTitle +".title"), typeMessage);
    }

    public MessageDialog(String message, String messageTitle){

        if(!UtilsSystem.showWarnMessages) return;

        JCheckBox checkBox = new JCheckBox(UtilsSystem.getLocaleString("checkbox.warns"));

        FPanel panel = new FPanel();
        panel.setPanelLayout(new BoxLayout(panel, BoxLayout.Y_AXIS))
                .addComponent(new JLabel(message))
                .createRigidArea(0, 8)
                .addComponent(checkBox);

        JOptionPane.showMessageDialog(null, panel, UtilsSystem.getLocaleString("message." + messageTitle + ".title"), JOptionPane.WARNING_MESSAGE);

        UtilsSystem.setShowWarnMessages(!checkBox.isSelected());
    }

    public MessageDialog(String message, String replaceMessage, String messageTitle, int typeMessage){
        //Replace custom %file_path%
        message = message.replace("%file_path%", "\"" + replaceMessage + "\"");
        showMessageDialog(null, message, UtilsSystem.getLocaleString("message." + messageTitle +".title"), typeMessage);
    }
}
