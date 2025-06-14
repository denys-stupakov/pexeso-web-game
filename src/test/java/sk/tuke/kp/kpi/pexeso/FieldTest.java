package sk.tuke.kp.kpi.pexeso;

import org.junit.jupiter.api.Test;
import sk.tuke.kp.kpi.pexeso.core.Field;
import sk.tuke.kp.kpi.pexeso.core.LevelDifficulty;
import sk.tuke.kp.kpi.pexeso.core.Tile;
import sk.tuke.kp.kpi.pexeso.core.TileState;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FieldTest {

    private Field field;
    private int rowCount = 4;
    private int columnCount = 4;
    private int pairCount = (rowCount * columnCount) / 2; // Total pairs in a 4x4 board

    public FieldTest() {
        field = new Field(LevelDifficulty.HARD);
    }

    @Test
    public void testAllTilesHavePairs() {
        int[] pairCounts = new int[pairCount + 1];

        for (int row = 0; row < rowCount; row++) {
            for (int col = 0; col < columnCount; col++) {
                Tile tile = field.getTile(row, col);
                pairCounts[tile.getPairId()]++;
            }
        }

        for (int i = 1; i <= pairCount; i++) {
            assertEquals(2, pairCounts[i], "Each pair should occur exactly twice");
        }
    }

    @Test
    public void testInitialTileStates() {
        for (int row = 0; row < rowCount; row++) {
            for (int col = 0; col < columnCount; col++) {
                assertEquals(TileState.CLOSED, field.getTile(row, col), "All tiles should start hidden");
            }
        }
    }

    @Test
    public void testTileReveal() {
        field.openTile(0, 0);
        assertEquals(TileState.CLOSED, field.getTile(0, 0).getState(), "Tile should be revealed after revealTile()");
    }

    @Test
    public void testPairMatching() {
        Tile firstTile = field.getTile(0, 0);
        Tile matchingTile = findMatchingTile(firstTile.getPairId());

        field.openTile(0, 0);
        field.openTile(matchingTile.getRow(), matchingTile.getColumn());

        assertEquals(TileState.PAIRED, firstTile.getState(), "Should be marked as PAIRED");
        assertEquals(TileState.PAIRED, matchingTile.getState(), "Should be marked as PAIRED");
    }

    private Tile findMatchingTile(int pairId) {
        for (int row = 0; row < rowCount; row++) {
            for (int col = 0; col < columnCount; col++) {
                Tile tile = field.getTile(row, col);
                if (tile.getPairId() == pairId && tile.getState() == TileState.CLOSED) {
                    return tile;
                }
            }
        }
        throw new IllegalStateException("Matching tile not found");
    }
}