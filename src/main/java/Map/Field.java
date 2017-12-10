package Map;

import java.awt.*;
import java.awt.geom.Ellipse2D;

public class Field extends Ellipse2D.Double {
    private ColorEnum color;

    public Field(double x, double y, double size) {
        this.x = x;
        this.y = y;
        this.height = size;
        this.width = size;
    }

    public ColorEnum getColor() {
        return this.color;
    }

    public void setColor(ColorEnum color) {
        this.color = color;
    }

    public Color getRGBColor() {
        return this.color.getRGBColor();
    }
}
