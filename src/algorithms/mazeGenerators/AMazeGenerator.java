package algorithms.mazeGenerators;

public abstract class AMazeGenerator implements IMazeGenerator
{
    /**
     * Measures the time taken to generate a maze with the specified number of rows and columns.
     * @param rows the number of rows in the maze
     * @param columns the number of columns in the maze
     * @return the time taken to generate the maze in milliseconds
     */
    @Override
    public long measureAlgorithmTimeMillis(int rows, int columns) {
        if ((rows <= 1) || (columns <= 1)) {
            throw new RuntimeException("The size of the rows and columns should be greater than 2");
        }
        long S_time = System.currentTimeMillis();
        this.generate(rows, columns);
        long E_time = System.currentTimeMillis();
        return E_time - S_time;
    }
    @Override
    public abstract Maze generate(int rows, int columns);
}
