package me.tapeline.quailstudio.forms;

import me.tapeline.quailstudio.contextmenus.FileContext;
import me.tapeline.quailstudio.contextmenus.FileContextPopup;
import me.tapeline.quailstudio.project.CodeEditor;
import me.tapeline.quailstudio.Main;
import me.tapeline.quailstudio.customui.IconTreeNodeRenderer;
import me.tapeline.quailstudio.customui.JClosableTabbedPane;
import me.tapeline.quailstudio.customui.JPathBreadCrumb;
import me.tapeline.quailstudio.dialogs.IDEHelp;
import me.tapeline.quailstudio.dialogs.NewProjectWizard;
import me.tapeline.quailstudio.dialogs.Preferences;
import me.tapeline.quailstudio.project.Console;
import me.tapeline.quailstudio.project.Project;
import me.tapeline.quailstudio.project.fsmodel.FileTreeNode;
import me.tapeline.quailstudio.utils.Icons;
import me.tapeline.quailstudio.utils.Utils;
import org.apache.commons.io.FileUtils;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;

public class EditorForm extends JFrame {
    public JTextArea consoleArea;
    private JButton consoleRunBtn;
    private JButton consolePreferencesBtn;
    public JTree fsTree;
    private JButton toolbarRunBtn;
    private JLabel pathLabel;
    private JPanel root;
    private JPanel tabbedPanePanel;
    private JButton refreshTree;
    private JButton toolbarProfileBtn;
    private JPanel breadcrumbPlace;
    public JClosableTabbedPane tabs;

    public JPathBreadCrumb pathBreadCrumb;

    public Project project;
    public Console boundConsole;

    public EditorForm(File projectDir) {
        super("Quail::Studio - " + projectDir.getAbsolutePath());
        EditorForm that = this;
        project = new Project(projectDir);
        setContentPane(root);
        setMaximumSize(new Dimension(640, 480));
        pack();
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        tabbedPanePanel.setLayout(new GridLayout());
        tabs = new JClosableTabbedPane();
        tabbedPanePanel.add(tabs);

        fsTree.setCellRenderer(new IconTreeNodeRenderer());
        fsTree.setModel(new DefaultTreeModel(project.fileSystem.toTree()));

        Utils.adaptButton(consolePreferencesBtn);
        Utils.adaptButton(consoleRunBtn);
        Utils.adaptButton(toolbarRunBtn);
        Utils.adaptButton(refreshTree);
        Utils.adaptButton(toolbarProfileBtn);

        refreshTree.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                project.resolveFiles();
                fsTree.setModel(new DefaultTreeModel(project.fileSystem.toTree()));
                pathBreadCrumb.renewCrumbs(pathBreadCrumb.path);
            }
        });

        toolbarRunBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!new File(Main.cfg.cfg.get("quail-home").toString() + "/" +
                        (String) Main.cfg.cfg.get("quail-name")).exists()) {
                    JOptionPane.showMessageDialog(that,
                            "quail::studio failed to find a valid Quail installation!\n\n" +
                                    "Please specify one in Preferences>Run Configuration",
                            "No Quail Installed!",
                            JOptionPane.ERROR_MESSAGE);
                } else {
                    Runtime r = Runtime.getRuntime();
                    try {
                        String cmd = Main.cfg.cfg.get("run-cmd").toString().replaceAll("\\%home",
                                (String) Main.cfg.cfg.get("quail-home"));
                        cmd = cmd.replaceAll("\\%name", (String) Main.cfg.cfg.get("quail-name"));
                        cmd = cmd.replaceAll("\\%file",
                                ((CodeEditor) tabs.getSelectedComponent()).file.getAbsolutePath());
                        cmd += " false";
                        Process process = r.exec(cmd);
                        boundConsole.bindToProcess(process);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        consoleRunBtn.addActionListener(toolbarRunBtn.getActionListeners()[0]);

        consolePreferencesBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Preferences dialog = new Preferences();
                dialog.pack();
                dialog.setVisible(true);
            }
        });

        toolbarProfileBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!new File(Main.cfg.cfg.get("quail-home").toString() + "/" +
                        (String) Main.cfg.cfg.get("quail-name")).exists()) {
                    JOptionPane.showMessageDialog(that,
                            "quail::studio failed to find a valid Quail installation!\n\n" +
                                    "Please specify one in Preferences>Run Configuration",
                            "No Quail Installed!",
                            JOptionPane.ERROR_MESSAGE);
                } else {
                    Runtime r = Runtime.getRuntime();
                    try {
                        String cmd = Main.cfg.cfg.get("run-cmd").toString().replaceAll("\\%home",
                                (String) Main.cfg.cfg.get("quail-home"));
                        cmd = cmd.replaceAll("\\%name", (String) Main.cfg.cfg.get("quail-name"));
                        cmd = cmd.replaceAll("\\%file",
                                ((CodeEditor) tabs.getSelectedComponent()).file.getAbsolutePath());
                        cmd += " true";
                        Process process = r.exec(cmd);
                        boundConsole.bindToProcess(process);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                saveAll();
                e.getWindow().dispose();
            }
        });

        fsTree.setComponentPopupMenu(new FileContextPopup(this, project.root));
        fsTree.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && fsTree.getLastSelectedPathComponent() != null) {
                    File f = ((FileTreeNode) fsTree.getLastSelectedPathComponent()).file;
                    if (f.isDirectory()) return;
                    if (f.getName().startsWith("QuailProfilingReport") && Utils.getExtension(f).equals("json")) {
                        try {
                            ReportViewer viewer = new ReportViewer(f);
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                        return;
                    }
                    boolean isAlreadyOpened = false;
                    Component tab = null;
                    for (int i = 0; i < tabs.getTabCount(); i++) {
                        if (((JClosableTabbedPane.CloseButtonTab) tabs.getTabComponentAt(i)).tab
                                instanceof CodeEditor) {
                            if (((CodeEditor) ((JClosableTabbedPane.CloseButtonTab)
                                    tabs.getTabComponentAt(i)).tab).file.getAbsolutePath().equals(
                                    f.getAbsolutePath()
                            )) {
                                tab = ((JClosableTabbedPane.CloseButtonTab) tabs.getTabComponentAt(i)).tab;
                                isAlreadyOpened = true;
                                break;
                            }
                        }
                    }
                    if (isAlreadyOpened) tabs.setSelectedComponent(tab);
                    else {
                        tabs.addTab(f.getName(),
                                new ImageIcon(Utils.getExtension(f).equals("q") ?
                                        Icons.iconQFile : Icons.iconFile),
                                new CodeEditor(that, f));
                    }
                    pathBreadCrumb.renewCrumbs(f);
                } else if (e.getButton() == MouseEvent.BUTTON3) {
                    TreePath pathForLocation = fsTree.getPathForLocation(e.getPoint().x, e.getPoint().y);
                    if (pathForLocation != null) {
                        FileTreeNode ftn = (FileTreeNode) pathForLocation.getLastPathComponent();
                        ((FileContextPopup) fsTree.getComponentPopupMenu()).parent = ftn.file;
                    }
                    super.mousePressed(e);
                }
            }
        });

        tabs.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (tabs.getTabCount() == 0) { }
                else pathBreadCrumb.renewCrumbs(((CodeEditor) tabs.getSelectedComponent()).file);
            }
        });

        InputMap iMap = root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap aMap = root.getActionMap();
        iMap.put(KeyStroke.getKeyStroke('S', InputEvent.CTRL_DOWN_MASK),
                "Control-S-Save-All");
        aMap.put("Control-S-Save-All", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                that.saveAll();
            }
        });

        boundConsole = new Console(consoleArea);

        pathBreadCrumb = new JPathBreadCrumb(that, project.root);
        breadcrumbPlace.setLayout(new GridLayout());
        breadcrumbPlace.add(pathBreadCrumb);

        setupMenuBar(this);

        setVisible(true);
    }
    
    public void setupMenuBar(EditorForm that) {
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new FileContext(this, project.root);

        JMenu runMenu = new JMenu("Run");
        JMenuItem runRun = new JMenuItem("Run Current");
        runRun.setIcon(new ImageIcon(Icons.iconStart));
        runRun.addActionListener(consoleRunBtn.getActionListeners()[0]);
        runMenu.add(runRun);
        JMenuItem runProfile = new JMenuItem("Profile Current");
        runProfile.setIcon(new ImageIcon(Icons.iconProfile));
        runProfile.addActionListener(toolbarProfileBtn.getActionListeners()[0]);
        runMenu.add(runProfile);

        JMenu helpMenu = new JMenu("Help");
        JMenuItem helpIDE = new JMenuItem("quail::studio help");
        helpIDE.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                IDEHelp dialog = new IDEHelp();
                dialog.pack();
                dialog.setVisible(true);
            }
        });
        helpMenu.add(helpIDE);

        menuBar.add(fileMenu);
        menuBar.add(runMenu);
        menuBar.add(helpMenu);
        setJMenuBar(menuBar);
    }

    public void saveAll() {
        for (int i = 0; i < tabs.getTabCount(); i++) {
            JClosableTabbedPane.CloseButtonTab btn =
                    (JClosableTabbedPane.CloseButtonTab) tabs.getTabComponentAt(i);
            ((CodeEditor) btn.tab).save();
        }
    }
}
