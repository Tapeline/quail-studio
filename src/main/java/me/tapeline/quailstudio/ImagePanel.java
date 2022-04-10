package me.tapeline.quailstudio;

import javax.swing.*;
import java.awt.*;

public class ImagePanel extends JPanel {

    private Image img;

    private boolean keepRatio = false;
    private boolean original = false;
    private boolean fitToWidth = false;

    private final Color defaultColor = new Color(0, 0, 0, 0);

    private int offsetY = 0;

    public ImagePanel(Image img) {
        this.img = img;
    }

    public void setOffsetY(int offsetY) {
        this.offsetY = offsetY;
    }

    public void fitToImage() {
        Dimension size = new Dimension(img.getWidth(null), img.getHeight(null));
        setPreferredSize(size);
        setMinimumSize(size);
        setMaximumSize(size);
        setSize(size);
    }

    public void setImage(Image img) {
        this.img = img;
        repaint();
    }

    public void setRenderOriginal(boolean flag) {
        this.original = flag;
    }

    public void setKeepRatio(boolean flag) {
        this.keepRatio = flag;
    }

    public void setFitToWidth(boolean fitToWidth) {
        this.fitToWidth = fitToWidth;
    }

    @Override
    public void paintComponent(Graphics g) {
        g.setColor(defaultColor);
        g.fillRect(0, 0, getSize().width, getSize().height);

        if (img != null) {
            if (original)
                g.drawImage(img, 0, offsetY, this);
            else if (fitToWidth)
                g.drawImage(img, 0, offsetY, getSize().width,
                        (int) ((float) getSize().width * ((float) img.getHeight(this) / (float) img.getWidth(this))),
                        this);
            else if (!keepRatio)
                g.drawImage(img, 0, offsetY, getSize().width, getSize().height, this);

        }
    }

}