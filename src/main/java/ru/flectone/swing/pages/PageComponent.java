package ru.flectone.swing.pages;

import ru.flectone.Image;
import ru.flectone.utils.UtilsSystem;

import javax.swing.*;
import java.awt.*;

public class PageComponent extends PageDefault {

    private boolean changeBackgroundColor = UtilsSystem.changeBackgroundColor;

    public PageComponent(String imageName){
        add(new Image(imageName));
    }

    public PageComponent(String imageName, String version, String firstCheckBox, String secondCheckBox, String description, String page){
        this(imageName);

        UtilsSystem.getCountCheckBox(page);

        Box box = Box.createVerticalBox();
        box.add(createLabel(version));
        box.add(createRigidArea(0, 5));
        box.add(createCheckBox(firstCheckBox, page));
        box.add(createRigidArea(0, 5));
        box.add(createCheckBox(secondCheckBox, page));
        box.add(createRigidArea(0, 5));
        box.add(createLabel(description));

        box.setPreferredSize(new Dimension(324, 100));
        setSecondBackgroundColor();

        add(box);
    }

    public PageComponent(String imageName, String checkBox, String description, String page){
        this(imageName);

        UtilsSystem.getCountCheckBox(page);

        Box box = Box.createVerticalBox();
        box.add(createCheckBox(checkBox, page));
        box.add(createRigidArea(0, 5));
        box.add(createLabel(description));

        box.setPreferredSize(new Dimension(500, 50));
        setSecondBackgroundColor();

        add(box);
        setName(page);
    }

    public PageComponent(String imageName, String checkBox, int countEnable, String description, String page){
        this(imageName);

        UtilsSystem.setCountCheckBox(page, countEnable);

        Box box = Box.createVerticalBox();
        box.add(createCheckBox(checkBox, page));
        box.add(createRigidArea(0, 5));
        box.add(createLabel(description));

        box.setPreferredSize(new Dimension(500, 45));
        setSecondBackgroundColor();
        add(box);

    }

    private void setSecondBackgroundColor(){
        if(changeBackgroundColor) {
            setBackground(UtilsSystem.getSecondColor());
            UtilsSystem.setChangeBackgroundColor(false);

        } else UtilsSystem.setChangeBackgroundColor(true);
    }

    public PageComponent(String imageName, String version, String checkBox, String description, String page){
        this(imageName);

        UtilsSystem.getCountCheckBox(page);

        Box box = Box.createVerticalBox();
        box.add(createLabel(version));
        box.add(createRigidArea(0, 5));
        box.add(createCheckBox(checkBox, page));
        box.add(createRigidArea(0, 5));
        box.add(createLabel(description));
        box.setPreferredSize(new Dimension(384, 70));
        setSecondBackgroundColor();
        add(box);
    }

}
