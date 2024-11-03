package Model;

import Server.Configurations;
import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.MyMazeGenerator;
import algorithms.search.Solution;

import java.util.Observable;
import java.util.Observer;

public class MyModel extends Observable implements IModel {

    private int [][]maze;
    private int rowChar;
    private int colChar;
    private Solution solution;

    public MyModel() {
        maze = null;
        rowChar = 0;
        colChar = 0;
    }

    public int getRowChar() {
        return rowChar;
    }

    public int getColChar() {
        return colChar;
    }

    public int[][] getMaze() {
        return maze;
    }
    /**
     * update the loctaion of the player, check if it can move, so it won't go on walls and not go out of the maze length
     * @param direction
     */
    public void updateCharacterLocation(int direction) {
    /*
        direction = 1 -> Up
        direction = 2 -> Down
        direction = 3 -> Left
        direction = 4 -> Right
     */

        switch (direction) {
            case 1: // Up
                if (rowChar > 0 && maze[rowChar - 1][colChar] != 1) {
                    rowChar--;
                }
                break;
            case 2: // Down
                if (rowChar < maze.length - 1 && maze[rowChar + 1][colChar] != 1) {
                    rowChar++;
                }
                break;
            case 3: // Left
                if (colChar > 0 && maze[rowChar][colChar - 1] != 1) {
                    colChar--;
                }
                break;
            case 4: // Right
                if (colChar < maze[0].length - 1 && maze[rowChar][colChar + 1] != 1) {
                    colChar++;
                }
                break;
        }
        if(rowChar == maze.length-1 && colChar == maze[0].length -1){
            setChanged();
            notifyObservers("Goal State");
        }
    }
    /**
     * add an observer to the model, so it will get update when there is change
     * @param o
     */
    @Override
    public void assignObserver(Observer o) {
        this.addObserver(o);
    }
    public int[][] generateRandomMaze(int row, int col) {
        MyMazeGenerator myMazeGenerator = new MyMazeGenerator();
        Maze maze1 = myMazeGenerator.generate(row, col);
        this.maze = maze1.getMaze();
        this.rowChar = 0;
        this.colChar = 0;
        setChanged();
        return this.maze;
    }

    /**
     * solve the maze and update it
     * @param
     */
    @Override
    public Solution solveMaze(Maze maze,String selectedSearching)
    {
        //update the configuration file with the selected value from the client
        Configurations con = Configurations.getInstance();
        con.setProp("mazeSearchingAlgorithm", selectedSearching);
        //Solving maze
        MyModelServerClient myModelServerClient= new MyModelServerClient();
        myModelServerClient.ServerSolvingMaze(maze);
        return myModelServerClient.solution;
    }
    @Override
    public Solution getSolution() {
        return this.solution;
    }
}
