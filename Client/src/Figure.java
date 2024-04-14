import java.awt.*;

public interface Figure extends Comparable<Figure>{
    long getCreationTime();
    void draw(Graphics g);
    void setEndPoint(Point p);
    void fill(Color fillColor);
    boolean contains(Point p);
}