package com.example.demo.gui;

import com.example.demo.Message;

import java.awt.*;

public class Circle implements Figure {
    public final Color[] colorList = {
            Color.BLACK, Color.BLUE, Color.CYAN, Color.DARK_GRAY,
            Color.GRAY, Color.GREEN, Color.LIGHT_GRAY, Color.MAGENTA,
            Color.ORANGE, Color.PINK, Color.RED, Color.YELLOW,
            null
    };

    public int id, x, y, x2, y2, lineWidth, lineColorIdx, fillColorIdx;
    public long creationTime;

    public Circle(int id, int x, int y, int lineWidth, int lineColorIdx) {
        this.id = id;
        this.x = this.x2 = x;
        this.y = this.y2 = y;
        this.lineWidth = lineWidth;
        this.lineColorIdx = lineColorIdx;
        this.fillColorIdx = 12;
        this.creationTime = System.currentTimeMillis();
    }

    public Circle(int id, int x, int y, int x2, int y2, int lineWidth, int lineColorIdx, int fillColorIdx, long creationTime) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.x2 = x2;
        this.y2 = y2;
        this.lineWidth = lineWidth;
        this.lineColorIdx = lineColorIdx;
        this.fillColorIdx = fillColorIdx;
        this.creationTime = creationTime;
    }

    public void set(int x, int y, int x2, int y2, int lineWidth, int lineColorIdx, int fillColorIdx, long creationTime) {
        this.x = x;
        this.y = y;
        this.x2 = x2;
        this.y2 = y2;
        this.lineWidth = lineWidth;
        this.lineColorIdx = lineColorIdx;
        this.fillColorIdx = fillColorIdx;
        this.creationTime = creationTime;
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
        int minX = Math.min(x, x2);
        int minY = Math.min(y, y2);
        int width = Math.abs(x2 - x);
        int height = Math.abs(y2 - y);

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setStroke(new BasicStroke(lineWidth));

        if (colorList[fillColorIdx] != null) {
            g.setColor(colorList[fillColorIdx]);
            g.fillOval(minX, minY, width, height);
        }
        g.setColor(colorList[lineColorIdx]);
        g.drawOval(minX, minY, width, height);
    }

    @Override
    public void setEndPoint(Point p) {
        this.x2 = p.x;
        this.y2 = p.y;
    }

    @Override
    public void setLineWidth(int lineWidth) {
        this.lineWidth = lineWidth;
    }

    @Override
    public void setLineColor(int lineColorIdx) {
        this.lineColorIdx = lineColorIdx;
    }

    @Override
    public void setFillColor(int fillColorIdx) {
        if (this.fillColorIdx == fillColorIdx)
            this.fillColorIdx = 12;
        else
            this.fillColorIdx = fillColorIdx;
    }

    @Override
    public boolean contains(Point p) {
        int minX = Math.min(x, x2);
        int minY = Math.min(y, y2);
        int width = Math.abs(x2 - x);
        int height = Math.abs(y2 - y);
        int centerX = minX + width / 2;
        int centerY = minY + height / 2;
        int a = width / 2;
        int b = height / 2;
        return Math.pow((p.x - centerX) / (double) a, 2) + Math.pow((p.y - centerY) / (double) b, 2) <= 1;
    }

    @Override
    public int compareTo(Figure other) {
        return Long.compare(this.getCreationTime(), other.getCreationTime());
    }

    @Override
    public Message getMessage() {
        return Message.figure(0, creationTime, id, lineWidth, fillColorIdx, lineColorIdx, x, y, x2, y2);
    }

    @Override
    public String getInfo() {
        StringBuilder ret = new StringBuilder("");
        ret.append("1_");
        ret.append(creationTime);
        ret.append("_");
        ret.append(lineWidth);
        ret.append("_");
        ret.append(fillColorIdx);
        ret.append("_");
        ret.append(lineColorIdx);
        ret.append("_");
        ret.append(x);
        ret.append("_");
        ret.append(y);
        ret.append("_");
        ret.append(x2);
        ret.append("_");
        ret.append(y2);
        return ret.toString();
    }
}
