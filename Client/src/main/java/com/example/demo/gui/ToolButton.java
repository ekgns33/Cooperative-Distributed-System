package com.example.demo.gui;

import javax.swing.*;
import java.awt.*;

public class ToolButton extends JButton {
    Color baseColor = new Color(0xededed);
    //    Color baseColor = new Color(0xF5F5F5); // bg color
    Color pressColor = new Color(0xcbcbcb);
    Color rolloverColor = new Color(0xe2e2e2);
    Color selectedColor = new Color(0xd7d7d7);
    String imagePath = null;
    boolean selected = false;
    final int margin = 10;
    int type, lineWidth = 1;

    public ToolButton(int type, Dimension size) {
        super();
        init(size);
        this.type = type;
    }

    void init(Dimension size) {
        setFocusPainted(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setOpaque(false);
        setPreferredSize(size);
        setMaximumSize(size);
        setMinimumSize(size);
    }

    void setLineWidth(int lineWidth) {
        this.lineWidth = lineWidth;
        repaint();
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
        g2d.setColor(Color.black);
        if (type == 0) {
            g2d.setStroke(new BasicStroke(lineWidth));
            g2d.drawOval(margin, margin, getWidth() - 2 * margin, getHeight() - 2 * margin);
        } else if (type == 1) {
            g2d.setStroke(new BasicStroke(lineWidth));
            g2d.drawRect(margin, margin, getWidth() - 2 * margin, getHeight() - 2 * margin);
        } else if (type == 2) {
            g2d.setStroke(new BasicStroke(lineWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2d.drawLine(margin, margin, getWidth() - margin, getHeight() - margin);
        }


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
