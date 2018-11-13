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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

class AccountCreationScene {
    private Account editedAccount;
    private ComboBox customerBox;

    // Fields Needed By All Account Types
    private final StringProperty customerProperty = new SimpleStringProperty("");
    private final StringProperty accountTypeProperty = new SimpleStringProperty("");
    private final StringProperty accountBalanceProperty = new SimpleStringProperty("$");

    // Checking Account Specific Fields
    private final StringProperty checkingAccountTypeProperty = new SimpleStringProperty("");

    // Loan Specific Fields
    private final StringProperty loanTypeProperty = new SimpleStringProperty("");
    private final Property<LocalDate> datePaymentDue = new SimpleObjectProperty<>();


    private final Property<LocalDate> dateCDDue = new SimpleObjectProperty<>();

    // Boxes to Hold Nodes
    private HBox buttonHBox = new HBox();
    private VBox savingsAccountFieldsVBox = new VBox();
    private VBox checkingAccountFieldsVBox = new VBox();
    private VBox loanFieldsVBox = new VBox();
    private VBox cdFieldsVBox = new VBox();

    private VBox fieldVBox = new VBox();
    StackPane root = new StackPane(fieldVBox);

    AccountCreationScene() {
        initScene();
    }

    AccountCreationScene(Account editedAccount) {
        this.editedAccount = editedAccount;

        initScene();

        Person customer = Person.searchPeopleByCustomerID(editedAccount.customerID);
        customerProperty.set(customer.lastName + ", " + customer.firstName);
        accountBalanceProperty.set(String.valueOf("$" + editedAccount.accountBalance));

        if (editedAccount instanceof CheckingAccount) {
            checkingAccountTypeProperty.set(((CheckingAccount)editedAccount).accountType);
            accountTypeProperty.set(UICreationHelpers.accountTypes.get(1));
        } else if (editedAccount instanceof LoanAccount) {
            loanTypeProperty.set(((LoanAccount)editedAccount).accountType);
            accountTypeProperty.set(UICreationHelpers.accountTypes.get(2));
            if (editedAccount.accountType.equalsIgnoreCase("LT")) {
                loanTypeProperty.set("Long Term");
            } else if (editedAccount.accountType.equalsIgnoreCase("ST")) {
                loanTypeProperty.set("Short Term");
            }
            // TODO
//            datePaymentDue.set(((LoanAccount)editedAccount).getDatePaymentDue());
        } else if (editedAccount instanceof SavingAccount) {
            accountTypeProperty.set(UICreationHelpers.accountTypes.get(0));
        } else if (editedAccount instanceof CD) {
            accountTypeProperty.set(UICreationHelpers.accountTypes.get(3));
        }
    }

    public void initScene() {
        UICreationHelpers.setBaseSceneSettings(root, fieldVBox);
        UICreationHelpers.setButtonSettings(buttonHBox);

        createBaseAccountCreationNodes();
        createCheckingFields();
        createLoanFields();
        createCdFields();

        savingsAccountFieldsVBox.setSpacing(8);
        checkingAccountFieldsVBox.setSpacing(8);
        loanFieldsVBox.setSpacing(8);
    }

    // Creates base fields that are used by all account types.
    private void createBaseAccountCreationNodes() {
        ObservableList<String> personNames = FXCollections.observableArrayList();
        for (Person person : Person.people) {
            personNames.add(person.lastName + ", " + person.firstName);
        }
        customerBox = UICreationHelpers.createComboBox(personNames, customerProperty);
        fieldVBox.getChildren().add(UICreationHelpers.createHBox("Customer:", customerBox));

        ComboBox accountTypeBox = UICreationHelpers.createComboBox(UICreationHelpers.accountTypes, accountTypeProperty);
        accountTypeBox.valueProperty().addListener((observable, oldValue, newValue) -> updateAccountType(newValue.toString()));
        fieldVBox.getChildren().add(UICreationHelpers.createHBox("Account Type:", accountTypeBox));

        HBox balanceField = UICreationHelpers.createHBox(
                "Account Balance:", UICreationHelpers.createBalanceField(accountBalanceProperty));
        fieldVBox.getChildren().add(balanceField);

        // Cancel Button
        Button cancelButton = new Button("Cancel");
        cancelButton.setOnAction(x -> UICreationHelpers.navigateToScene(new AccountManagementScene().root));
        buttonHBox.getChildren().add(cancelButton);

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

        fieldVBox.getChildren().add(buttonHBox);
    }

    // Creates fields used by checking accounts.
    private void createCheckingFields() {
        ComboBox checkingAccountTypeBox = UICreationHelpers.createComboBox(UICreationHelpers.checkingAccountTypes, checkingAccountTypeProperty);
        checkingAccountFieldsVBox.getChildren().add(UICreationHelpers.createHBox("Checking Account Type:", checkingAccountTypeBox));
    }

    // Creates fields used by loans.
    private void createLoanFields() {
        ComboBox loanTypeBox = UICreationHelpers.createComboBox(UICreationHelpers.loanTypes, loanTypeProperty);
        loanFieldsVBox.getChildren().add(UICreationHelpers.createHBox("Loan Type:", loanTypeBox));

        DatePicker datePicker = UICreationHelpers.createDatePicker(datePaymentDue);
        loanFieldsVBox.getChildren().add(UICreationHelpers.createHBox("Date Payment Due:", datePicker));
    }

    // Creates fields used by loans.
    private void createCdFields() {
        DatePicker datePicker = UICreationHelpers.createDatePicker(dateCDDue);
        cdFieldsVBox.getChildren().add(UICreationHelpers.createHBox("Date CD Due:", datePicker));
    }

    // Runs when the "Save" button is pressed.
    private void saveAccount() throws IOException, ParseException {
        String errorMessage = "";

        int customerId = 0;
        if (customerProperty.get().isEmpty()) {
            errorMessage += "Customer must be selected.\n";
        } else {
            int selectedCustomerId = customerBox.getSelectionModel().getSelectedIndex();
            Person selectedCustomer = Person.people.get(selectedCustomerId);
            customerId = selectedCustomer.id;
        }

        double accountBalance = Double.parseDouble(accountBalanceProperty.get().replace("$", ""));

        if (accountTypeProperty.get().isEmpty()) {
            errorMessage += "Account Type must be selected.\n";
        }

        if (!errorMessage.isEmpty()) {
            UICreationHelpers.showAlert(Alert.AlertType.ERROR, errorMessage);
        } else {
            if (editedAccount instanceof CheckingAccount) {
                CheckingAccount.checkingAccounts.remove(editedAccount);
            } else if (editedAccount instanceof LoanAccount) {
                LoanAccount.loans.remove(editedAccount);
            }
            switch (UICreationHelpers.accountTypes.indexOf(accountTypeProperty.get())) {
                case 0:
                    SavingAccount savingAccount = new SavingAccount(
                            customerId,
                            accountBalance,
                            0.2,
                            new Date()
                    );
                    SavingAccount.savingAccounts.add(savingAccount);
                    SavingAccount.exportFile(SavingAccount.savingAccounts);
                    break;
                case 1:
                    CheckingAccount checkingAccount = new CheckingAccount(
                            customerId,
                            accountBalance,
                            null,
                            null,
                            0,
                            new Date()
                    );
                    CheckingAccount.checkingAccounts.add(checkingAccount);
                    CheckingAccount.exportFile();
                    break;
                case 2:
                    String loanType = loanTypeProperty.get();
//                    if (loanType.equals("Long Term")) {
//                        loanType = "LT";
//                    } else if (loanType.equals("Short Term")) {
//                        loanType = "ST";
//                    }
                    LoanAccount loanAccount = new LoanAccount(
                            customerId,
                            accountBalance,
                            0.2,
                            loanType.toLowerCase()
                    );
                    LoanAccount.loans.add(loanAccount);
                    LoanAccount.exportFile();
                    break;
                case 3:
                    CD cd = new CD(
                            customerId,
                            accountBalance,
                            0.2,
                            new Date(),
                            new Date(), // TODO: Dates
                            CD.cds.get(CD.cds.size() - 1).cdNumber + 1
                    );
                    CD.cds.add(cd);
                    CD.exportFile();
                    break;
            }
            UICreationHelpers.showAlert(Alert.AlertType.INFORMATION, "The account has been saved successfully.");
            UICreationHelpers.navigateToScene(new AccountManagementScene().root);
        }
    }

    // Runs when Account Type is changed to add fields related to account type and remove fields of other account types.
    private void updateAccountType(String newValue) {
        switch (newValue) {
            case "Savings":
                fieldVBox.getChildren().add(savingsAccountFieldsVBox);
                fieldVBox.getChildren().remove(checkingAccountFieldsVBox);
                fieldVBox.getChildren().remove(loanFieldsVBox);
                fieldVBox.getChildren().remove(cdFieldsVBox);
                break;
            case "Checking":
                fieldVBox.getChildren().remove(savingsAccountFieldsVBox);
                fieldVBox.getChildren().add(checkingAccountFieldsVBox);
                fieldVBox.getChildren().remove(loanFieldsVBox);
                fieldVBox.getChildren().remove(cdFieldsVBox);
                break;
            case "Loan":
                fieldVBox.getChildren().remove(savingsAccountFieldsVBox);
                fieldVBox.getChildren().remove(checkingAccountFieldsVBox);
                fieldVBox.getChildren().add(loanFieldsVBox);
                fieldVBox.getChildren().remove(cdFieldsVBox);
                break;
            case "CD":
                fieldVBox.getChildren().remove(savingsAccountFieldsVBox);
                fieldVBox.getChildren().remove(checkingAccountFieldsVBox);
                fieldVBox.getChildren().remove(loanFieldsVBox);
                fieldVBox.getChildren().add(cdFieldsVBox);
                break;
        }
        fieldVBox.getChildren().remove(buttonHBox);
        fieldVBox.getChildren().add(buttonHBox);
    }
}
