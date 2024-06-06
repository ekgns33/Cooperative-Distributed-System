package com.example.demo.gui;

import javax.swing.*;
import java.awt.*;

public class ToolButton extends JButton {
    Color baseColor = new Color(0xededed);
    Color pressColor= new Color(0xcbcbcb);
    Color rolloverColor = new Color(0xe2e2e2);
    Color selectedColor = new Color(0xd7d7d7);
    String imagePath = null;
    boolean selected = false;

    public ToolButton() {
        super();
        init();
    }

    void init() {
        setFocusPainted(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setOpaque(false);
    }

    void toggle() {
        this.selected = !this.selected;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        if (selected) {
            g2d.setColor(selectedColor);
        } else if (getModel().isPressed()) {
            g2d.setColor(pressColor);
        } else if (getModel().isRollover()) {
            g2d.setColor(rolloverColor);
        } else {
            g2d.setColor(baseColor);
        }
        g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);

//        if (getModel().isPressed()) {
//            g2d.setColor(pressColor);
//        } else if (getModel().isRollover()) {
//            g2d.setColor(rolloverColor);
//        }else {
//            g2d.setColor(color);
//        }
//        g2d.fillOval(thickness, thickness, getWidth() - 2 * thickness, getHeight() - 2 * thickness);
//
//
//        if (imagePath != null) {
//            File dir = new File(imagePath);
//            try {
//                BufferedImage img = ImageIO.read(dir);
//                g2d.drawImage(img, 0, 0, getWidth(), getHeight(), this);
//            } catch (IOException e) {
//                System.err.println("Image Path Err");
//            }
//        }

        g2d.dispose();
    }
}
