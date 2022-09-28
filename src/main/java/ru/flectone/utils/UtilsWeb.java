package ru.flectone.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.flectone.swing.MessageDialog;

import java.util.ArrayList;

public class UtilsWeb {

    //Get name mods from list.txt from www.flectone.ru/mods.../list
    public static void parsingWebSite() {
        new Thread(() -> {
            String[] versionsNotOpMods = UtilsSystem.listObjectsFromConfig.get("mods.notop.version");
            for(String version : versionsNotOpMods){
                if(UtilsSystem.listObjectsFromConfig.get("/notop/" + version) != null) continue;
                UtilsSystem.listObjectsFromConfig.put("/notop/" + version, getModsFromWebSite("/notop/" + version));
            }
        }).start();

        for(String folder : UtilsSystem.listObjectsFromConfig.get("web.folders")){
            new Thread(() -> {
                ArrayList<String> listComponents = new ArrayList<>();
                Elements components = getHtmlPage("/" + folder + "/").select("a[href]");

                for(Element element : components){
                    String name = element.attr("href");

                    if(name.contains(".") && !name.contains("litematic")){
                        listComponents.add(name.replace(".zip", ""));
                    }
                }
                UtilsSystem.listObjectsFromConfig.put(folder, listComponents.toArray(new String[0]));
            }).start();
        }

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
        
        //Get files from html document
        Elements links = getHtmlPage("mods/" + folderPath).select("a[href]");
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

    public static Document getHtmlPage(String urlForSite){
        try {
            //Get document html
            return Jsoup.connect(UtilsSystem.getWebSiteIp() + urlForSite).userAgent("Mozilla/5.0").get();
        } catch (Exception error){
            new MessageDialog(UtilsSystem.getLocaleString("message.error.site") + "\n" + error.getMessage(), "error", 0);
            return null;
        }
    }
}
