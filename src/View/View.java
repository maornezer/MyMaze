package View;

import ViewModel.MyViewModel;
import algorithms.mazeGenerators.Maze;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import algorithms.search.Solution;
import javafx.stage.Modality;
import javafx.stage.Stage;

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
        this.solution = viewModel.solveMaze(tempMaze,this.selectedValueSearching);
        viewModel.update(viewModel,"maze solved");
    }


    @FXML
    public void showAlgorithmDetails(String title, String message) {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(400);
        window.setMinHeight(400);

        Label label = new Label();
        label.setText(message);
        label.setWrapText(true);
        label.setStyle("-fx-text-fill: #ffffff; -fx-font-size: 24px;"); // הוסף את גודל הגופן

        Button closeButton = new Button("Close");
        closeButton.setOnAction(e -> window.close());
        closeButton.setStyle("-fx-text-fill: #000000; -fx-background-color: #ffcc00;");

        VBox layout = new VBox(10);
        layout.getChildren().addAll(label, closeButton);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-padding: 20; -fx-background-color: #333333; -fx-border-color: #ffcc00; -fx-border-width: 2;");

        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();
    }

    @FXML
    public void AboutTheApp(ActionEvent actionEvent) {
        showAlgorithmDetails("About MazeApp",
                "This application allows you to create and solve mazes using various algorithms. " +
                        "The Strategy design pattern is implemented to support selecting different algorithms at runtime.");
    }

    @FXML
    public void Help(ActionEvent actionEvent) {
        showAlgorithmDetails("Help",
                "This application allows you to create and solve mazes. The objective is to move Pacman to reach the Ghost and solve the maze. " +
                        "To generate a new maze, click the 'Generate Maze' button. To solve an existing maze, click the 'Solve Maze' button. Use the arrow keys on the keyboard to move the character.");
    }

    @FXML
    public void BFS(ActionEvent actionEvent) {
        showAlgorithmDetails("Breadth-First Search (BFS)",
                "Breadth-First Search (BFS) is an algorithm used to explore or search through all possible paths in a maze or network. " +
                        "It starts from the starting point and explores all possible paths step by step, layer by layer, making sure every path is explored evenly before moving further.");
    }

    @FXML
    public void DFS(ActionEvent actionEvent) {
        showAlgorithmDetails("Depth-First Search (DFS)",
                "Depth-First Search (DFS) is an algorithm used to explore or search through a maze or network by going as far as possible in one direction before turning back. " +
                        "It keeps moving forward until it hits a dead end, and then it goes back to the last point where it can try a new direction.");
    }

    @FXML
    public void BestFirstSearch(ActionEvent actionEvent) {
        showAlgorithmDetails("Best-First Search",
                "Best-First Search is a type of search that tries to find the goal as quickly as possible by choosing the most promising path first. " +
                        "It uses a smart guess (heuristic) to decide which direction to explore next, often making it faster in finding the solution compared to other methods.");
    }

}
