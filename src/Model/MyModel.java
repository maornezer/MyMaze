package Model;

import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.MyMazeGenerator;
import algorithms.search.ISearchable;
import algorithms.search.SearchableMaze;

import java.util.Observable;
import java.util.Observer;
import java.util.Random;

public class MyModel extends Observable implements IModel {
    private boolean[][] points;
    private int [][]maze;
    private int rowChar;
    private int colChar;
    public ISearchable iSearchable;

    public MyModel() {
        maze = null;
        rowChar =0;
        colChar =0;

    }
    /////////
    public void updateCharacterLocation(int direction)
    {
        /*
            direction = 1 -> Up
            direction = 2 -> Down
            direction = 3 -> Left
            direction = 4 -> Right
         */

        switch(direction)
        {
            case 1: //Up
                //if(rowChar!=0)
                rowChar--;
                break;

            case 2: //Down
                //  if(rowChar!=maze.length-1)
                rowChar++;
                break;
            case 3: //Left
                //  if(colChar!=0)
                colChar--;
                break;
            case 4: //Right
                //  if(colChar!=maze[0].length-1)
                colChar++;
                break;

        }

        setChanged();
        notifyObservers("move player");
    }
    ////////////
    public void updateCharacterLocation2(int direction) {
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

//                setChanged();
//                notifyObservers("move player");
        }
    }

    public int getRowChar() {
        return rowChar;
    }

    public int getColChar() {
        return colChar;
    }

    @Override
    public void assignObserver(Observer o) {
        this.addObserver(o);
    }

    @Override
    public void solveMaze(int[][] maze) {
        //Solving maze
        setChanged();
        notifyObservers("maze solved");
    }

    @Override
    public void getSolution() {
        //return this.solution;
    }


//    public void generateRandomMaze(int row, int col)
//    {
//        MyMazeGenerator myMazeGenerator = new MyMazeGenerator();
//        Maze maze1 = myMazeGenerator.generate(row, col);
//        this.maze = maze1.getMaze();
//        setChanged();
////        notifyObservers("maze generated");
//    }
//
//    public int[][] getMaze() {
//        return maze;
//    }
    public int[][] generateRandomMaze(int row, int col)
    {
        MyMazeGenerator myMazeGenerator = new MyMazeGenerator();
        Maze maze1 = myMazeGenerator.generate(row, col);

//        points = new boolean[row][col];
//        for (int i = 0; i < row; i++) {
//            for (int j = 0; j < col; j++) {
//                if (maze[i][j] == 0) {
//                    points[i][j] = true; // יש נקודה בתא פנוי
//                }
//            }
//        }
        this.maze = maze1.getMaze();
        setChanged();
        //notifyObservers("maze generated");
        return this.maze;
    }

    public int[][] getMaze() {
        return maze;
    }
}
