package me.tapeline.quailstudio.project;

import me.tapeline.quailstudio.Main;
import me.tapeline.quailstudio.forms.EditorForm;
import me.tapeline.quailstudio.project.fsmodel.FileSystemModel;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

public class Project {

    public FileSystemModel fileSystem;
    public File root;

    public Project(File f) {
        root = f;
        fileSystem = new FileSystemModel(root);
        boolean hasProject = false;
        for (String path : ((List<String>) Main.cfg.cfg.get("last-projects"))) {
            if (path.equals(f.getAbsolutePath())) {
                hasProject = true;
                break;
            }
        }
        if (!hasProject) ((List<String>) Main.cfg.cfg.get("last-projects")).add(f.getAbsolutePath());
        try {
            Main.cfg.saveConfig();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveProject(EditorForm form) throws IOException {
        fileSystem.root.save();
    }

    public void resolveFiles() {
        fileSystem.reload();
    }

    public File getRelativeToProjectRoot(File f) {
        String p = f.getAbsolutePath().replace(root.getAbsolutePath(), "");
        if (p.startsWith("/")) p = p.substring(1);
        p = root.getName() + "/" + p;
        return new File(p);
    }

}
