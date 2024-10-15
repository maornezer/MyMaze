package algorithms.search;

import java.util.Objects;

public class MazeState extends AState
{
    private int row;  // Row position in the maze
    private int col;  // Column position in the maze

    // Constructor
    public MazeState(String state, int row, int col) {
        super(state);
        this.row = row;
        this.col = col;
    }

    // Returns the row position
    public int getRow() {
        return row;
    }

    // Returns the column position
    public int getCol() {
        return col;
    }

    // Custom toString method to include coordinates
    @Override
    public String toString() {
        return "MazeState{" + "row=" + row + ", col=" + col + '}';
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        MazeState other = (MazeState) obj;
        return this.row == other.row && this.col == other.col;  // Compare rows and columns
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, col);  // Hash based on row and column
    }
}
