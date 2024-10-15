package algorithms.mazeGenerators;

import java.util.Random;

public class SimpleMazeGenerator extends AMazeGenerator
{
    /**
     * Generates a simple maze with the specified number of rows and columns.
     * @param rows the number of rows in the maze
     * @param cols the number of columns in the maze
     * @return the generated maze
     */
    @Override
    public Maze generate(int rows, int cols) {
        if ((rows <= 1) || (cols <= 1)) {
            throw new RuntimeException("The size of the rows and columns should be greater than 2");
        }
        Maze maze = new Maze(rows, cols);
        Random random = new Random();
        // Add random walls to the maze (with 1s)
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                maze.setCell(i, j, random.nextBoolean() ? 1 : 0);
            }
        }
        // Ensure the start position is open (0) and not a wall
        maze.setCell(0, 0, 0);
        // Ensure the goal position is open (0) and not a wall
        maze.setCell(rows - 1, cols - 1, 0);
        //Path from the start to the goal to guarantee resolvability
        createPath(maze, rows, cols);
        return maze;
    }
    private void createPath(Maze maze, int rows, int columns) {
        // Simple strategy to create a basic path from start to goal (for simplicity)
        for (int i = 0; i < rows; i++) {
            maze.setCell(i, 0, 0); // Clear the first column
        }
        for (int j = 0; j < columns; j++) {
            maze.setCell(rows - 1, j, 0); // Clear the bottom row
        }
    }
}
