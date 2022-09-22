package ru.flectone.components;

import ru.flectone.utils.UtilsSystem;

import javax.swing.*;

public class FLabel extends JLabel {

    public FLabel(String string) {
        setText(UtilsSystem.getLocaleString(string));
    }

    public FLabel setComponentAlignmentX(float alignmentX) {
        setAlignmentX(alignmentX);
        return this;
    }
}
