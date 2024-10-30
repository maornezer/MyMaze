import Model.IModel;
import Model.MyModel;
import View.View;
import ViewModel.MyViewModel;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.media.AudioClip;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("View/MyView.fxml"));
        Parent root = fxmlLoader.load();
        primaryStage.setTitle("Hello World");
        Scene scene = new Scene(root, 700, 800);
        primaryStage.setScene(scene);
        primaryStage.show();

        // Set background music
        AudioClip backgroundMusic = new AudioClip(Main.class.getResource("/pacman-theme.mp3").toString());
        backgroundMusic.setCycleCount(AudioClip.INDEFINITE);
        backgroundMusic.play();

        IModel model = new MyModel();
        MyViewModel viewModel = new MyViewModel(model);
        View view = fxmlLoader.getController();
        view.setViewModel(viewModel);
        viewModel.addObserver(view);

    }

    public static void main(String[] args) {
        launch(args);
    }
}
