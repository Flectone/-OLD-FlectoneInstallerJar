package ru.flectone.swing.pages;

import com.formdev.flatlaf.ui.FlatButtonBorder;
import ru.flectone.components.Image;
import ru.flectone.utils.UtilsSystem;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.util.ArrayList;

public class PageComponent extends JPanel {

    private boolean changeBackgroundColor = UtilsSystem.changeBackgroundColor;

    public PageComponent(String imageName){
        setLayout(new FlowLayout(FlowLayout.LEFT));
        add(new Image(imageName));
    }

    public PageComponent(String component, String version, String firstCheckBox, String secondCheckBox, String description, String page){
        this(component + ".jpg");

        UtilsSystem.getCountCheckBox(page);

        component = "farms." + component;

        Box box = Box.createVerticalBox();
        box.add(createLabel(component + ".version"));
        box.add(createRigidArea(0, 5));
        box.add(createCheckBox(component + ".install", page));
        box.add(createRigidArea(0, 5));
        box.add(createCheckBox(component + ".litematic", page));
        box.add(createRigidArea(0, 5));
        box.add(createLabel(component + ".description"));

        setSecondBackgroundColor();
        add(box);

    }

    public PageComponent(String component, String checkBox, String description, String page){
        this(component + ".png");

        UtilsSystem.getCountCheckBox(page);

        component = "resourcepacks." + component;

        Box box = Box.createVerticalBox();
        box.add(createCheckBox(component + ".install", page));
        box.add(createRigidArea(0, 5));
        box.add(createLabel(component + ".description"));

        setSecondBackgroundColor();
        add(box);
        setName(page);

    }

    public PageComponent(String component, String checkBox, int countEnable, String description, String page){
        this(component + ".png");

        UtilsSystem.setCountCheckBox(page, countEnable);

        component = "mods." + component;

        Box box = Box.createVerticalBox();
        box.add(createCheckBox(component + ".install", page));
        box.add(createRigidArea(0, 5));
        box.add(createLabel(component + ".description"));


        setSecondBackgroundColor();
        add(box);

    }

    private void setSecondBackgroundColor(){
        if(changeBackgroundColor) {
            setBackground(UtilsSystem.getSecondColor());
            UtilsSystem.setChangeBackgroundColor(false);

        } else UtilsSystem.setChangeBackgroundColor(true);
    }

    public PageComponent(String component, String version, String checkBox, String description, String page){
        this(component + ".png");

        UtilsSystem.getCountCheckBox(page);

        component = "datapacks." + component;

        Box box = Box.createVerticalBox();
        box.add(createLabel(component + ".version"));
        box.add(createRigidArea(0, 5));
        box.add(createCheckBox(component + ".install", page));
        box.add(createRigidArea(0, 5));
        box.add(createLabel(component + ".description"));

        setSecondBackgroundColor();
        add(box);
    }

    //Create text component
    protected JTextComponent createTextComponent(){
        //Create field
        JTextComponent textComponent = new JTextField();

        textComponent.setBorder(new FlatButtonBorder());

        //Create text component with name from locale
        textComponent.setText(UtilsSystem.pathToMinecraftFolder);
        //Set size for field
        return textComponent;
    }

    //Create label
    protected JLabel createLabel(String labelName){
        //Create label with name from locale
        JLabel label = new JLabel(UtilsSystem.getLocaleString(labelName));
        //Set alignment
        label.setAlignmentX(LEFT_ALIGNMENT);
        return label;
    }

    //Create button
    protected JButton createButton(String buttonName){
        JButton button = new JButton(UtilsSystem.getLocaleString(buttonName));
        //Add to action listener so that check click
        button.setToolTipText(UtilsSystem.getLocaleString(buttonName + ".toolTip"));
        return button;
    }

    protected Component createRigidArea(int width, int height){
        return Box.createRigidArea(new Dimension(width, height));
    }

    protected JCheckBox createCheckBox(String checkBoxName, String page){
        JCheckBox checkBox = new JCheckBox(UtilsSystem.getLocaleString(checkBoxName));
        checkBox.setAlignmentX(LEFT_ALIGNMENT);

        checkBox.setName(checkBoxName.split("\\.")[1]);
        if(checkBoxName.contains("litematic") && page.equals("farms")) checkBox.setName(checkBoxName.split("\\.")[1] + "litematic");

        ArrayList<JCheckBox> listCheckBox = new ArrayList<>();
        if(UtilsSystem.listCheckBox.get(page) != null){
            listCheckBox = UtilsSystem.listCheckBox.get(page);
        }
        listCheckBox.add(checkBox);
        UtilsSystem.listCheckBox.put(page, listCheckBox);

        if(page.equals("modsmain")){
            checkBox.setSelected(true);
        }
        if(page.equals("modsextension")) return checkBox;

        checkBox.addActionListener(e -> {
            int count = UtilsSystem.countCheckBoxHashMap.get(page);

            if(checkBox.isSelected()){
                count = count + 1;
            } else {
                count = count - 1;
            }
            ArrayList<Component> arrayList = UtilsSystem.enabledComponentsHashMap.get(page);
            for(Component component : arrayList){
                boolean bol = count > 0;
                component.setEnabled(bol);
                if(component instanceof JLabel){
                    ((JLabel) component).setText(UtilsSystem.getLocaleString("label.status.ready." + bol));
                }
            }

            UtilsSystem.countCheckBoxHashMap.put(page, count);
        });

        return checkBox;
    }

}
