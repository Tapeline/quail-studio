package me.tapeline.quailstudio.dialogs;

import me.tapeline.quailstudio.Main;

import javax.swing.*;
import java.awt.event.*;
import java.io.IOException;

public class Preferences extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JPanel buttonPanel;
    private JPanel contentPanel;
    private JTabbedPane tabbedPane1;
    private JSpinner fsz;
    private JTextField qhome;
    private JTextField qname;
    private JTextField cmd;
    private JButton resetSettingsToDefaultButton;
    private JTextArea thisCommandWouldBeTextArea;
    private JButton reloadButton;

    public Preferences() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        qhome.setText((String) Main.cfg.cfg.get("quail-home"));
        qname.setText((String) Main.cfg.cfg.get("quail-name"));
        fsz.setValue(Main.cfg.cfg.get("font-size"));
        cmd.setText((String) Main.cfg.cfg.get("run-cmd"));

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    onOK();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
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
        resetSettingsToDefaultButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Main.cfg.reset();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                dispose();
            }
        });
        reloadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Main.reloadCfg();
            }
        });
    }

    private void onOK() throws IOException {
        Main.cfg.cfg.put("quail-home", qhome.getText());
        Main.cfg.cfg.put("quail-name", qname.getText());
        Main.cfg.cfg.put("font-size", fsz.getValue());
        Main.cfg.cfg.put("run-cmd", cmd.getText());
        Main.cfg.saveConfig();
        Main.reloadCfg();
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public static void main(String[] args) {
        Preferences dialog = new Preferences();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
