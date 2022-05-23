package me.tapeline.quailstudio.project;

import org.fife.ui.rtextarea.RTextScrollPane;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Project {

    public File root;
    public List<ProjectFile> loadedFiles = new ArrayList<>();

    public Project(File root) {
        this.root = root;
    }

    public boolean isSingleFile() {
        return !root.isDirectory();
    }

    public void save() {
        if (isSingleFile())
            loadedFiles.get(0).save();
        else {
            if (!root.exists())
                if (!root.mkdirs()) return;
            loadedFiles.forEach(ProjectFile::save);
        }
    }

    public void load() {
        if (isSingleFile()) {
            ProjectFile f = new ProjectFile(root);
            f.load();
            loadedFiles.add(0, f);
        } else {
            for (File file : root.listFiles()) {
                ProjectFile f = new ProjectFile(file);
                f.load();
                loadedFiles.add(f);
            }
        }
    }

    public void reload() {
        save();
        load();
    }

    public static Project open(File file) {
        Project p = new Project(file);
        p.load();
        return p;
    }

    public ProjectFile get(File f) {
        for (ProjectFile ff : loadedFiles)
            if (ff.file.getAbsoluteFile().getAbsolutePath().equals(
                    f.getAbsoluteFile().getAbsolutePath()))
                return ff;
        return null;
    }

}
