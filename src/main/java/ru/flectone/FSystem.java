package ru.flectone;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;
import ru.flectone.components.FSwing;
import ru.flectone.components.Installation;

import java.awt.*;
import java.io.File;
import java.net.URL;
import java.nio.file.Paths;

public class FSystem {

    private static String systemLocale;

    private static String themeName;

    private static String pathToMinecraft;

    private static Color backgroundColor;

    public FSystem(){

        getFileFlectone();

        getFileLocale();
        getFileConfig();
        getFileOthers();

        getDefaultPathToMinecraft();

        getDefaultTabbedPaneAlign();

        setThemeName(Utils.getString("chosen.Theme"));

        if(themeName.equals("light")){
            FlatLightLaf.setup();
            FSwing.setSelectedTheme("light");
            setBackgroundColor(new Color(218, 218, 218));
        } else {
            FlatDarkLaf.setup();
            FSwing.setSelectedTheme("dark");
            setBackgroundColor(new Color(54, 57, 59));
        }

        if(!Utils.getString("show.Warns").equals(Utils.getString(""))){
            FSwing.setShowWarns(Boolean.parseBoolean(Utils.getString("show.Warns")));
        }

        if(!Utils.getString("boolean.Update").equals(Utils.getString(""))){
            FSwing.setUpdateProgram(Boolean.parseBoolean(Utils.getString("boolean.Update")));
        }

        if(!Utils.getString("frame.Size").equals(Utils.getString(""))){
            String[] ints = Utils.getString("frame.Size").split(" ");
            FSwing.setFrameSize(new Dimension(Integer.parseInt(ints[0]), Integer.parseInt(ints[1])));
        }


    }

    public static void setBackgroundColor(Color backgroundColor) {
        FSystem.backgroundColor = backgroundColor;
    }

    public static Color getBackgroundColor() {
        return backgroundColor;
    }

    private void getDefaultTabbedPaneAlign(){
        String stringInt = Utils.getString("chosen.Tab_Align");

        if(stringInt.equals(Utils.getString(""))) stringInt = "2";

        FSwing.setTabbedPaneAlign(Integer.parseInt(stringInt));
    }

    public static void setPathToMinecraft(String pathToMinecraft) {
        if(pathToMinecraft.length() < 10) {
            getDefaultPathToMinecraft();
            return;
        }
        FSystem.pathToMinecraft = pathToMinecraft;
    }

    public static String getPathToMinecraft() {
        return pathToMinecraft;
    }

    public static void getDefaultPathToMinecraft() {


        if(!Utils.getString("path.Minecraft").equals(Utils.getString(""))){
            setPathToMinecraft(Utils.getString("path.Minecraft"));
            return;
        }

        String systemOc = java.lang.System.getProperty("os.name").toLowerCase();

        String path = "/home/" + getUserNameOC("/home/") + "/.minecraft/";

        if(systemOc.contains("win")){
            //Appdata + ./minecraft/mods/
            path = java.lang.System.getenv("APPDATA") + "\\.minecraft\\";
        }

        if(systemOc.contains("mac")){
            //Users  + user name + /Library/Application Support/minecraft/mods
            path = "/Users/" + getUserNameOC("/Users/") + "/Library/Application Support/minecraft/";
        }

        setPathToMinecraft(path);

    }
    //Get user name for linux or mac
    private static String getUserNameOC(String replaceFolder){
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

    public static void setThemeName(String themeName) {
        FSystem.themeName = themeName;
    }

    public static void setSystemLocale(String systemLocale) {
        FSystem.systemLocale = systemLocale;
    }

    public static String getSystemLocale() {
        if(Utils.getString("chosen.Language") != null && !Utils.getString("chosen.Language").equals(Utils.getString(""))) return Utils.getString("chosen.Language");
        if(systemLocale != null) return systemLocale;
        return System.getenv("user.language");
    }

    private void getFileLocale(){

        URL fileLocale = getUrlFileLocale();

        if(fileLocale == null){
            FSystem.setSystemLocale("ru");
            fileLocale = getUrlFileLocale();
        }

        Utils.readFile(fileLocale);
    }

    private URL getUrlFileLocale(){
        return Utils.getUrlResources("/language/" + FSystem.getSystemLocale() + ".yml");
    }

    private void getFileConfig(){
        Utils.readFile(Utils.getUrlResources("/config.yml"));
    }

    private void getFileOthers(){
        Utils.readFile(Utils.getUrlResources("/others.yml"));
    }

    private void getFileFlectone(){
        File file = new File(Utils.getWorkingDirectory() + File.separator + "flectone.installer");
        if(file.exists()){
            try {
                Utils.readFile(file.toURL());
            } catch (Exception error){

            }
        }
    }

}
