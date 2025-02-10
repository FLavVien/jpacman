package nl.tudelft.jpacman.board;

/**
 * A top-down view of a matrix of {@link Square}s.
 */
public class Board {

    private final Square[][] board;

    Board(Square[][] grid) {
        if (grid == null || !isValidGrid(grid)) {
            throw new IllegalArgumentException("Invalid board grid.");
        }
        this.board = deepCopy(grid);
    }

    private boolean isValidGrid(Square[][] grid) {
        for (Square[] row : grid) {
            for (Square square : row) {
                if (square == null) {
                    return false;
                }
            }
        }
        return true;
    }

    private Square[][] deepCopy(Square[][] grid) {
        int width = grid.length;
        int height = grid[0].length;
        Square[][] copy = new Square[width][height];
        for (int x = 0; x < width; x++) {
            System.arraycopy(grid[x], 0, copy[x], 0, height);
        }
        return copy;
    }

    public int getWidth() {
        return board.length;
    }

    public int getHeight() {
        return board[0].length;
    }

    public Square squareAt(int x, int y) {
        if (!withinBorders(x, y)) {
            throw new IndexOutOfBoundsException("Position out of board boundaries.");
        }
        return board[x][y];
    }

    public boolean withinBorders(int x, int y) {
        return x >= 0 && x < getWidth() && y >= 0 && y < getHeight();
    }
}
