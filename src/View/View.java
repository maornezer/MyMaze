package View;

import ViewModel.MyViewModel;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.sql.SQLOutput;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;

public class View implements Initializable,Observer {
    private MyViewModel viewModel;
    @FXML
    public TextField textField_mazeRows;
    @FXML
    public TextField textField_mazeColumns;
    @FXML
    public MazeDisplayer mazeDisplayer;
    @FXML
    public Label lbl_player_row;
    @FXML
    public Label lbl_player_column;
    StringProperty update_player_position_row = new SimpleStringProperty();
    StringProperty update_player_position_col = new SimpleStringProperty();
    private int [][] maze;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        lbl_player_row.textProperty().bind(update_player_position_row);
        lbl_player_column.textProperty().bind(update_player_position_col);
    }

    public void setViewModel(MyViewModel viewModel) {
        this.viewModel = viewModel;
        // Bind the view's properties to the ViewModel's properties
        lbl_player_row.textProperty().bind(viewModel.playerRowProperty().asString());
        lbl_player_column.textProperty().bind(viewModel.playerColProperty().asString());
    }


    public String get_update_player_position_row() {
        return update_player_position_row.get();
    }

    public void set_update_player_position_row(String update_player_position_row) {
        this.update_player_position_row.set(update_player_position_row);
    }

    public String get_update_player_position_col() {
        return update_player_position_col.get();
    }

    public void set_update_player_position_col(String update_player_position_col) {
        this.update_player_position_col.set(update_player_position_col);
    }



    public void generateMaze()
    {
        int rows = Integer.valueOf(textField_mazeRows.getText());
        int cols = Integer.valueOf(textField_mazeColumns.getText());

        viewModel.generateMaze(rows,cols);

        viewModel.update(viewModel,"maze generated");
        //viewModel.notifyObservers("maze generated");




    }

    public void solveMaze()
    {
        viewModel.solveMaze(this.maze);

    }


    public void showAlert(String message)
    {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(message);;
        alert.show();
    }


    public void keyPressed(KeyEvent keyEvent) {

        viewModel.moveCharacter(keyEvent);
        keyEvent.consume();

    }

    public void mouseClicked(MouseEvent mouseEvent) {
        mazeDisplayer.requestFocus();
    }


//    @Override
//    public void update(Observable o, Object arg) {
//        if(o instanceof MyViewModel)
//        {
//            if(maze == null)//generateMaze
//            {
//                this.maze = viewModel.getMaze();
//                drawMaze();
//            }
//            else {
//                int[][] maze = viewModel.getMaze();
//
//                if (maze == this.maze)//Not generateMaze
//                {
//                    int rowChar = mazeDisplayer.getRow_player();
//                    int colChar = mazeDisplayer.getCol_player();
//                    int rowFromViewModel = viewModel.getRowChar();
//                    int colFromViewModel = viewModel.getColChar();
//
//                    if(rowFromViewModel == rowChar && colFromViewModel == colChar)//Solve Maze
//                    {
//                        viewModel.getSolution();
//                        showAlert("Solving Maze ... ");
//                    }
//                    else//Update location
//                    {
//                        set_update_player_position_row(rowFromViewModel + "");
//                        set_update_player_position_col(colFromViewModel + "");
//                        this.mazeDisplayer.set_player_position(rowFromViewModel,colFromViewModel);
//                    }
//
//
//                }
//                else//GenerateMaze
//                {
//                    this.maze = maze;
//                    drawMaze();
//                }
//            }
//        }
//    }


    @Override
    public void update(Observable o, Object arg) {
        if (arg == null) {
            System.out.println("Received null argument in update");
            return;
        }

        try {
            String change = (String) arg;

            switch (change) {
                case "maze generated" -> mazeGenerated();
                case "move player" -> playerMoved();
                case "maze solved" -> mazeSolved();
                default -> System.out.println("Not implemented change: " + change);
            }
        } catch (ClassCastException e) {
            System.out.println("Invalid argument type in update: " + arg);
        }
    }

    private void mazeSolved() {
    }

    private void playerMoved() {
        System.out.println("i am here View - player move");

        setPlayerPosition(viewModel.getPlayerRow(), viewModel.getPlayerCol());
//        int rowFromViewModel = viewModel.getRowChar();
//        int colFromViewModel = viewModel.getColChar();
//        set_update_player_position_row(rowFromViewModel + "");
//        set_update_player_position_col(colFromViewModel + "");
//        this.mazeDisplayer.set_player_position(rowFromViewModel,colFromViewModel);
    }
    public void setPlayerPosition(int row, int col){
        mazeDisplayer.set_player_position(row, col);
        set_update_player_position_row(row + "");
        set_update_player_position_col(col+ "");
    }
    private void mazeGenerated() {
        System.out.println(viewModel.getMaze().toString());
        mazeDisplayer.drawMaze(viewModel.getMaze());
    }

    public void drawMaze()
    {
        mazeDisplayer.drawMaze(maze);
    }
}


