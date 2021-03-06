package me.tapeline.quailstudio.test;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.*;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

public class Main extends javax.swing.JFrame {

    private JTree t;
    private DefaultTreeModel model;
    private DefaultMutableTreeNode selectedNode;

    public Main() {
        DefaultMutableTreeNode n = new DefaultMutableTreeNode("test");
        n.add(new DefaultMutableTreeNode("test2"));
        model = new DefaultTreeModel(n);
        t = new JTree(model);
        t.setEditable(true);
        t.setComponentPopupMenu(getPopUpMenu());
        t.addMouseListener(getMouseListener());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().add(t);
        pack();
        setVisible(true);
    }

    private MouseListener getMouseListener() {
        return new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent arg0) {
                if(arg0.getButton() == MouseEvent.BUTTON3){
                    TreePath pathForLocation = t.getPathForLocation(arg0.getPoint().x, arg0.getPoint().y);
                    if(pathForLocation != null){
                        selectedNode = (DefaultMutableTreeNode) pathForLocation.getLastPathComponent();
                    } else{
                        selectedNode = null;
                    }

                }
                super.mousePressed(arg0);
            }
        };
    }

    private JPopupMenu getPopUpMenu() {
        JPopupMenu menu = new JPopupMenu();
        JMenuItem item = new JMenuItem("edit");
        item.addActionListener(getEditActionListener());
        menu.add(item);

        JMenuItem item2 = new JMenuItem("add");
        item2.addActionListener(getAddActionListener());
        menu.add(item2);

        return menu;
    }

    private ActionListener getAddActionListener() {
        return new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                if(selectedNode != null){
                    System.out.println("pressed" + selectedNode);
                    DefaultMutableTreeNode n = new DefaultMutableTreeNode("added");
                    selectedNode.add(n);
                    t.repaint();
                    t.updateUI();
                }
            }
        };
    }

    private ActionListener getEditActionListener() {
        return new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                if(selectedNode != null){
                    //edit here
                    System.out.println("pressed" + selectedNode);
                }
            }
        };
    }

    public static void main(String... s){
        new Main();
    }

}