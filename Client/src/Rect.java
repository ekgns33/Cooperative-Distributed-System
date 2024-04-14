import java.awt.*;

public class Rect implements Figure {
    public int x, y, width, height;
    public Color lineColor, fillColor;

    public Rect(int x, int y, int endX, int endY, Color lineColor) {
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
        g.fillRect(x, y, width, height);
        g.setColor(lineColor);
        g.drawRect(x, y, width, height);
    }

    @Override
    public boolean contains(Point p) {
        int curX = p.x - this.x;
        int curY = p.y - this.y;
        if(curX < 0 || this.width < curX)
            return false;
        return 0 <= curY && curY <= this.height;
    }
}

