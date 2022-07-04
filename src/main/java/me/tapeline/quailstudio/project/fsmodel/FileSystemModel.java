package me.tapeline.quailstudio.project.fsmodel;

import javax.swing.tree.DefaultMutableTreeNode;
import java.io.File;

public class FileSystemModel {

    public Element root;

    public FileSystemModel(File root) {
        this.root = new Element(root);
    }

    public FileTreeNode toTree() {
        return root.toTree();
    }

    public void reload() {
        root.reload();
    }

}
