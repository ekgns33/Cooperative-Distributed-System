package com.example.demo.gui;

import com.example.demo.Message;

import java.awt.*;

public class Line implements Figure {
    public final Color[] colorList = {
            Color.BLACK, Color.BLUE, Color.CYAN, Color.DARK_GRAY,
            Color.GRAY, Color.GREEN, Color.LIGHT_GRAY, Color.MAGENTA,
            Color.ORANGE, Color.PINK, Color.RED, Color.YELLOW,
            null
    };

    public int id, x, y, x2, y2, lineWidth, lineColorIdx;
    public long creationTime;

    public Line(int id, int x, int y, int lineWidth, int lineColorIdx) {
        this.id = id;
        this.x = this.x2 = x;
        this.y = this.y2 = y;
        this.lineWidth = lineWidth;
        this.lineColorIdx = lineColorIdx;
        this.creationTime = System.currentTimeMillis();
    }

    public Line(int id, int x, int y, int x2, int y2, int lineWidth, int lineColorIdx, long creationTime) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.x2 = x2;
        this.y2 = y2;
        this.lineWidth = lineWidth;
        this.lineColorIdx = lineColorIdx;
        this.creationTime = creationTime;
    }

    public void set(int x, int y, int x2, int y2, int lineWidth, int lineColorIdx, long creationTime) {
        this.x = x;
        this.y = y;
        this.x2 = x2;
        this.y2 = y2;
        this.lineWidth = lineWidth;
        this.lineColorIdx = lineColorIdx;
        this.creationTime = creationTime;
    }

    public long getCreationTime() {
        return creationTime;
    }

    @Override
    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setStroke(new BasicStroke(lineWidth));

        g.setColor(colorList[lineColorIdx]);
        g.drawLine(x, y, x2, y2);
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
        // Do nothing
    }

    public boolean contains(Point p) {
        if (p.x < Math.min(x, x2) || p.x > Math.max(x, x2) || p.y < Math.min(y, y2) || p.y > Math.max(y, y2))
            return false;
        double distance = Math.abs((x2 - x) * (y - p.y) - (x - p.x) * (y2 - y)) / Math.sqrt(Math.pow(x2 - x, 2) + Math.pow(y2 - y, 2));
        return distance <= lineWidth;
    }

    @Override
    public int compareTo(Figure other) {
        return Long.compare(this.getCreationTime(), other.getCreationTime());
    }

    @Override
    public Message getMessage() {
        return Message.figure(2, creationTime, id, lineWidth, 0, lineColorIdx, x, y, x2, y2);
    }

    @Override
    public String getInfo() {
        StringBuilder ret  = new StringBuilder("");
        ret.append("3_");
        ret.append(creationTime);
        ret.append("_");
        ret.append(lineWidth);
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

