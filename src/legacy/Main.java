package me.tapeline.quailstudio;

import com.formdev.flatlaf.FlatDarculaLaf;
import me.tapeline.quailstudio.forms.WelcomeView;
import org.fife.ui.rsyntaxtextarea.AbstractTokenMakerFactory;
import org.fife.ui.rsyntaxtextarea.TokenMakerFactory;

import java.awt.Component;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;

public class Main {

    public static HashMap<String, JFrame> frames = new HashMap<>();
    public static Configuration cfg;

    public static BufferedImage iconStart;
    public static BufferedImage iconFile;
    public static BufferedImage iconQFile;
    public static BufferedImage iconFolder;
    public static BufferedImage iconWrench;
    public static BufferedImage iconCog;
    public static BufferedImage iconFolderNew;
    public static BufferedImage iconOpenFile;
    public static BufferedImage iconOpenFolder;
    public static BufferedImage iconCross;
    public static BufferedImage iconCrossHover;
    public static BufferedImage iconStartHover;
    public static BufferedImage iconWrenchHover;
    public static BufferedImage appIcon;

    public static String OS = null;

    public static Component getComponentByName(Container window, String name) {
        for (Component c : window.getComponents())
            if (c.getName() != null && c.getName().equals(name))
                return c;
        return null;
    }

    public static void reloadCfg() {
        if (frames.containsKey("editor"))
            ((EditorFrame) frames.get("editor")).codeTabList.forEach((k, v) -> {
                v.getTextArea().setFont(new Font("Noto Mono", Font.PLAIN, ((int) cfg.cfg.get("font-size"))));
            });
    }

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

    public static void updateIcons() {
        frames.forEach((k, v) -> v.setIconImage(appIcon));
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        System.out.println(getOsName());
        final SplashScreen splashScreen = new SplashScreen();
        splashScreen.setVisible(true);
        cfg = new Configuration("qsconfig.yml");

        File startSh = new File("quailStart.sh");
        if (!startSh.exists()) {
            IOManager.fileSet(startSh.getAbsolutePath(),
                    "java -jar $1 false $2;\n" +
                            "read -p \"Press any key\"\n");
        }
        File startCmd = new File("quailStart.cmd");
        if (!startCmd.exists()) {
            IOManager.fileSet(startCmd.getAbsolutePath(),
                    "java -jar %1 false %2;\n" +
                            "pause\n");
        }

        AbstractTokenMakerFactory atmf = (AbstractTokenMakerFactory) TokenMakerFactory.getDefaultInstance();
        atmf.putMapping("text/quail", "me.tapeline.quailstudio.Tokenizer");

        iconStart = ImageIO.read(Main.class.getClassLoader().getResource("start.png"));
        iconFile = ImageIO.read(Main.class.getClassLoader().getResource("file.png"));
        iconFolder = ImageIO.read(Main.class.getClassLoader().getResource("folder.png"));
        iconQFile = ImageIO.read(Main.class.getClassLoader().getResource("qfile.png"));
        iconWrench = ImageIO.read(Main.class.getClassLoader().getResource("settings.png"));
        iconCog = ImageIO.read(Main.class.getClassLoader().getResource("cog.png"));
        iconFolderNew = ImageIO.read(Main.class.getClassLoader().getResource("foldernew.png"));
        iconOpenFolder = ImageIO.read(Main.class.getClassLoader().getResource("folderopen.png"));
        iconOpenFile = ImageIO.read(Main.class.getClassLoader().getResource("fileopen.png"));
        iconCross = ImageIO.read(Main.class.getClassLoader().getResource("cross.png"));
        iconCrossHover = ImageIO.read(Main.class.getClassLoader().getResource("crossHover.png"));
        iconStartHover = ImageIO.read(Main.class.getClassLoader().getResource("startHover.png"));
        iconWrenchHover = ImageIO.read(Main.class.getClassLoader().getResource("settingsHover.png"));
        appIcon = ImageIO.read(Main.class.getClassLoader().getResource("icon.png"));
        Thread.sleep(650); // So everyone will enjoy my beautiful splash :D
        FlatDarculaLaf.setup();
        splashScreen.setVisible(false);

        frames.put("welcome", new JFrame("quail::studio - Welcome"));
        WelcomeView w = new WelcomeView();
        frames.get("welcome").setContentPane(w.panel1);
        frames.get("welcome").setPreferredSize(new Dimension(800, 600));
        frames.get("welcome").setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frames.get("welcome").pack();
        frames.get("welcome").setVisible(true);
        frames.get("welcome").setIconImage(appIcon);

        while (true) {
            AtomicBoolean cont = new AtomicBoolean(false);
            frames.forEach((k, v) -> {
                if (v.isShowing()) cont.set(true);
            });
            if (!cont.get()) break;
        }

        cfg.saveConfig();
        System.out.println("Bye");
    }
}
