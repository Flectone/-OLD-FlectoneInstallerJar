package ru.flectone;

import ru.flectone.utils.UtilsMessage;
import ru.flectone.utils.UtilsSystem;
import ru.flectone.utils.UtilsWeb;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
public class Installation {

    public Installation(JLabel labelStatus, ArrayList<JCheckBox> arrayList, Path folderPath, String fileExtension, String urlFolder){
        for(JCheckBox checkBox : arrayList){
            if(!checkBox.isSelected()) continue;

            labelStatus.setForeground(new Color(79, 240, 114));
            labelStatus.setText(UtilsSystem.getLocaleString("label.status.install") + checkBox.getName() + "...");

            if(urlFolder.equals("farms")){
                String toFile = "saves" + File.separator + checkBox.getName();
                if(checkBox.getName().contains("litematic")){
                    toFile = "schematics" + File.separator;
                }

                String url = urlFolder + "/" + checkBox.getName() + ".zip";
                String path = Paths.get(folderPath.toString(), toFile).toString();

                System.out.println(path);

                UtilsWeb.unzip(url, Paths.get(path), labelStatus);
                continue;
            }


            String url = urlFolder + "/" + checkBox.getName() + fileExtension;
            String path = Paths.get(folderPath.toString(), checkBox.getName() + fileExtension).toString();

            UtilsWeb.downloadFiles(url, path);
        }
        if(!urlFolder.contains("mods/")){
            showSuccessInstallMessage(labelStatus);
        }
    }

    public Installation(JLabel labelStatus, String[] strings, Path folderPath){
        //Download settings from site
        for(String fileName : strings){

            Path path;

            //Put options.txt to ./minecraft but another files to ./minecraft/config/
            if(fileName.equals("options.txt")) {
                path = Paths.get(folderPath.toString(), "options.txt");
            } else {
                path = Paths.get(folderPath.toString(), "config", fileName);
            }

            labelStatus.setForeground(new Color(79, 240, 114));
            labelStatus.setText(UtilsSystem.getLocaleString("label.status.install") + fileName + "...");

            //Download file
            UtilsWeb.downloadFiles("mods/" + fileName, path.toString());
        }
        showSuccessInstallMessage(labelStatus);
    }

    private void showSuccessInstallMessage(JLabel labelStatus){
        labelStatus.setForeground(null);
        labelStatus.setText(UtilsSystem.getLocaleString("label.status.ready.true"));
        UtilsMessage.showInformation(UtilsSystem.getLocaleString("message.install.success"));
    }
}
