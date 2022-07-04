package me.tapeline.quailstudio;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatIntelliJLaf;
import me.tapeline.quailstudio.customui.JClosableTabbedPane;
import me.tapeline.quailstudio.forms.EditorForm;
import me.tapeline.quailstudio.forms.SplashScreen;
import me.tapeline.quailstudio.forms.WelcomeScreen;
import me.tapeline.quailstudio.project.CodeEditor;
import me.tapeline.quailstudio.utils.*;
import org.fife.ui.rsyntaxtextarea.AbstractTokenMakerFactory;
import org.fife.ui.rsyntaxtextarea.Style;
import org.fife.ui.rsyntaxtextarea.TokenMakerFactory;
import org.fife.ui.rsyntaxtextarea.folding.FoldParserManager;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Main {

    public static List<EditorForm> editors = new ArrayList<>();
    public static String OS = null;
    public static Configuration cfg;
    public static String getOsName() {
        if(OS == null) { OS = System.getProperty("os.name"); }
        return OS;
    }

    public static boolean isWindows() {
        return getOsName().startsWith("Windows");
    }

    public static boolean isUnix() {
        return getOsName().startsWith("Linux");
    }

    public static void reloadCfg() {
        for (EditorForm e : editors) {
            e.consoleArea.setFont(Fonts.adapt(Fonts.defaultFont));
            for (int i = 0; i < e.tabs.getTabCount(); i++) {
                ((CodeEditor) ((JClosableTabbedPane.CloseButtonTab)
                        e.tabs.getTabComponentAt(i)).tab).area.setFont(
                                Fonts.adapt(Fonts.defaultFont));
                for (Style style : ((CodeEditor) ((JClosableTabbedPane.CloseButtonTab)
                        e.tabs.getTabComponentAt(i)).tab).area.getSyntaxScheme().getStyles()) {
                    style.font = Fonts.adapt(Fonts.defaultFont);
                }
                ((CodeEditor) ((JClosableTabbedPane.CloseButtonTab)
                        e.tabs.getTabComponentAt(i)).tab).scrollPane.
                        getGutter().setLineNumberFont(
                                Fonts.adapt(Fonts.defaultFont));
            }
        }
    }

    public static void main(String[] args) throws IOException, FontFormatException {
        final SplashScreen splashScreen = new SplashScreen();
        splashScreen.setVisible(true);

        System.out.println(Utils.splitPath(new File("/home/tapeline/Q")));

        // Load Locale
        Locale.setDefault(Locale.ENGLISH);

        // Load config
        cfg = new Configuration("config.yml");

        // Load theme
        if (cfg.cfg.get("theme").equals("dark"))
            FlatDarculaLaf.setup();
        else if (cfg.cfg.get("theme").equals("light"))
            FlatIntelliJLaf.setup();

        // Load icons
        Icons.loadIcons();

        // Load font
        Fonts.loadFonts();

        // Setup syntax
        AbstractTokenMakerFactory atmf = (AbstractTokenMakerFactory) TokenMakerFactory.getDefaultInstance();
        atmf.putMapping("text/quail", "me.tapeline.quailstudio.utils.Tokenizer");
        FoldParserManager.get().addFoldParserMapping("text/quail", new FoldingParser());

        // Stay for a while
        try { Thread.sleep(600); } catch (InterruptedException ignored) {}
        splashScreen.setVisible(false);
        splashScreen.dispose();

        WelcomeScreen welcomeScreen = new WelcomeScreen();
    }
}