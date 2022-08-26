package ru.flectone.swing;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.ui.FlatButtonBorder;
import ru.flectone.swing.pages.PageBuilder;
import ru.flectone.swing.pages.PageComponent;
import ru.flectone.utils.UtilsMessage;
import ru.flectone.utils.UtilsOS;
import ru.flectone.utils.UtilsSystem;
import ru.flectone.utils.UtilsWeb;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Objects;

import static ru.flectone.utils.UtilsSystem.listCheckBox;

public class TabbedPane extends JTabbedPane {

    private final JComboBox<String> comboBoxType = createComboBox("type");

    private final JComboBox<String> comboBoxVersion = createComboBox("version." + comboBoxType.getSelectedItem());

    private final JCheckBox checkBoxFabric = new JCheckBox(UtilsSystem.getLocaleString("checkbox.profile"));

    private final JCheckBox checkBoxDelete = new JCheckBox(UtilsSystem.getLocaleString("checkbox.delete"));

    private final JCheckBox checkBoxSettings = new JCheckBox(UtilsSystem.getLocaleString("checkbox.settings"));

    private final JTextComponent textComponent = new JTextField();

    private final JTextComponent datapackTextComponent = new JTextField();

    private final Box modsMain = Box.createVerticalBox();
    private final Box modsExtension = Box.createVerticalBox();

    public TabbedPane(){
        setTabPlacement(JTabbedPane.LEFT);
        setBorder(null);

        PageBuilder modsBuilder = new PageBuilder();
        modsBuilder.add(addModInstallPanel());
        addInstallPanel("mods", modsBuilder);

        modsBuilder.add(modsMain);
        modsBuilder.add(modsExtension);

        createMods();

        addTab(UtilsSystem.getLocaleString("tab.optimization"), modsBuilder.build());

        PageBuilder farmsBuilder = new PageBuilder();
        for(String image : UtilsSystem.listObjectsFromConfig.get("farms.images")){

            String fileName = "farms." + image.replace(".jpg", "");
            String version = fileName + ".version";
            String firstCheckBox = fileName + ".install";
            String secondCheckBox = fileName + ".litematic";
            String description = fileName + ".description";

            farmsBuilder.add(new PageComponent(image, version, firstCheckBox, secondCheckBox, description, "farms"));
            farmsBuilder.add(new JSeparator(SwingConstants.HORIZONTAL));
        }
        addInstallPanel("farms", farmsBuilder);
        addTab(UtilsSystem.getLocaleString("tab.farms"), farmsBuilder.build());

        PageBuilder rpsBuilder = new PageBuilder();
        for(String image : UtilsSystem.listObjectsFromConfig.get("rps.images")){
            String fileName = "rps." + image
                    .replace(".jpg", "")
                    .replace(".png", "")
                    .replace(".jpeg", "");
            String firstCheckBox = fileName + ".install";
            String description = fileName + ".description";

            rpsBuilder.add(new PageComponent(image, firstCheckBox, description, "resourcepacks"));
            rpsBuilder.add(new JSeparator(SwingConstants.HORIZONTAL));
        }
        addInstallPanel("resourcepacks", rpsBuilder);
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
            dpsBuilder.add(new PageComponent(image, version, firstCheckBox, description, "datapacks"));
            dpsBuilder.add(new JSeparator(SwingConstants.HORIZONTAL));
        }
        addTextComponentFieldPanel(dpsBuilder);
        addInstallPanel("datapacks", dpsBuilder);
        addTab(UtilsSystem.getLocaleString("tab.dps"), dpsBuilder.build());

        PageBuilder settingsBuilder = new PageBuilder();
        createSettingsPanel(settingsBuilder);

        addTab(UtilsSystem.getLocaleString("tab.settings"), settingsBuilder.build());

    }

    public Component addModInstallPanel(){

        //Panel where everything is added
        JPanel mainPanel = new JPanel();

        //Panel for labels, so they don't depend on other components
        JPanel leftPanel = new JPanel();
        //Create vertical box
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        JLabel labelType = new JLabel(UtilsSystem.getLocaleString("label.type.mods"));
        labelType.setAlignmentX(Component.RIGHT_ALIGNMENT);
        leftPanel.add(labelType);
        //Add space
        leftPanel.add(Box.createRigidArea(new Dimension(0, 14)));
        JLabel labelVersion = new JLabel(UtilsSystem.getLocaleString("label.version.mods"));
        labelVersion.setAlignmentX(Component.RIGHT_ALIGNMENT);
        leftPanel.add(labelVersion);

        //Label panel add to main panel
        mainPanel.add(leftPanel);

        //Create panel for other components
        JPanel rightPanel = new JPanel();

        //Create vertical box
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));

        //Create additional panel for type Version, so they are on the same line
        JPanel typeVersionLine = new JPanel();
        //Create horizontal box
        typeVersionLine.setLayout(new BoxLayout(typeVersionLine, BoxLayout.X_AXIS));

        comboBoxType.setPreferredSize(new Dimension(200, 20));
        comboBoxType.addActionListener(e -> {
            String[] list = UtilsSystem.listObjectsFromConfig.get("version." + comboBoxType.getSelectedItem());
            comboBoxVersion.setModel(new DefaultComboBoxModel<>(list));

            createMods();
        });

        typeVersionLine.add(comboBoxType);
        typeVersionLine.add(Box.createRigidArea(new Dimension(1, 0)));

        JCheckBox checkBoxFPS = new JCheckBox(UtilsSystem.getLocaleString("checkbox.FPS"));

        typeVersionLine.add(checkBoxFPS);

        //Add type version to right panel
        rightPanel.add(typeVersionLine);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        rightPanel.add(comboBoxVersion);

        comboBoxVersion.addActionListener(e -> {
            createMods();
        });

        //Add right panel to main panel
        mainPanel.add(rightPanel);

        //Create Box so that other components do not depend on the main panel
        Box box = Box.createVerticalBox();
        box.add(Box.createRigidArea(new Dimension(0, 5)));
        //Add main panel to box
        box.add(mainPanel);

        //Create line for check box
        JPanel checkBoxLine = new JPanel();

        checkBoxLine.add(checkBoxFabric);
        checkBoxLine.add(checkBoxSettings);
        checkBoxLine.add(checkBoxDelete);

        //Add line to box
        box.add(checkBoxLine);

        return box;
    }

    //Create combo box
    private JComboBox<String> createComboBox(String nameMassive){
        //Create combo box with UtilsSystem.getFoldersList
        JComboBox<String> comboBox = new JComboBox<>(Objects.requireNonNull(UtilsSystem.listObjectsFromConfig.get(nameMassive)));
        //Add to action listener so that check click
        if(nameMassive.contains("type")){
            comboBox.setSelectedIndex(1);
        }
        return comboBox;
    }

    private void createMods(){
        modsMain.removeAll();
        createModsUtil("main", modsMain);
        modsExtension.removeAll();
        createModsUtil("extension", modsExtension);
    }

    private void createModsUtil(String folder, Box panel){
        String pathToMods = comboBoxType.getSelectedItem() + "/" + comboBoxVersion.getSelectedItem() + "/" + folder;

        String[] modsList = UtilsSystem.listObjectsFromConfig.get(pathToMods);
        if(modsList == null){
            modsList = UtilsWeb.getModsFromWebSite(pathToMods);
            UtilsSystem.listObjectsFromConfig.put(pathToMods, modsList);
        }

        JPanel panelModsTotal = new JPanel();
        panelModsTotal.add(new JLabel(UtilsSystem.getLocaleString("mods.label." + folder)  + " (" + UtilsSystem.getLocaleString("mods.total") + modsList.length + ")"));
        panel.add(panelModsTotal);
        panel.add(new JSeparator(SwingUtilities.HORIZONTAL));

        for(String string : modsList){
            String fileName = "mods." + string
                    .replace(".jar", "");
            String firstCheckBox = fileName + ".install";
            String description = fileName + ".description";

            panel.add(new PageComponent(string.replace(".jar", ".png"), firstCheckBox, true, description, "mods"));
            panel.add(new JSeparator(SwingUtilities.HORIZONTAL));
        }
    }

    public void createSettingsPanel(PageBuilder builder){

        JPanel componentPanel = new JPanel();
        componentPanel.setLayout(new BoxLayout(componentPanel, BoxLayout.Y_AXIS));

        JPanel textComponentLine = new JPanel();
        textComponentLine.setLayout(new BoxLayout(textComponentLine, BoxLayout.X_AXIS));

        //Create field
        textComponent.setBorder(new FlatButtonBorder());

        //Create text component with name from locale
        textComponent.setText(UtilsSystem.pathToMinecraftFolder);

        textComponentLine.add(textComponent);

        textComponentLine.add(Box.createRigidArea(new Dimension(3, 0)));


        JButton buttonDialogSettings = new JButton(UtilsSystem.getLocaleString("button.dialog"));
        buttonDialogSettings.addActionListener(e -> new Thread(() -> actionOnButtonDialog(textComponent)).start());

        textComponentLine.add(buttonDialogSettings);

        componentPanel.add(Box.createRigidArea(new Dimension(0, 3)));
        componentPanel.add(textComponentLine);
        componentPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        JComboBox<String> comboBoxLanguage = createComboBox("ru", "en");

        componentPanel.add(comboBoxLanguage);
        comboBoxLanguage.setSelectedItem(UtilsSystem.getLocaleString("button." + UtilsOS.getSystemLocale()));

        componentPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        JComboBox<String> comboBoxTheme = createComboBox("dark", "light");

        componentPanel.add(comboBoxTheme);
        comboBoxTheme.setSelectedItem(FlatLightLaf.isLafDark() ? UtilsSystem.getLocaleString("button.dark") : UtilsSystem.getLocaleString("button.light"));

        JPanel labelPanel = new JPanel();
        labelPanel.setLayout(new BoxLayout(labelPanel, BoxLayout.Y_AXIS));

        JLabel labelFolder = new JLabel(UtilsSystem.getLocaleString("label.folder.minecraft"));
        labelFolder.setAlignmentX(Component.RIGHT_ALIGNMENT);
        labelPanel.add(labelFolder);
        labelPanel.add(Box.createRigidArea(new Dimension(0, 14)));

        JLabel labelLanguage = new JLabel(UtilsSystem.getLocaleString("label.language"));
        labelLanguage.setAlignmentX(Component.RIGHT_ALIGNMENT);
        labelPanel.add(labelLanguage);
        labelPanel.add(Box.createRigidArea(new Dimension(0, 14)));

        JLabel labelTheme = new JLabel(UtilsSystem.getLocaleString("label.theme"));
        labelTheme.setAlignmentX(Component.RIGHT_ALIGNMENT);
        labelPanel.add(labelTheme);

        JPanel panel = new JPanel();
        panel.add(labelPanel);
        panel.add(componentPanel);

        builder.add(panel);

        comboBoxLanguage.addActionListener(e -> actionWhenChangedLocale(comboBoxLanguage));
        comboBoxTheme.addActionListener(e -> actionWhenChangedTheme(comboBoxTheme));

    }

    private JComboBox<String> createComboBox(String firstButton, String secondButton){
        ArrayList<String> listLanguage = new ArrayList<>();
        listLanguage.add(UtilsSystem.getLocaleString("button." + firstButton));
        listLanguage.add(UtilsSystem.getLocaleString("button." + secondButton));

        return new JComboBox<>(listLanguage.toArray(new String[0]));
    }

    //Action when user click on button locale (update frame)
    private void actionWhenChangedLocale(JComboBox<String> comboBoxLanguage){
        //Update system locale
        if(comboBoxLanguage.getSelectedItem().equals(UtilsSystem.getLocaleString("button.ru"))){
            UtilsOS.setSystemLocale("ru");
        } else {
            UtilsOS.setSystemLocale("en");
        }

        //Reload "locale" file
        UtilsSystem.getLocaleFile();
        reloadFrame();
    }

    private void actionWhenChangedTheme(JComboBox<String> comboBoxTheme){
        if(comboBoxTheme.getSelectedItem().equals(UtilsSystem.getLocaleString("button.light"))){
            //Set light theme
            FlatLightLaf.setup();
        } else {
            //Set dark theme
            FlatDarkLaf.setup();
        }
        FlatLightLaf.updateUI();
        reloadFrame();
    }

    private void reloadFrame(){
        new Thread(() -> {
            JTabbedPane newTabbedPane = new TabbedPane();
            newTabbedPane.setSelectedIndex( Frame.getSelectedIndex());

            Frame.getFrame().setTitle(UtilsSystem.getLocaleString("frame.title"));
            Frame.getFrame().getContentPane().removeAll();
            Frame.getFrame().getContentPane().add(newTabbedPane);
            Frame.getFrame().validate();
        }).start();
    }

    private void actionOnButtonDialog(JTextComponent textComponentFolder){
        //Create new window with file chooser
        JFileChooser fileChooser = new JFileChooser();
        //Set file chooser title
        fileChooser.setDialogTitle(UtilsSystem.getLocaleString("dialog.title"));
        //Set default path
        fileChooser.setSelectedFile(new File(UtilsSystem.getDefaultMinecraftFolder()));
        //Set file chooser mode
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        //Update folders in file chooser
        SwingUtilities.updateComponentTreeUI(fileChooser);

        //Get file chooser value
        int returnVal = fileChooser.showSaveDialog(this);
        //If user click save
        if(returnVal == JFileChooser.APPROVE_OPTION) {
            //Set text component folder
            textComponentFolder.setText(fileChooser.getSelectedFile().getPath() + UtilsOS.systemSlash);
            if(textComponentFolder == textComponent) UtilsSystem.pathToMinecraftFolder = fileChooser.getSelectedFile().getPath() + UtilsOS.systemSlash;
        }
    }

    public void addTextComponentFieldPanel(PageBuilder builder){
        JPanel dialogPanel = new JPanel();
        dialogPanel.add(new JLabel(UtilsSystem.getLocaleString("label.folder.minecraft.saves")));
        datapackTextComponent.setText(UtilsSystem.pathToMinecraftFolder + "saves" + UtilsOS.systemSlash);
        dialogPanel.add(datapackTextComponent);
        JButton buttonDialog = new JButton(UtilsSystem.getLocaleString("button.dialog"));
        dialogPanel.add(buttonDialog);

        buttonDialog.addActionListener(e -> actionOnButtonDialog(datapackTextComponent));

        builder.add(dialogPanel);
    }

    public void addInstallPanel(String page, PageBuilder builder){

        ArrayList<Component> arrayList = new ArrayList<>();

        Box box = Box.createVerticalBox();

        JLabel labelStatus = new JLabel(UtilsSystem.getLocaleString("label.status.ready.false"));
        labelStatus.setEnabled(false);

        JPanel panelStatus = new JPanel();
        panelStatus.add(labelStatus);
        arrayList.add(labelStatus);
        box.add(panelStatus);

        JButton buttonInstall = new JButton(UtilsSystem.getLocaleString("button.install"));
        buttonInstall.addActionListener(e -> new Thread(() -> {
            switch(page){
                case "datapacks":
                    installZipFile(page, datapackTextComponent, labelStatus);
                    break;
                case "resourcepacks":
                    installZipFile(page, textComponent, labelStatus);
                    break;
                case "farms":
                    installFarms(labelStatus);
                    break;
            }
        }).start());
        buttonInstall.setEnabled(false);
        JButton buttonDialog = new JButton(UtilsSystem.getLocaleString("button.dialog"));
        buttonDialog.addActionListener(e -> {
            Frame.getTabbedPane().setSelectedIndex(Frame.getTabbedPane().getTabCount()-1);
            actionOnButtonDialog(textComponent);
        });

        JPanel panelInstall = new JPanel();
        panelInstall.add(buttonInstall);
        arrayList.add(buttonInstall);
        panelInstall.add(buttonDialog);
        box.add(panelInstall);

        builder.add(box);
        UtilsSystem.enabledComponentsHashMap.put(page, arrayList);
    }

    private void installZipFile(String page, JTextComponent textComponent, JLabel labelStatus){
        for(JCheckBox checkBox : listCheckBox.get(page)){
            if(!checkBox.isSelected()) continue;

            labelStatus.setForeground(new Color(79, 240, 114));
            labelStatus.setText(UtilsSystem.getLocaleString("label.status.install") + checkBox.getName() + "...");

            String url = page + "/" + checkBox.getName() + ".zip";

            String path = textComponent.getText() + page + UtilsOS.getSystemSlash() + checkBox.getName() + ".zip";

            UtilsWeb.downloadFiles(url, path);
        }
        labelStatus.setForeground(null);
        labelStatus.setText(UtilsSystem.getLocaleString("label.status.ready.true"));
        UtilsMessage.showInformation(UtilsSystem.getLocaleString("message.install.success"));
    }

    private void installFarms(JLabel labelStatus){

        for(JCheckBox checkBox : listCheckBox.get("farms")){
            if(!checkBox.isSelected()) continue;

            String toFile = "saves" + UtilsOS.getSystemSlash() + checkBox.getName();
            if(checkBox.getName().contains("litematic")){
                toFile = "schematics" + UtilsOS.getSystemSlash();
            }

            String url = "farms/" + checkBox.getName() + ".zip";
            String path = UtilsSystem.pathToMinecraftFolder + toFile;

            UtilsWeb.unzip(url, Paths.get(path), labelStatus);
        }

        labelStatus.setText(UtilsSystem.getLocaleString("label.status.ready.true"));
        labelStatus.setForeground(null);

        //Show successful message
        UtilsMessage.showInformation(UtilsSystem.getLocaleString("message.install.success"));
    }

}
