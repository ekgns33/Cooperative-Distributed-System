import java.awt.*;

public class Circle implements Figure {
    public int x, y, x2, y2, strokeWidth;
    public Color lineColor, fillColor;
    public long creationTime;

    public Circle(int x, int y, int strokeWidth, Color lineColor) {
        this.x = this.x2 = x;
        this.y = this.y2 = y;
        this.strokeWidth = strokeWidth;
        this.lineColor = lineColor;
        this.fillColor = null;
        this.creationTime = System.currentTimeMillis();
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
        g2d.setStroke(new BasicStroke(strokeWidth));

        if (fillColor != null) {
            g.setColor(fillColor);
            g.fillOval(minX, minY, width, height);
        }
        g.setColor(lineColor);
        g.drawOval(minX, minY, width, height);
    }

    @Override
    public void setEndPoint(Point p) {
        this.x2 = p.x;
        this.y2 = p.y;
    }

    @Override
    public void setFillColor(Color fillColor) {
        if (this.fillColor == fillColor)
            this.fillColor = null;
        else
            this.fillColor = fillColor;
    }

    @Override
    public void setLineColor(Color lineColor) {
        this.lineColor = lineColor;
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
}
