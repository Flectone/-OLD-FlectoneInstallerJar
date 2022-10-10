package ru.flectone;

import ru.flectone.components.FMessageDialog;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class FImage extends JLabel {


    public FImage(String string){

        if(Utils.getImage(string) != null){
            setIcon(Utils.getImage(string).getIcon());
        } else {
            getImageForLabel("/images/" + string);
            Utils.putIcon(string, this);
        }

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            }

            public void mouseClicked(MouseEvent e) {

                try {
                    Desktop.getDesktop().browse(new URL(Utils.getString("url." + string)).toURI());
                } catch (Exception error) {
                    new FMessageDialog(error.getMessage(), "error", 0);
                }

            }
        });
    }

    public FImage setCustomBorder(Border border){
        setBorder(border);
        return this;
    }

    protected void getImageForLabel(String url){

        new Thread(() -> {

            int k = 10;

            //Default flectone.gif width
            while(getIcon() == null){
                try {

                    //Connect to site
                    URLConnection openConnection = new URL(Utils.getString("flectone.url") + url + ".png").openConnection();

                    //Add property to request than connect was real
                    openConnection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0");

                    //Trying to download a file
                    InputStream in = openConnection.getInputStream();

                    BufferedImage myPicture = ImageIO.read(in);

                    setIcon(new ImageIcon(myPicture));

                } catch (Exception ignored){
                    try {
                        k--;
                        if(k == 0) {
                            setIcon(Utils.getImageResources("timed-out"));
                            break;
                        }
                        Thread.sleep(5000);
                    } catch (Exception error){

                    }
                }

            }

        }).start();
    }
}
