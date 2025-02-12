package nl.tudelft.jpacman.board;

/**
 * A top-down view of a matrix of {@link Square}s.
 */
public class Board {


    /**
     * The grid of squares with board[x][y] being the square at column x, row y.
     */
    private final Square[][] squares;


    Board(Square[][] grid) {

        assert grid != null;
        this.squares = grid;
        assert invariant() : "Initial grid cannot contain null squares";
    }

    /**
     * Whatever happens, the squares on the board can't be null.
     * @return false if any square on the board is null.
     */
    protected final boolean invariant() {
        for (Square[] row : squares) {
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
        return this.squares.length;
    }

    public int getHeight() {
        return this.squares[0].length;
    }

    public Square squareAt(int x, int y) {

        assert withinBorders(x, y);
        Square result = this.squares[x][y];
        assert result != null : "Follows from invariant.";
        return result;

    }

    public boolean withinBorders(int x, int y) {
        return x >= 0 && x < getWidth() && y >= 0 && y < getHeight();
    }
}
