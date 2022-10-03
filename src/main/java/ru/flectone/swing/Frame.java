package ru.flectone.swing;

import ru.flectone.Main;
import ru.flectone.utils.UtilsSystem;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class Frame extends JFrame{

    static TabbedPane tabbedPane = new TabbedPane();
    static JFrame frame;
    public Frame(){
        frame = this;
        this.add(tabbedPane);

        setSize(new Dimension(1000, 550));
        setMinimumSize(new Dimension(1000, 550));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle(UtilsSystem.getLocaleString("frame.title") + UtilsSystem.getVersionProgram());
        setVisible(true);
        setResizable(true);
        setLocationRelativeTo(null);
        setIconImage(new ImageIcon(Objects.requireNonNull(Main.class.getResource("/images/logo.png"))).getImage());

    }

    public static JFrame getFrame(){
        return frame;
    }

    public static int getSelectedIndex(){
        return tabbedPane.getSelectedIndex();
    }

    public static JTabbedPane getTabbedPane(){
        return tabbedPane;
    }
}
