package ru.flectone;

import com.formdev.flatlaf.ui.FlatButtonBorder;
import ru.flectone.swing.MessageDialog;
import ru.flectone.utils.UtilsSystem;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class Image extends JLabel {

    public Image(String string){
        ImageIcon imageIcon = new ImageIcon(Main.class.getResource("/flectone.gif"));

        setIcon(imageIcon);
        setBorder(new FlatButtonBorder());

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

    private void getImageForLabel(String url){
        new Thread(() -> {
            try {

                //Connect to site
                URLConnection openConnection = new URL(UtilsSystem.getWebSiteIp() + url).openConnection();
                //Add property to request than connect was real
                openConnection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0");

                //Trying to download a file
                InputStream in = openConnection.getInputStream();
                BufferedImage myPicture = ImageIO.read(in);

                this.setIcon(new ImageIcon(myPicture));


            } catch (Exception ignored){

            }

        }).start();
    }
}
