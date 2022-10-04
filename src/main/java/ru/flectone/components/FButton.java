package ru.flectone.components;

import ru.flectone.swing.MessageDialog;
import ru.flectone.utils.UtilsSystem;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class FButton extends JButton {

    public FButton(String localeString) {
        setText(UtilsSystem.getLocaleString("button." + localeString));
        setName(localeString);
    }

    public FButton addURL(){
        addActionListener(e -> {
            try {
                Desktop.getDesktop().browse(new URL(UtilsSystem.getLocaleString("url." + getName())).toURI());
            } catch (Exception error) {
                new MessageDialog(error.getMessage(), "error", 0);
            }
        });
        return this;
    }
}

