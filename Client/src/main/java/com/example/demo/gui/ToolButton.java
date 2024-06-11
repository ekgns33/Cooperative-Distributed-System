package com.example.demo.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class ToolButton extends JButton {
    Color color = Color.BLACK, beforeColor = Color.RED;
    Color baseColor = new Color(0xededed);
    //    Color baseColor = new Color(0xF5F5F5); // bg color
    Color pressColor = new Color(0xc6c6c6);
    Color rolloverColor = new Color(0xe2e2e2);
    Color selectedColor = new Color(0xd7d7d7);
    String imagePath = null;
    boolean selected = false;
    final int margin = 8;
    int type, lineWidth = 2;
    int tag = 0;

    public ToolButton(int type, Dimension size) {
        super();
        init(size);
        this.type = type;
        if (type == 5 || type == 6) {
            Timer timer = new Timer(1000, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    tag = (tag + 1) % 3;
                    repaint();
                }
            });
//            if (type == 6) {
//                try {
//                    Thread.sleep(200);
//                } catch (InterruptedException e) {
//                }
//            }
            timer.start();
        }
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
    }

    void setColor(Color color) {
        this.beforeColor = this.color;
        this.color = color;
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
        if (getModel().isPressed()) {
            g2d.setColor(pressColor);
        } else if (selected) {
            g2d.setColor(selectedColor);
        } else if (getModel().isRollover()) {
            g2d.setColor(rolloverColor);
        } else {
            g2d.setColor(baseColor);
        }
        g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
        g2d.setColor(Color.BLACK);
        if (type == 0) {
            g2d.setStroke(new BasicStroke(lineWidth));
            g2d.drawOval(margin, margin, getWidth() - 2 * margin, getHeight() - 2 * margin);
        } else if (type == 1) {
            g2d.setStroke(new BasicStroke(lineWidth));
            g2d.drawRect(margin, margin, getWidth() - 2 * margin, getHeight() - 2 * margin);
        } else if (type == 2) {
            g2d.setStroke(new BasicStroke(lineWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2d.drawLine(margin, margin, getWidth() - margin, getHeight() - margin);
        } else if (type == 3) {
            try {
                File fontFile = new File("./CactusClassicalSerif-Regular.ttf");
                Font customFont = Font.createFont(Font.TRUETYPE_FONT, fontFile);

                float fontSize = getWidth() - margin;
                Font sizedFont = customFont.deriveFont(fontSize);

                g2d.setFont(sizedFont);

                FontMetrics metrics = g2d.getFontMetrics(sizedFont);
                String text = "T";

                int stringWidth = metrics.stringWidth(text);
                int stringHeight = metrics.getHeight();

                int x = (getWidth() - stringWidth) / 2;
                int y = (getHeight() + stringHeight) / 2 - metrics.getDescent();
                g2d.drawString(text, x, y);

            } catch (FontFormatException | IOException e) {
                e.printStackTrace();
            }
        } else if (type == 4) {
            g2d.setStroke(new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2d.drawLine(margin, getHeight() / 4 + 1, getWidth() - margin, getHeight() / 4 + 1);
            g2d.setStroke(new BasicStroke(3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2d.drawLine(margin, getHeight() / 2 - 1, getWidth() - margin, getHeight() / 2 - 1);
            g2d.setStroke(new BasicStroke(4, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2d.drawLine(margin, getHeight() * 3 / 4 - 2, getWidth() - margin, getHeight() * 3 / 4 - 2);
        } else if (type == 5) {
            g2d.setStroke(new BasicStroke(lineWidth));
            if (tag == 0) {
                g2d.setColor(beforeColor);
            } else {
                g2d.setColor(color);
            }
            g2d.drawRect(margin, margin, getWidth() - 3 * margin, getHeight() - 3 * margin);
            if (tag == 1)
                g2d.setColor(beforeColor);
            g2d.drawOval(margin * 2, margin * 2, getWidth() - 3 * margin, getHeight() - 3 * margin);
        } else if (type == 6) {
            g2d.setStroke(new BasicStroke(lineWidth));
            if (tag == 0) {
                g2d.drawRect(margin, margin, getWidth() - 3 * margin, getHeight() - 3 * margin);
            } else {
                g2d.fillRect(margin - 1, margin -1 , getWidth() - 3 * margin + 2, getHeight() - 3 * margin + 2);
            }
            g2d.setColor(Color.RED);
            if (tag == 2) {
                g2d.fillOval(margin * 2 - 1, margin * 2 - 1, getWidth() - 3 * margin + 2, getHeight() - 3 * margin + 2);
            } else {
                g2d.drawOval(margin * 2, margin * 2, getWidth() - 3 * margin, getHeight() - 3 * margin);
            }
        } else if(type == 7){
            g2d.setStroke(new BasicStroke(lineWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2d.drawLine(margin + 2, getHeight() - 10, getWidth() - margin - 2, getHeight() - 10);
            g2d.drawLine(margin + 2, getHeight() - 14, margin + 2, getHeight() - 10);
            g2d.drawLine(getWidth() - margin - 2, getHeight() - 14, getWidth() - margin - 2, getHeight() - 10);
            g2d.drawLine(getWidth() / 2, margin + 2 , getWidth() / 2, getHeight() - 16);
            g2d.drawLine(getWidth() / 2, getHeight() - 16, getWidth() / 2 - 7, getHeight() - 23);
            g2d.drawLine(getWidth() / 2, getHeight() - 16, getWidth() / 2 + 7, getHeight() - 23);
        }else if (type == 8) {
            g2d.setStroke(new BasicStroke(lineWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2d.drawLine(margin + 2, getHeight() - 10, getWidth() - margin - 2, getHeight() - 10);
            g2d.drawLine(margin + 2, getHeight() - 14, margin + 2, getHeight() - 10);
            g2d.drawLine(getWidth() - margin - 2, getHeight() - 14, getWidth() - margin - 2, getHeight() - 10);
            g2d.drawLine(getWidth() / 2, margin + 2 , getWidth() / 2, getHeight() - 16);
            g2d.drawLine(getWidth() / 2, margin + 2, getWidth() / 2 - 7, margin + 9);
            g2d.drawLine(getWidth() / 2, margin + 2, getWidth() / 2 + 7, margin + 9);
        }

        g2d.dispose();
    }
}
