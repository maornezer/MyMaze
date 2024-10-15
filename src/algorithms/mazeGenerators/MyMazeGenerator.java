//package algorithms.mazeGenerators;
//
//import java.util.Random;
//import java.util.ArrayList;
//import java.util.List;
//
//public class MyMazeGenerator extends AMazeGenerator {
//
//    @Override
//    public Maze generate(int rows, int cols) {
//        if (rows <= 1 || cols <= 1) {
//            throw new RuntimeException("The maze size must be greater than 1x1.");
//        }
//
//        // Create a new maze instance
//        Maze maze = new Maze(rows, cols);
//
//        // Initialize the maze with walls (1)
//        for (int i = 0; i < rows; i++) {
//            for (int j = 0; j < cols; j++) {
//                maze.setCell(i, j, 1); // Set all cells to walls
//            }
//        }
//
//        // Generate the maze using Prim's algorithm (or any other algorithm)
//        generateMazeUsingPrimsAlgorithm(maze, rows, cols);
//
//        // Ensure a path exists from start to goal
//        //createPath(maze, rows, cols);
//
//        // Return the generated maze
//        return maze;
//    }
//
//    // Prim's algorithm or any other maze generation algorithm
//    private void generateMazeUsingPrimsAlgorithm(Maze maze, int rows, int cols) {
//        Random random = new Random();
//        List<int[]> walls = new ArrayList<>();
//
//        // Start from a random cell
////        int startX = random.nextInt(rows);
////        int startY = random.nextInt(cols);
//        int startX = 0;
//        int startY = 0;
//        maze.setCell(startX, startY, 0); // Mark it as a path
//
//        // Add walls of the starting cell to the wall list
//        addWalls(startX, startY, walls, rows, cols, maze);
//
//        // Process walls until there are none left
//        while (!walls.isEmpty()) {
//            int[] wall = walls.remove(random.nextInt(walls.size()));
//            int x = wall[0];
//            int y = wall[1];
//
//            // Check if the wall can be converted into a path
//            if (canBePath(x, y, maze, rows, cols)) {
//                maze.setCell(x, y, 0); // Mark it as a path
//                addWalls(x, y, walls, rows, cols, maze);
//            }
//        }
//    }
//
//    // Function to add walls around a given cell
//    private void addWalls(int x, int y, List<int[]> walls, int rows, int cols, Maze maze) {
//        if (x > 1 && maze.getCell(x - 2, y) == 1) walls.add(new int[]{x - 1, y});
//        if (x < rows - 2 && maze.getCell(x + 2, y) == 1) walls.add(new int[]{x + 1, y});
//        if (y > 1 && maze.getCell(x, y - 2) == 1) walls.add(new int[]{x, y - 1});
//        if (y < cols - 2 && maze.getCell(x, y + 2) == 1) walls.add(new int[]{x, y + 1});
//    }
//
//    // Check if the wall can be converted into a path
//    private boolean canBePath(int x, int y, Maze maze, int rows, int cols) {
//        int adjacentPaths = 0;
//        if (x > 0 && maze.getCell(x - 1, y) == 0) adjacentPaths++;
//        if (x < rows - 1 && maze.getCell(x + 1, y) == 0) adjacentPaths++;
//        if (y > 0 && maze.getCell(x, y - 1) == 0) adjacentPaths++;
//        if (y < cols - 1 && maze.getCell(x, y + 1) == 0) adjacentPaths++;
//
//        // Only convert the wall if it has exactly one adjacent path
//        return adjacentPaths == 1;
//    }
//
//    // Function to ensure there is a guaranteed path from start to goal
//    private void createPath(Maze maze, int rows, int cols) {
//        // Ensure the first column has a path from the top to the bottom
//        for (int i = 0; i < rows; i++) {
//            maze.setCell(i, 0, 0); // Clear the first column as a path
//        }
//
//        // Ensure the bottom row has a path from the left to the right
//        for (int j = 0; j < cols; j++) {
//            maze.setCell(rows - 1, j, 0); // Clear the bottom row as a path
//        }
//    }
//}
package algorithms.mazeGenerators;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MyMazeGenerator extends AMazeGenerator {

    @Override
    public Maze generate(int rows, int columns) {
        if ((rows <= 1) || (columns <= 1)) {
            throw new RuntimeException("The size of the rows and columns should be greater than 2");
        }

        // Handle the special case of a 2x2 maze
//        if(rows == 2 && columns == 2){
//            return generate2on2();
//        }

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
