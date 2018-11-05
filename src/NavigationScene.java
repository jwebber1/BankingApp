import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

class NavigationScene {
    private VBox fieldVBox = new VBox();
    private StackPane root = new StackPane(fieldVBox);

    public NavigationScene() {
        UICreationHelpers.setBaseSceneSettings(root, fieldVBox);

        Label nameLabel = new Label(UICreationHelpers.currentUser.lName + ", " + UICreationHelpers.currentUser.fName);
        nameLabel.setAlignment(Pos.BASELINE_RIGHT);
        fieldVBox.getChildren().add(nameLabel);
        if (UICreationHelpers.currentUserLevel > 0) {
            UICreationHelpers.createButton("Person Creation", fieldVBox, x -> personCreation());
            UICreationHelpers.createButton("Account Creation", fieldVBox, x -> accountCreation());
        } else {

        }
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
