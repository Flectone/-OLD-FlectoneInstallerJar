package ru.flectone.swing;

import ru.flectone.swing.pages.PageBuilder;
import ru.flectone.swing.pages.PageComponent;
import ru.flectone.utils.UtilsSystem;

import javax.swing.*;

public class TabbedPane extends JTabbedPane {
    public TabbedPane(){
        setTabPlacement(JTabbedPane.LEFT);
        setBorder(null);

        PageBuilder modsBuilder = new PageBuilder();
        modsBuilder.addModInstallPanel();
        modsBuilder.addInstallPanel("mods");

        addTab(UtilsSystem.getLocaleString("tab.optimization"), modsBuilder.build());

        PageBuilder farmsBuilder = new PageBuilder();
        for(String image : UtilsSystem.listObjectsFromConfig.get("farms.images")){

            String fileName = "farms." + image.replace(".jpg", "");
            String version = fileName + ".version";
            String firstCheckBox = fileName + ".install";
            String secondCheckBox = fileName + ".litematic";
            String description = fileName + ".description";

            farmsBuilder.addPageComponent(new PageComponent(image, version, firstCheckBox, secondCheckBox, description));
            farmsBuilder.add(new JSeparator(SwingUtilities.HORIZONTAL));
        }
        farmsBuilder.addInstallPanel("farms");
        addTab(UtilsSystem.getLocaleString("tab.farms"), farmsBuilder.build());

        PageBuilder rpsBuilder = new PageBuilder();
        for(String image : UtilsSystem.listObjectsFromConfig.get("rps.images")){
            String fileName = "rps." + image
                    .replace(".jpg", "")
                    .replace(".png", "")
                    .replace(".jpeg", "");
            String firstCheckBox = fileName + ".install";
            String description = fileName + ".description";

            rpsBuilder.addPageComponent(new PageComponent(image, firstCheckBox, description));
            rpsBuilder.add(new JSeparator(SwingUtilities.HORIZONTAL));
        }
        rpsBuilder.addInstallPanel("resourcepacks");
        addTab(UtilsSystem.getLocaleString("tab.rps"), rpsBuilder.build());

        PageBuilder dpsBuilder = new PageBuilder();
        for(String image : UtilsSystem.listObjectsFromConfig.get("dps.images")){
            String fileName = "dps." + image
                    .replace(".jpg", "")
                    .replace(".png", "")
                    .replace(".jpeg", "");
            String version = fileName + ".version";
            String firstCheckBox = fileName + ".install";
            String description = fileName + ".description";
            dpsBuilder.addPageComponent(new PageComponent(image, version, firstCheckBox, description));
            dpsBuilder.add(new JSeparator(SwingUtilities.HORIZONTAL));
        }
        dpsBuilder.addTextComponentFieldPanel();
        dpsBuilder.addInstallPanel("datapacks");
        addTab(UtilsSystem.getLocaleString("tab.dps"), dpsBuilder.build());

        PageBuilder settingsBuilder = new PageBuilder();
        settingsBuilder.addSettingsPanel();

        addTab(UtilsSystem.getLocaleString("tab.settings"), settingsBuilder.build());

    }
}
