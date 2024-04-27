import java.awt.*;

public class Rect implements Figure {
    public int x, y, x2, y2, strokeWidth;
    public Color lineColor, fillColor;
    public long creationTime;

    public Rect(int x, int y, int strokeWidth, Color lineColor) {
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
            g.fillRect(minX, minY, width, height);
        }
        g.setColor(lineColor);
        g.drawRect(minX, minY, width, height);
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
        Rectangle rectBounds = new Rectangle(Math.min(x, x2), Math.min(y, y2), Math.abs(x2 - x),  Math.abs(y2 - y));
        return rectBounds.contains(p);
    }

    @Override
    public int compareTo(Figure other) {
        return Long.compare(this.getCreationTime(), other.getCreationTime());
    }
}