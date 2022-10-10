package ru.flectone;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.flectone.components.FMessageDialog;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;

public class Utils {

    private static final HashMap<String, ArrayList<Component>> panelEnabled = new HashMap<>();

    public static void putPanelEnabled(String pageName, ArrayList<Component> arrayList) {
        panelEnabled.put(pageName, arrayList);
    }

    public static ArrayList<Component> getPanelEnabled(String pageName) {
        return panelEnabled.get(pageName);
    }


    private static final HashMap<String, Integer> countCheckBoxes = new HashMap<>();

    public static void putCountCheckBoxes(String pageName, int count) {
        countCheckBoxes.put(pageName, count);
    }

    public static int getCountCheckBoxes(String pageName) {
        int count;
        if(countCheckBoxes.get(pageName) == null){
            count = 0;
            countCheckBoxes.put(pageName, count);
        } else {
            count = countCheckBoxes.get(pageName);
        }

        return count;
    }

    private static final HashMap<String, ArrayList<JCheckBox>> listCheckBoxes = new HashMap<>();

    public static void putListCheckBoxes(String pageName, ArrayList<JCheckBox> arrayList) {
        listCheckBoxes.put(pageName, arrayList);
    }

    public static ArrayList<JCheckBox> getListCheckBoxes(String pageName) {
        return listCheckBoxes.get(pageName);
    }

    private static final HashMap<String, FImage> imageHashMap = new HashMap<>();

    public static FImage getImage(String iconName){
        return imageHashMap.get(iconName);
    }

    public static void putIcon(String iconName, FImage image){
        imageHashMap.put(iconName, image);
    }

    public static ImageIcon getImageResources(String imageName){
        if(imageName.equals("flectone")) return new ImageIcon(getUrlResources("/images/flectone.gif"));
        return new ImageIcon(getUrlResources("/images/" + imageName + ".png"));
    }

    public static HashMap<String, String> stringsFiles = new HashMap<>();
    public static String getString(String string){
        String fileString = stringsFiles.get(string);

        if(fileString == null){
            return stringsFiles.get("label.empty");
        }

        return stringsFiles.get(string);
    }

    public static InputStream getAsStreamResources(String pathToFile){
        return Main.class.getResourceAsStream(pathToFile);
    }

    public static URL getUrlResources(String pathToFile){
        return Main.class.getResource(pathToFile);
    }

    public static void readFile(URL url){
        try {

            InputStreamReader inputStreamReader = new InputStreamReader(url.openStream(), StandardCharsets.UTF_8);

            BufferedReader read = new BufferedReader(inputStreamReader);

            String line;
            while((line = read.readLine()) != null){

                if(line.isEmpty() || line.startsWith("#")) continue;

                String[] strings = line.replace("\"", "").split(": ");
                stringsFiles.put(strings[0], strings[1]);
            }
            read.close();

        } catch (Exception error){
            System.out.println();
        }
    }

    public static String getWorkingDirectory() {
        return new File("").getAbsolutePath();
    }

    public static void parsingWebSite() {

        putModsToHashMap(getString("mods.notop.version"));

        for(String folder : Utils.getString("web.folders").split(" ")){
            StringBuilder listComponents = new StringBuilder();
            Elements components = getHtmlPage("/" + folder + "/").select("a[href]");

            for(Element element : components){
                String name = element.attr("href");

                if(name.contains(".") && !name.contains("litematic")){
                    listComponents.append(name.replace(".zip", "")).append(" ");
                }
            }
            Utils.stringsFiles.put(folder, listComponents.toString());
        }

        StringBuilder versionPaths = new StringBuilder();
        for(String modType : getString("type").split(" ")){
            String[] versionMods = getString("version." + modType).split(" ");
            for(String versionMod : versionMods){
                versionPaths.append(modType + "/" + versionMod + "/main ");
                versionPaths.append(modType + "/" + versionMod + "/extension ");
            }

        }

        putModsToHashMap(versionPaths.toString());
    }

    private static void putModsToHashMap(String stringsWithMods){
        new Thread(() ->{
            for(String folderPath : stringsWithMods.split(" ")){
                if(!getString(folderPath).equals(getString("")) || !folderPath.contains("/")) continue;
                getModsFromHtml(folderPath);
            }
        }).start();
    }

    private static String getModsFromHtml(String folderPath){
        Elements links = getHtmlPage("mods/" + folderPath).select("a[href]");

        StringBuilder modsName = new StringBuilder();
        for(Element link : links){
            //Get file name
            String name = link.attr("href");
            if(name.contains(".jar")){
                //If file is jar than add to massive
                modsName.append(name + " ");
            }
        }
        stringsFiles.put(folderPath, modsName.toString());
        return modsName.toString();
    }

    public static Document getHtmlPage(String urlForSite){
        try {
            //Get document html
            return Jsoup.connect(Utils.getString("flectone.url") + urlForSite).userAgent("Mozilla/5.0").get();
        } catch (Exception error){
            new FMessageDialog(Utils.getString("message.error.site") + "\n" + error.getMessage(), "error", 0);
            return null;
        }
    }

    public static String getStringWithMods(String folderPath){
        return stringsFiles.get(folderPath) != null ? stringsFiles.get(folderPath) : Utils.getModsFromHtml(folderPath);
    }

    //Remove all jar files from folder
    public static void removeListFiles(String textComponentFolder){
        try {
            //Get folder
            for(File mod : new File(textComponentFolder).listFiles()){
                //Get file name
                String fileName = mod.toString();

                //If file have extension
                int index = fileName.lastIndexOf('.');
                if(index > 0) {
                    //Get extension file
                    String extension = fileName.substring(index + 1);
                    //If file .jar
                    if(extension.equals("jar")){
                        //Delete file
                        Files.delete(mod.toPath());
                    }
                }
            }
        } catch (Exception ignored){
            new FMessageDialog(getString("message.error.remove"), "error", 0);
        }
    }

}
