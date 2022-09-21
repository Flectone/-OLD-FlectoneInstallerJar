package ru.flectone.utils;

import com.formdev.flatlaf.FlatDarkLaf;
import ru.flectone.Main;
import ru.flectone.swing.MessageDialog;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class UtilsSystem {

    public static HashMap<String, Integer> countCheckBoxHashMap;

    public static HashMap<String, ArrayList<Component>> enabledComponentsHashMap;

    public static String pathToMinecraftFolder;
    public static Color secondColor;

    public static boolean changeBackgroundColor = false;

    public static void setChangeBackgroundColor(boolean changeBackgroundColor) {
        UtilsSystem.changeBackgroundColor = changeBackgroundColor;
    }

    public static Color getSecondColor() {
        if(secondColor == null){
            if(FlatDarkLaf.isLafDark()) return new Color(54, 57, 59);
            else return new Color(218, 218, 218);
        }

        return secondColor;
    }

    public static void setSecondColor(Color secondColor) {
        UtilsSystem.secondColor = secondColor;
    }

    public static int getCountCheckBox(String page){
        int count;
        if(countCheckBoxHashMap.get(page) == null){
            count = 0;
            countCheckBoxHashMap.put(page, count);
        } else {
            count = countCheckBoxHashMap.get(page);
        }

        return count;
    }

    public static void setCountCheckBox(String page, int count) {
        countCheckBoxHashMap.put(page, count);
    }

    //Get default minecraft path
    public static void getMinecraftFolder(){

        pathToMinecraftFolder = getDefaultMinecraftFolder();
        
        if(UtilsSystem.getSettingsString("path.Minecraft") != null){
            pathToMinecraftFolder = UtilsSystem.getSettingsString("path.Minecraft");
        }
    }
    
    public static String getDefaultMinecraftFolder(){
        //Windows OS
        if(UtilsOS.systemOc.contains("win")){
            //Appdata + ./minecraft/mods/
            return System.getenv("APPDATA") + "\\.minecraft\\";
        }
        //Mac OS
        if(UtilsOS.systemOc.contains("mac")){
            //Users  + user name + /Library/Application Support/minecraft/mods
            return "/Users/" + UtilsSystem.getUserNameOC("/Users/") + "/Library/Application Support/minecraft/";
        }
        //Unix
        return "/home/" + UtilsSystem.getUserNameOC("/home/") + "/.minecraft/";
    }

    //Get user name for linux or mac
    public static String getUserNameOC(String replaceFolder){
        //Get full path
        String path = String.valueOf(Paths.get("").toAbsolutePath()).replace(replaceFolder, "");

        //String to char array
        char[] pathArray = path.toCharArray();
        //Ad username
        String userName = "";
        for(int x = 0; x < path.length(); x++){
            //Break when first slash found
            if(String.valueOf(pathArray[x]).equals("/")) break;
            //Else add elements to username
            userName = userName + String.valueOf(pathArray[x]);
        }
        //return final username
        return userName;
    }

    //Ad locale file
    public static HashMap<String, String> localeFile;


    //Get locale file
    public static void getLocaleFile(){
        //Create new locale file
        localeFile = new HashMap<>();
        getFileUtil(localeFile, Main.class.getResourceAsStream("/urls.yml"));

        for(String string : UtilsSystem.listObjectsFromConfig.get("support.languages")){
            if(UtilsOS.getSystemLocale().equals(string)){
                getFileUtil(localeFile, Main.class.getResourceAsStream("/language/" + string + ".yml"));
                return;
            }
        }
        getFileUtil(localeFile, Main.class.getResourceAsStream("/language/ru.yml"));
    }

    //All strings from locales ru, en
    public static String getLocaleString(String string){
        //Get string from hash map
        return localeFile.get(string);
    }

    //Ad settings file (flectonemods.txt)
    public static HashMap<String, String> settingsFile;

    //Get settings file (flectonemods.txt)
    public static void getSettingsFile(){
        //Create new settings
        settingsFile = new HashMap<>();
        try {
            //Get flectonemods.txt from path minecraft
            File file = new File(getWorkingDirectory() + File.separator + "flectone.installer");
            //Get settings file

            getFileUtil(settingsFile, Files.newInputStream(file.toPath()));
        } catch (Exception e){
            //File is empty
        }
    }

    //Add string to hash map
    private static void getFileUtil(HashMap hashMap, InputStream inputStream){
        Scanner scanner = new Scanner(inputStream, "UTF-8").useDelimiter("\\A");
        while(scanner.hasNext()){
            if(scanner.hasNextLine()){
                String[] nextLine = scanner.nextLine().split(": ");
                if(nextLine.length > 1){
                    hashMap.put(nextLine[0], nextLine[1].replace("\"", ""));
                }
            }
        }
    }

    //Get string from settings file
    public static String getSettingsString(String string){
        //Check file settings null or not
        if(settingsFile == null) return null;
        //Return string
        return settingsFile.get(string);
    }

    //Get path jar directory
    public static String getWorkingDirectory() {
        return new File("").getAbsolutePath();
    }

    //Create hash map from config.yml
    public static HashMap<String, String[]> listObjectsFromConfig;

    //Get name folders in "Resources"
    public static void getConfigFile(){
        Scanner scanner = new Scanner(Objects.requireNonNull(Main.class.getResourceAsStream("/config.yml")), "UTF-8").useDelimiter("\\A");
        while(scanner.hasNext()){
            String[] nextLine = scanner.nextLine().split(": ");
            listObjectsFromConfig.put(nextLine[0], nextLine[1].split(" "));
        }
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
                        removeFile(mod);
                    }
                }
            }
        } catch (Exception ignored){

        }
    }

    //Delete one file
    private static void removeFile(File file){
        try {
            //Delete
            Files.delete(file.toPath());
        } catch (Exception error) {
            //If file doesn't exist
            new MessageDialog(getLocaleString("message.error.remove"), file.getPath(), "error", 0);
        }
    }

    public static String getWebSiteIp(){
        return getLocaleString("flectone.url");
    }

    public static HashMap<String, ArrayList<JCheckBox>> listCheckBox;

    public static Process runJarFile(String pathToFile){
        try {
            return Runtime.getRuntime().exec("java -jar " + pathToFile);
        } catch (Exception error){
            new MessageDialog(UtilsSystem.getLocaleString("message.error.jar") + error, "error", 0);
        }
        return null;
    }

    public static String getVersionProgram(){
        return UtilsSystem.listObjectsFromConfig.get("version.program")[0];
    }
}
