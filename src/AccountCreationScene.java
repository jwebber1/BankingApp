import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class AccountCreationScene {

    // TODO: Get customers correctly.
//    private HashMap<Integer, String> customers = new HashMap<>();
//            FXCollections.observableArrayList(
//                    "Customer 1",
//                    "Customer 2",
//                    "Customer 3"
//            );

    // Fields Needed By All Account Types
    private final StringProperty customerProperty = new SimpleStringProperty("");
    private final StringProperty accountTypeProperty = new SimpleStringProperty("");
    private final StringProperty accountBalanceProperty = new SimpleStringProperty("$");

    // Checking Account Specific Fields
    private final StringProperty checkingAccountTypeProperty = new SimpleStringProperty("");

    // Loan Specific Fields
    private final StringProperty loanTypeProperty = new SimpleStringProperty("");
    private final Property<LocalDate> datePaymentDue = new SimpleObjectProperty<>();

    // Boxes to Hold Nodes
    private HBox buttonHBox = new HBox();
    private VBox savingsAccountFieldsVBox = new VBox();
    private VBox checkingAccountFieldsVBox = new VBox();
    private VBox loanFieldsVBox = new VBox();

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
            "Gold"
    );

    private VBox fieldVBox = new VBox();
    private StackPane root = new StackPane(fieldVBox);

    public AccountCreationScene() {
//        for (Person person: Main.persons) {
//            customers.put(person.id, person.lName + ", " + person.fName);
//        }

        createBaseAccountCreationNodes();
        createCheckingFields();
        createLoanFields();

        savingsAccountFieldsVBox.setSpacing(8);
        checkingAccountFieldsVBox.setSpacing(8);
        loanFieldsVBox.setSpacing(8);

        UICreationHelpers.setBaseSceneSettings(root, fieldVBox);
    }

    public StackPane getRoot() {
        return root;
    }

    ComboBox customerBox;

    // Creates base fields that are used by all account types.
    private void createBaseAccountCreationNodes() {
        ObservableList<String> personNames = FXCollections.observableArrayList();
        for (Person person : Main.persons) {
            personNames.add(person.lName + ", " + person.fName);

        }
        customerBox = UICreationHelpers.createComboBox(personNames, customerProperty);
        fieldVBox.getChildren().add(UICreationHelpers.createHBox("Customer:", customerBox));

        ComboBox accountTypeBox = UICreationHelpers.createComboBox(accountTypes, accountTypeProperty);
        accountTypeBox.valueProperty().addListener((observable, oldValue, newValue) -> updateAccountType(newValue.toString()));
        fieldVBox.getChildren().add(UICreationHelpers.createHBox("Account Type:", accountTypeBox));

        HBox hBox = UICreationHelpers.createHBox("Account Balance:", UICreationHelpers.createBalanceField(accountBalanceProperty));
        fieldVBox.getChildren().add(hBox);

        // Save Button
        Button saveButton = new Button("Save");
        saveButton.setOnAction(x -> {
            try {
                saveAccount();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
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

        DatePicker datePicker = UICreationHelpers.createDatePicker(datePaymentDue);
        loanFieldsVBox.getChildren().add(UICreationHelpers.createHBox("Date Payment Due:", datePicker));
    }

    // Runs when the "Save" button is pressed.
    private void saveAccount() throws IOException, ParseException {
        String errorMessage = "";

        int customerId = 0;
        if (customerProperty.get().isEmpty()) {
            errorMessage += "Customer must be selected.\n";
        } else {
            customerId = Main.persons.get(customerBox.getSelectionModel().getSelectedIndex()).id;
        }

        double accountBalance = Double.parseDouble(accountBalanceProperty.get().replace("$", ""));

        if (accountTypeProperty.get().isEmpty()) {
            errorMessage += "Account Type must be selected.\n";
        }

        if (!errorMessage.isEmpty()) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("Input Not Valid");
            errorAlert.setContentText(errorMessage);
            errorAlert.showAndWait();
        } else {
            switch (accountTypes.indexOf(accountTypeProperty.get())) {
                case 0:
                    ArrayList<SavingAccount> savingAccounts = Main.savingsImportFile();
                    SavingAccount savingAccount = new SavingAccount(
                            customerId,
                            accountBalance,
                            0.2,
                            new Date()
                    );
                    savingAccounts.add(savingAccount);
                    Main.exportSavings(savingAccounts);
                    break;
                case 1:
                    ArrayList<CheckingAccount> checkingAccounts = Main.checkingsImportFile(new ArrayList<>());
                    CheckingAccount checkingAccount = new CheckingAccount(
                            customerId,
                            accountBalance,
                            checkingAccountTypeProperty.get(),
                            null,
                            null,
                            0,
                            new Date(),
                            new ArrayList<>()
                    );
                    checkingAccounts.add(checkingAccount);
                    Main.exportCheckings(checkingAccounts);
                    break;
                case 2:
                    ArrayList<LoanAccount> loanAccounts = Main.loansImportFile();
                    LoanAccount loanAccount = new LoanAccount(
                            customerId,
                            accountBalance,
                            0.2,
                            new Date(),
                            0,
                            new Date(),
                            new Date(),
                            null,
                            ""
                    );
                    loanAccounts.add(loanAccount);
                    Main.exportLoans(loanAccounts);
                    break;
            }
            Alert successfulAlert = new Alert(Alert.AlertType.INFORMATION);
            successfulAlert.setHeaderText("Save Successful");
            successfulAlert.setContentText("The user has been saved successfully.");
            successfulAlert.showAndWait();
        }
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
