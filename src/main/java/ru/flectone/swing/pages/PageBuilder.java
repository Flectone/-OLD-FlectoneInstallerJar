package ru.flectone.swing.pages;

import javax.swing.*;
import java.awt.*;

public class PageBuilder {

    private final Box builder = Box.createVerticalBox();

    public void add(Component component){
        builder.add(component);
    }


    public JScrollPane build(){
        JScrollPane scrollPane = new JScrollPane(builder);

        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        //Set speed for scroll
        scrollPane.getVerticalScrollBar().setUnitIncrement(10);
        //Remove ugly border
        scrollPane.setBorder(null);
        return scrollPane;
    }

    public void clearBuilder(){
        builder.removeAll();
    }

    public Component[] getComponents(){
        return builder.getComponents();
    }

    public void removeComponent(Component component){
        builder.remove(component);
    }

}

