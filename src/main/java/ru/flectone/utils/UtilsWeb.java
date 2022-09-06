package ru.flectone.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.flectone.swing.MessageDialog;

import java.util.ArrayList;

public class UtilsWeb {

    //Get name mods from list.txt from www.flectone.ru/mods.../list
    public static void getModsList() {

        new Thread(() -> {
            String[] versionsNotOpMods = UtilsSystem.listObjectsFromConfig.get("mods.notop.version");
            for(String version : versionsNotOpMods){
                if(UtilsSystem.listObjectsFromConfig.get("/notop/" + version) != null) continue;
                UtilsSystem.listObjectsFromConfig.put("/notop/" + version, getModsFromWebSite("/notop/" + version));
            }
        }).start();

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
        } catch (Exception error){
            new MessageDialog(UtilsSystem.getLocaleString("message.error.site") + "\n" + error.getMessage(), "error", 0);
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
