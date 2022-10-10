package ru.flectone;

import javax.swing.*;
import java.awt.*;

public class Frame extends JFrame {


    private static Frame thisFrame;

    //Main frame
    public Frame(){

        thisFrame = this;

        //Set size for frame
        setSizeFrame(1000, 550);
        setMinimumSize(1000, 550);

        //Close and exit
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Frame properties
        setVisible(true);
        setResizable(true);
        setLocationRelativeTo(null);


        setTitle(Utils.getString("frame.title") + Utils.getString("version.program"));
        setIconImage(Utils.getImageResources("logo").getImage());

        JPanel panel = new JPanel();
        JLabel label = new JLabel();
        label.setIcon(Utils.getImageResources("flectone"));
        panel.add(label);

        this.add(panel);

        this.add(new TabbedPane());

        this.remove(panel);

        revalidate();
    }

    public static Frame getFrame(){
        return thisFrame;
    }

    public void setSizeFrame(int width, int height){
        setSize(new Dimension(width, height));
    }

    public void setMinimumSize(int width, int height){
        setMinimumSize(new Dimension(width, height));
    }
}
