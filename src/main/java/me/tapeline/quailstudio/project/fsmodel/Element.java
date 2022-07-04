package me.tapeline.quailstudio.project.fsmodel;

import org.apache.commons.io.FileUtils;

import javax.swing.tree.DefaultMutableTreeNode;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Element {

    public List<Element> children = new ArrayList<>();
    public File file;
    public String contents = "";

    public Element(File file) {
        this.file = file;
        if (file.isDirectory())
            for (File f : Objects.requireNonNull(file.listFiles()))
                children.add(new Element(f));
    }

    public void open() throws IOException {
        if (file.isFile())
            contents = FileUtils.readFileToString(file, "UTF-8");
    }

    public void save() throws IOException {
        if (file.isFile())
            FileUtils.writeStringToFile(file, contents, "UTF-8");
        else
            for (Element child : children)
                child.save();
    }

    public FileTreeNode toTree() {
        FileTreeNode me = new FileTreeNode(file);
        for (Element child : children)
            me.add(child.toTree());
        return me;
    }

    public void reload() {
        children.clear();
        if (file.isDirectory())
            for (File f : Objects.requireNonNull(file.listFiles()))
                children.add(new Element(f));
    }

}
