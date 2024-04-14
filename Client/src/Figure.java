import java.awt.*;

public interface Figure {
    void draw(Graphics g);
    void setEndPoint(Point p);
    boolean contains(Point p);
}