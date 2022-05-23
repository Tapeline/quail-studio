package me.tapeline.quailstudio.forms;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import java.awt.*;
import java.net.URI;
import java.net.URISyntaxException;

public class IDEHelp {
    public JPanel main;
    private JTextPane managingFilesEachTimeTextPane;
    private JTextPane runningYourProjectClickingTextPane;
    private JTextPane quailStudioByTapelineTextPane;

    public IDEHelp() {
        quailStudioByTapelineTextPane.addHyperlinkListener(new HyperlinkListener() {
            @Override
            public void hyperlinkUpdate(HyperlinkEvent e) {
                try {
                    openWebpage(e.getURL().toURI());
                } catch (URISyntaxException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }

    public static boolean openWebpage(URI uri) {
        Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
        if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
            try {
                desktop.browse(uri);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
