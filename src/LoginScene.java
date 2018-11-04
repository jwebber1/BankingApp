import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class LoginScene {
    private VBox fieldVBox = new VBox();
    private StackPane root = new StackPane(fieldVBox);

    private final StringProperty userLevelProperty = new SimpleStringProperty("");

    public LoginScene() {
        ComboBox userLevelField = UICreationHelpers.createComboBox(UICreationHelpers.userLevels, userLevelProperty);
        fieldVBox.getChildren().add(UICreationHelpers.createHBox("User Level:", userLevelField));


        // Save Button
        Button loginButton = new Button("Login");
//        saveButton.setOnAction(x -> saveAccount());
//        buttonHBox.getChildren().add(saveButton);
        loginButton.setAlignment(Pos.BASELINE_RIGHT);
        fieldVBox.getChildren().add(loginButton);

        UICreationHelpers.setBaseSceneSettings(root, fieldVBox);
    }

    public StackPane getRoot() {
        return root;
    }
}
