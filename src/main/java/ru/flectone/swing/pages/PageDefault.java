package ru.flectone.swing.pages;

import com.formdev.flatlaf.ui.FlatButtonBorder;
import ru.flectone.utils.UtilsSystem;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.util.ArrayList;

public class PageDefault extends JPanel {

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

            System.out.println(page);
            int count = UtilsSystem.countCheckBoxHashMap.get(page);

            if(checkBox.isSelected()){
                count = count + 1;
            } else {
                count = count - 1;
            }
            System.out.println(UtilsSystem.countCheckBoxHashMap.get(page));
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
