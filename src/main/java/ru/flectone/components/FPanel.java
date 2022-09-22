package ru.flectone.components;

import ru.flectone.utils.UtilsSystem;

import javax.swing.*;
import java.awt.*;

public class FPanel extends JPanel {

    //Create label
    protected JLabel createLabel(String labelName){
        //Create label with name from locale
        JLabel label = new JLabel(UtilsSystem.getLocaleString(labelName));
        //Set alignment
        label.setAlignmentX(LEFT_ALIGNMENT);
        return label;
    }

    public FPanel addComponent(Component component){
        this.add(component);
        return this;
    }

    public FPanel setPanelLayout(LayoutManager layout){
        setLayout(layout);

        return this;
    }

    public FPanel createRigidArea(int width, int height){
        add(Box.createRigidArea(new Dimension(width, height)));
        return this;
    }
}
