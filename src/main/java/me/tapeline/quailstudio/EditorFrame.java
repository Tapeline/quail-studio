package me.tapeline.quailstudio;

import com.formdev.flatlaf.FlatDarculaLaf;
import me.tapeline.quailstudio.forms.EditorView;
import me.tapeline.quailstudio.forms.IDEHelp;
import me.tapeline.quailstudio.forms.Preferences;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rsyntaxtextarea.SyntaxScheme;
import org.fife.ui.rsyntaxtextarea.Token;
import org.fife.ui.rtextarea.RTextScrollPane;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;

public class EditorFrame extends JFrame {

    public EditorView view;
    public HashMap<String, RTextScrollPane> codeTabList = new HashMap<>();

    public EditorFrame(File file) {
        super("quail::studio");
        view = new EditorView();
        setContentPane(view.main);
        setPreferredSize(new Dimension(900, 600));
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        view.path.setText(file.getAbsolutePath());
        loadDir(file.getParentFile());
        view.btnCfg.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Preferences dialog = new Preferences();
                dialog.pack();
                dialog.setVisible(true);
            }
        });
        view.runBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!new File(Main.cfg.cfg.get("quail-home").toString() + "/" +
                        (String) Main.cfg.cfg.get("quail-name")).exists()) {
                    JOptionPane.showMessageDialog(Main.frames.get("editor"),
                            "quail::studio failed to find a valid Quail installation!\n\n" +
                                    "Please specify one in Preferences>Run Configuration",
                            "No Quail Installed!",
                            JOptionPane.ERROR_MESSAGE);
                }
                Runtime r = Runtime.getRuntime();
                try {
                    String cmd = Main.cfg.cfg.get("run-cmd").toString().replaceAll("\\%home",
                            (String) Main.cfg.cfg.get("quail-home"));
                    cmd = cmd.replaceAll("\\%name", (String) Main.cfg.cfg.get("quail-name"));
                    cmd = cmd.replaceAll("\\%file",
                            ((RTextScrollPane) view.codeTabs.getSelectedComponent()).getTextArea().getName());
                    r.exec(cmd);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        view.closeTab.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (view.codeTabs.getComponents().length > 1) {
                    final String[] path = new String[1];
                    int selectedHash = view.codeTabs.getSelectedComponent().hashCode();
                    codeTabList.forEach((k, v) -> {
                        if (v.hashCode() == selectedHash)
                            path[0] = v.getTextArea().getName();
                    });
                    IOManager.fileSet(path[0],
                            ((RTextScrollPane) view.codeTabs.getSelectedComponent()).getTextArea().getText());
                    codeTabList.remove(path[0]);
                    view.codeTabs.remove(view.codeTabs.getSelectedComponent());
                }
            }
        });

        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenu fileMenuNew = new JMenu("New");
        fileMenuNew.setIcon(new ImageIcon(Main.iconFile));
        JMenuItem fileNewPlain = new JMenuItem("New Plain File");
        fileNewPlain.setIcon(new ImageIcon(Main.iconFile));
        fileNewPlain.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Create Plain file");
                fileChooser.setDialogType(JFileChooser.SAVE_DIALOG);
                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                int result = fileChooser.showOpenDialog(Main.frames.get("editor"));
                if (result == JFileChooser.APPROVE_OPTION) {
                    IOManager.fileSet(fileChooser.getSelectedFile().getAbsolutePath(), "");
                    openFile(fileChooser.getSelectedFile());
                }
            }
        });
        fileMenuNew.add(fileNewPlain);
        JMenuItem fileNewQuail = new JMenuItem("New Quail file");
        fileNewQuail.setIcon(new ImageIcon(Main.iconQFile));
        fileNewQuail.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Create Quail file");
                fileChooser.setAcceptAllFileFilterUsed(false);
                FileNameExtensionFilter filter = new FileNameExtensionFilter("Quail Source Code",
                        "q", "quail");
                fileChooser.addChoosableFileFilter(filter);
                fileChooser.setDialogType(JFileChooser.SAVE_DIALOG);
                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                int result = fileChooser.showOpenDialog(Main.frames.get("editor"));
                if (result == JFileChooser.APPROVE_OPTION) {
                    IOManager.fileSet(fileChooser.getSelectedFile().getAbsolutePath(), "");
                    openFile(fileChooser.getSelectedFile());
                }
            }
        });
        fileMenuNew.add(fileNewQuail);
        JMenuItem fileNewFolder = new JMenuItem("New Folder");
        fileNewFolder.setIcon(new ImageIcon(Main.iconFolderNew));
        fileNewFolder.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Create folder");
                fileChooser.setDialogType(JFileChooser.SAVE_DIALOG);
                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                int result = fileChooser.showOpenDialog(Main.frames.get("editor"));
                if (result == JFileChooser.APPROVE_OPTION) {
                    fileChooser.getSelectedFile().mkdirs();
                }
            }
        });
        fileMenuNew.add(fileNewFolder);
        fileMenu.add(fileMenuNew);
        JMenuItem fileOpen = new JMenuItem("Open file...");
        fileOpen.setIcon(new ImageIcon(Main.iconOpenFile));
        fileOpen.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Open Quail file");
                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                int result = fileChooser.showOpenDialog(Main.frames.get("editor"));
                if (result == JFileChooser.APPROVE_OPTION) {
                    openFile(fileChooser.getSelectedFile());
                }
            }
        });
        fileMenu.add(fileOpen);
        JMenuItem folderOpen = new JMenuItem("Open folder...");
        folderOpen.setIcon(new ImageIcon(Main.iconOpenFolder));
        folderOpen.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Open folder");
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int result = fileChooser.showOpenDialog(Main.frames.get("editor"));
                if (result == JFileChooser.APPROVE_OPTION) {
                    loadDir(fileChooser.getSelectedFile());
                }
            }
        });
        fileMenu.add(folderOpen);
        JMenuItem fileSaveAll = new JMenuItem("Save All");
        fileSaveAll.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                codeTabList.forEach((path, pane) -> {
                    IOManager.fileSet(path, pane.getTextArea().getText());
                });
            }
        });
        fileMenu.add(fileSaveAll);
        fileMenu.addSeparator();
        JMenuItem filePref = new JMenuItem("Preferences...");
        filePref.setIcon(new ImageIcon(Main.iconWrench));
        filePref.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Preferences dialog = new Preferences();
                dialog.pack();
                dialog.setVisible(true);
            }
        });
        fileMenu.add(filePref);
        fileMenu.addSeparator();
        JMenuItem fileExit = new JMenuItem("Quit");
        fileExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                codeTabList.forEach((path, pane) -> {
                    IOManager.fileSet(path, pane.getTextArea().getText());
                });
                Main.frames.forEach((k, frame) -> {
                    frame.dispose();
                });
            }
        });
        fileMenu.add(fileExit);

        JMenu helpMenu = new JMenu("Help");
        JMenuItem helpIDE = new JMenuItem("quail::studio help");
        helpIDE.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Main.frames.put("ide-help", new JFrame("quail::studio IDE help"));
                Main.frames.get("ide-help").setContentPane(new IDEHelp().main);
                Main.frames.get("ide-help").setPreferredSize(new Dimension(800, 600));
                Main.frames.get("ide-help").setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
                Main.frames.get("ide-help").pack();
                Main.frames.get("ide-help").setVisible(true);
            }
        });
        helpMenu.add(helpIDE);

        menuBar.add(fileMenu);
        menuBar.add(helpMenu);
        setJMenuBar(menuBar);

        pack();
        setVisible(true);
        openFile(file);

        view.codeTabs.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                view.path.setText(((RTextScrollPane) view.codeTabs.getSelectedComponent()).getTextArea().getName());
            }
        });

        InputMap iMap = view.main.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap aMap = view.main.getActionMap();
        Action ctrlS = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                codeTabList.forEach((path, pane) -> {
                    IOManager.fileSet(path, pane.getTextArea().getText());
                });
            }
        };
        iMap.put(KeyStroke.getKeyStroke('S', InputEvent.CTRL_DOWN_MASK), "Control-S-Save-All");
        aMap.put("Control-S-Save-All", ctrlS);
    }

    public void openFile(File file) {
        if (file == null) return;
        String name = file.getAbsolutePath().toString();
        if (codeTabList.containsKey(name)) {
            view.codeTabs.setSelectedComponent(codeTabList.get(name));
            return;
        }
        RSyntaxTextArea area = new RSyntaxTextArea();
        area.setSyntaxEditingStyle("text/quail");
        area.setBackground(Color.DARK_GRAY);
        area.getSyntaxScheme().getStyle(Token.IDENTIFIER).foreground = Color.WHITE;
        area.getSyntaxScheme().getStyle(Token.RESERVED_WORD).foreground = new Color(210, 100, 0);
        area.getSyntaxScheme().getStyle(Token.RESERVED_WORD_2).foreground = new Color(210, 100, 0);
        area.getSyntaxScheme().getStyle(Token.OPERATOR).foreground = new Color(210, 100, 0);
        area.getSyntaxScheme().getStyle(Token.SEPARATOR).foreground = new Color(210, 100, 0);
        area.getSyntaxScheme().getStyle(Token.LITERAL_BOOLEAN).foreground = new Color(210, 100, 0);
        area.getSyntaxScheme().getStyle(Token.LITERAL_NUMBER_DECIMAL_INT).foreground = new Color(120, 150, 220);
        area.getSyntaxScheme().getStyle(Token.LITERAL_NUMBER_FLOAT).foreground = new Color(120, 150, 220);
        area.getSyntaxScheme().getStyle(Token.LITERAL_STRING_DOUBLE_QUOTE).foreground = new Color(30, 160, 50);
        area.getSyntaxScheme().getStyle(Token.COMMENT_EOL).foreground = new Color(100, 100, 100);
        area.setHighlightCurrentLine(false);
        area.setName(name);
        area.setText(IOManager.fileInput(file.getAbsolutePath()));
        area.setFont(new Font("Noto Mono", Font.PLAIN,
                (Integer) Main.cfg.cfg.get("font-size")));
        RTextScrollPane s = new RTextScrollPane(area);
        s.getGutter().setBookmarkingEnabled(true);
        s.getGutter().setBookmarkIcon(new ImageIcon(Main.iconFile));
        codeTabList.put(name, s);
        view.codeTabs.addTab(file.getName(), codeTabList.get(name));
    }

    public void loadDir(File dir) {
        FileSystemModel fileSystemModel = new FileSystemModel(dir);
        view.fileTree.setModel(fileSystemModel);
        view.fileTree.setEditable(true);
        view.fileTree.addTreeSelectionListener(new TreeSelectionListener() {
            public void valueChanged(TreeSelectionEvent event) {
                File file = (File) view.fileTree.getLastSelectedPathComponent();
                openFile(file);
            }
        });
    }

}
