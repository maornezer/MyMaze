package test;
import algorithms.mazeGenerators.*;
import algorithms.search.*;
import java.util.List;

public class RunSearchOnMaze {
    public static void main(String[] args) {
        IMazeGenerator mg = new MyMazeGenerator();
        Maze maze = mg.generate(8, 100);

        maze.print();

        SearchableMaze searchableMaze = new SearchableMaze(maze);

        // Get the solution from solveProblem
        Solution solution1 = solveProblem(searchableMaze, new BreadthFirstSearch());
        printMazeWithSolution(maze, solution1);

        Solution solution2 = solveProblem(searchableMaze, new DepthFirstSearch());
        printMazeWithSolution(maze, solution2);

        Solution solution3 = solveProblem(searchableMaze, new BestFirstSearch());
        printMazeWithSolution(maze, solution3);

    }

    private static Solution solveProblem(ISearchable domain, ISearchingAlgorithm searcher) {
        Solution solution = searcher.solve(domain);
        System.out.println(String.format("'%s' algorithm - nodes evaluated: %s", searcher.getName(), searcher.getNumberOfNodesEvaluated()));
        return solution;
    }

    // Make printMazeWithSolution static to be called from main
    public static void printMazeWithSolution(Maze maze, Solution solution) {
        int[][] mazeArray = maze.getMaze();
        List<AState> solutionPath = solution.getSolutionPath();

        // Create a copy of the maze for printing the solution
        char[][] displayMaze = new char[mazeArray.length][mazeArray[0].length];

        // Initialize the display maze with the original maze values
        for (int i = 0; i < mazeArray.length; i++) {
            for (int j = 0; j < mazeArray[i].length; j++) {
                displayMaze[i][j] = mazeArray[i][j] == 1 ? '1' : '0'; // '1' for walls, '0' for empty spaces
            }
        }

        // Mark the solution path with '+'
        for (AState state : solutionPath) {
            if (state instanceof MazeState) {
                MazeState mazeState = (MazeState) state;
                displayMaze[mazeState.getRow()][mazeState.getCol()] = '█';

            }
        }

        // Mark the start and goal positions explicitly
        Position startPosition = maze.getStartPosition();
        Position goalPosition = maze.getGoalPosition();
        displayMaze[startPosition.getRow()][startPosition.getCol()] = 'S'; // Start position
        displayMaze[goalPosition.getRow()][goalPosition.getCol()] = 'E';   // End position

        // Print the maze with the solution path
        for (int i = 0; i < displayMaze.length; i++) {
            for (int j = 0; j < displayMaze[i].length; j++) {
                System.out.print(displayMaze[i][j] + " ");
            }
            System.out.println();
        }
    }
}

