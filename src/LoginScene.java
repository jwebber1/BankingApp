import Enums.AccountType;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 * The initial screen of the application. Initializes the app and accepts an SSN and, if valid, logs in as that
 * user.
 *
 * @author  Hunter Berten
 */

class LoginScene {
    // "fieldVBox" stacks all UI elements of the scene vertically.
    // "root" contains all UI of the scene (this is transferred on navigation to another page).
    private VBox fieldVBox = new VBox();
    StackPane root = new StackPane(fieldVBox);

    private final StringProperty socialSecurityProperty = new SimpleStringProperty("");

    LoginScene() {
        try {
            for (AccountType type : AccountType.values()) {
                UIHelpers.accountTypes.add(type.name);
            }
            Scene scene = new Scene(new StackPane(), 640, 480);
            scene.getStylesheets().add("stylesheet.css");
            Main.primaryStage.setScene(scene);
            Person.importFile();
            CheckingAccount.importFile();
            CD.importFile();
            LoanAccount.importFile();
            Check.importFile();
            SavingAccount.importFile();
            CreditCardPurchase.importFile();

        } catch (Exception e) {
            e.printStackTrace();
        }

        // Sets base scene settings (padding, etc.).
        UIHelpers.setBaseSceneSettings(root, fieldVBox);

        // SSN Field
        TextField socialSecurityField = UIHelpers.createTextField(socialSecurityProperty);
        fieldVBox.getChildren().add(UIHelpers.createHBox("Social Security #:", socialSecurityField));

        // Save Button
        Button loginButton = UIHelpers.createButton("Login", fieldVBox, x -> login());
        loginButton.setAlignment(Pos.BASELINE_RIGHT);

        UIHelpers.setBaseSceneSettings(root, fieldVBox);
    }

    // The "Login" button click event. Logs in as the user with the entered SSN (ensuring that the SSN is valid).
    private void login() {
        String errorMessage = "";
        errorMessage += UIHelpers.checkNumberField("Social Security #", socialSecurityProperty);
        if (socialSecurityProperty.get().length() != 9) {
            errorMessage += "Social Security # field must contain exactly nine digits.\n";
        }

        Person person = null;
        if (errorMessage.isEmpty()) {
            int socialSecurityNumber = Integer.parseInt(socialSecurityProperty.get());
            person = Person.searchPeopleByCustomerID(socialSecurityNumber);
            if (person == null) {
                errorMessage += "No user with the specified SSN exists.\n";
            } else if (socialSecurityNumber <= 2) {
                person.userLevel = socialSecurityNumber;
            } else {
                person.userLevel = 0;
            }
        }

        if (!errorMessage.isEmpty()) {
            UIHelpers.showAlert(Alert.AlertType.ERROR, errorMessage);
        } else {
            UIHelpers.currentUser = person;
            UIHelpers.currentUserLevel = person.userLevel;
            if (person.userLevel == 0) {
                UIHelpers.navigateToScene(new AccountTypeSelectionScene().root);
            } else {
                UIHelpers.navigateToScene(new NavigationScene().root);
            }
        }
    }
}
