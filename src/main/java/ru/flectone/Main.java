package ru.flectone;

import com.formdev.flatlaf.FlatDarkLaf;
import ru.flectone.swing.Frame;
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
        //Get locale file (ru.yml or en.yml)
        UtilsSystem.getLocaleFile();

        //Get folders from hash map config.yml
        UtilsSystem.getFoldersList();

        new Thread(() -> UtilsWeb.getModsList()).start();

        //Set dark theme
        FlatDarkLaf.setup();

        //Get settings file (flectonemods.txt), default this null
        UtilsSystem.getSettingsFile();

        //Get default minecraft path
        UtilsSystem.getMinecraftFolder();

        FlatDarkLaf.setup();

        new Frame();
    }
}