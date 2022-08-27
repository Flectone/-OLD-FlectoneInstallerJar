package ru.flectone.utils;

import com.formdev.flatlaf.ui.FlatButtonBorder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.flectone.Main;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.WRITE;

public class UtilsWeb {

    //Get name mods from list.txt from www.flectone.ru/mods.../list
    public static void getModsList() {

        String[] modsType = UtilsSystem.listObjectsFromConfig.get("type");

        ArrayList<String> arrayList = new ArrayList<>();
        for(String modType : modsType){
            String[] versionMods = UtilsSystem.listObjectsFromConfig.get("version." + modType);
            for(String versionMod : versionMods){
                arrayList.add(modType + "/" + versionMod + "/main");
                arrayList.add(modType + "/" + versionMod + "/extension");
            }
        }

        for(String folderPath : arrayList.toArray(new String[0])){
            if(UtilsSystem.listObjectsFromConfig.get(folderPath) != null) continue;
            UtilsSystem.listObjectsFromConfig.put(folderPath, getModsFromWebSite(folderPath));
        }
    }

    public static String[] getModsFromWebSite(String folderPath){
        //Create document html
        Document html = null;
        try {
            //Get document html
            html = Jsoup.connect(UtilsSystem.getWebSiteIp() + "mods/" + folderPath).userAgent("Mozilla/5.0").get();
        } catch (Exception e){

        }
        //Get files from html document
        Elements links = html.select("a[href]");
        //Create new list for massive
        ArrayList<String> list = new ArrayList<>();
        for(Element link : links){
            //Get file name
            String name = link.attr("href");
            if(name.contains(".jar")){
                //If file is jar than add to massive
                list.add(name);
            }
        }

        return list.toArray(new String[0]);
    }
}
