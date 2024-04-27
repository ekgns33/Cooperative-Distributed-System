import java.awt.*;

public class Line implements Figure {
    public int x, y, x2, y2;
    public Color lineColor;
    public long creationTime;
    int strokeWidth;

    public Line(int x, int y, int strokeWidth, Color lineColor) {
        this.x = this.x2 = x;
        this.y = this.y2 = y;
        this.strokeWidth = strokeWidth;
        this.lineColor = lineColor;
        this.creationTime = System.currentTimeMillis();
    }


    public long getCreationTime() {
        return creationTime;
    }

    @Override
    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setStroke(new BasicStroke(strokeWidth));

        g.setColor(lineColor);
        g.drawLine(x, y, x2, y2);
    }

    @Override
    public void setEndPoint(Point p) {
        this.x2 = p.x;
        this.y2 = p.y;
    }

    @Override
    public void setFillColor(Color fillColor) {
        // Do nothing
    }

    @Override
    public void setLineColor(Color lineColor) {
        this.lineColor = lineColor;
    }

    public boolean contains(Point p) {
        if (p.x < Math.min(x, x2) || p.x > Math.max(x, x2) || p.y < Math.min(y, y2) || p.y > Math.max(y, y2))
            return false;
        double distance = Math.abs((x2 - x) * (y - p.y) - (x - p.x) * (y2 - y)) / Math.sqrt(Math.pow(x2 - x, 2) + Math.pow(y2 - y, 2));
        return distance <= strokeWidth;
    }

    @Override
    public int compareTo(Figure other) {
        return Long.compare(this.getCreationTime(), other.getCreationTime());
    }
}

