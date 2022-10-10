package ru.flectone.components;

import ru.flectone.FSystem;

import javax.swing.*;
import java.awt.*;

public class FBuilder {

    private int countComponents;

    public FBuilder(int countComponents){
        this.countComponents = countComponents;
    }

    public FBuilder(){

    }

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

            if(count%2 == 0) component.setBackground(FSystem.getBackgroundColor());
            else component.setBackground(panelForColor.getBackground());
            count++;

        }
    }

    public JPanel panelBuild(){
        JPanel panel = new JPanel();
        panel.add(builder);
        return panel;
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
