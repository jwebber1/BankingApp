import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class NavigationScene {
    private VBox fieldVBox = new VBox();
    private StackPane root = new StackPane(fieldVBox);

    public NavigationScene() {
        UICreationHelpers.createButton("Person Creation", fieldVBox, x -> personCreation());
        UICreationHelpers.createButton("Account Creation", fieldVBox, x -> accountCreation());

        UICreationHelpers.setBaseSceneSettings(root, fieldVBox);
    }

    public StackPane getRoot() {
        return root;
    }

    private void personCreation() {
        UICreationHelpers.navigateToScene(new CustomerCreationScene().getRoot());
    }

    private void accountCreation() {
        UICreationHelpers.navigateToScene(new AccountCreationScene().getRoot());
    }
}
