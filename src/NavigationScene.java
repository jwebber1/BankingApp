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
    // "fieldVBox" stacks all UI elements of the scene vertically.
    // "root" contains all UI of the scene (this is transferred on navigation to another page).
    private VBox fieldVBox = new VBox();
    StackPane root = new StackPane(fieldVBox);

    // Constructor
    public NavigationScene() {
        UIHelpers.setBaseSceneSettings(root, fieldVBox);

        // User Name Label
        Label nameLabel = new Label(UIHelpers.currentUser.lastName + ", " + UIHelpers.currentUser.firstName +
                " - " + UIHelpers.userLevels.get(UIHelpers.currentUserLevel));
        nameLabel.setAlignment(Pos.BASELINE_RIGHT);
        fieldVBox.getChildren().add(nameLabel);

        // Creates and injects navigation buttons into UI.
        if (UIHelpers.currentUserLevel > 0) {
            UIHelpers.createButton("Person Management", fieldVBox, x -> UIHelpers.navigateToScene(new PersonManagementScene().root));
            UIHelpers.createButton("Account Management", fieldVBox, x ->
                    UIHelpers.navigateToScene(new AccountTypeSelectionScene().root));
            UIHelpers.createButton("Check Management", fieldVBox, x ->
                    UIHelpers.navigateToScene(new CheckViewScene(0).root));
            UIHelpers.createButton("Credit Card Management", fieldVBox, x ->
                    UIHelpers.navigateToScene(new CreditCardViewScene(0).root));
        }
    } // End of Constructor
} // End of NavigationScene