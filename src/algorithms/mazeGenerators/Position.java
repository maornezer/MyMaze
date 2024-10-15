package algorithms.mazeGenerators;


//Position represents a specific location in a maze with row and column indices.
public class Position {

    private int row;
    private int col;
    /**
     * Constructs a Position with the specified row and column indices.
     * @param row the row index of the position
     * @param col the column index of the position
     * @throws IllegalArgumentException if the row or column indices are negative
     */
    public Position(int row, int col) {
        if (row < 0 || col < 0) {
            throw new IllegalArgumentException("Row and column indices must be non-negative.");
        }
        this.row = row;
        this.col = col;
    }

    public int getRow() {return row;}

    public int getCol() {return col;}

    @Override
    public String toString() {
        return "{" + row + "," + col + "}";
    }

}