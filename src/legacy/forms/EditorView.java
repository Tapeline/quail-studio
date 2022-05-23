package me.tapeline.quailstudio.forms;

import me.tapeline.quailstudio.Main;

import javax.swing.*;

public class EditorView {
    public JPanel toolbar;
    public JPanel workspace;
    public JSplitPane workspaceSplitter;
    public JTabbedPane codeTabs;
    public JPanel folderTreePanel;
    public JTree fileTree;
    public JLabel path;
    public JButton runBtn;
    public JPanel main;
    public JButton btnCfg;
    public JButton closeTab;

    private void createUIComponents() {
        runBtn = new JButton(new ImageIcon(Main.iconStart));
        runBtn.setBorderPainted(false);
        runBtn.setOpaque(true);
        runBtn.setRolloverIcon(new ImageIcon(Main.iconStartHover));
        runBtn.setContentAreaFilled(false);
        btnCfg = new JButton(new ImageIcon(Main.iconWrench));
        btnCfg.setBorderPainted(false);
        btnCfg.setOpaque(true);
        btnCfg.setContentAreaFilled(false);
        btnCfg.setRolloverIcon(new ImageIcon(Main.iconWrenchHover));
        closeTab = new JButton(new ImageIcon(Main.iconCross));
        closeTab.setBorderPainted(false);
        closeTab.setOpaque(true);
        closeTab.setContentAreaFilled(false);
        closeTab.setRolloverIcon(new ImageIcon(Main.iconCrossHover));
    }
}
