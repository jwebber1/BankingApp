import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.text.ParseException;

public class LoginScene {
    private VBox fieldVBox = new VBox();
    StackPane root = new StackPane(fieldVBox);

//    private final StringProperty userLevelProperty = new SimpleStringProperty("");
    private final StringProperty socialSecurityProperty = new SimpleStringProperty("");

    public LoginScene() {
        try {
            Person.people = Person.importFile();
            CheckingAccount.checkingAccounts = CheckingAccount.importFile();
//            LoanAccount.importFile();
        } catch (Exception e) {
            e.printStackTrace();
        }

        UICreationHelpers.setBaseSceneSettings(root, fieldVBox);

        TextField socialSecurityField = UICreationHelpers.createTextField(socialSecurityProperty);
        fieldVBox.getChildren().add(UICreationHelpers.createHBox("Social Security #:", socialSecurityField));

//        ComboBox userLevelField = UICreationHelpers.createComboBox(UICreationHelpers.userLevels, userLevelProperty);
//        fieldVBox.getChildren().add(UICreationHelpers.createHBox("User Level:", userLevelField));

        // Save Button
        Button loginButton = UICreationHelpers.createButton("Login", fieldVBox, x -> login());
        loginButton.setAlignment(Pos.BASELINE_RIGHT);

        UICreationHelpers.setBaseSceneSettings(root, fieldVBox);
    }

    private void login() {
        String errorMessage = "";
        errorMessage += UICreationHelpers.checkNumberField("Social Security #", socialSecurityProperty);
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
            UICreationHelpers.showAlert(Alert.AlertType.ERROR, errorMessage);
        } else {
//        UICreationHelpers.currentUserLevel = UICreationHelpers.userLevels.indexOf(userLevelProperty.get());
            UICreationHelpers.currentUser = person;
            UICreationHelpers.currentUserLevel = person.userLevel;
            UICreationHelpers.navigateToScene(new NavigationScene().root);
        }
    }
}
