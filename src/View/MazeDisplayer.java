package View;

import algorithms.search.AState;
import algorithms.search.MazeState;
import algorithms.search.Solution;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class MazeDisplayer extends Canvas {

    private int [][] maze;
    private int row_player = 0;
    private int col_player = 0;

    StringProperty imageFileNameWall = new SimpleStringProperty();
    StringProperty imageFileNamePlayer = new SimpleStringProperty();
    StringProperty imageFileNameDot = new SimpleStringProperty();
    StringProperty imageFileNameSolve = new SimpleStringProperty();
    StringProperty imageFileNameGhost = new SimpleStringProperty();
    StringProperty imageFileNameSolvePath = new SimpleStringProperty();

    public String getImageFileNameGhost() {
        return imageFileNameGhost.get();
    }
    public String getImageFileNameSolve() {
        return imageFileNameSolve.get();
    }
    public String getImageFileNameSolvePath() {
        return imageFileNameSolvePath.get();
    }

    public void setImageFileNameDot(String imageFileNameDot) {
        this.imageFileNameDot.set(imageFileNameDot);
    }

    public void setImageFileNameSolve(String imageFileNameSolve) {
        this.imageFileNameSolve.set(imageFileNameSolve);
    }
    public void setImageFileNameSolvePath(String imageFileNameSolve) {
        this.imageFileNameSolvePath.set(imageFileNameSolve);
    }

    public void setImageFileNameGhost(String imageFileNameGhost) {
        this.imageFileNameGhost.set(imageFileNameGhost);
    }

    public String getImageFileNameDot() {
        return imageFileNameDot.get();
    }

    public String getImageFileNameWall() {
        return imageFileNameWall.get();
    }

    public void setImageFileNameWall(String imageFileNameWall) {
        this.imageFileNameWall.set(imageFileNameWall);
    }

    public String getImageFileNamePlayer() {
        return imageFileNamePlayer.get();
    }
    public void setImageFileNamePlayer(String imageFileNamePlayer) {
        this.imageFileNamePlayer.set(imageFileNamePlayer);
    }

    public int getRow_player() {
        return row_player;
    }

    public int getCol_player() {
        return col_player;
    }

    public void set_player_position(int row, int col){
        this.row_player = row;
        this.col_player = col;
        draw();
    }
    /**
     * draw the maze, update the start position of the player
     * @param maze - int [][]
     */
    public void drawMaze(int [][] maze) {
        this.maze = maze;
        this.col_player = 0 ;
        this.row_player = 0 ;
        draw();

    }
    /**
     * the function that draw the walls player and the pacman icon and the end of the maze
     */
    public void draw() {
        if( maze!=null)
        {
            double canvasHeight = getHeight();
            double canvasWidth = getWidth();
            int row = maze.length;
            int col = maze[0].length;
            double cellHeight = canvasHeight/row;
            double cellWidth = canvasWidth/col;
            GraphicsContext graphicsContext = getGraphicsContext2D();
            graphicsContext.clearRect(0,0,canvasWidth,canvasHeight);
            graphicsContext.setFill(Color.RED);
            double w,h;
            //Draw Maze
            Image dot = null;
            Image wallImage = null;
            try {
                wallImage = new Image(new FileInputStream(getImageFileNameWall()));
                dot = new Image(new FileInputStream(getImageFileNameDot()));

            } catch (FileNotFoundException e) {
                System.out.println("There is no file....");
            }
            for(int i=0;i<row;i++)
            {
                for(int j=0;j<col;j++)
                {
                    h = i * cellHeight;
                    w = j * cellWidth;
                    if(maze[i][j] == 1) // Wall
                    {
                        if (wallImage == null){
                            graphicsContext.fillRect(w,h,cellWidth,cellHeight);
                        }else{
                            graphicsContext.drawImage(wallImage,w,h,cellWidth,cellHeight);
                        }
                    }
                    else
                    {
                        if (dot == null){
                            graphicsContext.fillRect(w,h,cellWidth,cellHeight);
                        }else{
                            graphicsContext.drawImage(dot,w,h,cellWidth,cellHeight);
                        }
                    }

                }
            }

            double h_player = getRow_player() * cellHeight;
            double w_player = getCol_player() * cellWidth;
            Image playerImage = null;
            Image ghost = null;
            try {
                ghost = new Image(new FileInputStream(getImageFileNameGhost()));
                playerImage = new Image(new FileInputStream(getImageFileNamePlayer()));
            } catch (FileNotFoundException e) {
                System.out.println("There is no Image player....");
            }
            graphicsContext.drawImage(playerImage,w_player,h_player,cellWidth,cellHeight);
            graphicsContext.drawImage(ghost, (col - 1) * cellWidth, (row - 1) * cellHeight, cellWidth, cellHeight);

            graphicsContext.setStroke(Color.SNOW); // צבע המסגרת
            graphicsContext.setLineWidth(4); // עובי המסגרת
            graphicsContext.strokeRect(0, 0, this.getWidth(), this.getHeight()); // ציור המסגרת מסביב למבוך
        }
    }

    public void drawSolution(int [][] maze, Solution solution) {
        drawMaze(maze);
        double canvasHeight = getHeight();
        double canvasWidth = getWidth();
        int row = maze.length;
        int col = maze[0].length;
        double cellHeight = canvasHeight / row;
        double cellWidth = canvasWidth / col;
        GraphicsContext graphicsContext = getGraphicsContext2D();
        //graphicsContext.clearRect(0, 0, canvasWidth, canvasHeight);
        ArrayList<AState> solpath =  solution.getSolutionPath();
        Image solutionPathImae = null;
        try {
            solutionPathImae = new Image(new FileInputStream(getImageFileNameSolvePath()));
        } catch (FileNotFoundException e) {
            System.out.println("There is no Image for solving");
        }

        for(int i=0; i<solpath.size(); i++){
            int r = ((MazeState) solpath.get(i)).getRow();
            int c = ((MazeState) solpath.get(i)).getCol();
            if((r==0 && c==0)|| (r==maze.length-1 && c==maze[0].length -1)){
                continue;
            }

            double x = c*cellWidth;
            double y = r*cellHeight;
            graphicsContext.drawImage(solutionPathImae, x, y, cellWidth, cellHeight);
        }
    }

}
