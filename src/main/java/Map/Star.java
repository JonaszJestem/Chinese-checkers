package Map;

import java.util.HashSet;

public class Star extends Map {
    HashSet<Field> fieldList = new HashSet<Field>();
    int fieldSize = 20;

    public Star(int height, int width) {

        Field f;
        for (int i = 0; i <= 13; i++) {
            for (int j = 0; j < i; j++) {
                f = new Field((width / 2) - i * fieldSize / 2 + j * fieldSize, height / fieldSize + i * fieldSize, fieldSize);
                if (i <= 4) {
                    f.setColor(ColorEnum.RED);
                } else if (i >= 10 && i <= 13 && j <= i - 10) {
                    f.setColor(ColorEnum.BLUE);
                } else {
                    f.setColor(ColorEnum.WHITE);
                }
                fieldList.add(f);
            }
        }

        int row = fieldSize * 5;
        for (int i = 13; i > 0; i--) {
            for (int j = 0; j < i; j++) {
                f = new Field(width / 2 - i * fieldSize / 2 + j * fieldSize, height / fieldSize + row, fieldSize);
                if (i <= 4) {
                    f.setColor(ColorEnum.GREEN);
                } else {
                    f.setColor(ColorEnum.WHITE);
                }
                fieldList.add(f);
            }
            row += fieldSize;
        }
    }

    public HashSet<Field> getFieldList() {
        return fieldList;
    }

    public int getFieldSize() {
        return fieldSize;
    }
}
