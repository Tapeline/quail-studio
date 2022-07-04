package me.tapeline.quailstudio.customui;

import me.tapeline.quailstudio.forms.EditorForm;
import me.tapeline.quailstudio.utils.Utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class JPathBreadCrumb extends JPanel {

    public static class BreadCrumbButton extends JButton {

        public File f;

        public BreadCrumbButton(EditorForm form, File f) {
            super(f.getName());
            this.f = f;
            setBorderPainted(false);
            setBackground(new Color(0x454545));
            addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    setBackground(new Color(0x555555));
                }

                public void mouseExited(java.awt.event.MouseEvent evt) {
                    setBackground(new Color(0x454545));
                }
            });
            addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {

                }
            });
        }

    }

    public File path;
    public EditorForm form;
    public List<JButton> crumbs = new ArrayList<>();

    public JPathBreadCrumb(EditorForm form, File f) {
        super();
        setLayout(new FlowLayout());
        path = f;
        this.form = form;
        renewCrumbs(f);
    }

    public void renewCrumbs(File file) {
        this.path = file;
        crumbs.clear();
        this.removeAll();
        List<String> pathElements = Utils.splitPath(form.project.getRelativeToProjectRoot(file));
        List<File> filePyramid = Utils.filePyramid(pathElements);
        for (File f : filePyramid) {
            BreadCrumbButton btn = new BreadCrumbButton(form, f);
            crumbs.add(btn);
            add(btn);
        }
    }

}
