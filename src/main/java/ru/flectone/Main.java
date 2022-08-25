package ru.flectone;

import com.formdev.flatlaf.FlatDarkLaf;
import ru.flectone.swing.Frame;
import ru.flectone.utils.UtilsOS;
import ru.flectone.utils.UtilsSystem;

import java.util.HashMap;

public class Main {
    public static void main(String[] args) {

        UtilsSystem.countCheckBoxHashMap = new HashMap<>();
        UtilsSystem.enabledComponentsHashMap = new HashMap<>();
        UtilsSystem.listCheckBox = new HashMap<>();

        //Set dark theme
        FlatDarkLaf.setup();

        UtilsOS.setSystemSlash();

        //Get settings file (flectonemods.txt), default this null
        UtilsSystem.getSettingsFile();

        //Get default minecraft path
        UtilsSystem.getMinecraftFolder();
        //Create new hash map from config.yml
        UtilsSystem.listObjectsFromConfig = new HashMap<>();
        //Get folders from hash map config.yml
        UtilsSystem.getFoldersList();
        //Get locale file (ru.yml or en.yml)
        UtilsSystem.getLocaleFile();

        FlatDarkLaf.setup();

        new Frame();
    }
}