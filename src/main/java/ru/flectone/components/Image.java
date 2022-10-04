package ru.flectone.components;

import com.formdev.flatlaf.ui.FlatButtonBorder;
import ru.flectone.Main;
import ru.flectone.swing.Frame;
import ru.flectone.swing.MessageDialog;
import ru.flectone.swing.TabbedPane;
import ru.flectone.utils.UtilsSystem;

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

public class Image extends JLabel {

    public Image(String string){

        System.out.println(string);

        UtilsSystem.imageHashMap.put(string, this);

        getImageForLabel("/images/" + string);

        String urlString = string
                .replace(".jpg", "")
                .replace(".png", "")
                .replace(".jpeg", "");

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
                    Desktop.getDesktop().browse(new URL(UtilsSystem.getLocaleString("url." + urlString)).toURI());
                } catch (Exception error) {
                    new MessageDialog(error.getMessage(), "error", 0);
                }

            }
        });
    }

    public Image setCustomBorder(Border border){
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
                    URLConnection openConnection = new URL(UtilsSystem.getWebSiteIp() + url).openConnection();

                    //Add property to request than connect was real
                    openConnection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0");

                    //Trying to download a file
                    InputStream in = openConnection.getInputStream();

                    BufferedImage myPicture = ImageIO.read(in);

                    setIcon(new ImageIcon(myPicture));

                } catch (Exception ignored){
                    try {
                        k -= 1;
                        if(k == 0) {
                            setIcon(new ImageIcon(Main.class.getResource("/images/timed-out.png")));
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
