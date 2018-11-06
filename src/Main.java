import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    public static Stage primaryStage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        UICreationHelpers.navigateToScene(new LoginScene().root);
        primaryStage.setTitle("Banking App");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}//end of main