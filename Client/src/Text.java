import java.awt.*;

public class Text implements Figure {
    public int x, y, x2, y2, fontSize;
    public Color lineColor;
    public long creationTime;
    public boolean isDrawing;
    public String text;
    private FontMetrics fontMetrics;

    public Text(int x, int y, Color lineColor) {
        this.x = this.x2 = x;
        this.y = this.y2 = y;
        this.fontSize = 0;
        this.lineColor = lineColor;
        this.creationTime = System.currentTimeMillis();
        this.isDrawing = true;
        this.text = "Sample Text";
    }

    @Override
    public long getCreationTime() {
        return creationTime;
    }

    @Override
    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        Font font = g.getFont().deriveFont((float) fontSize);
        g.setFont(font);
        fontMetrics = g.getFontMetrics(font);
        if (isDrawing) {
            g.setColor(Color.BLACK);
            g2d.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{4}, 0));
            g.drawRect(Math.min(x, x2), Math.min(y, y2), Math.abs(x2 - x), fontSize);
        }
        g.setColor(lineColor);
        g2d.setStroke(new BasicStroke(1));
        g.drawString(text, Math.min(x, x2), Math.max(y, y2));
    }

    @Override
    public void setEndPoint(Point p) {
        this.x2 = p.x;
        this.y2 = p.y;
        fontSize = Math.abs(y - y2);
    }

    @Override
    public void setFillColor(Color fillColor) {
        // Do nothing
    }

    @Override
    public void setLineColor(Color lineColor) {
        this.lineColor = lineColor;
    }

    @Override
    public boolean contains(Point p) {
        int textWidth = fontMetrics.stringWidth(text);
        Rectangle textBounds = new Rectangle(Math.min(x, x2), Math.min(y, y2), textWidth, fontSize);
        return textBounds.contains(p);
    }

    @Override
    public int compareTo(Figure other) {
        return Long.compare(this.getCreationTime(), other.getCreationTime());
    }

    public void setText(String text) {
        this.text = text;
    }
}
