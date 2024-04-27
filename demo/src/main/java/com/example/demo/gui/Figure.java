package com.example.demo.gui;

import java.awt.*;

public interface Figure extends Comparable<Figure>{
    long getCreationTime();
    void draw(Graphics g);
    void setEndPoint(Point p);
    void setLineWidth(int width);
    void setLineColor(Color lineColor);
    void setFillColor(Color fillColor);
    boolean contains(Point p);
}