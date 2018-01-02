package Map;

import java.awt.*;
import java.util.Arrays;

public class Star extends Map {

    public Star(){
        Field f;
        for (int i = 0; i <= 13; i++) {
            for (int j = 0; j < i; j++) {
                f = new Field((width / 2) - i * fieldSize / 2 + j * fieldSize, height / fieldSize + i * fieldSize);
                fieldList.put(f, ColorEnum.WHITE);
            }
        }

        int row = fieldSize * 5;
        for (int i = 13; i > 0; i--) {
            for (int j = 0; j < i; j++) {
                f = new Field(width / 2 - i * fieldSize / 2 + j * fieldSize, height / fieldSize + row);
                fieldList.put(f, ColorEnum.WHITE);
            }
            row += fieldSize;
        }
    }

    @Override
    public void buildWithPlayers(int maxPlayers) {
        System.out.println("Building with " + maxPlayers);
        this.maxPlayers = maxPlayers;
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
        drawRed();
        drawGreen();
        availableColors.clear();
        availableColors.addAll(Arrays.asList(ColorEnum.RED, ColorEnum.GREEN));
    }

    private void makeThreePlayers() {
        drawRed();
        drawBlue();
        drawBlack();
        availableColors.clear();
        availableColors.addAll(Arrays.asList(ColorEnum.RED, ColorEnum.BLUE, ColorEnum.BLACK));
    }

    private void makeFourPlayers() {
        drawBlue();
        drawBlack();
        drawPurple();
        drawYellow();
        availableColors.clear();
        availableColors.addAll(Arrays.asList(ColorEnum.BLUE, ColorEnum.BLACK, ColorEnum.PURPLE, ColorEnum.YELLOW));
    }

    private void makeSixPlayers() {
        drawBlue();
        drawRed();
        drawBlack();
        drawPurple();
        drawYellow();
        drawGreen();
        availableColors.clear();
        availableColors.addAll(Arrays.asList(ColorEnum.BLUE, ColorEnum.RED, ColorEnum.BLACK, ColorEnum.PURPLE, ColorEnum.YELLOW, ColorEnum.GREEN));
    }

    @Override
    public void drawColor(ColorEnum colorEnum){
        if(colorEnum == ColorEnum.BLACK) drawBlack();
        else if(colorEnum == ColorEnum.BLUE) drawBlue();
        else if(colorEnum == ColorEnum.RED) drawRed();
        else if(colorEnum == ColorEnum.GREEN) drawGreen();
        else if(colorEnum == ColorEnum.YELLOW) drawYellow();
        else if(colorEnum == ColorEnum.PURPLE) drawPurple();
    }

    private void drawRed(){
        Field f;
        for (int i = 0; i <= 13; i++) {
            for (int j = 0; j < i; j++) {
                if (i <= 4) {
                    f = new Field((width / 2) - i * fieldSize / 2 + j * fieldSize, height / fieldSize + i * fieldSize);
                    f.setEndPoint(ColorEnum.GREEN);
                    fieldList.put(f, ColorEnum.RED);

                }
            }
        }
    }

    private void drawGreen() {
        Field f;
        int row = fieldSize * 5;
        for (int i = 13; i > 0; i--) {
            for (int j = 0; j < i; j++) {
                if (i <= 4) {
                    f = new Field(width / 2 - i * fieldSize / 2 + j * fieldSize, height / fieldSize + row);
                    f.setEndPoint(ColorEnum.RED);
                    fieldList.put(f, ColorEnum.GREEN);
                }
            }
            row += fieldSize;
        }
    }

    private void drawBlue(){
        Field f;
        for (int i = 0; i <= 13; i++) {
            for (int j = 0; j < i; j++) {
                if (i >= 10 && j <= i - 10) {
                    f = new Field((width / 2) - i * fieldSize / 2 + j * fieldSize, height / fieldSize + i * fieldSize, fieldSize);
                    f.setEndPoint(ColorEnum.YELLOW);
                    fieldList.put(f, ColorEnum.BLUE);
                }
            }
        }
    }

    private void drawBlack(){
        Field f;
        for (int i = 0; i <= 13; i++) {
            for (int j = 0; j < i; j++) {
                if (i >= 10 && j >= 9) {
                    f = new Field((width / 2) - i * fieldSize / 2 + j * fieldSize, height / fieldSize + i * fieldSize, fieldSize);
                    f.setEndPoint(ColorEnum.PURPLE);
                    fieldList.put(f, ColorEnum.BLACK);
                }
            }
        }
    }

    private void drawYellow(){
        Field f;
        int row = fieldSize * 5;
        for (int i = 13; i > 0; i--) {
            for (int j = 0; j < i; j++) {
                if (i >= 10 && j >= 9) {
                    f = new Field(width / 2 - i * fieldSize / 2 + j * fieldSize, height / fieldSize + row, fieldSize);
                    f.setEndPoint(ColorEnum.BLUE);
                    fieldList.put(f, ColorEnum.YELLOW);
                }
            }
            row += fieldSize;
        }
    }

    private void drawPurple(){
        Field f;
        int row = fieldSize * 5;
        for (int i = 13; i > 0; i--) {
            for (int j = 0; j < i; j++) {
                if (i >= 10 && i - j >= 10) {
                    f = new Field(width / 2 - i * fieldSize / 2 + j * fieldSize, height / fieldSize + row, fieldSize);
                    f.setEndPoint(ColorEnum.BLACK);
                    fieldList.put(f, ColorEnum.PURPLE);
                }
            }
            row += fieldSize;
        }
    }
}