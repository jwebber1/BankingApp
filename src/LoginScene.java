import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

class LoginScene {
    private VBox fieldVBox = new VBox();
    StackPane root = new StackPane(fieldVBox);

//    private final StringProperty userLevelProperty = new SimpleStringProperty("");
    private final StringProperty socialSecurityProperty = new SimpleStringProperty("");

    LoginScene() {
        try {
            Person.people = Person.importFile();
            CheckingAccount.checkingAccounts = CheckingAccount.importFile();
            CD.cds = CD.importFile();
            LoanAccount.importFile();
            SavingAccount.savingAccounts = SavingAccount.importFile();

//            for (CheckingAccount account : CheckingAccount.checkingAccounts) {
//                account.mainAccountType = "Checking - " +
//                    account.accountType.substring(0, 1).toUpperCase() +
//                    account.accountType.substring(1);
//            }
//            for (LoanAccount account : LoanAccount.loans) {
//                account.mainAccountType = "Loan - " + account.accountType;
//            }
//            for (CD account : CD.cds) {
//                account.mainAccountType = "CD";
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        UICreationHelpers.setBaseSceneSettings(root, fieldVBox);

        TextField socialSecurityField = UICreationHelpers.createTextField(socialSecurityProperty);
        fieldVBox.getChildren().add(UICreationHelpers.createHBox("Social Security #:", socialSecurityField));

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
            UICreationHelpers.currentUser = person;
            UICreationHelpers.currentUserLevel = person.userLevel;
            if (person.userLevel == 0) {
                UICreationHelpers.navigateToScene(new AccountManagementScene().root);
            } else {
                UICreationHelpers.navigateToScene(new NavigationScene().root);
            }
        }
    }
}
