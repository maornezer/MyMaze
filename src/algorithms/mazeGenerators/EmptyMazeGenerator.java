package algorithms.mazeGenerators;
/**
 * EmptyMazeGenerator is a maze generator that creates an empty maze.
 * All cells in the maze are set to 0 (open path).
 */
public class EmptyMazeGenerator  extends AMazeGenerator
{
    public Maze generate(int rows, int cols)
    {
        if ((rows <= 1) || (cols <= 1)) {
            throw new RuntimeException("The size of the rows and columns should be greater than 2");
        }
        return new Maze(rows,cols);
    }
}
