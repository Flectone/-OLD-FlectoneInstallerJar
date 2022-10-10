package ru.flectone.components;

import com.formdev.flatlaf.ui.FlatButtonBorder;
import ru.flectone.FImage;
import ru.flectone.Utils;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class FComponent extends JPanel {

    public FComponent(String imageName){
        setLayout(new FlowLayout(FlowLayout.LEFT));
        add(new FImage(imageName).setCustomBorder(new FlatButtonBorder()));
    }

    public FComponent(String componentName, String pageName, int countComponents, boolean enable){
        this(componentName);

        if(pageName.equals("notop") || pageName.equals("main")){
            componentName = "mods." + componentName;
        } else componentName = pageName + "." + componentName;


        setName(componentName);

        Utils.getCountCheckBoxes(pageName);

        Box box = Box.createVerticalBox();


        if(countComponents > 2){
            box.add(createLabel(componentName + ".version"));
            box.add(createRigidArea(0, 5));
        }

        box.add(createCheckBox(Utils.getString("label.install").replace("%component_name%", Utils.getString(componentName)) ,componentName + ".install", pageName, enable));
        box.add(createRigidArea(0, 5));

        if(countComponents == 4){
            box.add(createCheckBox(Utils.getString("farms.litematic"),componentName + ".litematic", pageName, false));
            box.add(createRigidArea(0, 5));
        }

        box.add(createLabel(componentName + ".description"));

        add(box);
    }
    //Create label
    protected JLabel createLabel(String labelName){
        //Create label with name from locale
        JLabel label = new JLabel(Utils.getString(labelName));
        //Set alignment
        label.setAlignmentX(LEFT_ALIGNMENT);
        return label;
    }

    protected Component createRigidArea(int width, int height){
        return Box.createRigidArea(new Dimension(width, height));
    }

    protected JCheckBox createCheckBox(String checkBoxName, String checkBoxConfigName, String page, boolean enable){
        JCheckBox checkBox = new JCheckBox(checkBoxName);
        checkBox.setAlignmentX(LEFT_ALIGNMENT);

        checkBox.setName(checkBoxConfigName.split("\\.")[1]);

        if(checkBoxConfigName.contains("litematic") && page.equals("farms")) checkBox.setName(checkBoxConfigName.split("\\.")[1] + "litematic");

        ArrayList<JCheckBox> listCheckBox = new ArrayList<>();
        if(Utils.getListCheckBoxes(page) != null){
            listCheckBox = Utils.getListCheckBoxes(page);
        }
        int countCheck = Utils.getCountCheckBoxes(page);
        if(enable) countCheck++;
        Utils.putCountCheckBoxes(page, countCheck);

        listCheckBox.add(checkBox);
        Utils.putListCheckBoxes(page, listCheckBox);

        checkBox.setSelected(enable);

        checkBox.addActionListener(e -> {
            int count = Utils.getCountCheckBoxes(page);

            if(checkBox.isSelected()){
                count = count + 1;
            } else {
                count = count - 1;
            }

            ArrayList<Component> arrayList = Utils.getPanelEnabled(page);
            for(Component component : arrayList){
                boolean bol = count > 0;
                component.setEnabled(bol);
                if(component instanceof JLabel){
                    ((JLabel) component).setText(Utils.getString("label.status.ready." + bol));
                }
            }

            Utils.putCountCheckBoxes(page, count);
        });

        return checkBox;
    }
}
