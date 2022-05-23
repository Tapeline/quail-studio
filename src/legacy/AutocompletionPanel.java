package me.tapeline.quailstudio;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.util.List;

public class AutocompletionPanel {

    private RSyntaxTextArea area;
    private List<String> variants;
    private List<JButton> btns;
    public JPanel panel;
    public JWindow panelWindow;
    public String token;

    public AutocompletionPanel(List<String> variants, RSyntaxTextArea area, JFrame main, String token) {
        super();
        this.token = token;
        panel = new JPanel();
        GridLayout gridLayout = new GridLayout(variants.size(), 1);
        panel.setLayout(gridLayout);
        panel.setOpaque(true);
        for (String v : variants) {
            JButton btn = new JButton(v);
            btn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    area.insert(v.substring(token.length()), area.getCaretPosition());
                    panelWindow.dispose();
                }
            });
            btn.setOpaque(true);
            btn.setBorderPainted(false);
            panel.add(btn);
        }
        this.area = area;
        this.variants = variants;
        panelWindow = new JWindow(main);
        panelWindow.getContentPane().add(panel);
        panelWindow.setMinimumSize(new Dimension(panel.getWidth(), 30));
        panelWindow.setSize(300, variants.size()*48);
        panelWindow.setVisible(true);
        int windowX = area.getCaret().getMagicCaretPosition().x;
        int windowY = area.getCaret().getMagicCaretPosition().y;
        panelWindow.setLocationRelativeTo(area);
        panelWindow.setLocation(panelWindow.getX() - (area.getWidth() / 2) + windowX + panelWindow.getWidth() / 2,
                panelWindow.getY() - (area.getHeight() / 2) + windowY + panelWindow.getHeight() / 2 +
                    area.getFont().getSize());
        if (area.getY() + area.getHeight() < panelWindow.getY() + panelWindow.getHeight()) {
            panelWindow.setLocation(panelWindow.getX(),
                    panelWindow.getY() - panelWindow.getHeight() - area.getFont().getSize());
        }
        panelWindow.setAlwaysOnTop(true);
        panelWindow.requestFocus();
        panelWindow.requestFocusInWindow();
        panelWindow.revalidate();
        panelWindow.repaint();
        panelWindow.addWindowFocusListener(new WindowFocusListener() {

            @Override
            public void windowGainedFocus(WindowEvent e) {

            }

            @Override
            public void windowLostFocus(WindowEvent e) {
                e.getWindow().dispose();
            }
        });
    }


}
