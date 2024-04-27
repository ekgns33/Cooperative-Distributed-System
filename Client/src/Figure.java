import java.awt.*;

public interface Figure extends Comparable<Figure>{
    long getCreationTime();
    void draw(Graphics g);
    void setEndPoint(Point p);
    void setFillColor(Color fillColor);
    void setLineColor(Color lineColor);
    boolean contains(Point p);
}