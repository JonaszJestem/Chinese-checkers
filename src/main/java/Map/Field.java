package Map;

import java.awt.geom.Ellipse2D;

public class Field extends Ellipse2D.Double {
    //public static int id = 0;
    public int x_int;
    public int y_int;
    private int size = 30;

    public Field(int x, int y) {
        this.x_int = x;
        this.y_int = y;
        this.x = (double) x;
        this.y = (double) y;
        this.height = size;
        this.width = size;
        //id++;
    }

    public Field(int x, int y, int size) {
        this.x_int = x;
        this.y_int = y;
        this.x = (double) x;
        this.y = (double) y;
        this.height = size;
        this.width = size;
        this.size = size;
        //id++;
    }

    @Override
    public String toString() {
        return "(" + x_int + "," + y_int + ")";
    }

    @Override
    public int hashCode() {
        StringBuffer sb = new StringBuffer();
        sb.append(x_int);
        sb.append(y_int);
        return Integer.parseInt(sb.toString());
    }
}
