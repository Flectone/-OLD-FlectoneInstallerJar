package ru.flectone.swing.pages;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.ui.FlatButtonBorder;
import ru.flectone.swing.Frame;
import ru.flectone.swing.TabbedPane;
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

public class PageBuilder {

    private final Box builder = Box.createVerticalBox();

    public void add(Component component){
        builder.add(component);
    }

    public void addPageComponent(PageComponent pageComponent){
        builder.add(pageComponent);
    }

    static JTextComponent textComponent = new JTextField();

    JTextComponent datapackTextComponent = new JTextField();

    JLabel labelStatus = new JLabel(UtilsSystem.getLocaleString("label.status.ready.false"));

    private final JCheckBox checkBoxFabric = new JCheckBox(UtilsSystem.getLocaleString("checkbox.profile"));

    private final JCheckBox checkBoxDelete = new JCheckBox(UtilsSystem.getLocaleString("checkbox.delete"));

    private final JCheckBox checkBoxSettings = new JCheckBox(UtilsSystem.getLocaleString("checkbox.settings"));

    public void addInstallPanel(String page){

        ArrayList<Component> arrayList = new ArrayList<>();

        Box box = Box.createVerticalBox();

        labelStatus.setEnabled(false);

        JPanel panelStatus = new JPanel();
        panelStatus.add(labelStatus);
        arrayList.add(labelStatus);
        box.add(panelStatus);

        JButton buttonInstall = new JButton(UtilsSystem.getLocaleString("button.install"));
        buttonInstall.addActionListener(e -> new Thread(() -> {
            switch(page){
                case "datapacks":
                    installZipFile(page, datapackTextComponent);
                    break;
                case "resourcepacks":
                    installZipFile(page, textComponent);
                    break;
                case "farms":
                    installFarms();
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

    public JScrollPane build(){
        JScrollPane scrollPane = new JScrollPane(builder);

        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        //Set speed for scroll
        scrollPane.getVerticalScrollBar().setUnitIncrement(10);
        //Remove ugly border
        scrollPane.setBorder(null);
        return scrollPane;
    }

    public void addTextComponentFieldPanel(){
        JPanel dialogPanel = new JPanel();
        dialogPanel.add(new JLabel(UtilsSystem.getLocaleString("label.folder.minecraft.saves")));
        datapackTextComponent.setText(UtilsSystem.pathToMinecraftFolder + "saves" + UtilsOS.systemSlash);
        dialogPanel.add(datapackTextComponent);
        JButton buttonDialog = new JButton(UtilsSystem.getLocaleString("button.dialog"));
        dialogPanel.add(buttonDialog);

        buttonDialog.addActionListener(e -> actionOnButtonDialog(datapackTextComponent));

        add(dialogPanel);
    }

    public void addSettingsPanel(){

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

        add(panel);

        comboBoxLanguage.addActionListener(e -> actionWhenChangedLocale(comboBoxLanguage));
        comboBoxTheme.addActionListener(e -> actionWhenChangedTheme(comboBoxTheme));

    }

    private JComboBox<String> createComboBox(String firstButton, String secondButton){
        ArrayList<String> listLanguage = new ArrayList<>();
        listLanguage.add(UtilsSystem.getLocaleString("button." + firstButton));
        listLanguage.add(UtilsSystem.getLocaleString("button." + secondButton));

        return new JComboBox<>(listLanguage.toArray(new String[0]));
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
        int returnVal = fileChooser.showSaveDialog(textComponentFolder);
        //If user click save
        if(returnVal == JFileChooser.APPROVE_OPTION) {
            //Set text component folder
            textComponentFolder.setText(fileChooser.getSelectedFile().getPath() + UtilsOS.systemSlash);
            if(textComponentFolder == textComponent) UtilsSystem.pathToMinecraftFolder = fileChooser.getSelectedFile().getPath() + UtilsOS.systemSlash;
        }
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
    
    private void installZipFile(String page, JTextComponent textComponent){
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

    private void installFarms(){

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

    public void addModInstallPanel(){
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

        JComboBox<String> comboBoxType = createComboBox("type");
        comboBoxType.setPreferredSize(new Dimension(200, 20));
        typeVersionLine.add(comboBoxType);
        typeVersionLine.add(Box.createRigidArea(new Dimension(1, 0)));

        JCheckBox checkBoxFPS = new JCheckBox(UtilsSystem.getLocaleString("checkbox.FPS"));

        typeVersionLine.add(checkBoxFPS);

        //Add type version to right panel
        rightPanel.add(typeVersionLine);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        JComboBox<String> comboBoxVersion = createComboBox("version." + comboBoxType.getSelectedItem());
        rightPanel.add(comboBoxVersion);

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

        builder.add(box);
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

}

