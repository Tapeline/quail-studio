package me.tapeline.quailstudio.project.fsmodel;

import me.tapeline.quailstudio.customui.IconTreeNode;
import me.tapeline.quailstudio.utils.Utils;

import javax.swing.tree.DefaultMutableTreeNode;
import java.io.File;

public class FileTreeNode extends IconTreeNode {

    public File file;

    public FileTreeNode(File f) {
        super(Utils.getIconForFile(f), f.getName());
        file = f;
    }

}
