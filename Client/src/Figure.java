import java.awt.*;

public interface Figure {
    void draw(Graphics g);
    boolean contains(Point p);
}