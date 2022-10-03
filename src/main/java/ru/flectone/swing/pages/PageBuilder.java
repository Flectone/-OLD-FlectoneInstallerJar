package ru.flectone.swing.pages;

import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.FlatLightLaf;
import ru.flectone.components.FPanel;
import ru.flectone.utils.UtilsSystem;

import javax.swing.*;
import java.awt.*;

public class PageBuilder {

    private final Box builder = Box.createVerticalBox();

    private final JScrollPane scrollPane = new JScrollPane(builder);

    public void add(Component component){
        builder.add(component);
    }


    public JScrollPane scrollBuild(){

        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        //Set speed for scroll
        scrollPane.getVerticalScrollBar().setUnitIncrement(10);
        //Remove ugly border
        scrollPane.setBorder(null);

        updateColorComponents();

        return scrollPane;
    }

    public void updateColorComponents(){
        JPanel panelForColor = new JPanel();

        int count = 2;

        for(int x = 0; x < builder.getComponents().length; x++){
            Component component = builder.getComponents()[x];

            if(!component.isVisible() || (component.getName() != null && component.getName().equals("!!!"))) continue;

            if(count%2 == 0) component.setBackground(UtilsSystem.getSecondColor());
            else component.setBackground(panelForColor.getBackground());
            count++;

        }
    }

    public FPanel panelBuild(){
        return new FPanel().addComponent(builder);
    }

    public Box build(){
        return builder;
    }

    public void setVisibleComponent(int countComponent, boolean isVisible){
        Component component = getComponents()[countComponent];
        component.setVisible(isVisible);
    }

    public void clearBuilder(){
        builder.removeAll();
    }

    public void setScrollBarPolicy(int scrollBarPolicy){
        scrollPane.setVerticalScrollBarPolicy(scrollBarPolicy);
    }

    public Component[] getComponents(){
        return builder.getComponents();
    }

    public void removeComponent(Component component){
        builder.remove(component);
        builder.revalidate();
    }

}

