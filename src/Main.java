
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Objects;


public class Main extends Application {


    public void start(Stage primaryStage) {
        GameContorller controller = new GameContorller();
        primaryStage.setTitle("Mastermind made by kero");
        primaryStage.setResizable(false);
        controller.startTimer();
        VBox layout = controller.createGamelayout();
        Scene scene = new Scene(layout, 800.0, 600.0);

        scene.getStylesheets().add(Objects.requireNonNull(this.getClass().getResource("style.css")).toExternalForm());
        // primaryStage.getIcons().add(new Image(this.getClass().getResourceAsStream("27138.jpg")));
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}


