package UI;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class AccountCreationScene {
    public StackPane root = new StackPane();

    // Properties fields are bound to.
    private final StringProperty socialSecurityProperty = new SimpleStringProperty("");
    private final StringProperty accountTypeProperty = new SimpleStringProperty("");
    private final StringProperty accountBalanceProperty = new SimpleStringProperty("$");
    private final StringProperty firstNameProperty = new SimpleStringProperty("");
    private final StringProperty lastNameProperty = new SimpleStringProperty("");

    private final StringProperty loanTypeProperty = new SimpleStringProperty("");
    private final StringProperty checkingAccountTypeProperty = new SimpleStringProperty("");

    // Account Type-specific Fields
    private VBox savingsAccountFieldsVBox = new VBox();
    private VBox checkingAccountFieldsVBox = new VBox();
    private VBox loanFieldsVBox = new VBox();

    private HBox buttonHBox = new HBox();

    private ObservableList<String> accountTypes = FXCollections.observableArrayList(
            "Savings",
            "Checking",
            "Loan"
    );

    private ObservableList<String> loanTypes = FXCollections.observableArrayList(
            "Long Term",
            "Short Term",
            "Credit Card"
    );

    private ObservableList<String> checkingAccountTypes = FXCollections.observableArrayList(
            "That's My Bank",
            "Gold/Diamond"
    );

    private VBox fieldVBox = new VBox();

    public AccountCreationScene() {
        createBaseAccountCreationNodes();
        createCheckingFields();
        createLoanFields();

        fieldVBox.setSpacing(8);
        root.getChildren().add(fieldVBox);
        root.setPadding(new Insets(20));
    }

    // Creates base fields that are used by all account types.
    private void createBaseAccountCreationNodes() {
        ComboBox accountTypeBox = UICreationHelpers.createComboBox(accountTypes, accountTypeProperty);
        accountTypeBox.valueProperty().addListener((observable, oldValue, newValue) -> updateAccountType(newValue.toString()));
        fieldVBox.getChildren().add(UICreationHelpers.createHBox("Account Type:", accountTypeBox));

        HBox hBox = UICreationHelpers.createHBox("Account Balance:", UICreationHelpers.createBalanceField(accountBalanceProperty));
        fieldVBox.getChildren().add(hBox);

//        createTextField("First Name:", firstNameProperty);
//        createTextField("Last Name:", lastNameProperty);

        // Save Button
        Button saveButton = new Button("Save");
        saveButton.setOnAction(x -> saveAccount());
        buttonHBox.getChildren().add(saveButton);
        buttonHBox.setAlignment(Pos.BASELINE_RIGHT);
        fieldVBox.getChildren().add(buttonHBox);
    }

    // Creates fields used by checking accounts.
    private void createCheckingFields() {
        ComboBox checkingAccountTypeBox = UICreationHelpers.createComboBox(checkingAccountTypes, checkingAccountTypeProperty);
        checkingAccountFieldsVBox.getChildren().add(UICreationHelpers.createHBox("Checking Account Type:", checkingAccountTypeBox));
    }

    // Creates fields used by loans.
    private void createLoanFields() {
        ComboBox loanTypeBox = UICreationHelpers.createComboBox(loanTypes, loanTypeProperty);
        loanFieldsVBox.getChildren().add(UICreationHelpers.createHBox("Loan Type:", loanTypeBox));

        DatePicker datePicker = UICreationHelpers.createDatePicker(loanTypeProperty);
        loanFieldsVBox.getChildren().add(UICreationHelpers.createHBox("Date Payment Due:", datePicker));
    }


    // Runs when the "Save" button is pressed.
    private void saveAccount() {
        String errorMessage = "";

        errorMessage += UICreationHelpers.checkNumberField("Social Security #", socialSecurityProperty);
        // Extra Check for SSN (EXACTLY 9 Characters)
        if (socialSecurityProperty.get().length() == 9) {
            errorMessage += "Social Security # field must contain exactly nine digits.\n";
        }

        //
        if (accountTypeProperty.get().isEmpty()) {
            errorMessage += "Account Type must be selected.\n";
        }

//        errorMessage += checkTextField("First Name", firstNameProperty);
//        errorMessage += checkTextField("Last Name", lastNameProperty);

        if (!errorMessage.isEmpty()) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("Input Not Valid");
            errorAlert.setContentText(errorMessage);
            errorAlert.showAndWait();
        } else {
            // TODO: Save to account. Autoset date account opened.
            Alert successfulAlert = new Alert(Alert.AlertType.INFORMATION);
            successfulAlert.setHeaderText("Save Successful");
            successfulAlert.setContentText("The user has been saved successfully.");
            successfulAlert.showAndWait();
        }
//        System.out.println(firstNameProperty.get());
//        System.out.println(lastNameProperty.get());
    }

    // Runs when Account Type is changed to add fields related to account type and remove fields of other account types.
    private void updateAccountType(String newValue) {
        switch (newValue) {
            case "Savings":
                fieldVBox.getChildren().add(savingsAccountFieldsVBox);
                fieldVBox.getChildren().remove(checkingAccountFieldsVBox);
                fieldVBox.getChildren().remove(loanFieldsVBox);
                break;
            case "Checking":
                fieldVBox.getChildren().remove(savingsAccountFieldsVBox);
                fieldVBox.getChildren().add(checkingAccountFieldsVBox);
                fieldVBox.getChildren().remove(loanFieldsVBox);
                break;
            case "Loan":
                fieldVBox.getChildren().remove(savingsAccountFieldsVBox);
                fieldVBox.getChildren().remove(checkingAccountFieldsVBox);
                fieldVBox.getChildren().add(loanFieldsVBox);
                break;
        }
        fieldVBox.getChildren().remove(buttonHBox);
        fieldVBox.getChildren().add(buttonHBox);
    }
}
