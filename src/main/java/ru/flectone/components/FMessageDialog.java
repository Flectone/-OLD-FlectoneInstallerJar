package ru.flectone.components;


import ru.flectone.Utils;

import javax.swing.*;
import java.awt.*;

public class FMessageDialog extends JOptionPane {

    public FMessageDialog(String message, String messageTitle, int typeMessage){
        showMessageDialog(null, message, Utils.getString("message." + messageTitle + ".title"), typeMessage);
    }

    public FMessageDialog(String message, String messageTitle){

        if(!FSwing.isShowWarns()) return;

        JCheckBox checkBox = new JCheckBox(Utils.getString("checkbox.warns"));

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(new JLabel(message));
        panel.add(Box.createRigidArea(new Dimension(0, 8)));
        panel.add(checkBox);

        JOptionPane.showMessageDialog(null, panel, Utils.getString("message." + messageTitle + ".title"), JOptionPane.WARNING_MESSAGE);

        FSwing.setShowWarns(!checkBox.isSelected());

    }

    public FMessageDialog(String message, String replaceMessage, String messageTitle, int typeMessage){
        //Replace custom %file_path%
        message = message.replace("%file_path%", "\"" + replaceMessage + "\"");
        showMessageDialog(null, message, Utils.getString("message." + messageTitle +".title"), typeMessage);
    }
}
