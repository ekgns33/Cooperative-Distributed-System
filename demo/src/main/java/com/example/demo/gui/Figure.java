package com.example.demo.gui;

import com.example.demo.Message;

import java.awt.*;

public interface Figure extends Comparable<Figure>{
    int getId();
    long getCreationTime();
    void draw(Graphics g);
    void setEndPoint(Point p);
    void setLineWidth(int width);
    void setLineColor(int lineColor);
    void setFillColor(int fillColor);
    boolean contains(Point p);
    Message getMessage();
}