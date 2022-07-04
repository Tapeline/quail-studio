package me.tapeline.quailstudio.utils;

import me.tapeline.quailstudio.Main;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;

public class Fonts {

    public static Font defaultFont;
    public static int fontSize = 18;

    public static void loadFonts() throws IOException, FontFormatException {
        InputStream fontStream = Main.class.getResourceAsStream("/fonts/JetBrainsMono-Regular.ttf");
        defaultFont = Font.createFont(Font.TRUETYPE_FONT, fontStream);
        defaultFont = defaultFont.deriveFont(Font.PLAIN, fontSize);
    }

    public static Font adapt(Font f) {
        return f.deriveFont(f.getStyle(), (int) Main.cfg.cfg.get("font-size"));
    }

}
