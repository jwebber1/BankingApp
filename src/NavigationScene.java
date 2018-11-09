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
            UICreationHelpers.createButton("Person Management", fieldVBox, x -> personManagement());
            UICreationHelpers.createButton("Account Management", fieldVBox, x -> accountManagement());
        }
    }

    private void personManagement() {
        UICreationHelpers.navigateToScene(new PersonManagementScene().root);
    }

    private void accountManagement() {
        UICreationHelpers.navigateToScene(new AccountManagementScene().root);
    }
}
