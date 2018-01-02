package Map;

import java.util.Arrays;

public class Star extends Map {

    @Override
    public void buildWithPlayers(int maxPlayers) {
        System.out.println("Building with " + maxPlayers);
        switch (maxPlayers) {
            case 6:
                makeSixPlayers();
                break;
            case 4:
                makeFourPlayers();
                break;
            case 3:
                makeThreePlayers();
                break;
            case 2:
                makeTwoPlayers();
                break;
        }
    }

    private void makeTwoPlayers() {
        Field f;
        for (int i = 0; i <= 13; i++) {
            for (int j = 0; j < i; j++) {
                f = new Field((width / 2) - i * fieldSize / 2 + j * fieldSize, height / fieldSize + i * fieldSize);
                if (i <= 4) {
                    fieldList.put(f, ColorEnum.RED);
                } else {
                    fieldList.put(f, ColorEnum.WHITE);
                }
            }
        }

        int row = fieldSize * 5;
        for (int i = 13; i > 0; i--) {
            for (int j = 0; j < i; j++) {
                f = new Field(width / 2 - i * fieldSize / 2 + j * fieldSize, height / fieldSize + row);
                if (i <= 4) {
                    fieldList.put(f, ColorEnum.GREEN);
                } else {
                    fieldList.put(f, ColorEnum.WHITE);
                }
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
                    fieldList.put(f, ColorEnum.RED);
                } else if (i >= 10 && j <= i - 10) {
                    fieldList.put(f, ColorEnum.BLUE);
                } else if (i >= 10 && j >= 9) {
                    fieldList.put(f, ColorEnum.BLACK);
                } else {
                    fieldList.put(f, ColorEnum.WHITE);
                }
            }
        }

        int row = fieldSize * 5;
        for (int i = 13; i > 0; i--) {
            for (int j = 0; j < i; j++) {
                f = new Field(width / 2 - i * fieldSize / 2 + j * fieldSize, height / fieldSize + row, fieldSize);
                fieldList.put(f, ColorEnum.WHITE);
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
                    fieldList.put(f, ColorEnum.BLUE);
                } else if (i >= 10 && j >= 9) {
                    fieldList.put(f, ColorEnum.BLACK);
                } else {
                    fieldList.put(f, ColorEnum.WHITE);
                }
            }
        }

        int row = fieldSize * 5;
        for (int i = 13; i > 0; i--) {
            for (int j = 0; j < i; j++) {
                f = new Field(width / 2 - i * fieldSize / 2 + j * fieldSize, height / fieldSize + row, fieldSize);
                if (i >= 10 && i - j >= 10) {
                    fieldList.put(f, ColorEnum.PURPLE);
                } else if (i >= 10 && i - j <= 4) {
                    fieldList.put(f, ColorEnum.YELLOW);
                } else {
                    fieldList.put(f, ColorEnum.WHITE);
                }
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
                    fieldList.put(f, ColorEnum.RED);
                } else if (i >= 10 && j <= i - 10) {
                    fieldList.put(f, ColorEnum.BLUE);
                } else if (i >= 10 && j >= 9) {
                    fieldList.put(f, ColorEnum.BLACK);
                } else {
                    fieldList.put(f, ColorEnum.WHITE);
                }
            }
        }

        int row = fieldSize * 5;
        for (int i = 13; i > 0; i--) {
            for (int j = 0; j < i; j++) {
                f = new Field(width / 2 - i * fieldSize / 2 + j * fieldSize, height / fieldSize + row, fieldSize);
                if (i <= 4) {
                    fieldList.put(f, ColorEnum.GREEN);
                } else if (i >= 10 && i - j >= 10) {
                    fieldList.put(f, ColorEnum.PURPLE);
                } else if (i >= 10 && i - j <= 4) {
                    fieldList.put(f, ColorEnum.YELLOW);
                } else {
                    fieldList.put(f, ColorEnum.WHITE);
                }
            }
            row += fieldSize;
        }

        availableColors.clear();
        availableColors.addAll(Arrays.asList(ColorEnum.BLUE, ColorEnum.RED, ColorEnum.BLACK, ColorEnum.PURPLE, ColorEnum.YELLOW, ColorEnum.GREEN));
    }

}
