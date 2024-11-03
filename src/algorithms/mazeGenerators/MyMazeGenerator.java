package algorithms.mazeGenerators;
import java.util.ArrayList;


public class MyMazeGenerator extends AMazeGenerator {

    @Override
    public Maze generate(int rows, int columns) {
        if ((rows <= 1) || (columns <= 1)) {
            throw new RuntimeException("The size of the rows and columns should be greater than 2");
        }

        // Create a new maze instance
        Maze maze = new Maze(rows, columns);

        // Initialize maze with walls (1)
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                maze.setCell(i, j, 1); // Set all cells to walls
            }
        }

        // Start position
        maze.setCell(0, 0, 0); // Mark start as path

        ArrayList<Position> frontVal1 = new ArrayList<>();
        Position startPosition = new Position(0, 0);
        Position[] startNeighbors = findLegalNeighbors(startPosition, rows, columns);

        // Add the legal neighbors of the start position
        for (Position neighbor : startNeighbors) {
            if (neighbor != null && maze.getCell(neighbor.getRow(), neighbor.getCol()) == 1) {
                frontVal1.add(neighbor);
            }
        }

        // Prim's Algorithm to generate the maze
        while (!frontVal1.isEmpty()) {
            int randomPick = (int) (Math.random() * (frontVal1.size()));
            Position randFront = frontVal1.get(randomPick);
            ArrayList<Position> backVal0 = new ArrayList<>();
            Position[] randFrontNeighbors = findLegalNeighbors(randFront, rows, columns);

            // Add neighbors to frontVal1 and find a legal back neighbor
            for (Position neighbor : randFrontNeighbors) {
                if (neighbor != null) {
                    if (maze.getCell(neighbor.getRow(), neighbor.getCol()) == 0) {
                        backVal0.add(neighbor);
                    } else if (maze.getCell(neighbor.getRow(), neighbor.getCol()) == 1) {
                        frontVal1.add(neighbor);
                    }
                }
            }

            // Choose a random legal back neighbor and update the maze
            if (!backVal0.isEmpty()) {
                int randomNei = (int) (Math.random() * (backVal0.size()));
                Position randBack = backVal0.get(randomNei);
                updatePositionsVal(randFront, randBack, maze);
            }
            frontVal1.remove(randFront);
        }

        // Finalize the maze by ensuring there's a path to the goal
        FinishMaze(maze);

        // Mark the goal position
        maze.setCell(rows - 1, columns - 1, 0);

        return maze;
    }

    // Finds the legal neighbors of a given position
    public static Position[] findLegalNeighbors(Position position, int rows, int columns) {
        Position[] poseArr = new Position[4];
        int neighborsCounter = 0;

        if (position.getRow() - 2 >= 0) {
            poseArr[neighborsCounter++] = new Position(position.getRow() - 2, position.getCol()); // upper
        }
        if (position.getRow() + 2 < rows) {
            poseArr[neighborsCounter++] = new Position(position.getRow() + 2, position.getCol()); // lower
        }
        if (position.getCol() + 2 < columns) {
            poseArr[neighborsCounter++] = new Position(position.getRow(), position.getCol() + 2); // right
        }
        if (position.getCol() - 2 >= 0) {
            poseArr[neighborsCounter] = new Position(position.getRow(), position.getCol() - 2); // left
        }
        return poseArr;
    }

    // Update the values of two positions: back and the position between front and back
    private void updatePositionsVal(Position front, Position back, Maze maze) {
        maze.setCell(front.getRow(), front.getCol(), 0);
        if (front.getRow() == back.getRow()) {
            maze.setCell(front.getRow(), (front.getCol() + back.getCol()) / 2, 0);
        }
        if (front.getCol() == back.getCol()) {
            maze.setCell((front.getRow() + back.getRow()) / 2, front.getCol(), 0);
        }
    }

    // Ensures there is a valid path to the goal
    private void FinishMaze(Maze maze) {
        int rows = maze.getMaze().length;
        int cols = maze.getMaze()[0].length;

        if (rows % 2 == 0 && cols % 2 == 0) { // even even
            for (int i = 0; i < rows - 1; i++) {
                maze.setCell(i, cols - 1, (int) (Math.random() * 2));
            }
            for (int j = 0; j < cols - 1; j++) {
                maze.setCell(rows - 1, j, (int) (Math.random() * 2));
            }
            maze.setCell(rows - 2, cols - 1, 0);
        }
        // Additional handling for odd/even combinations can be added here
    }

    // Generate a 2x2 maze
    public Maze generate2on2() {
        Maze maze = new Maze(2, 2);
        int randomizeMaze = (int) (Math.random() * 2);
        if (randomizeMaze == 0) {
            maze.setCell(1, 0, 1);
        } else {
            maze.setCell(0, 1, 1);
        }
        return maze;
    }
}
