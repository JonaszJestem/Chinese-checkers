package Map;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public abstract class Map {
    protected ConcurrentHashMap<Field, ColorEnum> fieldList = new ConcurrentHashMap<>();
    protected int fieldSize = 30;
    protected int width = 500;
    protected int height = 510;
    protected ArrayList<ColorEnum> availableColors = new ArrayList<ColorEnum>();
    protected int maxPlayers;

    public ConcurrentHashMap<Field, ColorEnum> getFieldList() {
        return fieldList;
    }

    public void setFieldList(ConcurrentHashMap<Field, ColorEnum> newFieldList) {
        newFieldList.forEach((k, v) -> this.fieldList.put(k, v));
    }

    public int getFieldSize() {
        return fieldSize;
    }

    public abstract void buildWithPlayers(int maxPlayers);

    public ColorEnum getColor() {
        if (!availableColors.isEmpty()) {
            ColorEnum toRet = availableColors.get(0);
            availableColors.remove(0);
            return toRet;
        }
        return null;
    }

    public abstract void drawColor(ColorEnum colorEnum);
}
