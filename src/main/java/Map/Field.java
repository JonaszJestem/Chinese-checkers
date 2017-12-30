package Map;

import java.awt.*;
import java.awt.geom.Ellipse2D;

public class Field extends Ellipse2D.Double {
    private static int fieldsCounter = 0;
    private int id;
    public int x_int;
    public int y_int;
    private int size = 30;
    public ColorEnum endPoint = ColorEnum.WHITE;

    public Field(int x, int y) {
        this.x_int = x;
        this.y_int = y;
        this.x = (double) x;
        this.y = (double) y;
        this.height = size;
        this.width = size;
        this.id = fieldsCounter;
        fieldsCounter++;
    }

    Field(int x, int y, int size) {
        this.x_int = x;
        this.y_int = y;
        this.x = (double) x;
        this.y = (double) y;
        this.height = size;
        this.width = size;
        this.size = size;
        this.id = fieldsCounter;
        fieldsCounter++;
    }

    public void setEndPoint(ColorEnum colorEnum){
        this.endPoint = colorEnum;
    }

    public boolean isEndPoint(ColorEnum colorEnum){
        return this.endPoint == colorEnum;
    }

    @Override
    public String toString() {
        return "(" + x_int + "," + y_int + ")";
    }

    @Override
    public int hashCode() {
        return Integer.parseInt(String.valueOf(x_int) + String.valueOf(y_int));
    }

    public int getId() {
        return id;
    }
}
