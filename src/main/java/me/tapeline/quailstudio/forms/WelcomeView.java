package me.tapeline.quailstudio.forms;

import me.tapeline.quailstudio.EditorFrame;
import me.tapeline.quailstudio.IOManager;
import me.tapeline.quailstudio.Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

public class WelcomeView {
    public JPanel panel1;
    private JButton open;
    private JButton create;
    private JButton quit;

    private final String[][] FILTERS = {{"q", "Quail Source Code (*.q)"}};

    public WelcomeView() {
        quit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Main.frames.get("welcome").dispose();
            }
        });
        open.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Open Quail file");
                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                int result = fileChooser.showOpenDialog(Main.frames.get("welcome"));
                if (result == JFileChooser.APPROVE_OPTION) {
                    Main.frames.put("editor", new EditorFrame(fileChooser.getSelectedFile()));
                    Main.frames.get("welcome").dispose();
                }
            }
        });
        create.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Create Quail file");
                fileChooser.setDialogType(JFileChooser.SAVE_DIALOG);
                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                int result = fileChooser.showOpenDialog(Main.frames.get("editor"));
                if (result == JFileChooser.APPROVE_OPTION) {
                    IOManager.fileSet(fileChooser.getSelectedFile().getAbsolutePath(), "");
                    Main.frames.put("editor", new EditorFrame(fileChooser.getSelectedFile()));
                    Main.frames.get("welcome").dispose();
                }
            }
        });
    }
}
