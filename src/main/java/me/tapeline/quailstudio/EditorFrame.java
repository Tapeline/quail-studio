package me.tapeline.quailstudio;

import com.formdev.flatlaf.FlatDarculaLaf;
import me.tapeline.quailstudio.forms.EditorView;
import me.tapeline.quailstudio.forms.IDEHelp;
import me.tapeline.quailstudio.forms.Preferences;
import me.tapeline.quailstudio.project.Project;
import me.tapeline.quailstudio.project.ProjectFile;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rsyntaxtextarea.SyntaxScheme;
import org.fife.ui.rsyntaxtextarea.Token;
import org.fife.ui.rtextarea.RTextScrollPane;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class EditorFrame extends JFrame {

    public EditorView view;
    public Project project;

    public EditorFrame(File file) {
        super("quail::studio");
        view = new EditorView();
        view.path.setText(file.getAbsolutePath());
        setContentPane(view.main);
        setPreferredSize(new Dimension(900, 600));

        openProject(file.getParentFile());
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
                    if (view.codeTabs.getComponents().length > 1) {
                        RTextScrollPane selected = (RTextScrollPane) view.codeTabs.getSelectedComponent();
                        for (ProjectFile file : project.loadedFiles) {
                            if (file.pane == selected) {
                                file.save();
                                file.isOpened = false;
                                view.codeTabs.remove(selected);
                            }
                        }
                    }
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
                    project.reload();
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
                    project.reload();
                }
            }
        });
        fileMenuNew.add(fileNewQuail);
        JMenuItem fileNewFolder = new JMenuItem("New Project");
        fileNewFolder.setIcon(new ImageIcon(Main.iconFolderNew));
        fileNewFolder.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Create project");
                fileChooser.setDialogType(JFileChooser.SAVE_DIALOG);
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int result = fileChooser.showOpenDialog(Main.frames.get("editor"));
                if (result == JFileChooser.APPROVE_OPTION)
                    fileChooser.getSelectedFile().mkdirs();
            }
        });
        fileMenuNew.add(fileNewFolder);
        fileMenu.add(fileMenuNew);
        JMenuItem folderOpen = new JMenuItem("Open project...");
        folderOpen.setIcon(new ImageIcon(Main.iconOpenFolder));
        folderOpen.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Open folder");
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int result = fileChooser.showOpenDialog(Main.frames.get("editor"));
                if (result == JFileChooser.APPROVE_OPTION) {
                    openProject(fileChooser.getSelectedFile());
                }
            }
        });
        fileMenu.add(folderOpen);
        JMenuItem fileSaveAll = new JMenuItem("Save All");
        fileSaveAll.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                project.save();
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
                project.save();
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
                Main.updateIcons();
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
                project.save();
            }
        };
        iMap.put(KeyStroke.getKeyStroke('S', InputEvent.CTRL_DOWN_MASK), "Control-S-Save-All");
        aMap.put("Control-S-Save-All", ctrlS);
    }

    public void openFile(File file) {
        if (file == null) return;
        String name = file.getAbsolutePath();
        ProjectFile projectFile = project.get(file);
        if (projectFile == null) return;
        if (projectFile.isOpened) {
            view.codeTabs.setSelectedComponent(projectFile.pane);
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
        InputMap iMap = area.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap aMap = area.getActionMap();
        Action ctrlSpace = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String token = getLastWord(((RTextScrollPane) view.codeTabs.getSelectedComponent()).
                        getTextArea().getText());
                List<String> possible = new ArrayList<>();
                Tokenizer.getWordsToHighlightHash().forEach((k, v) -> {
                    if (k.startsWith(token))
                        possible.add(k);
                });
                if (possible.size() == 1) {
                    ((RTextScrollPane) view.codeTabs.getSelectedComponent()).getTextArea().insert(
                            possible.get(0).substring(token.length()),
                            ((RTextScrollPane) view.codeTabs.getSelectedComponent()).getTextArea().getCaretPosition()
                    );
                } else if (possible.size() > 1) {
                    AutocompletionPanel a = new AutocompletionPanel(possible, area, Main.frames.get("editor"), token);
                }
            }
        };
        iMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, InputEvent.CTRL_DOWN_MASK),
                "Control-Space");
        aMap.put("Control-Space", ctrlSpace);
        projectFile.pane = s;
        projectFile.isOpened = true;
        view.codeTabs.addTab(projectFile.file.getName(), projectFile.pane);
    }

    public static String getLastWord(String s) {
        int size = 0;
        for (int i = s.length() - 1; i >= 0; i--) {
            if (s.charAt(i) != ' ' && s.charAt(i) != '\20' && s.charAt(i) != '\n' && s.charAt(i) != '\t')
                size++;
            else break;
        }
        return s.substring(s.length() - size);
    }

    public void openProject(File dir) {
        project = Project.open(dir);
        FileSystemModel fileSystemModel = new FileSystemModel(dir);
        view.fileTree.setModel(fileSystemModel);
        view.fileTree.setEditable(true);
        view.fileTree.addTreeSelectionListener(new TreeSelectionListener() {
            public void valueChanged(TreeSelectionEvent event) {
                File file = (File) view.fileTree.getLastSelectedPathComponent();
                openFile(file);
            }
        });
        if (project.isSingleFile()) {
            openFile(dir);
        } else {
            for (ProjectFile file : project.loadedFiles) {
                openFile(file.file);
            }
        }
    }

}
