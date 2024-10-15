package algorithms.search;

import java.util.ArrayList;
import java.util.List;
import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.Position;


public class SearchableMaze implements ISearchable {
    private Maze maze;
    private MazeState startState;
    private MazeState goalState;

    // Constructor that accepts a Maze object
    public SearchableMaze(Maze maze) {
        this.maze = maze;

        // Initialize start and goal states based on the Maze's start and goal positions
        Position startPos = maze.getStartPosition();
        Position goalPos = maze.getGoalPosition();

        this.startState = new MazeState("Start", startPos.getRow(), startPos.getCol());
        this.goalState = new MazeState("Goal", goalPos.getRow(), goalPos.getCol());
    }

    @Override
    public AState getStartState() {
        return startState;
    }

    @Override
    public AState getGoalState() {
        return goalState;
    }

    @Override
    public List<AState> getAllPossibleStates(AState currentState) {
        List<AState> possibleStates = new ArrayList<>();
        MazeState mazeState = (MazeState) currentState;
        int row = mazeState.getRow();
        int col = mazeState.getCol();

        // Possible directions (up, down, left, right)
        int[][] directions = {
                {-1, 0}, {1, 0}, {0, -1}, {0, 1}
        };

        // Check for diagonal moves (up-left, up-right, down-left, down-right)
        int[][] diagonalDirections = {
                {-1, -1}, {-1, 1}, {1, -1}, {1, 1}
        };

        // Check regular moves (up, down, left, right)
        for (int[] direction : directions) {
            int newRow = row + direction[0];
            int newCol = col + direction[1];
            if (isInBounds(newRow, newCol) && maze.getCell(newRow, newCol) == 0) {
                possibleStates.add(new MazeState("State(" + newRow + "," + newCol + ")", newRow, newCol));
            }
        }

        // Check diagonal moves
        for (int[] direction : diagonalDirections) {
            int newRow = row + direction[0];
            int newCol = col + direction[1];

            // Ensure diagonal movement is valid by checking adjacent cells
            if (isInBounds(newRow, newCol) && maze.getCell(newRow, newCol) == 0 &&
                    maze.getCell(row + direction[0], col) == 0 && maze.getCell(row, col + direction[1]) == 0) {
                possibleStates.add(new MazeState("State(" + newRow + "," + newCol + ")", newRow, newCol));
            }
        }

        return possibleStates;
    }

    // Check if a cell is within the maze's bounds
    private boolean isInBounds(int row, int col) {
        int[][] mazeArray = maze.getMaze();
        return row >= 0 && row < mazeArray.length && col >= 0 && col < mazeArray[0].length;
    }
}
