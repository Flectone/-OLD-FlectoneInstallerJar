package ru.flectone;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;
import org.jsoup.select.Elements;
import ru.flectone.components.Installation;
import ru.flectone.swing.Frame;
import ru.flectone.swing.MessageDialog;
import ru.flectone.utils.UtilsOS;
import ru.flectone.utils.UtilsSystem;
import ru.flectone.utils.UtilsWeb;

import javax.swing.*;
import java.io.File;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

public class Main {

    public static boolean updateProgram = false;

    public static void main(String[] args){

        UtilsSystem.countCheckBoxHashMap = new HashMap<>();
        UtilsSystem.enabledComponentsHashMap = new HashMap<>();
        UtilsSystem.listCheckBox = new HashMap<>();
        UtilsSystem.imageHashMap = new HashMap<>();

        //Create new hash map from config.yml
        UtilsSystem.listObjectsFromConfig = new HashMap<>();

        //Get settings file (flectonemods.txt), default this null
        UtilsSystem.getSettingsFile();

        if(UtilsSystem.getSettingsString("chosen.Language") != null){
            UtilsOS.setSystemLocale(UtilsSystem.getSettingsString("chosen.Language"));
        }

        FlatDarkLaf.setup();
        if(UtilsSystem.getSettingsString("chosen.Theme") != null){
            if("light".equals(UtilsSystem.getSettingsString("chosen.Theme"))){
                FlatLightLaf.setup();
            }
        }

        //Get folders from hash map config.yml
        UtilsSystem.getConfigFile();

        //Get locale file (ru.yml or en.yml)
        UtilsSystem.getLocaleFile();

        try {
            new Thread(() -> UtilsWeb.parsingWebSite()).start();
        } catch (Exception ignored){

        }

        //Get default minecraft path
        UtilsSystem.getMinecraftFolder();
        UtilsSystem.getBooleanShowWarnMessages();
        UtilsSystem.getTabbedPaneAlign();

        new Frame();

        if(Boolean.parseBoolean(UtilsSystem.getSettingsString("boolean.Update"))) {
            try {
                Files.delete(Paths.get(UtilsSystem.getSettingsString("last.Path")));
            } catch (Exception error){
                new MessageDialog(error.toString(), "error", 0);
            }
        }

        checkUpdate();
    }

    private static void checkUpdate(){

        //Get files from html document
        Elements links = UtilsWeb.getHtmlPage("download/version/").select("a[href]");

        String lastProgramName = links.last().attr("href");

        String lastVersion = lastProgramName.split("_")[0].split("-")[1];

        if(!lastVersion.equals(UtilsSystem.getVersionProgram())){

            int reply = JOptionPane.showConfirmDialog(null, UtilsSystem.getLocaleString("message.update.agreement"), UtilsSystem.getLocaleString("message.update.title"), JOptionPane.YES_NO_OPTION);;

            if(reply == JOptionPane.YES_OPTION){
                Installation.downloadFiles("download/version/" + lastProgramName, UtilsSystem.getWorkingDirectory() + File.separator + lastProgramName);
                UtilsSystem.runJarFile(UtilsSystem.getWorkingDirectory() + File.separator + lastProgramName);

                updateProgram = true;

                System.exit(0);
            }
        }

    }
}