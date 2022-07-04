package me.tapeline.quailstudio.forms;

import me.tapeline.quailstudio.Main;
import me.tapeline.quailstudio.dialogs.NewProjectWizard;
import me.tapeline.quailstudio.dialogs.Preferences;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.List;

public class WelcomeScreen extends JFrame {
    private JButton newProjectButton;
    private JButton openProjectButton;
    private JButton preferencesButton;
    private JButton quitButton;
    private JPanel root;
    private JPanel projects;

    public WelcomeScreen() {
        super("Quail::Studio - Welcome");
        setContentPane(root);
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        pack();

        WelcomeScreen that = this;

        newProjectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                File projectRoot = NewProjectWizard.dialog();
                if (projectRoot == null) return;
                projectRoot.mkdirs();
                Main.editors.add(new EditorForm(projectRoot));
                that.dispose();
            }
        });

        openProjectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                File projectRoot = null;
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Choose Project");
                fileChooser.setDialogType(JFileChooser.OPEN_DIALOG);
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int result = fileChooser.showOpenDialog(that);
                if (result == JFileChooser.APPROVE_OPTION) {
                    projectRoot = fileChooser.getSelectedFile();
                }
                if (projectRoot == null) return;
                Main.editors.add(new EditorForm(projectRoot));
                that.dispose();
            }
        });

        quitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        preferencesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Preferences dialog = new Preferences();
                dialog.pack();
                dialog.setVisible(true);
            }
        });

        projects.setLayout(new BoxLayout(projects, BoxLayout.PAGE_AXIS));
        for (String path : ((List<String>) Main.cfg.cfg.get("last-projects"))) {
            projects.add(new ProjectEntry(this, path));
        }

        setVisible(true);
    }

    static class ProjectEntry extends JPanel {

        public String path;
        public JLabel title;
        public JLabel desc;
        public WelcomeScreen screen;

        public ProjectEntry(WelcomeScreen sc, String p) {
            super();
            setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
            path = p;
            screen = sc;

            title = new JLabel(new File(p).getName());
            title.setFont(new Font(title.getFont().getFontName(), Font.BOLD, 20));
            desc = new JLabel(p);
            desc.setFont(new Font(desc.getFont().getFontName(), Font.ITALIC, 14));

            add(title);
            add(desc);

            setBackground(new Color(40, 40, 40, 0));
            addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    setBackground(new Color(0x404040));
                }

                public void mouseExited(java.awt.event.MouseEvent evt) {
                    setBackground(new Color(40, 40, 40, 0));
                }

                public void mouseClicked(MouseEvent e) {
                    Main.editors.add(new EditorForm(new File(path)));
                    screen.dispose();
                }
            });
        }
    }


}
