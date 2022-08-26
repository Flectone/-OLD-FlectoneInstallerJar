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

    public static void unzip(final String url, final Path decryptTo, JLabel labelStatus) {

        try {

            URLConnection openConnection = new URL(UtilsSystem.getWebSiteIp() + url).openConnection();
            //Add property to request than connect was real
            openConnection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0");

            ZipInputStream zipInputStream = new ZipInputStream(openConnection.getInputStream());

            if(!new File(String.valueOf(decryptTo)).exists()){
                new File(String.valueOf(decryptTo)).mkdirs();
            }

            for (ZipEntry entry = zipInputStream.getNextEntry(); entry != null; entry = zipInputStream.getNextEntry()) {
                labelStatus.setForeground(new Color(79, 240, 114));
                labelStatus.setText(UtilsSystem.getLocaleString("label.status.install") + entry.getName() + "...");

                Path toPath = decryptTo.resolve(entry.getName());
                if (entry.isDirectory()) {
                    Files.createDirectory(toPath);
                } else try (FileChannel fileChannel = FileChannel.open(toPath, WRITE, CREATE)) {
                    fileChannel.transferFrom(Channels.newChannel(zipInputStream), 0, Long.MAX_VALUE);
                }
            }

        } catch (Exception error){
            UtilsMessage.showErrorMessage(UtilsSystem.getLocaleString("message.error.file.exist") + error.getMessage(), null);
        }
    }

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

    //Download files from www.flectone.ru/mods
    public static void downloadFiles(String urlString, String toFileName){
        try {

            File file = new File(toFileName);
            if(!file.exists()) file.mkdirs();

            //Connect to site
            URLConnection openConnection = new URL(UtilsSystem.getLocaleString("flectone.url") + urlString).openConnection();
            //Add property to request than connect was real
            openConnection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0");

            //Trying to download a file
            InputStream in = openConnection.getInputStream();
            //Copy file is download to file path
            Files.copy(in, Paths.get(toFileName), StandardCopyOption.REPLACE_EXISTING);

        } catch (IOException e){
            //If file unable to download
            UtilsMessage.showErrorMessage(UtilsSystem.getLocaleString("message.error.download") + e.getMessage(), urlString);
        }
    }
}
