package Model;
import algorithms.mazeGenerators.Maze;
import algorithms.search.Solution;
import java.util.Observer;

public interface IModel {
    public int[][] getMaze();
    public int getRowChar();
    public int getColChar();
    public Solution solveMaze(Maze maze,String str);
    public Solution getSolution();
    public void assignObserver(Observer o);
    public void updateCharacterLocation(int direction);
    public int[][] generateRandomMaze(int row, int col);

}
