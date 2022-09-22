package ru.flectone.components;

import ru.flectone.Main;
import ru.flectone.swing.MessageDialog;
import ru.flectone.utils.UtilsSystem;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.WRITE;

public class Installation {

    //Create profile in minecraft launcher
    public Installation(String minecraftVersion, String pathToMinecraftFolder){
        try {

            //Get minecraft launcher_profiles.json
            File jsonFile = new File(Paths.get(pathToMinecraftFolder, "launcher_profiles.json").toString());
            //Check default minecraft path
            if(!jsonFile.exists()){
                new MessageDialog(UtilsSystem.getLocaleString("message.error.profile"), "error", 0);
                return;
            }
            //Get path to fabric jar and download
            String pathFabricJar = Paths.get(UtilsSystem.getWorkingDirectory(), "fabric.jar").toString();
            downloadFiles("mods/fabric.jar", pathFabricJar);

            //Get command and run fabric jar
            //Waiting process
            Process process = UtilsSystem.runJarFile(pathFabricJar + " client -dir " + pathToMinecraftFolder + " -noprofile -mcversion " + minecraftVersion);
            if(process == null) return;
            process.waitFor();

            //Delete temp fabric.jar
            Files.delete(Paths.get(pathFabricJar));

            //Get fabric name for start minecraft profile, default - user selected version
            String fabricName = minecraftVersion;

            //Get folder ./minecraft/versions
            File folderMinecraftVersions = new File(Paths.get(pathToMinecraftFolder, "versions").toString());

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
                                        .replace("%date%", LocalDateTime.now().toString()));
                            }
                        }
                        //Set profileCreated true
                        profileCreated = true;
                    }
                }
            }
            //Write ./minecraft/launcher_profiles.json
            Files.write(Paths.get(jsonFile.getPath()), listForFile);
        } catch (Exception error){
            //Show error
            new MessageDialog(UtilsSystem.getLocaleString("message.error.profile") + "\n" + error.getMessage(), "error", 0);
        }
    }

    public Installation(JLabel labelStatus, ArrayList<JCheckBox> arrayList, Path folderPath, String fileExtension, String urlFolder){

        new File(folderPath.toString()).mkdirs();

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

                unZipFile(url, Paths.get(path), labelStatus);
                continue;
            }


            String url = urlFolder + "/" + checkBox.getName() + fileExtension;
            String path = Paths.get(folderPath.toString(), checkBox.getName() + fileExtension).toString();

            downloadFiles(url, path);
        }
        if(!urlFolder.contains("/main")){
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
            downloadFiles("mods/" + fileName, path.toString());
        }
    }

    private void showSuccessInstallMessage(JLabel labelStatus){
        labelStatus.setForeground(null);
        labelStatus.setText(UtilsSystem.getLocaleString("label.status.ready.true"));
        new MessageDialog(UtilsSystem.getLocaleString("message.install.success"), "install", 1);
    }

    private void unZipFile(String url, Path decryptTo, JLabel labelStatus) {

        try {

            ZipInputStream zipInputStream = new ZipInputStream(openConnection(url).getInputStream());

            if(!new File(String.valueOf(decryptTo)).exists()){
                new File(String.valueOf(decryptTo)).mkdirs();
            }

            for (ZipEntry entry = zipInputStream.getNextEntry(); entry != null; entry = zipInputStream.getNextEntry()) {
                labelStatus.setForeground(new Color(79, 240, 114));
                labelStatus.setText(UtilsSystem.getLocaleString("label.status.install") + entry.getName() + "...");

                Path toPath = decryptTo.resolve(entry.getName());
                if (entry.isDirectory()) {
                    Files.createDirectory(toPath);
                } else try (FileChannel fileChannel = FileChannel.open(toPath, WRITE, CREATE)) {
                    fileChannel.transferFrom(Channels.newChannel(zipInputStream), 0, Long.MAX_VALUE);
                }
            }

        } catch (Exception error){
            new MessageDialog(UtilsSystem.getLocaleString("message.error.file.exist") + "\n" + error.getMessage(), "error", 0);
        }
    }

    //Download files from www.flectone.ru/mods
    public static void downloadFiles(String urlString, String toFileName){
        try {
            File file = new File(toFileName);
            if(!file.exists()) file.mkdirs();

            //Copy file is download to file path
            Files.copy(openConnection(urlString).getInputStream(), Paths.get(toFileName), StandardCopyOption.REPLACE_EXISTING);

        } catch (IOException error){
            //If file unable to download
            new MessageDialog(UtilsSystem.getLocaleString("message.error.download") + "\n" + error.getMessage(), urlString, "error", 0);
        }
    }

    private static URLConnection openConnection(String urlString){
        try {
            //Connect to site
            URLConnection openConnection = new URL(UtilsSystem.getWebSiteIp() + urlString).openConnection();
            //Add property to request than connect was real
            openConnection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0");
            return openConnection;
        } catch (IOException error) {
            new MessageDialog(UtilsSystem.getLocaleString("message.error.site") + "\n" + error.getMessage(), "error", 0);
            return null;
        }
    }
}
