package ru.flectone;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;
import ru.flectone.swing.Frame;
import ru.flectone.utils.UtilsOS;
import ru.flectone.utils.UtilsSystem;
import ru.flectone.utils.UtilsWeb;

import java.util.HashMap;

public class Main {
    public static void main(String[] args) {

        UtilsSystem.countCheckBoxHashMap = new HashMap<>();
        UtilsSystem.enabledComponentsHashMap = new HashMap<>();
        UtilsSystem.listCheckBox = new HashMap<>();
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
            new Thread(() -> UtilsWeb.getModsList()).start();
        } catch (Exception ignored){

        }

        //Get default minecraft path
        UtilsSystem.getMinecraftFolder();

        new Frame();
    }
}