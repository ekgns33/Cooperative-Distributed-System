package com.example.demo.gui;

import com.example.demo.Message;

import java.awt.*;

public class Text implements Figure {
    public final Color[] colorList = {
            Color.BLACK, Color.GRAY,
            Color.BLUE, Color.CYAN,
            Color.GREEN, Color.YELLOW,
            Color.ORANGE, Color.PINK,
            Color.MAGENTA, Color.RED,
            null
    };

    public int id, x, y, x2, y2, fontSize, lineColorIdx;
    public long creationTime;
    public String text;
    private FontMetrics fontMetrics;

    public Text(int id, int x, int y, int lineColor) {
        this.id = id;
        this.x = this.x2 = x;
        this.y = this.y2 = y;
        this.fontSize = 0;
        this.lineColorIdx = lineColor;
        this.creationTime = System.currentTimeMillis();
        this.text = "Sample Text";
    }

    public Text(int id, int x, int y, int x2, int y2, int fontSize, int lineColorIdx, long creationTime, String text) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.x2 = x2;
        this.y2 = y2;
        this.fontSize = fontSize;
        this.lineColorIdx = lineColorIdx;
        this.creationTime = creationTime;
        this.text = text;
    }

    public void set(int x, int y, int x2, int y2, int fontSize, int lineColorIdx, long creationTime, String text) {
        this.x = x;
        this.y = y;
        this.x2 = x2;
        this.y2 = y2;
        this.fontSize = fontSize;
        this.lineColorIdx = lineColorIdx;
        this.creationTime = creationTime;
        this.text = text;
    }

    @Override
    public int getID() {
        return this.id;
    }

    @Override
    public long getCreationTime() {
        return creationTime;
    }

    @Override
    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        Font font = g.getFont().deriveFont((float) fontSize);
        g.setFont(font);
        fontMetrics = g.getFontMetrics(font);
        g.setColor(colorList[lineColorIdx]);
        g2d.setStroke(new BasicStroke(1));
        g.drawString(text, Math.min(x, x2), Math.max(y, y2));
    }

    @Override
    public void setEndPoint(Point p) {
        this.x2 = p.x;
        this.y2 = p.y;
        fontSize = Math.abs(y - y2);
    }

    @Override
    public void setLineWidth(int lineWidth) {
        // Do nothing
    }


    @Override
    public void setLineColor(int lineColorIdx) {
        this.lineColorIdx = lineColorIdx;
    }

    @Override
    public void setFillColor(int fillColorIdx) {
        // Do nothing
    }

    @Override
    public boolean contains(Point p) {
        int textWidth = fontMetrics.stringWidth(text);
        Rectangle textBounds = new Rectangle(Math.min(x, x2), Math.min(y, y2), textWidth, fontSize);
        return textBounds.contains(p);
    }

    @Override
    public int compareTo(Figure other) {
        return Long.compare(this.getCreationTime(), other.getCreationTime());
    }

    @Override
    public Message getMessage() {
        return Message.text(3, creationTime, id, fontSize, 0, lineColorIdx, x, y, x2, y2, text);
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String getInfo() {
        StringBuilder ret  = new StringBuilder("");
        ret.append("3_");
        ret.append(creationTime);
        ret.append("_");
        ret.append(fontSize);
        ret.append("_0_");
        ret.append(lineColorIdx);
        ret.append("_");
        ret.append(x);
        ret.append("_");
        ret.append(y);
        ret.append("_");
        ret.append(x2);
        ret.append("_");
        ret.append(y2);
        ret.append("_");
        ret.append(text);
        return ret.toString();
    }
}
