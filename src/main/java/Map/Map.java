package Map;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public abstract class Map {
    final ConcurrentHashMap<Field, ColorEnum> fieldList = new ConcurrentHashMap<>();
    final int fieldSize = 30;
    final int width = 500;
    final int height = 510;
    final ArrayList<ColorEnum> availableColors = new ArrayList<>();

    public ConcurrentHashMap<Field, ColorEnum> getFieldList() {
        return fieldList;
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
}
