package ru.flectone.components;

import javax.swing.*;
import java.awt.*;

public class FPanel extends JPanel {

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
