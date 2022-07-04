package me.tapeline.quailstudio.utils;

import me.tapeline.quailstudio.Main;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Icons {

    public static BufferedImage studioBg;
    public static BufferedImage splash;
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
    public static BufferedImage iconFileYml;
    public static BufferedImage iconFileJson;
    public static BufferedImage iconFileReport;
    public static BufferedImage iconProfile;
    public static BufferedImage appIcon;
    public static BufferedImage iconFold;
    public static BufferedImage iconUnfold;

    public static void loadIcons() throws IOException {
        studioBg = ImageIO.read(Main.class.getClassLoader().getResource("images/studiobg.png"));
        iconStart = ImageIO.read(Main.class.getClassLoader().getResource("images/start.png"));
        iconFile = ImageIO.read(Main.class.getClassLoader().getResource("images/file.png"));
        iconFolder = ImageIO.read(Main.class.getClassLoader().getResource("images/folder.png"));
        iconQFile = ImageIO.read(Main.class.getClassLoader().getResource("images/qfile.png"));
        iconWrench = ImageIO.read(Main.class.getClassLoader().getResource("images/settings.png"));
        iconCog = ImageIO.read(Main.class.getClassLoader().getResource("images/cog.png"));
        iconFolderNew = ImageIO.read(Main.class.getClassLoader().getResource("images/foldernew.png"));
        iconOpenFolder = ImageIO.read(Main.class.getClassLoader().getResource("images/folderopen.png"));
        iconOpenFile = ImageIO.read(Main.class.getClassLoader().getResource("images/fileopen.png"));
        iconCross = ImageIO.read(Main.class.getClassLoader().getResource("images/crossNocolor.png"));
        iconCrossHover = ImageIO.read(Main.class.getClassLoader().getResource("images/crossNocolorHover.png"));
        iconStartHover = ImageIO.read(Main.class.getClassLoader().getResource("images/startHover.png"));
        iconWrenchHover = ImageIO.read(Main.class.getClassLoader().getResource("images/settingsHover.png"));
        appIcon = ImageIO.read(Main.class.getClassLoader().getResource("images/icon.png"));
        iconFileYml = ImageIO.read(Main.class.getClassLoader().getResource("images/fileYml.png"));
        iconFileJson = ImageIO.read(Main.class.getClassLoader().getResource("images/fileJson.png"));
        iconFileReport = ImageIO.read(Main.class.getClassLoader().getResource("images/profileReport.png"));
        iconProfile = ImageIO.read(Main.class.getClassLoader().getResource("images/profile.png"));
        iconFold = ImageIO.read(Main.class.getClassLoader().getResource("images/fold.png"));
        iconUnfold = ImageIO.read(Main.class.getClassLoader().getResource("images/unfold.png"));
    }

}
