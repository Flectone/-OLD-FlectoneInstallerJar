package ru.flectone.utils;

import ru.flectone.Main;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.Scanner;

public class UtilsSystem {

    public static HashMap<String, Integer> countCheckBoxHashMap;

    public static HashMap<String, ArrayList<Component>> enabledComponentsHashMap;

    public static String pathToMinecraftFolder;

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

        //User knows russian language?
        if(UtilsOS.getSystemLocale().equals("ru") || UtilsOS.getSystemLocale().equals("ua") || UtilsOS.getSystemLocale().equals("be") || UtilsOS.getSystemLocale().equals("kz")){
            //Get ru locale file
            getFileUtil(localeFile, Main.class.getResourceAsStream("/language/ru.yml"));
        } else {
            //Get en locale file
            getFileUtil(localeFile, Main.class.getResourceAsStream("/language/en.yml"));
        }
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
            File file = new File(getWorkingDirectory() + File.separator + "flectonemods.txt");
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

    //Get logo for program from resources
    public static Image getImageIcon(){
        return new ImageIcon(Objects.requireNonNull(Main.class.getResource("/logo.png"))).getImage();
    }

    //Get path jar directory
    public static String getWorkingDirectory() {
        return new File("").getAbsolutePath();
    }

    //Create hash map from config.yml
    public static HashMap<String, String[]> listObjectsFromConfig;

    //Get name folders in "Resources"
    public static void getFoldersList(){
        Scanner scanner = new Scanner(Objects.requireNonNull(Main.class.getResourceAsStream("/config.yml")), "UTF-8").useDelimiter("\\A");
        while(scanner.hasNext()){
            String[] nextLine = scanner.nextLine().split(": ");
            listObjectsFromConfig.put(nextLine[0], nextLine[1].split(" "));
        }
    }

    //Get system date
    private static String getSystemDate(){
        return LocalDateTime.now().toString();
    }

    //Create profile in minecraft launcher
    public static void createCustomProfile(String minecraftVersion){
        try {

            //Get folder ./minecraft
            String pathMinecraftFolder = pathToMinecraftFolder;

            //Get minecraft launcher_profiles.json
            File jsonFile = new File(pathMinecraftFolder + File.separator + "launcher_profiles.json");
            //Check default minecraft path
            if(!jsonFile.exists()){
                UtilsMessage.showErrorMessage(getLocaleString("message.error.profile"), null);
                return;
            }

            //Get path to fabric jar and download
            String pathFabricJar = getWorkingDirectory() + File.separator + "fabric.jar";
            UtilsWeb.downloadFiles("mods/fabric.jar", pathFabricJar);

            //Get command and run fabric jar
            String commandRunFabric = "java -jar " + pathFabricJar + " client -dir " + pathMinecraftFolder + " -noprofile -mcversion " + minecraftVersion;
            Process process = Runtime.getRuntime().exec(commandRunFabric);
            //Waiting process
            process.waitFor();

            //Get fabric name for start minecraft profile, default - user selected version
            String fabricName = minecraftVersion;

            //Get folder ./minecraft/versions
            File folderMinecraftVersions = new File(pathMinecraftFolder + File.separator + "versions");

            //Get all files from /versions
            for(File version : folderMinecraftVersions.listFiles()){
                //Get version folder name
                String fileName = version.getName();

                //If fabric installed
                if(fileName.contains("fabric-loader") && fileName.contains(minecraftVersion)){
                    //Set fabric name
                    fabricName = fileName;
                    break;
                }
            }

            //Check profile exist
            boolean profileCreated = false;
            //Create list for new file
            ArrayList<String> listForFile = new ArrayList<>();
            //Read file line by line
            Scanner scanner = new Scanner(jsonFile, "UTF-8").useDelimiter("\\A");

            while(scanner.hasNext()){
                //Check next line
                if(scanner.hasNextLine()){
                    //Get next line
                    String line = scanner.nextLine();
                    //Add line to final file
                    listForFile.add(line);

                    //Add flectone profile to file
                    if(line.equals("    },") && !profileCreated){
                        //Read file from resources line by line
                        Scanner scannerProfile = new Scanner(Main.class.getResourceAsStream("/profile.yml"), "UTF-8").useDelimiter("\\A");

                        //If profile.yml not empty
                        while(scannerProfile.hasNext()){
                            //Check next line
                            if(scannerProfile.hasNextLine()){
                                //Get next line
                                String lineProfile = scannerProfile.nextLine();
                                //Add line profile to final file
                                listForFile.add(lineProfile
                                        .replace("%version%", minecraftVersion)
                                        .replace("%fabric%", fabricName)
                                        .replace("%date%", getSystemDate()));
                            }
                        }
                        //Set profileCreated true
                        profileCreated = true;
                    }
                }
            }
            //Write ./minecraft/launcher_profiles.json
            Files.write(Paths.get(jsonFile.getPath()), listForFile);
        } catch (Exception e){
            //Show error
            UtilsMessage.showErrorMessage(getLocaleString("message.error.profile") + e.getLocalizedMessage(), null);
        }
    }


    //Remove all jar files from folder
    public static void removeListFiles(String textComponentFolder){
        //Get folder
        File file = new File(textComponentFolder);
        for(File mod : Objects.requireNonNull(file.listFiles())){
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
    }

    //Delete one file
    private static void removeFile(File file){
        try {
            //Delete
            Files.delete(file.toPath());
        } catch (Exception error) {
            //If file doesn't exist
            UtilsMessage.showErrorMessage(getLocaleString("message.error.remove"), file.getPath());
        }
    }

    public static String getWebSiteIp(){
        return getLocaleString("flectone.url");
    }

    public static HashMap<String, ArrayList<JCheckBox>> listCheckBox;
}
