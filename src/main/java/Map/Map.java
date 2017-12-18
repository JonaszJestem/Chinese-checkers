package Map;

import java.util.HashSet;

public abstract class Map {
    HashSet<Field> fieldList = new HashSet<>();
    int fieldSize = 20;

    public HashSet<Field> getFieldList() {
        return fieldList;
    }

    public void setFieldList(HashSet<Field> newFieldList) {
        this.fieldList.clear();
        newFieldList.forEach((f) -> this.fieldList.add(f));
    }

    public int getFieldSize() {
        return fieldSize;
    }

    public abstract void buildWithPlayers(int maxPlayers);

    public abstract ColorEnum getColor();
}
