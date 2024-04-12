import java.awt.*;
import java.math.BigInteger;

public class Figure {
    enum FigureType {CIRCLE, FILLED_CIRCLE, RECTANGLE, FILLED_RECTANGLE, LINE, PENCIL, TEXT}
    FigureType type;
    boolean fillColor;
    Color drawColor;
    BigInteger id;
    float x, y, width, height, time, x2, y2;
    string text;
    int size;

}
