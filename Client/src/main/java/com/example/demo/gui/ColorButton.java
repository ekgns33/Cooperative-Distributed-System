package com.example.demo.gui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ColorButton extends JButton {
    Color color, pressColor, rolloverColor;
    Color selectedColor = new Color(0xd7d7d7);
    String imagePath = null;
    boolean selected = false;
    final int thickness = 5;

    public ColorButton(Color color) {
        super();
        this.color = color;
        this.pressColor = mixBlack(color, 0.3);
        this.rolloverColor = mixWhite(color, 0.3);
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
            g2d.fillOval(0, 0, getWidth(), getHeight());
        }

        if (getModel().isPressed()) {
            g2d.setColor(pressColor);
        } else if (getModel().isRollover()) {
            g2d.setColor(rolloverColor);
        } else {
            g2d.setColor(color);
        }
        g2d.fillOval(thickness, thickness, getWidth() - 2 * thickness, getHeight() - 2 * thickness);


        if (imagePath != null) {
            File dir = new File(imagePath);
            try {
                BufferedImage img = ImageIO.read(dir);
                g2d.drawImage(img, 0, 0, getWidth(), getHeight(), this);
            } catch (IOException e) {
                System.err.println("Image Path Err");
            }
        }

        g2d.dispose();
    }

    public static Color mixBlack(Color color, double ratio) {
        int red = (int) (color.getRed() * (1 - ratio));
        int green = (int) (color.getGreen() * (1 - ratio));
        int blue = (int) (color.getBlue() * (1 - ratio));

        return new Color(red, green, blue);
    }

    public static Color mixWhite(Color color, double ratio) {
        int red = (int) (color.getRed() + ratio * (255 - color.getRed()));
        int green = (int) (color.getGreen() + ratio * (255 - color.getGreen()));
        int blue = (int) (color.getBlue() + ratio * (255 - color.getBlue()));

        return new Color(red, green, blue);
    }
}

//      setBorder(BorderFactory.createLineBorder(Color.WHITE, 3));

