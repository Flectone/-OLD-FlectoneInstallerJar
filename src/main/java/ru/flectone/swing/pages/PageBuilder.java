package ru.flectone.swing.pages;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.ui.FlatButtonBorder;
import ru.flectone.swing.Frame;
import ru.flectone.swing.TabbedPane;
import ru.flectone.utils.UtilsMessage;
import ru.flectone.utils.UtilsOS;
import ru.flectone.utils.UtilsSystem;
import ru.flectone.utils.UtilsWeb;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;

import static ru.flectone.utils.UtilsSystem.listCheckBox;

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

