import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

class NavigationScene {
    private VBox fieldVBox = new VBox();
    StackPane root = new StackPane(fieldVBox);

    public NavigationScene() {
        UICreationHelpers.setBaseSceneSettings(root, fieldVBox);

        Label nameLabel = new Label(UICreationHelpers.currentUser.lastName + ", " + UICreationHelpers.currentUser.firstName);
        nameLabel.setAlignment(Pos.BASELINE_RIGHT);
        fieldVBox.getChildren().add(nameLabel);
        if (UICreationHelpers.currentUserLevel > 0) {
            UICreationHelpers.createButton("Person Creation", fieldVBox, x -> personCreation());
            UICreationHelpers.createButton("Account Creation", fieldVBox, x -> accountCreation());
            UICreationHelpers.createButton("Person Selection", fieldVBox, x -> personSelection());
        } else {

        }
    }

    private void personCreation() {
        UICreationHelpers.navigateToScene(new PersonCreationScene().root);
    }

    private void accountCreation() {
        UICreationHelpers.navigateToScene(new AccountCreationScene().root);
    }

    private void personSelection() {
        UICreationHelpers.navigateToScene(new PersonSelectionScene().root);
    }
}
