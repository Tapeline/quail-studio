package me.tapeline.quailstudio.contextmenus;

import me.tapeline.quailstudio.Main;
import me.tapeline.quailstudio.dialogs.NewProjectWizard;
import me.tapeline.quailstudio.dialogs.Preferences;
import me.tapeline.quailstudio.forms.EditorForm;
import me.tapeline.quailstudio.utils.Icons;
import org.apache.commons.io.FileUtils;

import javax.swing.*;
import javax.swing.tree.DefaultTreeModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class FileContextPopup extends JPopupMenu {

    public File parent;
    public EditorForm editorForm;

    public FileContextPopup(EditorForm form, File f) {
        super("File");
        parent = f;
        editorForm = form;
        
        JPopupMenu fileMenu = this;

        JMenu fileMenuNew = new JMenu("New");
        fileMenuNew.setIcon(new ImageIcon(Icons.iconFile));
        JMenuItem fileNewPlain = new JMenuItem("New Plain File");
        fileNewPlain.setIcon(new ImageIcon(Icons.iconFile));
        fileNewPlain.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser(parent);
                fileChooser.setDialogTitle("Create Plain file");
                fileChooser.setDialogType(JFileChooser.SAVE_DIALOG);
                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                int result = fileChooser.showOpenDialog(form);
                if (result == JFileChooser.APPROVE_OPTION) {
                    try {
                        FileUtils.writeStringToFile(fileChooser.getSelectedFile(), "", "UTF-8");
                    } catch (IOException exception) {
                        JOptionPane.showMessageDialog(form,
                                "quail::studio failed create file",
                                "File Fail",
                                JOptionPane.ERROR_MESSAGE);
                    }
                    form.project.resolveFiles();
                    form.fsTree.setModel(new DefaultTreeModel(form.project.fileSystem.toTree()));
                }
            }
        });
        fileMenuNew.add(fileNewPlain);

        JMenuItem fileNewQuail = new JMenuItem("New Quail file");
        fileNewQuail.setIcon(new ImageIcon(Icons.iconQFile));
        fileNewQuail.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser(parent);
                fileChooser.setDialogTitle("Create Quail file");
                fileChooser.setDialogType(JFileChooser.SAVE_DIALOG);
                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                int result = fileChooser.showOpenDialog(form);
                if (result == JFileChooser.APPROVE_OPTION) {
                    try {
                        FileUtils.writeStringToFile(fileChooser.getSelectedFile(), "", "UTF-8");
                    } catch (IOException exception) {
                        JOptionPane.showMessageDialog(form,
                                "quail::studio failed create file",
                                "File Fail",
                                JOptionPane.ERROR_MESSAGE);
                    }
                    form.project.resolveFiles();
                    form.fsTree.setModel(new DefaultTreeModel(form.project.fileSystem.toTree()));
                }
            }
        });
        fileMenuNew.add(fileNewQuail);

        JMenuItem fileNewFolder = new JMenuItem("New Folder");
        fileNewFolder.setIcon(new ImageIcon(Icons.iconFolderNew));
        fileNewFolder.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser(parent);
                fileChooser.setDialogTitle("Create Plain file");
                fileChooser.setDialogType(JFileChooser.SAVE_DIALOG);
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int result = fileChooser.showOpenDialog(form);
                if (result == JFileChooser.APPROVE_OPTION) {
                    if (!fileChooser.getSelectedFile().mkdirs()) {
                        JOptionPane.showMessageDialog(form,
                                "quail::studio failed create folder",
                                "File Fail",
                                JOptionPane.ERROR_MESSAGE);
                    }
                    form.project.resolveFiles();
                    form.fsTree.setModel(new DefaultTreeModel(form.project.fileSystem.toTree()));
                }
            }
        });
        fileMenuNew.add(fileNewFolder);

        JMenuItem fileNewProject = new JMenuItem("New Project");
        fileNewProject.setIcon(new ImageIcon(Icons.iconFolderNew));
        fileNewProject.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                File projectRoot = NewProjectWizard.dialog();
                if (projectRoot == null) return;
                projectRoot.mkdirs();
                Main.editors.add(new EditorForm(projectRoot));
            }
        });
        fileMenuNew.add(fileNewProject);
        fileMenu.add(fileMenuNew);

        JMenuItem fileDelete = new JMenuItem("Remove");
        fileDelete.setIcon(new ImageIcon(Icons.iconCross));
        fileDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                parent.delete();
            }
        });
        fileMenuNew.add(fileDelete);
        fileMenu.add(fileDelete);

    }

}
