package me.tapeline.quailstudio;

import javafx.scene.control.ProgressBar;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class SplashScreen extends JWindow {

    public SplashScreen() throws IOException {
        JPanel panel = new JPanel();

        BufferedImage myPicture = ImageIO.read(Main.class.getClassLoader().getResource("splash.png"));
        // BufferedImage myPicture = ImageIO.read(new File("/home/tapeline/IdeaProjects/QuailStudioReforged/" +
                                                        // "src/main/resources/splash.png"));
        JLabel image = new JLabel(new ImageIcon(myPicture));
        panel.add(image);

        add(panel);
        panel.setBackground(new Color(0, 0, 0, 0));
        setBackground(new Color(0, 0, 0, 0));
        setSize(640, 365);
        setLocationRelativeTo(null);
        setVisible(true);
        requestFocus();
        requestFocusInWindow();
        toFront();
    }

}