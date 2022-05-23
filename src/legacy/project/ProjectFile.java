package me.tapeline.quailstudio.project;

import me.tapeline.quailstudio.IOManager;

import java.io.File;

public class ProjectFile {

    public File file;

    public String contents;

    public ProjectFile(File file) {
        this.file = file;
    }

    public void load() {
        this.contents = IOManager.fileInput(file.getAbsolutePath());
    }

    public void save() {
        if (!file.getParentFile().exists())
            if (!file.mkdirs()) return;
        IOManager.fileSet(file.getAbsolutePath(), contents);
    }

}
