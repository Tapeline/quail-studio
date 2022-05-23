package me.tapeline.quailstudio.project;

import java.io.File;
import java.util.List;

public class ProjectDirectory extends ProjectComponent {

    public List<ProjectComponent> contents;
    public File file;

    public ProjectDirectory(File file) {
        assert file.isDirectory();
        this.file = file;
    }

    @Override
    void save() {
        contents.forEach(ProjectComponent::save);
    }

    @Override
    void load() {
        contents.forEach(ProjectComponent::load);
    }

    void open() {
        for (File f : file.listFiles()) {
            if (f.isDirectory()) {

            }
        }
    }
}
