package ru.flectone.swing.pages;

import ru.flectone.swing.Image;
import ru.flectone.utils.UtilsSystem;

import javax.swing.*;
import java.awt.*;

public class PageComponent extends PageDefault {

    public PageComponent(String imageName){
        add(new Image(imageName));
    }

    public PageComponent(String imageName, String version, String firstCheckBox, String secondCheckBox, String description){
        this(imageName);

        UtilsSystem.getCountCheckBox("farms");

        Box box = Box.createVerticalBox();
        box.add(createLabel(version));
        box.add(createRigidArea(0, 5));
        box.add(createCheckBox(firstCheckBox, "farms"));
        box.add(createRigidArea(0, 5));
        box.add(createCheckBox(secondCheckBox, "farms"));
        box.add(createRigidArea(0, 5));
        box.add(createLabel(description));

        box.setPreferredSize(new Dimension(325, 100));
        add(box);
    }

    public PageComponent(String imageName, String checkBox, String description){
        this(imageName);

        UtilsSystem.getCountCheckBox("resourcepacks");

        Box box = Box.createVerticalBox();
        box.add(createCheckBox(checkBox, "resourcepacks"));
        box.add(createRigidArea(0, 5));
        box.add(createLabel(description));

        box.setPreferredSize(new Dimension(500, 40));
        add(box);
    }

    public PageComponent(String imageName, String version, String checkBox, String description){
        this(imageName);

        UtilsSystem.getCountCheckBox("datapacks");
        Box box = Box.createVerticalBox();
        box.add(createLabel(version));
        box.add(createRigidArea(0, 5));
        box.add(createCheckBox(checkBox, "datapacks"));
        box.add(createRigidArea(0, 5));
        box.add(createLabel(description));

        box.setPreferredSize(new Dimension(440, 60));
        add(box);
    }

}
