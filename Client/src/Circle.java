import java.awt.*;

public class Circle implements Figure {
    public int x, y, width, height;
    public Color lineColor, fillColor;

    public Circle(int x, int y, int endX, int endY, Color lineColor) {
        this.x = Math.min(x, endX);
        this.y = Math.min(y, endY);
        this.width = Math.abs(endX - x);
        this.height = Math.abs(endY - y);
        this.lineColor = lineColor;
        this.fillColor = null;
    }

    public void fill(Color fillColor) {
        if (this.fillColor != null)
            this.fillColor = null;
        else
            this.fillColor = fillColor;
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(fillColor);
        g.fillOval(x, y, width, height);
        g.setColor(lineColor);
        g.drawOval(x, y, width, height);
    }

    @Override
    public boolean contains(Point p) {
        int centerX = this.x + width / 2;
        int centerY = this.y + height / 2;
        int a = width / 2;
        int b = height / 2;
        return Math.pow((p.x - centerX) / (double) a, 2) + Math.pow((p.y - centerY) / (double) b, 2) <= 1;

    }
}
