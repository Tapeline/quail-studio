package me.tapeline.quailstudio.project;

import me.tapeline.quailstudio.IOManager;
import org.fife.ui.rtextarea.RTextScrollPane;

import java.io.File;

public class ProjectFile extends ProjectComponent {

    public File file;

    public String contents;
    public RTextScrollPane pane;
    public boolean isOpened;

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
