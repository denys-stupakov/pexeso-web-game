package sk.tuke.kp.kpi.pexeso.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Field {
    private final Tile[][] tiles;
    private final int rowCount;
    private final int columnCount;
    private Tile lastOpenedTile;
    private Tile pairTile;
    private FieldState fieldState = FieldState.PLAYING;
    private int countPairedTiles = 0;
    private int score = 0;
    private static Field lastField = null;
    private static Random rand = new Random();
    private int lives = 3;

    public Field(LevelDifficulty levelDifficulty) {
        int bound = 5;
        switch(levelDifficulty) {
            case EASY -> bound = 6;
            case MEDIUM -> bound = 7;
            case HARD -> bound = 8;
        }
        int rowCount = rand.nextInt(bound);
        while (rowCount == 0) rowCount = rand.nextInt(bound);
        rowCount = rowCount + (rowCount % 2);
        if(rowCount == 2) rowCount = 4;
        int columnCount = rowCount;

        this.rowCount = rowCount;
        this.columnCount = columnCount;
        lastOpenedTile = null;
        pairTile = null;

        tiles = new Tile[rowCount][columnCount];

        generate();
    }

    private void generate() {
        Random rand = new Random();
        int imageCount = (rowCount * columnCount) / 2;

        List<String> images = new ArrayList<>();
        for (int i = 1; i <= imageCount; i++) {
            images.add(i + ".png");
            images.add(i + ".png");
        }

        Collections.shuffle(images, rand);

        int index = 0;
        for (int row = 0; row < rowCount; row++) {
            for (int column = 0; column < columnCount; column++) {
                tiles[row][column] = new Tile(images.get(index), Integer.parseInt(images.get(index).replace(".png", "")), row, column);
                index++;
            }
        }
    }

    public int getRowCount() {
        return rowCount;
    }

    public int getColumnCount() {
        return columnCount;
    }

    public Tile getTile(int row, int column) {
        if(row < rowCount && column < columnCount)
            return tiles[row][column];
        return null;
    }

    public void openTile(int row, int column) {
        if (pairTile != null) {
            pairTile.setState(TileState.CLOSED);
            lastOpenedTile.setState(TileState.CLOSED);
            pairTile = null;
            lastOpenedTile = null;
            return;
        } else if (lastOpenedTile == null &&
                tiles[row][column].getState() == TileState.CLOSED) {
            lastOpenedTile = tiles[row][column];
            lastOpenedTile.setState(TileState.OPENED);
            return;
        } else if (lastOpenedTile != null &&
                tiles[row][column].getState() == TileState.CLOSED &&
                lastOpenedTile.getImagePath().equals(tiles[row][column].getImagePath())) {
            lastOpenedTile.setState(TileState.PAIRED);
            pairTile = tiles[row][column];
            pairTile.setState(TileState.PAIRED);
            lastOpenedTile = null;
            pairTile = null;
            this.countPairedTiles++;
            return;
        }

        if (tiles[row][column].getState() == TileState.CLOSED) {
            tiles[row][column].setState(TileState.OPENED);
            pairTile = tiles[row][column];
        }

        lives--;
        this.score += 1;
    }

    public void revealAllTemporarily() {
        for (int row = 0; row < rowCount; row++) {
            for (int col = 0; col < columnCount; col++) {
                Tile tile = tiles[row][col];
                if (tile.getState() == TileState.CLOSED) {
                    tile.setState(TileState.OPENED);
                }
            }
        }
    }

    public void hideUnpairedTiles() {
        for (int row = 0; row < rowCount; row++) {
            for (int col = 0; col < columnCount; col++) {
                Tile tile = tiles[row][col];
                if (tile.getState() == TileState.OPENED) {
                    tile.setState(TileState.CLOSED);
                }
            }
        }
    }

    public int getLives() {
        return lives;
    }

    public boolean isGameOver() {
        return lives <= 0;
    }

    public int getScore() { return score; }

    public FieldState getFieldState() {
        return fieldState;
    }

    public void setFieldState(FieldState fieldState) {
        this.fieldState = fieldState;
    }

    public int getCountPairedTiles() {
        return countPairedTiles;
    }

    public boolean checkVictory() {
        return countPairedTiles == rowCount * columnCount / 2;
    }
}