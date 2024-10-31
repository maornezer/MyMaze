package Model;

import java.util.Observer;

public interface IModel {
    public int[][] getMaze();
    public int getRowChar();
    public int getColChar();
    public void solveMaze(int [][] maze);
    public void getSolution();
    public void assignObserver(Observer o);
    public void updateCharacterLocation(int direction);
    public int[][] generateRandomMaze(int row, int col);

}
