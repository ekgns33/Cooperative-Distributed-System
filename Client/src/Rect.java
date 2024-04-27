import java.awt.*;

public class Rect implements Figure {
    public int x, y, x2, y2;
    public Color lineColor, fillColor;
    public long creationTime;

    public Rect(int x, int y, Color lineColor) {
        this.x = this.x2 = x;
        this.y = this.y2 = y;
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
        if(fillColor != null) {
            g.setColor(fillColor);
            g.fillRect(minX, minY, width, height);
        }
        g.setColor(lineColor);
        g.drawRect(minX, minY, width, height);
    }

    @Override
    public void setEndPoint(Point p){
        this.x2 = p.x;
        this.y2 = p.y;
    }

    public void fill(Color fillColor) {
        if (this.fillColor == fillColor)
            this.fillColor = null;
        else
            this.fillColor = fillColor;
    }

    @Override
    public boolean contains(Point p) {
        int minX = Math.min(x, x2);
        int minY = Math.min(y, y2);
        int width = Math.abs(x2 - x);
        int height = Math.abs(y2 - y);
        int curX = p.x - minX;
        int curY = p.y - minY;
        if(curX < 0 || width < curX)
            return false;
        return 0 <= curY && curY <= height;
    }

    @Override
    public int compareTo(Figure other) {
        return Long.compare(this.getCreationTime(), other.getCreationTime());
    }
}