import UI.AccountCreationScene;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * TODO
 */

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Banking App");
        primaryStage.setScene(new Scene(new AccountCreationScene().root, 640, 480));
        primaryStage.show();
    }

    public static void main(String[] args) { launch(args); }
}
