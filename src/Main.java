import javafx.application.Application;
import javafx.stage.Stage;

/**
 * TODO:
 */

public class Main extends Application {

    static Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {
        Main.primaryStage = primaryStage;
        UIHelpers.navigateToScene(new LoginScene().root);
        primaryStage.setTitle("Banking App");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}//end of main