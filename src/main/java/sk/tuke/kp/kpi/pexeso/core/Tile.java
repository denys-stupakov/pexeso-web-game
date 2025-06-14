package sk.tuke.kp.kpi.pexeso.core;

public class Tile {
    private final int pairId;
    private final int row;
    private final int column;
    private TileState state = TileState.CLOSED;
    private String imagePath;

    public Tile(String imagePath, int pairId, int row, int column) {
        this.imagePath = imagePath;
        this.pairId = pairId;
        this.row = row;
        this.column = column;
    }

    public int getPairId() {
        return pairId;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public TileState getState() {
        return state;
    }

    void setState(TileState state) {
        this.state = state;
    }

    public String getImagePath() {
        return imagePath;
    }

    void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
