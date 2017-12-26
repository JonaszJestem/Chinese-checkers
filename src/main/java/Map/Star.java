package Map;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

public class Star extends Map {
    HashSet<Field> fieldList = new HashSet<Field>();
    int fieldSize = 30;
    private int width;
    private int height;
    private ArrayList<ColorEnum> availableColors = new ArrayList<ColorEnum>();

    public Star(int height, int width) {
        this.height = height;
        this.width = width;
    }

    @Override
    public void buildWithPlayers(int maxPlayers) {
        System.out.println("Building with " + maxPlayers);
        if (maxPlayers == 6) {
            makeSixPlayers();
        } else if (maxPlayers == 4) {
            makeFourPlayers();
        } else if (maxPlayers == 3) {
            makeThreePlayers();
        } else if (maxPlayers == 2) {
            makeTwoPlayers();
        }
    }

    private void makeTwoPlayers() {
        Field f;
        for (int i = 0; i <= 13; i++) {
            for (int j = 0; j < i; j++) {
                f = new Field((width / 2) - i * fieldSize / 2 + j * fieldSize, height / fieldSize + i * fieldSize, fieldSize);
                if (i <= 4) {
                    f.setColor(ColorEnum.RED);
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

        availableColors.clear();
        availableColors.addAll(Arrays.asList(ColorEnum.RED, ColorEnum.GREEN));
    }

    private void makeThreePlayers() {
        Field f;
        for (int i = 0; i <= 13; i++) {
            for (int j = 0; j < i; j++) {
                f = new Field((width / 2) - i * fieldSize / 2 + j * fieldSize, height / fieldSize + i * fieldSize, fieldSize);
                if (i <= 4) {
                    f.setColor(ColorEnum.RED);
                } else if (i >= 10 && j <= i - 10) {
                    f.setColor(ColorEnum.BLUE);
                } else if (i >= 10 && j >= 9) {
                    f.setColor(ColorEnum.BLACK);
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
                f.setColor(ColorEnum.WHITE);
                fieldList.add(f);
            }
            row += fieldSize;
        }
        availableColors.clear();
        availableColors.addAll(Arrays.asList(ColorEnum.RED, ColorEnum.BLUE, ColorEnum.BLACK));
    }

    private void makeFourPlayers() {
        Field f;
        for (int i = 0; i <= 13; i++) {
            for (int j = 0; j < i; j++) {
                f = new Field((width / 2) - i * fieldSize / 2 + j * fieldSize, height / fieldSize + i * fieldSize, fieldSize);
                if (i >= 10 && j <= i - 10) {
                    f.setColor(ColorEnum.BLUE);
                } else if (i >= 10 && j >= 9) {
                    f.setColor(ColorEnum.BLACK);
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
                if (i >= 10 && i - j >= 10) {
                    f.setColor(ColorEnum.PURPLE);
                } else if (i >= 10 && i - j <= 4) {
                    f.setColor(ColorEnum.YELLOW);
                } else {
                    f.setColor(ColorEnum.WHITE);
                }
                fieldList.add(f);
            }
            row += fieldSize;
        }

        availableColors.clear();
        availableColors.addAll(Arrays.asList(ColorEnum.BLUE, ColorEnum.BLACK, ColorEnum.PURPLE, ColorEnum.YELLOW));
    }

    private void makeSixPlayers() {
        Field f;
        for (int i = 0; i <= 13; i++) {
            for (int j = 0; j < i; j++) {
                f = new Field((width / 2) - i * fieldSize / 2 + j * fieldSize, height / fieldSize + i * fieldSize, fieldSize);
                if (i <= 4) {
                    f.setColor(ColorEnum.RED);
                } else if (i >= 10 && j <= i - 10) {
                    f.setColor(ColorEnum.BLUE);
                } else if (i >= 10 && j >= 9) {
                    f.setColor(ColorEnum.BLACK);
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
                } else if (i >= 10 && i - j >= 10) {
                    f.setColor(ColorEnum.PURPLE);
                } else if (i >= 10 && i - j <= 4) {
                    f.setColor(ColorEnum.YELLOW);
                } else {
                    f.setColor(ColorEnum.WHITE);
                }
                fieldList.add(f);
            }
            row += fieldSize;
        }

        availableColors.clear();
        availableColors.addAll(Arrays.asList(ColorEnum.BLUE, ColorEnum.RED, ColorEnum.BLACK, ColorEnum.PURPLE, ColorEnum.YELLOW, ColorEnum.GREEN));
    }

    public ColorEnum getColor() {
        if (!availableColors.isEmpty()) {
            availableColors.remove(0);
            return availableColors.get(0);
        }
        return null;
    }

    public HashSet<Field> getFieldList() {
        return fieldList;
    }

    public int getFieldSize() {
        return fieldSize;
    }
}
