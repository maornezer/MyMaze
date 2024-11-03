package ViewModel;
import algorithms.mazeGenerators.Maze;
import algorithms.search.Solution;
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

    /**
     * the binding between the row position of the player and the view
     * @return
     */
    public IntegerProperty playerRowProperty() {
        return playerRow;
    }
    /**
     * the binding between the col position of the player and the view
     * @return
     */
    public IntegerProperty playerColProperty() {
        return playerCol;
    }
    public void setPlayerRow(int playerRow) {
        this.playerRow.set(playerRow);
    }

    public void setPlayerCol(int playerCol) {
        this.playerCol.set(playerCol);
    }

    @Override
    public void update(Observable o, Object arg) {
        setChanged();
        notifyObservers(arg);
    }

    public void generateMaze(int row, int col) {
        this.maze = this.model.generateRandomMaze(row, col);
    }
    /**
     * the function get the key event that happened and change the key code to int and send it to the model,
     * update the position of the player and notify the observers of the ViewModel class
     * @param keyEvent
     */
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
        model.updateCharacterLocation(direction);
        setPlayerRow(model.getRowChar());
        setPlayerCol(model.getColChar());
        setChanged();
        notifyObservers("move player");
    }

    public Solution solveMaze(Maze maze,String selectedSearchingValue) {
        return model.solveMaze(maze,selectedSearchingValue);
    }

}
