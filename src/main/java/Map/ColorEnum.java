package Map;

import java.awt.*;

public enum ColorEnum {
    WHITE(255, 255, 255),
    BLACK(0, 0, 0),
    GREEN(0, 255, 0),
    RED(255, 0, 0),
    BLUE(0, 0, 255),
    PURPLE(255, 0, 255),
    YELLOW(255, 255, 0);

    private final int r;
    private final int g;
    private final int b;

    ColorEnum(final int r, final int g, final int b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public ColorEnum getColor() {
        return this;
    }

    public Color getRGBColor() {
        return new Color(r, g, b);
    }
}
