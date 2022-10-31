package ru.flectone;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.ui.FlatButtonBorder;
import ru.flectone.components.*;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class TabbedPane extends FTabbedPane {

    private final Thread threadSaveConfig = new Thread(this::saveConfig);

    public TabbedPane(){
        setTabPlacement(FSwing.getTabbedPaneAlign());
        setTabLayoutPolicy(JTabbedPane.WRAP_TAB_LAYOUT);

        for(String tabName : FSwing.getTabSequence()){

            if(tabName.replace(" ", "").equals("byTheFaser")){
                addTab("by TheFaser", null, null, Utils.getString("tab.author.tooltip"));
                setEnabledAt(getTabCount() - 1, false);
                continue;
            }

            if(tabName.equals("social")){
                addTabCustomAlign("social", null, new JPanel());

                JPanel imagePanel = new JPanel();

                imagePanel.add(new FImage("discord"));
                imagePanel.add(new FImage("youtube"));
                imagePanel.add(new FImage("github"));

                setTabComponentAt(getTabCount() - 1, imagePanel);
                setEnabledAt(getTabCount() - 1, false);
                continue;
            }

            FBuilder finalBuilder = new FBuilder();
            FBuilder installBuilder = new FBuilder();
            FBuilder componentBuilder = new FBuilder();

            if(tabName.equals("plugins")){

                JButton downloadButton = new JButton(Utils.getString("button.plugins.download"));

                downloadButton.setFont(new Font("sdsd", Font.PLAIN, 20));
                downloadButton.addActionListener(e -> {
                    try {
                        Desktop.getDesktop().browse(new URL("https://www.spigotmc.org/resources/flectonechat.89411/download?version=473849").toURI());
                    } catch (Exception error) {
                        error.printStackTrace();
                    }
                });

                JButton informationButton = new JButton(Utils.getString("button.plugins.information"));
                informationButton.setFont(new Font("sdsd", Font.PLAIN, 20));
                informationButton.addActionListener(e -> {
                    try {
                        Desktop.getDesktop().browse(new URL("https://www.spigotmc.org/resources/flectonechat.89411").toURI());
                    } catch (Exception error) {
                        error.printStackTrace();
                    }
                });

                JPanel buttonPanel = new JPanel();
                buttonPanel.add(downloadButton);
                buttonPanel.add(createRigidArea(20, 0));
                buttonPanel.add(informationButton);

                installBuilder.add(buttonPanel);

                componentBuilder.add(new JLabel(Utils.getImageResources("flectonechat")));

                finalBuilder.add(installBuilder.panelBuild());
                finalBuilder.add(componentBuilder.scrollBuild());

                addTabCustomAlign(Utils.getString("tab.plugins"), Utils.getImageResources("plugins"), finalBuilder.build());
                continue;
            }


            if(tabName.equals("settings")){
                createSettingsTab();
                continue;
            }

            boolean enableInstallButton = false;

            String localeTabName = tabName;

            if(tabName.equals("optimization")){
                enableInstallButton = true;
                addToInstallOptimization(installBuilder, componentBuilder);
                localeTabName = "main";
            }

            if(tabName.equals("mods")){
                addToInstallMods(installBuilder, componentBuilder);
                localeTabName = "notop";
            }

            if(tabName.equals("datapacks")){
                installBuilder.add(createTextComponent());
            }

            createInstallPanel(localeTabName, enableInstallButton, componentBuilder, installBuilder);


            switch(tabName){
                case "optimization":
                    createModsPanel(componentBuilder, comboBoxType.getSelectedItem() + "/" + comboBoxVersionOp.getSelectedItem() + "/main", tabName, localeTabName, true);
                    createModsPanel(componentBuilder, comboBoxType.getSelectedItem() + "/" + comboBoxVersionOp.getSelectedItem() + "/extension", tabName, localeTabName, false);
                    break;
                case "mods":
                    createModsPanel(componentBuilder, "notop/" + comboBoxVersionNotOp.getSelectedItem(), tabName, localeTabName, false);
                    break;
                default:
                    String[] components = Utils.getString(tabName).split(" ");
                    for(String component : components){
                        component = component.replace(".jar", "");
                        componentBuilder.add(new FComponent(component, tabName, Integer.parseInt(Utils.getString("count." + tabName)), false));
                    }
            }

            finalBuilder.add(installBuilder.panelBuild());
            finalBuilder.add(componentBuilder.scrollBuild());

            addTabCustomAlign(Utils.getString("tab." + tabName), Utils.getImageResources(tabName), finalBuilder.build());

        }
        Runtime.getRuntime().addShutdownHook(threadSaveConfig);

        setSelectedIndex(Integer.parseInt(Utils.getString("tab.Selected").equals(Utils.getString("")) ? "0" : Utils.getString("tab.Selected")));
    }

    private void saveConfig() {
        ArrayList<String> listForFile = new ArrayList<>();

        listForFile.add("path.Minecraft: " + FSystem.getPathToMinecraft());
        listForFile.add("chosen.Language: " + FSwing.getSelectedLanguage());
        listForFile.add("chosen.Theme: " + FSwing.getSelectedTheme());
        listForFile.add("show.Warns: " + FSwing.isShowWarns());
        listForFile.add("chosen.Tab_Align: " + FSwing.getTabbedPaneAlign());

        StringBuilder tabSequence = new StringBuilder();
        for(int x = 0; x < getTabCount(); x++){

            String tabTitle = getTitleAt(x)
                    .replace("<html><p style=\"text-align: left; width: 70px\">", "")
                    .replace("</p></html>", "")
                    .replace(" ", "");

            if(tabTitle.equals("byTheFaser") || tabTitle.equals("social")){
                tabSequence.append(tabTitle).append(" ");
                continue;
            }

            for(String tabConfigName : Utils.getString("tabs").split(" ")){
                if(Utils.getString("tab." + tabConfigName).equals(tabTitle)){
                    tabSequence.append(tabConfigName).append(" ");
                    break;
                }
            }

        }

        listForFile.add("tab.Sequence: " + tabSequence);
        listForFile.add("tab.Selected: " + getSelectedIndex());

        try {
            listForFile.add("last.Path: " + Paths.get(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI()));
        } catch (Exception error){
            new FMessageDialog(error.toString(), "error", 0);
        }

        listForFile.add("boolean.Update: " + FSwing.isUpdateProgram());
        listForFile.add("frame.Size: " + Frame.getFrame().getWidth() + " " + Frame.getFrame().getHeight());

        try {
            Files.write(Paths.get(Utils.getWorkingDirectory(), File.separator + "flectone.installer"), listForFile);
        } catch (Exception error){
            new FMessageDialog(error.toString(), "error", 0);
        }

    }

    public void createInstallPanel(String page, boolean bolEnablePanel, FBuilder componentBuilder, FBuilder installBuilder) {

        JButton buttonInstall = createButton("button.install", bolEnablePanel);

        JPanel panelStatus = new JPanel();

        JLabel labelStatus = createLabel("label.status.ready." + bolEnablePanel, bolEnablePanel);

        panelStatus.add(labelStatus);

        ArrayList<Component> arrayList = new ArrayList<>();
        arrayList.add(labelStatus);
        arrayList.add(buttonInstall);

        JButton buttonDialog = createButton("button.dialog", "button.dialog.tooltip");
        buttonDialog.addActionListener(e -> actionOnButtonDialog(textComponentSettings));

        JButton buttonSelect = createButton("button.select.true", "button.select.tooltip");

        buttonSelect.addActionListener(e -> {
            boolean bol = buttonSelect.getText().equals(Utils.getString("button.select.true"));
            buttonSelect.setText(Utils.getString("button.select." + !bol));

            buttonInstall.setEnabled(bol);
            labelStatus.setEnabled(bol);
            labelStatus.setText(Utils.getString("label.status.ready." + bol));

            for(JCheckBox checkBox : Utils.getListCheckBoxes(page)){
                checkBox.setSelected(bol);
            }
            if(page.equals("main")) checkBoxFPS.setSelected(bol);

            if(bol){
                Utils.putCountCheckBoxes(page, Utils.getListCheckBoxes(page).toArray().length);
            } else {
                Utils.putCountCheckBoxes(page, 0);
            }
        });

        buttonInstall.addActionListener(e -> new Thread(() -> {
            buttonInstall.setEnabled(false);
            buttonDialog.setEnabled(false);
            buttonSelect.setEnabled(false);

            switch(page){
                case "main":

                    if(comboBoxType.getSelectedItem().equals("Vulkan")){
                        new FMessageDialog(Utils.getString("message.warn.vulkan"), "warn");
                    }

                    if(checkBoxDelete.isSelected()){
                        Utils.removeListFiles(FSystem.getPathToMinecraft() + "mods");
                    }

                    if(checkBoxFabric.isSelected()){
                        labelStatus.setForeground(new Color(79, 240, 114));
                        labelStatus.setText(Utils.getString("label.status.profile"));
                        new Installation(comboBoxVersionOp.getSelectedItem().toString(), textComponentSettings.getText());
                    }

                    //Download settings for minecraft
                    if(checkBoxSettings.isSelected()){
                        new Installation(labelStatus, Utils.getString("settings").split(" "), Paths.get(textComponentSettings.getText()));
                    }

                    String urlFolder = "mods/" + comboBoxType.getSelectedItem() + "/" + comboBoxVersionOp.getSelectedItem();
                    new Installation(labelStatus, Utils.getListCheckBoxes("main"), Paths.get(textComponentSettings.getText(), "mods"), ".jar", urlFolder + "/main");

                    break;
                case "resourcepacks":
                    new Installation(labelStatus, Utils.getListCheckBoxes(page), Paths.get(textComponentSettings.getText(), page), ".zip", page);
                    break;
                case "farms":
                    new Installation(labelStatus, Utils.getListCheckBoxes(page), Paths.get(textComponentSettings.getText()), ".zip", page);
                    break;
                case "datapacks":
                    new Installation(labelStatus, Utils.getListCheckBoxes(page), Paths.get(datapackTextComponent.getText(), page), ".zip", page);
                    break;
                case "notop":
                    new Installation(labelStatus, Utils.getListCheckBoxes(page), Paths.get(textComponentSettings.getText(), "mods"), ".jar", "mods/notop/" + comboBoxVersionNotOp.getSelectedItem());
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

                    String componentName = Utils.getString(component.getName()).toLowerCase();
                    String componentDescription = Utils.getString(component.getName() + ".description").toLowerCase();

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
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(buttonSelect);
        buttonPanel.add(buttonInstall);
        buttonPanel.add(buttonDialog);

        installBuilder.add(buttonPanel);

        JPanel searchPanel = new JPanel();
        searchPanel.add(createLabel("label.search"));
        searchPanel.add(searchField);
        searchPanel.add(createRigidArea(30, 0));

        installBuilder.add(searchPanel);

        Utils.putPanelEnabled(page, arrayList);
    }

    private void createSettingsTab(){

        //Create field
        textComponentSettings.setBorder(new FlatButtonBorder());

        //Create text component with name from locale
        textComponentSettings.setText(FSystem.getPathToMinecraft());

        //Create button for setting dialog for select path
        JButton buttonDialogSettings = createButton("button.dialog");
        buttonDialogSettings.addActionListener(e -> new Thread(() -> actionOnButtonDialog(textComponentSettings)).start());

        JPanel textComponentLine = new JPanel();
        textComponentLine.setLayout(new BoxLayout(textComponentLine, BoxLayout.X_AXIS));
        textComponentLine.add(textComponentSettings);
        textComponentLine.add(createRigidArea(3, 0));
        textComponentLine.add(buttonDialogSettings);

        JPanel componentPanel = new JPanel();
        componentPanel.setLayout(new BoxLayout(componentPanel, BoxLayout.Y_AXIS));

        componentPanel.add(createRigidArea(0, 2));
        componentPanel.add(textComponentLine);
        componentPanel.add(createRigidArea(0, 10));

        JComboBox<String> comboBoxLanguage = createComboBox(Utils.getString("support.languages").split(" "));

        componentPanel.add(comboBoxLanguage);
        componentPanel.add(createRigidArea(0, 10));

        JComboBox<String> comboBoxTheme = createComboBox(Utils.getString("support.themes").split(" "));

        componentPanel.add(comboBoxTheme);
        componentPanel.add(createRigidArea(0, 10));

        JComboBox<String> comboBoxTabAlign = createComboBox(Utils.getString("support.tab_align").split(" "));

        componentPanel.add(comboBoxTabAlign);

        comboBoxLanguage.setSelectedItem(Utils.getString("button." + FSystem.getSystemLocale()));
        comboBoxTheme.setSelectedItem(FlatLightLaf.isLafDark() ? Utils.getString("button.dark") : Utils.getString("button.light"));
        comboBoxTabAlign.setSelectedItem(Utils.getString("button." + FSwing.getTabbedPaneAlign()));

        comboBoxLanguage.addActionListener(e -> {

            for(String string : Utils.getString("support.languages").split(" ")){
                if(Utils.getString("button." + string).equals(comboBoxLanguage.getSelectedItem())){

                    if(string.equals(FSystem.getSystemLocale())) return;

                    FSystem.setSystemLocale(string);
                    FSystem.setThemeName(FSwing.getSelectedTheme());
                    FSwing.setSelectedLanguage(string);
                    break;
                }
            }
            saveConfig();
            reloadFrame();
        });
        comboBoxTheme.addActionListener(e -> {
            if(comboBoxTheme.getSelectedItem().equals(Utils.getString("button.light"))){
                //Set light theme
                FlatLightLaf.setup();
                FSystem.setBackgroundColor(new Color(218, 218, 218));
                FSwing.setSelectedTheme("light");
            } else {
                //Set dark theme
                FlatDarkLaf.setup();
                FSystem.setBackgroundColor(new Color(54, 57, 59));
                FSwing.setSelectedTheme("dark");
            }
            FlatLightLaf.updateUI();
            FlatLightLaf.revalidateAndRepaintAllFramesAndDialogs();

            saveConfig();
            reloadFrame();
        });

        comboBoxTabAlign.addActionListener(e -> {
            for(String string : Utils.getString("support.tab_align").split(" ")){
                if(Utils.getString("button." + string).equals(comboBoxTabAlign.getSelectedItem())){
                    setTabPlacement(Integer.parseInt(string));

                    FSwing.setTabbedPaneAlign(Integer.parseInt(string));

                    break;
                }
            }
            saveConfig();
            reloadFrame();
        });

        JPanel labelPanel = new JPanel();
        labelPanel.setLayout(new BoxLayout(labelPanel, BoxLayout.Y_AXIS));
        labelPanel.add(createLabel("label.folder.minecraft", Component.RIGHT_ALIGNMENT));
        labelPanel.add(createRigidArea(0, 14));
        labelPanel.add(createLabel("label.language", Component.RIGHT_ALIGNMENT));
        labelPanel.add(createRigidArea(0, 14));
        labelPanel.add(createLabel("label.theme", Component.RIGHT_ALIGNMENT));
        labelPanel.add(createRigidArea(0, 14));
        labelPanel.add(createLabel("label.tab_align", Component.RIGHT_ALIGNMENT));

        JPanel finalPanel = new JPanel();
        finalPanel.add(labelPanel);
        finalPanel.add(componentPanel);

        FBuilder componentBuilder = new FBuilder();
        componentBuilder.add(finalPanel);

        JPanel buttonPanel = new JPanel();

        JButton supportButton = createButton("button.support");
        addURL(supportButton);
        JButton answersButton = createButton("button.answers");
        addURL(answersButton);

        buttonPanel.add(supportButton);
        buttonPanel.add(answersButton);

        componentBuilder.add(buttonPanel);

        componentBuilder.add(Box.createRigidArea(new Dimension(0, 1200)));

        addTabCustomAlign(Utils.getString("tab.settings"), new ImageIcon(Utils.getImageResources("settings").getImage()), componentBuilder.build());
    }

    private void reloadFrame() {
        Runtime.getRuntime().removeShutdownHook(threadSaveConfig);

        new FSystem();

        TabbedPane newTabbedPane = new TabbedPane();
        newTabbedPane.setSelectedIndex(getSelectedIndex());

        removeAll();

        Frame.getFrame().setTitle(Utils.getString("frame.title") + Utils.getString("version.program"));
        Frame.getFrame().getContentPane().removeAll();
        Frame.getFrame().getContentPane().add(newTabbedPane);

    }

    private final JComboBox<String> comboBoxVersionNotOp = new JComboBox<>(Utils.getString("mods.notop.version").split(" "));

    private void addToInstallMods(FBuilder installBuilder, FBuilder componentBuilder){
        JLabel label = createLabel("label.version.mods");
        JPanel panelMods = new JPanel();
        panelMods.add(label);
        panelMods.add(comboBoxVersionNotOp);
        installBuilder.add(panelMods);

        comboBoxVersionNotOp.setPreferredSize(new Dimension(300, 20));

        comboBoxVersionNotOp.addActionListener(e -> {

            componentBuilder.clearBuilder();

            Utils.putListCheckBoxes("modsnotop", new ArrayList<>());

            createModsPanel(componentBuilder, "/notop/" + comboBoxVersionNotOp.getSelectedItem(), "mods", "notop", false);

            Utils.putCountCheckBoxes("modsnotop", 0);

        });
    }

    private void createModsPanel(FBuilder componentBuilder, String path, String pageName, String localeName, boolean enable){
        String[] components = Utils.getStringWithMods(path).split(" ");

        JPanel panel = new JPanel();
        panel.add(new JLabel( Utils.getString("mods.label." + localeName) + " (" + Utils.getString("mods.total") + components.length + ")"));
        panel.setName("!!!");

        componentBuilder.add(panel);

        for(String component : components){
            component = component.replace(".jar", "");
            componentBuilder.add(new FComponent(component, localeName, Integer.parseInt(Utils.getString("count." + pageName)), enable));
        }
        componentBuilder.updateColorComponents();
    }

    private final JTextComponent datapackTextComponent = new JTextField();

    private JPanel createTextComponent(){
        datapackTextComponent.setText(FSystem.getPathToMinecraft() + "saves" + File.separator);
        datapackTextComponent.setBorder(new FlatButtonBorder());

        JButton buttonDialog = new JButton(Utils.getString("button.dialog"));
        buttonDialog.addActionListener(e -> actionOnButtonDialog(datapackTextComponent));

        JPanel dialogPanel = new JPanel();
        dialogPanel.add(createLabel("label.folder.minecraft.saves"));
        dialogPanel.add(datapackTextComponent);
        dialogPanel.add(buttonDialog);

        return dialogPanel;
    }

    private final JTextComponent textComponentSettings = new JTextField();

    private void actionOnButtonDialog(JTextComponent textComponentFolder){
        //Create new window with file chooser
        JFileChooser fileChooser = new JFileChooser();
        //Set file chooser title
        fileChooser.setDialogTitle(Utils.getString("dialog.title"));
        //Set default path
        fileChooser.setSelectedFile(new File(textComponentFolder.getText()));
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
                FSystem.setPathToMinecraft(fileChooser.getSelectedFile().getPath() + File.separator);
        }
    }

    private final JCheckBox checkBoxDelete = createCheckBox("checkbox.delete");
    private final JCheckBox checkBoxSettings = createCheckBox("checkbox.settings");
    private final JCheckBox checkBoxFabric = createCheckBox("checkbox.profile");
    private final JCheckBox checkBoxFPS = createCheckBox("checkbox.FPS");

    private final JComboBox<String> comboBoxType = new JComboBox<>(Utils.getString("type").split(" "));

    private final JComboBox<String> comboBoxVersionOp = new JComboBox<>(Utils.getString("version." + comboBoxType.getSelectedItem()).split(" "));
    private void addToInstallOptimization(FBuilder installBuilder, FBuilder componentBuilder){

        comboBoxType.setPreferredSize(new Dimension(200, 20));

        checkBoxFPS.addActionListener(e -> {

            boolean bol = checkBoxFPS.isSelected();

            for(Component component : Utils.getPanelEnabled("main")){
                component.setEnabled(bol);
                if(component instanceof JLabel){
                    ((JLabel) component).setText(Utils.getString("label.status.ready." + bol));
                }
            }

            for(JCheckBox checkBox : Utils.getListCheckBoxes("main")){
                checkBox.setSelected(bol);
            }

            if(bol){
                Utils.putCountCheckBoxes("main", Utils.getListCheckBoxes("main").toArray().length);
            } else {
                Utils.putCountCheckBoxes("main", 0);
            }

            if(checkBoxFPS.isSelected()){
                new FMessageDialog(Utils.getString("message.warn.maxfps"), "warn");
            }

        });

        comboBoxVersionOp.addActionListener(e -> {
            Utils.putListCheckBoxes("main", new ArrayList<>());
            componentBuilder.clearBuilder();

            createModsPanel(componentBuilder, comboBoxType.getSelectedItem() + "/" + comboBoxVersionOp.getSelectedItem() + "/main", "optimization", "main", true);
            createModsPanel(componentBuilder, comboBoxType.getSelectedItem() + "/" + comboBoxVersionOp.getSelectedItem() + "/extension", "optimization", "main", false);

            componentBuilder.updateColorComponents();
        });

        comboBoxType.addActionListener(e -> {
            String[] list = Utils.getString("version." + comboBoxType.getSelectedItem()).split(" ");
            comboBoxVersionOp.setModel(new DefaultComboBoxModel<>(list));

            Utils.putListCheckBoxes("main", new ArrayList<>());

            componentBuilder.clearBuilder();

            createModsPanel(componentBuilder, comboBoxType.getSelectedItem() + "/" + comboBoxVersionOp.getSelectedItem() + "/main", "optimization", "main", true);
            createModsPanel(componentBuilder, comboBoxType.getSelectedItem() + "/" + comboBoxVersionOp.getSelectedItem() + "/extension", "optimization", "main", false);

            componentBuilder.updateColorComponents();
        });

        //Create additional panel for type Version, so they are on the same line
        JPanel typeVersionLine = new JPanel();

        typeVersionLine.setLayout(new BoxLayout(typeVersionLine, BoxLayout.X_AXIS));
        typeVersionLine.add(comboBoxType);
        typeVersionLine.add(createRigidArea(1, 0));
        typeVersionLine.add(checkBoxFPS);

        //Create panel for other components
        JPanel rightPanel = new JPanel();
        //Add type version to right panel
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.add(typeVersionLine);
        rightPanel.add(createRigidArea(0, 10));
        rightPanel.add(comboBoxVersionOp);

        //Panel for labels, so they don't depend on other components
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.add(createLabel("label.type.mods", Component.RIGHT_ALIGNMENT));
        leftPanel.add(createRigidArea(0, 14));
        leftPanel.add(createLabel("label.version.mods", Component.RIGHT_ALIGNMENT));

        JPanel rightAndLeft = new JPanel();
        rightAndLeft.add(leftPanel);
        rightAndLeft.add(rightPanel);

        //Create Box so that other components do not depend on the main panel
        JPanel finalPanel = new JPanel();
        finalPanel.setLayout(new BoxLayout(finalPanel, BoxLayout.Y_AXIS));
        finalPanel.add(createRigidArea(0, 5));
        finalPanel.add(rightAndLeft);

        JPanel panel = new JPanel();
        panel.add(checkBoxFabric);
        panel.add(checkBoxSettings);
        panel.add(checkBoxDelete);

        finalPanel.add(panel);

        installBuilder.add(finalPanel);
    }
}
