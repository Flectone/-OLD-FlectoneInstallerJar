package ru.flectone;

import org.jsoup.select.Elements;
import ru.flectone.components.FSwing;
import ru.flectone.components.Installation;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {

    public static void main(String[] args) throws IOException {
        new FSystem();

        new Thread(Utils::parsingWebSite).start();

        if(FSwing.isUpdateProgram()){
            Files.delete(Paths.get(Utils.getString("last.Path")));
            FSwing.setUpdateProgram(false);
        }

        checkUpdate();

        Frame frame = new Frame();


    }

    private static void checkUpdate() throws IOException {

        //Get files from html document
        Elements links = Utils.getHtmlPage("download/version/").select("a[href]");

        String lastProgramName = links.last().attr("href");

        String lastVersion = lastProgramName.split("_")[0].split("-")[1];

        if(!lastVersion.equals(Utils.getString("version.program"))){

            int reply = JOptionPane.showConfirmDialog(null, Utils.getString("message.update.agreement"), Utils.getString("message.update.title"), JOptionPane.YES_NO_OPTION);;

            if(reply == JOptionPane.YES_OPTION){
                Installation.downloadFiles("download/version/" + lastProgramName, Utils.getWorkingDirectory() + File.separator + lastProgramName);
                Installation.runJarFile(Utils.getWorkingDirectory() + File.separator + lastProgramName);

                FSwing.setUpdateProgram(true);

                System.exit(0);

            }
        }

    }

}
