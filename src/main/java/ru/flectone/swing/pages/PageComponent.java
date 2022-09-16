package ru.flectone.swing.pages;

import ru.flectone.Image;
import ru.flectone.utils.UtilsSystem;

import javax.swing.*;
import java.awt.*;

public class PageComponent extends PageDefault {

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
        add(box);
    }

    public PageComponent(String imageName, String checkBox, String description, String page){
        this(imageName);

        UtilsSystem.getCountCheckBox(page);

        Box box = Box.createVerticalBox();
        box.add(createCheckBox(checkBox, page));
        box.add(createRigidArea(0, 5));
        box.add(createLabel(description));

        box.setPreferredSize(new Dimension(500, 40));
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
        add(box);

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
        box.setPreferredSize(new Dimension(440, 70));
        add(box);
    }

}
