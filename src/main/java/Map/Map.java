package Map;

import java.util.HashSet;

public abstract class Map {
    HashSet<Field> fieldList = new HashSet<>();
    int fieldSize = 20;

    public HashSet<Field> getFieldList() {
        return fieldList;
    }

    public void setFieldList(HashSet<Field> fieldList) {
        this.fieldList = fieldList;
    }

    public int getFieldSize() {
        return fieldSize;
    }
}
