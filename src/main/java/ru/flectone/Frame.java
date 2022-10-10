package ru.flectone;

import ru.flectone.components.FSwing;

import javax.swing.*;
import java.awt.*;

public class Frame extends JFrame {


    private static Frame thisFrame;

    //Main frame
    public Frame(){

        thisFrame = this;

        //Set size for frame
        setSizeFrame(FSwing.getFrameSize().width, FSwing.getFrameSize().height);
        setMinimumSize(1000, 550);

        //Close and exit
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Frame properties
        setVisible(true);
        setResizable(true);
        setLocationRelativeTo(null);


        setTitle(Utils.getString("frame.title") + Utils.getString("version.program"));
        setIconImage(Utils.getImageResources("logo").getImage());


        JLabel flectoneGif = new JLabel();
        flectoneGif.setIcon(Utils.getImageResources("flectone"));
        flectoneGif.setHorizontalAlignment(JLabel.CENTER);

        this.add(flectoneGif);
        revalidate();

        this.add(new TabbedPane());

        this.remove(flectoneGif);

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
