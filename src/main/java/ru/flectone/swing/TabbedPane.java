package ru.flectone.swing;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.ui.FlatButtonBorder;
import ru.flectone.Main;
import ru.flectone.components.Image;
import ru.flectone.components.*;
import ru.flectone.swing.pages.PageBuilder;
import ru.flectone.swing.pages.PageComponent;
import ru.flectone.utils.UtilsOS;
import ru.flectone.utils.UtilsSystem;
import ru.flectone.utils.UtilsWeb;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.HyperlinkEvent;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Objects;

import static ru.flectone.utils.UtilsSystem.listCheckBox;
import static ru.flectone.utils.UtilsSystem.listObjectsFromConfig;

public class TabbedPane extends FTabbedPane {

    private final JComboBox<String> comboBoxType = createComboBox("type");

    private final JComboBox<String> comboBoxVersionOp = createComboBox("version." + comboBoxType.getSelectedItem());

    private final JComboBox<String> comboBoxLanguage = createComboBoxConfig("languages");

    private final JComboBox<String> comboBoxTheme = createComboBoxConfig("themes");

    private final JComboBox<String> comboBoxTabAlign = createComboBoxConfig("tab_align");

    private final JComboBox<String> comboBoxVersionNotOp = createComboBox("mods.notop.version");

    private final JCheckBox checkBoxFabric = createCheckBox("checkbox.profile");

    private final JCheckBox checkBoxDelete = createCheckBox("checkbox.delete");

    private final JCheckBox checkBoxSettings = createCheckBox("checkbox.settings");

    private final JCheckBox checkBoxFPS = createCheckBox("checkbox.FPS");

    private final JTextComponent textComponentSettings = new JTextField();

    private final JTextComponent datapackTextComponent = new JTextField();

    private final Thread threadSaveConfig = new Thread(() -> saveConfig());

    public TabbedPane() {
        setTabPlacement(UtilsSystem.tabbedPaneAlign);
        setTabLayoutPolicy(JTabbedPane.WRAP_TAB_LAYOUT);

        if(UtilsSystem.settingsFile.get("tab.Sequence") == null){
            UtilsSystem.settingsFile.put("tab.Sequence", String.join(" ", listObjectsFromConfig.get("tabs")));
        }

        for(String pageName : UtilsSystem.settingsFile.get("tab.Sequence").split(" ")){

            PageBuilder componentBuilder = new PageBuilder();
            PageBuilder installBuilder = new PageBuilder();
            PageBuilder finalBuilder = new PageBuilder();

            switch (pageName){
                case "tab.optimization":

                    comboBoxType.setPreferredSize(new Dimension(200, 20));

                    checkBoxFPS.addActionListener(e -> {
                        for (JCheckBox checkBox : listCheckBox.get("modsextension")) {
                            checkBox.setSelected(checkBoxFPS.isSelected());
                        }

                        if (checkBoxFPS.isSelected()) new MessageDialog(UtilsSystem.getLocaleString("message.warn.maxfps"), "warn");

                    });

                    //Create additional panel for type Version, so they are on the same line
                    FPanel typeVersionLine = new FPanel();

                    typeVersionLine
                            .setPanelLayout(new BoxLayout(typeVersionLine, BoxLayout.X_AXIS))
                            .addComponent(comboBoxType)
                            .createRigidArea(1, 0)
                            .addComponent(checkBoxFPS);

                    //Create panel for other components
                    FPanel rightPanel = new FPanel();

                    //Add type version to right panel
                    rightPanel
                            .setPanelLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS))
                            .addComponent(typeVersionLine)
                            .createRigidArea(0, 10)
                            .addComponent(comboBoxVersionOp);

                    //Panel for labels, so they don't depend on other components
                    FPanel leftPanel = new FPanel();
                    leftPanel
                            .setPanelLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS))
                            .addComponent(new FLabel("label.type.mods").setComponentAlignmentX(Component.RIGHT_ALIGNMENT))
                            .createRigidArea(0, 14)
                            .addComponent(new FLabel("label.version.mods").setComponentAlignmentX(Component.RIGHT_ALIGNMENT));

                    //Create Box so that other components do not depend on the main panel
                    FPanel finalPanel = new FPanel();
                    finalPanel
                            //Set vertical layout
                            .setPanelLayout(new BoxLayout(finalPanel, BoxLayout.Y_AXIS))
                            .createRigidArea(0, 5)
                            //Add left panel (text) and right panel (combobox and checkbox)
                            .addComponent(new FPanel().addComponent(leftPanel).addComponent(rightPanel))
                            //Add checkboxs in down
                            .add(new FPanel().addComponent(checkBoxFabric).addComponent(checkBoxSettings).addComponent(checkBoxDelete));

                    installBuilder.add(finalPanel);

                    comboBoxVersionOp.addActionListener(e -> {
                        listCheckBox.put("modsmain", new ArrayList<>());
                        listCheckBox.put("modsextension", new ArrayList<>());
                        componentBuilder.clearBuilder();
                        createModsListPanel(componentBuilder);
                        componentBuilder.updateColorComponents();
                    });

                    comboBoxType.addActionListener(e -> {
                        String[] list = UtilsSystem.listObjectsFromConfig.get("version." + comboBoxType.getSelectedItem());
                        comboBoxVersionOp.setModel(new DefaultComboBoxModel<>(list));
                        listCheckBox.put("modsmain", new ArrayList<>());
                        listCheckBox.put("modsextension", new ArrayList<>());
                        componentBuilder.clearBuilder();
                        createModsListPanel(componentBuilder);
                        componentBuilder.updateColorComponents();
                    });

                    createInstallPanel("modsmain", true, componentBuilder, installBuilder);
                    createModsListPanel(componentBuilder);

                    finalBuilder.add(installBuilder.panelBuild());
                    finalBuilder.add(componentBuilder.scrollBuild());

                    addTabCustomAlign(UtilsSystem.getLocaleString("tab.optimization"), new ImageIcon(Main.class.getResource("/images/optimization.png")), finalBuilder.build());
                    break;

                case "tab.mods":

                    JLabel label = new JLabel(UtilsSystem.getLocaleString("label.version.mods"));
                    FPanel panelMods = new FPanel()
                            .addComponent(label)
                            .addComponent(comboBoxVersionNotOp);
                    installBuilder.add(panelMods);

                    comboBoxVersionNotOp.setPreferredSize(new Dimension(300, 20));
                    comboBoxVersionNotOp.addActionListener(e -> {
                        componentBuilder.clearBuilder();

                        UtilsSystem.listCheckBox.put("modsnotop", new ArrayList<>());
                        createModsUtil("/notop/" + comboBoxVersionNotOp.getSelectedItem(), "notop", componentBuilder);
                        UtilsSystem.countCheckBoxHashMap.put("modsnotop", 0);
                        componentBuilder.updateColorComponents();
                    });


                    createModsUtil("/notop/" + comboBoxVersionNotOp.getSelectedItem(), "notop",componentBuilder);
                    UtilsSystem.countCheckBoxHashMap.put("modsnotop", 0);

                    createInstallPanel("modsnotop", false, componentBuilder, installBuilder);

                    finalBuilder.add(installBuilder.panelBuild());
                    finalBuilder.add(componentBuilder.scrollBuild());

                    addTabCustomAlign(UtilsSystem.getLocaleString("tab.mods"), new ImageIcon(Main.class.getResource("/images/mods.png")), finalBuilder.build());
                    break;

                case "tab.farms":
                    addFarmRpDpTab("farms", componentBuilder, installBuilder);
                    break;
                case "tab.resourcepacks":
                    addFarmRpDpTab("resourcepacks", componentBuilder, installBuilder);
                    break;
                case "tab.datapacks":
                    installBuilder.add(createTextComponent());

                    addFarmRpDpTab("datapacks", componentBuilder, installBuilder);
                    break;
                case "tab.settings":

                    //Create field
                    textComponentSettings.setBorder(new FlatButtonBorder());

                    //Create text component with name from locale
                    textComponentSettings.setText(UtilsSystem.pathToMinecraftFolder);

                    //Create button for setting dialog for select path
                    JButton buttonDialogSettings = createButton("dialog");
                    buttonDialogSettings.addActionListener(e -> new Thread(() -> actionOnButtonDialog(textComponentSettings)).start());

                    FPanel textComponentLine = new FPanel();
                    textComponentLine.setLayout(new BoxLayout(textComponentLine, BoxLayout.X_AXIS));

                    textComponentLine
                            .addComponent(textComponentSettings)
                            .createRigidArea(3, 0)
                            .addComponent(buttonDialogSettings);

                    FPanel componentPanel = new FPanel();
                    componentPanel.setLayout(new BoxLayout(componentPanel, BoxLayout.Y_AXIS));

                    componentPanel
                            .createRigidArea(0, 2)
                            .addComponent(textComponentLine)
                            .createRigidArea(0, 10)
                            .addComponent(comboBoxLanguage)
                            .createRigidArea(0, 10)
                            .addComponent(comboBoxTheme)
                            .createRigidArea(0, 10)
                            .addComponent(comboBoxTabAlign);

                    comboBoxLanguage.setSelectedItem(UtilsSystem.getLocaleString("button." + UtilsOS.getSystemLocale()));
                    comboBoxTheme.setSelectedItem(FlatLightLaf.isLafDark() ? UtilsSystem.getLocaleString("button.dark") : UtilsSystem.getLocaleString("button.light"));
                    comboBoxTabAlign.setSelectedItem(UtilsSystem.getLocaleString("button." + UtilsSystem.tabbedPaneAlign));

                    comboBoxLanguage.addActionListener(e -> actionWhenChangedLocale(comboBoxLanguage));
                    comboBoxTheme.addActionListener(e -> actionWhenChangedTheme(comboBoxTheme));
                    comboBoxTabAlign.addActionListener(e -> actionWhenChangedTabAlign(comboBoxTabAlign));

                    FPanel labelPanel = new FPanel();
                    labelPanel
                            .setPanelLayout(new BoxLayout(labelPanel, BoxLayout.Y_AXIS))
                            .addComponent(new FLabel("label.folder.minecraft").setComponentAlignmentX(Component.RIGHT_ALIGNMENT))
                            .createRigidArea(0, 14)
                            .addComponent(new FLabel("label.language").setComponentAlignmentX(Component.RIGHT_ALIGNMENT))
                            .createRigidArea(0, 14)
                            .addComponent(new FLabel("label.theme").setComponentAlignmentX(Component.RIGHT_ALIGNMENT))
                            .createRigidArea(0, 14)
                            .addComponent(new FLabel("label.tab_align").setComponentAlignmentX(Component.RIGHT_ALIGNMENT));

                    componentBuilder.add(new FPanel()
                            .addComponent(labelPanel)
                            .addComponent(componentPanel));

                    componentBuilder.add(new FPanel()
                            .addComponent(createButton("support").addURL())
                            .addComponent(createButton("answers").addURL()));

                    componentBuilder.add(Box.createRigidArea(new Dimension(0, 1200)));
//                    //?????????
//                    componentBuilder.add(new FPanel().addComponent(createEditorPane("clickme")));

                    addTabCustomAlign(UtilsSystem.getLocaleString("tab.settings"), new ImageIcon(Main.class.getResource("/images/settings.png")), componentBuilder.build());
                    break;

                case "byTheFaser":

                    addTab("by TheFaser", null, null, UtilsSystem.getLocaleString("tab.author.tooltip"));
                    setEnabledAt(getTabCount() - 1, false);
                    break;

                case "social":
                    addTabCustomAlign("social", null, new JPanel());

                    FPanel imagePanel = new FPanel()
                            .addComponent(UtilsSystem.getImageHashMap("discord.png"))
                            .addComponent(UtilsSystem.getImageHashMap("yt.png"))
                            .addComponent(UtilsSystem.getImageHashMap("github.png"));

                    setTabComponentAt(getTabCount() - 1, imagePanel);
                    setEnabledAt(getTabCount() - 1, false);
                    break;
            }
        }

        if(UtilsSystem.settingsFile.get("tab.Selected") != null)
            setSelectedIndex(Integer.valueOf(UtilsSystem.settingsFile.get("tab.Selected")));

        Runtime.getRuntime().addShutdownHook(threadSaveConfig);

    }

    private void addFarmRpDpTab(String folder, PageBuilder pageBuilder, PageBuilder installBuilder){

        for(String component : UtilsSystem.listObjectsFromConfig.get(folder)){
            //Maybe this bad code
            switch (folder) {
                case "farms":
                    pageBuilder.add(new PageComponent(component, "", "", "", "", folder));
                    break;
                case "resourcepacks":
                    pageBuilder.add(new PageComponent(component, "", "", folder));
                    break;

                case "datapacks":
                    pageBuilder.add(new PageComponent(component, "", "", "", folder));
                    break;
            }
        }
        createInstallPanel(folder, false, pageBuilder, installBuilder);

        PageBuilder finalBuilder = new PageBuilder();
        finalBuilder.add(installBuilder.panelBuild());
        finalBuilder.add(pageBuilder.scrollBuild());

        addTabCustomAlign(UtilsSystem.getLocaleString("tab." + folder), new ImageIcon(Main.class.getResource("/images/" + folder + ".png")), finalBuilder.build());
    }

    private JEditorPane createEditorPane(String labelLocale) {
        JEditorPane editorPane = new JEditorPane();
        editorPane.setContentType("text/html");

        editorPane.setText("<a href='" + UtilsSystem.getLocaleString("url." + labelLocale) + "'>" + UtilsSystem.getLocaleString("label." + labelLocale));
        editorPane.setEditable(false);
        editorPane.addHyperlinkListener(e -> {
            if (HyperlinkEvent.EventType.ACTIVATED.equals(e.getEventType())) {
                Desktop desktop = Desktop.getDesktop();
                try {
                    desktop.browse(e.getURL().toURI());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        return editorPane;
    }

    //Save flectonemods.txt
    private void saveConfig() {
        try {
            ArrayList<String> listForFile = new ArrayList<>();

            String pathMinecraftFolder = "path.Minecraft: ";
            //If user path is null that get default path minecraft
            if (textComponentSettings.getText().isEmpty()) {
                //Default path
                pathMinecraftFolder = pathMinecraftFolder + UtilsSystem.pathToMinecraftFolder;
            } else {
                //User path
                pathMinecraftFolder = pathMinecraftFolder + textComponentSettings.getText() + (textComponentSettings.getText().endsWith(File.separator) ? "" : File.separator);
            }
            listForFile.add(pathMinecraftFolder);

            for (String string : UtilsSystem.listObjectsFromConfig.get("support.languages")) {
                if (UtilsSystem.getLocaleString("button." + string).equals(comboBoxLanguage.getSelectedItem())) {
                    listForFile.add("chosen.Language: " + string);
                }
            }

            String chosenTheme = "chosen.Theme: " + (UtilsSystem.getLocaleString("button.dark").equals(comboBoxTheme.getSelectedItem()) ? "dark" : "light");
            listForFile.add(chosenTheme);

            listForFile.add("show.Warns: " + UtilsSystem.showWarnMessages);

            listForFile.add("chosen.Tab_Align: " + UtilsSystem.tabbedPaneAlign);

            String tabSequence = "";
            for(int x = 0; x < getTabCount(); x++){
                String tabTitle = getTitleAt(x).replace("<html><p style=\"text-align: left; width: 70px\">", "").replace("</p></html>", "");

                if(tabTitle.equals("by TheFaser") || tabTitle.equals("social")){
                    tabSequence += tabTitle.replace(" ", "") + " ";
                    continue;
                }


                for(String tabTitleConfig : listObjectsFromConfig.get("tabs")) {
                    if(tabTitle.replace(" ", "").equals(UtilsSystem.getLocaleString(tabTitleConfig))){
                        tabSequence += tabTitleConfig + " ";
                        break;
                    }
                }

            }

            listForFile.add("tab.Sequence: " + tabSequence);

            listForFile.add("tab.Selected: " + getSelectedIndex());

            try {
                listForFile.add("last.Path: " + Paths.get(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI()));
            } catch (Exception error){
                new MessageDialog(error.toString(), "error", 0);
            }

            listForFile.add("boolean.Update: " + Main.updateProgram);

            Files.write(Paths.get(UtilsSystem.getWorkingDirectory(), File.separator + "flectone.installer"), listForFile);

        } catch (IOException ignored) {

        }
    }

    //Create combo box
    private JComboBox<String> createComboBox(String nameMassive){
        //Create combo box with UtilsSystem.getFoldersList
        JComboBox<String> comboBox = new JComboBox<>(Objects.requireNonNull(UtilsSystem.listObjectsFromConfig.get(nameMassive)));
        //Add to action listener so that check click
        if (nameMassive.contains("type")) {
            comboBox.setSelectedIndex(1);
        }
        return comboBox;
    }

    private void createModsListPanel(PageBuilder builder){

        createModsUtil(comboBoxType.getSelectedItem() + "/" + comboBoxVersionOp.getSelectedItem() + "/main", "main", builder);

        createModsUtil(comboBoxType.getSelectedItem() + "/" + comboBoxVersionOp.getSelectedItem() + "/extension", "extension", builder);

        for(JCheckBox checkBox : listCheckBox.get("modsextension")){
            checkBox.setSelected(checkBoxFPS.isSelected());
        }
    }

    private void createModsUtil(String pathToMods, String folder, PageBuilder builder){

        String[] modsList = UtilsSystem.listObjectsFromConfig.get(pathToMods);
        if(modsList == null){
            modsList = UtilsWeb.getModsFromWebSite(pathToMods);

            UtilsSystem.listObjectsFromConfig.put(pathToMods, modsList);
        }

        FPanel panel = new FPanel();
        panel.addComponent(new JLabel( UtilsSystem.getLocaleString("mods.label." + folder) + " (" + UtilsSystem.getLocaleString("mods.total") + modsList.length + ")"));
        panel.setName("!!!");

        builder.add(panel);

        for(String string : modsList){
            string = string.replace(".jar", "");

            builder.add(new PageComponent(string, "", modsList.length, "", "mods" + folder));
        }
    }

    private void actionWhenChangedTabAlign(JComboBox<String> comboBoxTabAlign){
        saveConfig();
        for(String string : UtilsSystem.listObjectsFromConfig.get("support.tab_align")){
            if(UtilsSystem.getLocaleString("button." + string).equals(comboBoxTabAlign.getSelectedItem())){
                setTabPlacement(Integer.valueOf(string));
                UtilsSystem.setTabbedPaneAlign(Integer.valueOf(string));
                break;
            }
        }
        reloadFrame();
    }

    //Action when user click on button locale (update frame)
    private void actionWhenChangedLocale(JComboBox<String> comboBoxLanguage){
        saveConfig();

        for(String string : UtilsSystem.listObjectsFromConfig.get("support.languages")){
            if(UtilsSystem.getLocaleString("button." + string).equals(comboBoxLanguage.getSelectedItem())){
                UtilsOS.setSystemLocale(string);
                break;
            }
        }

        //Reload "locale" file
        UtilsSystem.getLocaleFile();
        reloadFrame();
    }

    private void actionWhenChangedTheme(JComboBox<String> comboBoxTheme){
        saveConfig();
        if(comboBoxTheme.getSelectedItem().equals(UtilsSystem.getLocaleString("button.light"))){
            //Set light theme
            FlatLightLaf.setup();
            UtilsSystem.setSecondColor(new Color(218, 218, 218));
        } else {
            //Set dark theme
            FlatDarkLaf.setup();
            UtilsSystem.setSecondColor(new Color(54, 57, 59));
        }
        FlatLightLaf.updateUI();
        FlatLightLaf.revalidateAndRepaintAllFramesAndDialogs();

        reloadFrame();
    }

    private void reloadFrame() {
        Runtime.getRuntime().removeShutdownHook(threadSaveConfig);

        UtilsSystem.getSettingsFile();

        UtilsSystem.enabledComponentsHashMap.clear();
        UtilsSystem.countCheckBoxHashMap.clear();
        listCheckBox.clear();

        FTabbedPane newTabbedPane = new TabbedPane();
        newTabbedPane.setSelectedIndex(getSelectedIndex());

        Frame.getFrame().setTitle(UtilsSystem.getLocaleString("frame.title") + UtilsSystem.getVersionProgram());

        Frame.getFrame().getContentPane().removeAll();
        removeAll();
        Frame.getFrame().getContentPane().add(newTabbedPane);


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
        if(returnVal == JFileChooser.APPROVE_OPTION){
            //Set text component folder
            textComponentFolder.setText(fileChooser.getSelectedFile().getPath() + File.separator);
            if(textComponentFolder == textComponentSettings)
                UtilsSystem.pathToMinecraftFolder = fileChooser.getSelectedFile().getPath() + File.separator;
        }
    }

    public FPanel createTextComponent(){
        datapackTextComponent.setText(UtilsSystem.pathToMinecraftFolder + "saves" + File.separator);
        datapackTextComponent.setBorder(new FlatButtonBorder());

        JButton buttonDialog = new JButton(UtilsSystem.getLocaleString("button.dialog"));
        buttonDialog.addActionListener(e -> actionOnButtonDialog(datapackTextComponent));

        FPanel dialogPanel = new FPanel();
        dialogPanel
                .addComponent(new JLabel(UtilsSystem.getLocaleString("label.folder.minecraft.saves")))
                .addComponent(datapackTextComponent)
                .addComponent(buttonDialog);

        return dialogPanel;
    }

    public void createInstallPanel(String page, boolean bolEnablePanel, PageBuilder componentBuilder, PageBuilder installBuilder) {

        JLabel labelStatus = new JLabel(UtilsSystem.getLocaleString("label.status.ready." + bolEnablePanel));
        labelStatus.setEnabled(bolEnablePanel);

        JButton buttonInstall = new JButton(UtilsSystem.getLocaleString("button.install"));
        buttonInstall.setEnabled(bolEnablePanel);

        FPanel panelStatus = new FPanel().addComponent(labelStatus);

        ArrayList<Component> arrayList = new ArrayList<>();
        arrayList.add(labelStatus);
        arrayList.add(buttonInstall);

        JButton buttonDialog = new JButton(UtilsSystem.getLocaleString("button.dialog"));
        buttonDialog.setToolTipText(UtilsSystem.getLocaleString("button.dialog.tooltip"));
        buttonDialog.addActionListener(e -> {
            actionOnButtonDialog(textComponentSettings);
        });

        JButton buttonSelect = new JButton(UtilsSystem.getLocaleString("button.select.true"));
        buttonSelect.setToolTipText(UtilsSystem.getLocaleString("button.select.tooltip"));
        buttonSelect.addActionListener(e -> {
            boolean bol = buttonSelect.getText().equals(UtilsSystem.getLocaleString("button.select.true"));
            buttonSelect.setText(UtilsSystem.getLocaleString("button.select." + !bol));

            buttonInstall.setEnabled(bol);
            labelStatus.setEnabled(bol);
            labelStatus.setText(UtilsSystem.getLocaleString("label.status.ready." + bol));

            for (JCheckBox checkBox : listCheckBox.get(page)) {
                checkBox.setSelected(bol);
            }
            if (page.equals("modsmain")) {
                for (JCheckBox checkBox : listCheckBox.get("modsextension")) {
                    checkBox.setSelected(bol);
                }
                checkBoxFPS.setSelected(bol);
            }

            if (bol) {
                UtilsSystem.countCheckBoxHashMap.put(page, listCheckBox.get(page).toArray().length);
            } else {
                UtilsSystem.countCheckBoxHashMap.put(page, 0);
            }
        });

        buttonInstall.addActionListener(e -> new Thread(() -> {
            buttonInstall.setEnabled(false);
            buttonDialog.setEnabled(false);
            buttonSelect.setEnabled(false);


            switch (page) {
                case "modsmain":

                    if (comboBoxType.getSelectedItem().equals("Vulkan")) {
                        new MessageDialog(UtilsSystem.getLocaleString("message.warn.vulkan"), "warn");
                    }

                    if (checkBoxDelete.isSelected()) {
                        UtilsSystem.removeListFiles(UtilsSystem.pathToMinecraftFolder + "mods");
                    }

                    if (checkBoxFabric.isSelected()) {
                        labelStatus.setForeground(new Color(79, 240, 114));
                        labelStatus.setText(UtilsSystem.getLocaleString("label.status.profile"));
                        new Installation(comboBoxVersionOp.getSelectedItem().toString(), textComponentSettings.getText());
                    }

                    //Download settings for minecraft
                    if (checkBoxSettings.isSelected()) {
                        new Installation(labelStatus, UtilsSystem.listObjectsFromConfig.get("settings"), Paths.get(textComponentSettings.getText()));
                    }

                    String urlFolder = "mods/" + comboBoxType.getSelectedItem() + "/" + comboBoxVersionOp.getSelectedItem();
                    new Installation(labelStatus, listCheckBox.get("modsmain"), Paths.get(textComponentSettings.getText(), "mods"), ".jar", urlFolder + "/main");
                    new Installation(labelStatus, listCheckBox.get("modsextension"), Paths.get(textComponentSettings.getText(), "mods"), ".jar", urlFolder + "/extension");

                    break;
                case "resourcepacks":
                    new Installation(labelStatus, listCheckBox.get(page), Paths.get(textComponentSettings.getText(), page), ".zip", page);
                    break;
                case "farms":
                    new Installation(labelStatus, listCheckBox.get(page), Paths.get(textComponentSettings.getText()), ".zip", page);
                    break;
                case "datapacks":
                    new Installation(labelStatus, listCheckBox.get(page), Paths.get(datapackTextComponent.getText(), page), ".zip", page);
                    break;
                case "modsnotop":
                    new Installation(labelStatus, listCheckBox.get(page), Paths.get(textComponentSettings.getText(), "mods"), ".jar", "mods/notop/" + comboBoxVersionNotOp.getSelectedItem());
                    break;

            }
            buttonInstall.setEnabled(true);
            buttonDialog.setEnabled(true);
            buttonSelect.setEnabled(true);
        }).start());

        JTextField searchField = new JTextField();
        searchField.setBorder(new FlatButtonBorder());
        searchField.setPreferredSize(new Dimension(200, 20));

        searchField.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
                changeTextField();
            }

            public void removeUpdate(DocumentEvent e) {
                changeTextField();
            }

            public void insertUpdate(DocumentEvent e) {
                changeTextField();
            }

            public void changeTextField() {

                int countComponents = componentBuilder.getComponents().length;

                for(int x = 0; x < componentBuilder.getComponents().length; x++){
                    Component component = componentBuilder.getComponents()[x];

                    String componentName = UtilsSystem.getLocaleString(component.getName()).toLowerCase();
                    String componentDescription = UtilsSystem.getLocaleString(component.getName() + ".description").toLowerCase();
                    String inputText = searchField.getText().toLowerCase();

                    boolean componentEquals = componentName.contains(inputText) || componentDescription.contains(inputText);
                    componentBuilder.setVisibleComponent(x, componentEquals);

                    if(!componentEquals) countComponents--;
                    if(component.getSize().height > 500) componentBuilder.removeComponent(component);

                }
                JLabel label = new JLabel("");
                label.setPreferredSize(new Dimension(0, 1000));

                if(countComponents < 4){
                    componentBuilder.add(label);
                    componentBuilder.setScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
                } else {
                    componentBuilder.setScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

                }

                componentBuilder.updateColorComponents();
                revalidate();
            }
        });

        installBuilder.add(panelStatus);
        installBuilder.add(new FPanel().addComponent(buttonSelect).addComponent(buttonInstall).addComponent(buttonDialog));

        installBuilder.add(new FPanel().addComponent(new FLabel("label.search")).addComponent(searchField).createRigidArea(30, 0));

        UtilsSystem.enabledComponentsHashMap.put(page, arrayList);
    }

    //Create check box
    public JCheckBox createCheckBox(String checkBoxName) {
        //Create check box with name from locale
        JCheckBox checkBox = new JCheckBox(UtilsSystem.getLocaleString(checkBoxName));
        //Add tool tip
        checkBox.setToolTipText(UtilsSystem.getLocaleString(checkBoxName + ".tooltip"));

        return checkBox;
    }

    private JComboBox<String> createComboBoxConfig(String supportString) {
        ArrayList<String> listLanguage = new ArrayList<>();
        for (String configString : UtilsSystem.listObjectsFromConfig.get("support." + supportString)) {
            listLanguage.add(UtilsSystem.getLocaleString("button." + configString));
        }
        return new JComboBox<>(listLanguage.toArray(new String[0]));
    }

}
