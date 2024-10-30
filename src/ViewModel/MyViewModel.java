package ViewModel;

import Model.IModel;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.input.KeyEvent;

import java.util.Observable;
import java.util.Observer;

public class MyViewModel extends Observable implements Observer {
    private IntegerProperty playerRow = new SimpleIntegerProperty();
    private IntegerProperty playerCol = new SimpleIntegerProperty();
    private IModel model;
    private int[][] maze;
    private int rowChar;
    private int colChar;


    public MyViewModel(IModel model) {
        this.model = model;
        this.model.assignObserver(this);
        this.maze = null;
    }

    public int getPlayerRow() {
        return playerRow.get();
    }

    public int getPlayerCol() {
        return playerCol.get();
    }

    public int[][] getMaze() {
        return maze;
    }


    public int getRowChar() {
        return rowChar;
    }

    public int getColChar() {
        return colChar;
    }

    //    @Override
//    public void update(Observable o, Object arg) {
//        if(o instanceof IModel)
//        {
//            if(maze == null)//generateMaze
//            {
//                this.maze = model.getMaze();
//            }
//            else {
//                int[][] maze = model.getMaze();
//
//                if (maze == this.maze)//Not generateMaze
//                {
//                    int rowChar = model.getRowChar();
//                    int colChar = model.getColChar();
//                    if(this.colChar == colChar && this.rowChar == rowChar)//Solve Maze
//                    {
//                        model.getSolution();
//                    }
//                    else//Update location
//                    {
//                        this.rowChar = rowChar;
//                        this.colChar = colChar;
//                    }
//
//
//                }
//                else//GenerateMaze
//                {
//                    this.maze = maze;
//                }
//            }
//
//            setChanged();
//            notifyObservers();
//        }
//    }
    @Override
    public void update(Observable o, Object arg) {
        setChanged();
        notifyObservers(arg);
    }

    public void generateMaze(int row, int col) {

        this.maze = this.model.generateRandomMaze(row, col);
    }

    public void moveCharacter(KeyEvent keyEvent) {
        int direction = -1;

        switch (keyEvent.getCode()) {
            case UP:
                direction = 1;
                break;
            case DOWN:
                direction = 2;
                break;
            case LEFT:
                direction = 3;
                break;
            case RIGHT:
                direction = 4;
                break;
        }
        model.updateCharacterLocation2(direction);
        setPlayerRow(model.getRowChar());
        setPlayerCol(model.getColChar());
        setChanged();
        System.out.println("i am here MyViewModel - moveChar ");
        notifyObservers("move player");
    }

    public void setPlayerRow(int playerRow) {
        this.playerRow.set(playerRow);
    }

    public void setPlayerCol(int playerCol) {
        this.playerCol.set(playerCol);
    }

    public void solveMaze(int[][] maze) {
        model.solveMaze(maze);
    }

    public void getSolution() {
        model.getSolution();
    }

    public IntegerProperty playerRowProperty() {
        return playerRow;
    }

    public IntegerProperty playerColProperty() {
        return playerCol;

    }
}
