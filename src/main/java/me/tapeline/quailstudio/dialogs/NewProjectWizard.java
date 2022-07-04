package me.tapeline.quailstudio.dialogs;

import me.tapeline.quailstudio.Main;

import javax.swing.*;
import java.awt.event.*;
import java.io.File;

public class NewProjectWizard extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField projectName;
    private JTextField projectHome;
    private JButton browseButton;
    public File projectLocation;

    public NewProjectWizard() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        NewProjectWizard that = this;

        browseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Choose Project Home Folder");
                fileChooser.setDialogType(JFileChooser.OPEN_DIALOG);
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int result = fileChooser.showOpenDialog(that);
                if (result == JFileChooser.APPROVE_OPTION) {
                    projectHome.setText(fileChooser.getSelectedFile().getAbsolutePath());
                    projectName.setText(fileChooser.getSelectedFile().getName());
                }
            }
        });

        projectHome.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                projectName.setText(new File(projectHome.getText()).getName());
            }
            @Override
            public void keyTyped(KeyEvent e) {
                projectName.setText(new File(projectHome.getText()).getName());
            }
            @Override
            public void keyReleased(KeyEvent e) {
                projectName.setText(new File(projectHome.getText()).getName());
            }
        });

        projectName.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (!projectName.getText().equals(""))
                    projectHome.setText(new File(projectHome.getText()).getParent() +
                        "/" + projectName.getText());
            }
            @Override
            public void keyTyped(KeyEvent e) {
                if (!projectName.getText().equals(""))
                    projectHome.setText(new File(projectHome.getText()).getParent() +
                            "/" + projectName.getText());
            }
            @Override
            public void keyReleased(KeyEvent e) {
                if (!projectName.getText().equals(""))
                    projectHome.setText(new File(projectHome.getText()).getParent() +
                            "/" + projectName.getText());
            }
        });

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
        projectLocation = new File(projectHome.getText());
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public static File dialog() {
        NewProjectWizard dialog = new NewProjectWizard();
        dialog.pack();
        dialog.setVisible(true);
        return dialog.projectLocation;
    }

    public static void main(String[] args) {
        NewProjectWizard dialog = new NewProjectWizard();
        dialog.pack();
        dialog.setVisible(true);
        System.out.println(dialog.projectLocation);
        System.exit(0);
    }
}
