package View;

import ViewModel.MyViewModel;
import algorithms.mazeGenerators.Maze;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import algorithms.search.Solution;

import java.io.File;
import java.net.URL;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;

public class View implements Initializable,Observer {

    public MediaView mediaView;
    private MediaPlayer mediaPlayer;

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

    @FXML
    public ComboBox <String> myComboBox; // for the solve maze
    @FXML
    String selectedValueSearching; //the selected solving strategy
    private Solution solution;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        lbl_player_row.textProperty().bind(update_player_position_row);
        lbl_player_column.textProperty().bind(update_player_position_col);

        myComboBox.getItems().addAll("Best First Search", "Depth First Search", "Breadth First Search");
        myComboBox.setOnAction(event -> {
            this.selectedValueSearching = myComboBox.getSelectionModel().getSelectedItem();
        });
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
    /**
     * generate the maze when click on the generate button in the app and update the view model
     */
    public void generateMaze() {
        int rows = Integer.valueOf(textField_mazeRows.getText());
        int cols = Integer.valueOf(textField_mazeColumns.getText());
        viewModel.generateMaze(rows,cols);
        this.maze = viewModel.getMaze();
        viewModel.update(viewModel,"maze generated");
    }

    public void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(message);;
        alert.show();
    }
    /**
     * the listener of the key pressed event to know where to move the player
     * @param keyEvent
     */
    public void keyPressed(KeyEvent keyEvent) {
        if(this.viewModel.getPlayerRow() != maze.length-1 || this.viewModel.getPlayerCol()  != maze[0].length -1) {
            viewModel.moveCharacter(keyEvent);
        }
        keyEvent.consume();

    }
    /**
     * the listener of the mouse clicked on to get the focus on the maze to move
     * @param mouseEvent
     */
    public void mouseClicked(MouseEvent mouseEvent) {
        mazeDisplayer.requestFocus();
    }
    /**
     * the update function that gets notify when something is changed in the app and act as it should.
     * generate maze, move player or solve the maze
     * @param o     the observable object.
     * @param arg   an argument passed to the {@code notifyObservers}
     */
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
                case "Goal State" -> playWinAnimation();
                default -> System.out.println("Not implemented change: " + change);
            }
        } catch (ClassCastException e) {
            System.out.println("Invalid argument type in update: " + arg);
        }
    }


    /**
     * the function that play the GIF of mario after the client solve the maze
     */
    public void playWinAnimation() {
        try {
            // Get the absolute path of the project directory
            String projectDir = System.getProperty("user.dir");
            String videoPath = projectDir + "/resources/GIF.mp4";
            File videoFile = new File(videoPath);
            if (!videoFile.exists()) {
                // Try alternative path
                videoPath = projectDir + "/Maze/resources/GIF.mp4";
                videoFile = new File(videoPath);
            }
            Media media = new Media(videoFile.toURI().toString());
            mediaPlayer = new MediaPlayer(media);
            mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            mediaView.setMediaPlayer(mediaPlayer);
            mediaPlayer.play();
        } catch (Exception e) {
            System.err.println("Error playing video: " + e.getMessage());
        }
    }
    /**
     * moving the image of the player based on the new position
     */
    private void playerMoved() {
        setPlayerPosition(viewModel.getPlayerRow(), viewModel.getPlayerCol());
    }

    public void setPlayerPosition(int row, int col){
        mazeDisplayer.set_player_position(row, col);
        set_update_player_position_row(row + "");
        set_update_player_position_col(col+ "");
    }
    private void mazeGenerated() {
        stopWinAnimation();
        mazeDisplayer.drawMaze(viewModel.getMaze());
        setPlayerPosition(0,0);
        viewModel.setPlayerCol(0);
        viewModel.setPlayerRow(0);

    }

    public void drawMaze() {
        mazeDisplayer.drawMaze(maze);
    }



    /**
     * menu function that present to the client an alert with the data of the application
     * @param actionEvent
     */
    @FXML
    public void AboutTheApp(ActionEvent actionEvent) {
        showCustomAlert("לפניך אפליקציה ליצירת מבוכים ופתרונם מבוססת אלגוריתמים שונים. יש שימוש באסטרטגיית Strategy כדי לתמוך בבחירה בזמן ריצה של אלגוריתמים שונים.");
    }

    /**
     * the function shows alert to the client with explanation of how the app is working and how to use it
     * @param actionEvent
     */
    public void Help(ActionEvent actionEvent) {
        showCustomAlert("לפנייך אפליקציה שיוצרת ופותרת מבוכים, המטרה הינה להגיע עם הדמות של מריו לדמות הנסיכה ולפתור את המבוך. ליצירת מבוך חדש לחץ על כתפתור הGENERATE, לפתירה של מבוך קיים לחץ על כפתור הSOLVE. בכדי לזוז השתמש בחצים של המקלדת");
    }
    public void BFS(ActionEvent actionEvent) {
        showCustomAlert("BFS");
    }
    public void DFS(ActionEvent actionEvent) {
        showCustomAlert("DFS");
    }
    public void Best(ActionEvent actionEvent) {
        showCustomAlert("Best");
    }

    /**
     * custom alert function to make sure that the message fits into it
     * @param message
     */
    public void showCustomAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information ");
        alert.setHeaderText("Information about the application and the way to use it");
        // Use a Label for the message text with wrapping enabled
        Label label = new Label(message);
        label.setWrapText(true);
        // Set the label as content and adjust the dialog pane
        alert.getDialogPane().setContent(label);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE); // Expand height based on content
        alert.getDialogPane().setPrefWidth(500); // Set a preferred width (adjust as needed)
        alert.showAndWait();
    }
    /**
     * the function stop all the animation so the client could generate new maze
     */
    public void stopWinAnimation() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.dispose(); // This releases resources
            mediaPlayer = null;    // Clear the reference
        }
        if (mediaView != null) {
            mediaView.setMediaPlayer(null); // Clear the media view
        }
    }
    /**
     * the function generate new maze and draw it to the client on the screen
     * @param actionEvent
     */
    public void generateNewMaze(ActionEvent actionEvent) {
        // Stop any playing video first
        stopWinAnimation();
        //this.selectedValueSearchable = "MyMaze";
        this.selectedValueSearching = "Depth First Search";
        generateMaze();
    }

    private void mazeSolved() {
        mazeDisplayer.drawSolution(maze,this.solution);
    }
    /**
     * solve the maze when click on the solve maze button in the app and update the view model
     */
    public void solveMaze() {
        Maze tempMaze = new Maze(this.maze.length,this.maze[0].length);
        for (int i=0; i < maze.length;i++) {
            for (int j=0; j < maze[0].length;j++) {
                tempMaze.setCell(i,j,this.maze[i][j]);
            }
        }
        this.solution = viewModel.solveMaze(tempMaze);
        viewModel.update(viewModel,"maze solved");
    }



}


/*
public class View implements Initializable, Observer {

    @FXML
    private MazeDisplayer mazeDisplayer;
    @FXML
    private TextField rowsTextField;
    @FXML
    private TextField columnsTextField;
    @FXML
    private Button generateMazeButton;
    @FXML
    private Button solveMazeButton;
    @FXML
    private Label lblPlayerRow;
    @FXML
    private Label lblPlayerColumn;

    private MyViewModel viewModel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        viewModel = new MyViewModel(new MyModel());

        // Binding UI elements to ViewModel
        generateMazeButton.setOnAction(event -> {
            int rows = Integer.parseInt(rowsTextField.getText());
            int columns = Integer.parseInt(columnsTextField.getText());
            viewModel.generateMaze(rows, columns);
        });

        solveMazeButton.setOnAction(event -> viewModel.solveMaze());

        // Binding MazeDisplayer to ViewModel
        viewModel.getMazeProperty().addListener((observable, oldValue, newValue) -> {
            mazeDisplayer.drawMaze(newValue);
            mazeDisplayer.drawBorder();
        });

        viewModel.getSolutionProperty().addListener((observable, oldValue, newValue) -> {
            mazeDisplayer.drawSolution(newValue);
        });

        // Player position binding
        lblPlayerRow.textProperty().bind(viewModel.getPlayerRowProperty().asString());
        lblPlayerColumn.textProperty().bind(viewModel.getPlayerColumnProperty().asString());
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o == viewModel) {
            // Handle updates from the ViewModel if needed
        }
    }

    public void keyPressed(KeyEvent keyEvent) {
        viewModel.moveCharacter(keyEvent);
        keyEvent.consume();
    }

    public void mouseClicked(MouseEvent mouseEvent) {
        mazeDisplayer.requestFocus();
    }

}

 */