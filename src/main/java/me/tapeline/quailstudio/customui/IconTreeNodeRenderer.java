package me.tapeline.quailstudio.customui;

import javax.swing.*;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;

public class IconTreeNodeRenderer extends DefaultTreeCellRenderer {

    public Component getTreeCellRendererComponent(JTree tree,
                                                  Object value,
                                                  boolean selected,
                                                  boolean expanded,
                                                  boolean leaf,
                                                  int row,
                                                  boolean hasFocus) {
        if (value instanceof IconTreeNode) {
            IconTreeNode.TreeNodeData data = (IconTreeNode.TreeNodeData) ((IconTreeNode) value).getUserObject();
            setIcon(data.getIcon());
            setText(data.getText());
        }
        return this;
    }
}