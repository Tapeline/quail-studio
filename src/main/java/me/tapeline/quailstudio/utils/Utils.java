package me.tapeline.quailstudio.utils;

import org.fife.ui.autocomplete.BasicCompletion;
import org.fife.ui.autocomplete.CompletionProvider;
import org.fife.ui.autocomplete.DefaultCompletionProvider;
import org.fife.ui.autocomplete.ShorthandCompletion;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Utils {

    public static String getExtension(File file) {
        String[] dotSep = file.getName().split("\\.");
        return dotSep[dotSep.length - 1];
    }

    public static ImageIcon getIconForFile(File file) {
        String ext = getExtension(file);
        if (file.isDirectory()) return new ImageIcon(Icons.iconFolder);
        if (file.getName().startsWith("QuailProfilingReport") && ext.equals("json"))
            return new ImageIcon(Icons.iconFileReport);
        switch (ext) {
            case "q": return new ImageIcon(Icons.iconQFile);
            case "json": return new ImageIcon(Icons.iconFileJson);
            case "yml": return new ImageIcon(Icons.iconFileYml);
            default: return new ImageIcon(Icons.iconFile);
        }
    }

    public static void adaptButton(JButton btn) {
        btn.setBorderPainted(false);
        btn.setBackground(new Color(0, 0, 0, 0));
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(0x404040));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(0, 0, 0, 0));
            }
        });
    }

    public static CompletionProvider createCompletionProvider() {

        DefaultCompletionProvider provider = new DefaultCompletionProvider();

        for (String s : Tokenizer.getWordsToHighlightHash().keySet()) {
            provider.addCompletion(new BasicCompletion(provider, s));
        }

        provider.addCompletion(new ShorthandCompletion(provider, "ovr",
                "override", "override"));
        provider.addCompletion(new ShorthandCompletion(provider, "stm",
                "staticmethod", "staticmethod"));
        provider.addCompletion(new ShorthandCompletion(provider, "tostr",
                "tostring", "tostring"));
        provider.addCompletion(new ShorthandCompletion(provider, "cont",
                "container", "container"));

        return provider;

    }

    public static List<String> splitPath(File f) {
        List<String> pathElements = new ArrayList<>();
        Paths.get(f.getPath()).forEach(p -> pathElements.add(p.toString()));
        return pathElements;
    }

    public static List<File> filePyramid(List<String> components) {
        List<File> fp = new ArrayList<>();
        for (int i = 0; i < components.size(); i++) {
            String path = "";
            for (int j = 0; j <= i; j++) path += components.get(j) + "/";
            fp.add(new File(path));
        }
        return fp;
    }

}
