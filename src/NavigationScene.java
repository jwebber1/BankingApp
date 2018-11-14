import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 * Allows navigation to either PersonManagementScene or AccountManagementScene.
 *
 * @author  Hunter Berten
 */

class NavigationScene {
    private VBox fieldVBox = new VBox();
    StackPane root = new StackPane(fieldVBox);

    public NavigationScene() {
        UIHelpers.setBaseSceneSettings(root, fieldVBox);

        Label nameLabel = new Label(UIHelpers.currentUser.lastName + ", " + UIHelpers.currentUser.firstName);
        nameLabel.setAlignment(Pos.BASELINE_RIGHT);
        fieldVBox.getChildren().add(nameLabel);
        if (UIHelpers.currentUserLevel > 0) {
            UIHelpers.createButton("Person Management", fieldVBox, x -> personManagement());
            UIHelpers.createButton("Account Management", fieldVBox, x ->
                    UIHelpers.navigateToScene(new AccountTypeSelectionScene().root));
        }
    }

    private void personManagement() {
        UIHelpers.navigateToScene(new PersonManagementScene().root);
    }

}
