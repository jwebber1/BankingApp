import Enums.AccountType;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

/**
 * Allows user to add/edit any account type.
 *
 * @author  Hunter Berten
 */

class AccountAddEditScene {
    private Account editedAccount;
    private ComboBox customerBox;

    // Fields Needed By All Account Types
    private final StringProperty customerProperty = new SimpleStringProperty("");
    private final StringProperty accountTypeProperty = new SimpleStringProperty("");
    private final StringProperty accountBalanceProperty = new SimpleStringProperty("$");

    private final StringProperty interestRateProperty = new SimpleStringProperty("0.2");

    // Checking Account Specific Fields
    private final StringProperty checkingAccountTypeProperty = new SimpleStringProperty("");
    private final StringProperty overdraftProtectionProperty = new SimpleStringProperty("False");

    // Loan Specific Fields
    private final StringProperty loanTypeProperty = new SimpleStringProperty("");
    private final Property<LocalDate> datePaymentDueProperty = new SimpleObjectProperty<>();

    private final Property<LocalDate> dateCDDueProperty = new SimpleObjectProperty<>();

    // Boxes to Hold Nodes
    private HBox buttonHBox = new HBox();
    private VBox savingsAccountFieldsVBox = new VBox();
    private VBox checkingAccountFieldsVBox = new VBox();
    private VBox loanFieldsVBox = new VBox();
    private VBox cdFieldsVBox = new VBox();

    private VBox fieldVBox = new VBox();
    StackPane root = new StackPane(fieldVBox);

    AccountAddEditScene() {
        initScene();
    }

    AccountAddEditScene(Account editedAccount) {
        this.editedAccount = editedAccount;

        initScene();

        Person customer = Person.searchPeopleByCustomerID(editedAccount.customerID);
        customerProperty.set(customer.lastName + ", " + customer.firstName);
        accountBalanceProperty.set(String.valueOf("$" + editedAccount.accountBalance));
        accountTypeProperty.set(UIHelpers.selectedAccountType.name);

        if (editedAccount instanceof CheckingAccount) {
            checkingAccountTypeProperty.set(((CheckingAccount)editedAccount).accountType);
        } else if (editedAccount instanceof LoanAccount) {
            loanTypeProperty.set(((LoanAccount)editedAccount).accountType);
            if (editedAccount.accountType.equalsIgnoreCase("LT")) {
                loanTypeProperty.set("Long Term");
            } else if (editedAccount.accountType.equalsIgnoreCase("ST")) {
                loanTypeProperty.set("Short Term");
            }
            interestRateProperty.set(Double.toString(((LoanAccount)editedAccount).getInterestRate()));
            LocalDate datePaymentDue = ((LoanAccount)editedAccount).getDatePaymentDue().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            datePaymentDueProperty.setValue(datePaymentDue);
        } else if (editedAccount instanceof SavingAccount) {
            interestRateProperty.set(Double.toString(((SavingAccount)editedAccount).getCurrentInterestRate()));
        } else if (editedAccount instanceof CD) {
            interestRateProperty.set(Double.toString(((CD)editedAccount).getCurrentInterestRate()));
            LocalDate dateCDDue = ((CD)editedAccount).getDateCDDue().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            dateCDDueProperty.setValue(dateCDDue);
        }
    }

    public void initScene() {
        UIHelpers.setBaseSceneSettings(root, fieldVBox);
        UIHelpers.setButtonSettings(buttonHBox);

        createBaseAccountCreationNodes();
        createCheckingFields();
        createLoanFields();
        createCdFields();

        savingsAccountFieldsVBox.setSpacing(8);
        checkingAccountFieldsVBox.setSpacing(8);
        loanFieldsVBox.setSpacing(8);
        cdFieldsVBox.setSpacing(8);
    }

    // Creates base fields that are used by all account types.
    private void createBaseAccountCreationNodes() {
        ObservableList<String> personNames = FXCollections.observableArrayList();
        for (Person person : Person.people) {
            personNames.add(person.lastName + ", " + person.firstName);
        }
        customerBox = UIHelpers.createComboBox(personNames, customerProperty);
        fieldVBox.getChildren().add(UIHelpers.createHBox("Customer:", customerBox));

        ComboBox accountTypeBox = UIHelpers.createComboBox(UIHelpers.accountTypes, accountTypeProperty);
        accountTypeBox.valueProperty().addListener((observable, oldValue, newValue) -> updateAccountType(newValue.toString()));
        fieldVBox.getChildren().add(UIHelpers.createHBox("Account Type:", accountTypeBox));

        HBox balanceField = UIHelpers.createHBox(
                "Account Balance:", UIHelpers.createBalanceField(accountBalanceProperty));
        fieldVBox.getChildren().add(balanceField);

        cdFieldsVBox.getChildren().add(UIHelpers.createHBox(
                "Interest Rate:", UIHelpers.createTextField(interestRateProperty)));
        savingsAccountFieldsVBox.getChildren().add(UIHelpers.createHBox(
                "Interest Rate:", UIHelpers.createTextField(interestRateProperty)));
        loanFieldsVBox.getChildren().add(UIHelpers.createHBox(
                "Interest Rate:", UIHelpers.createTextField(interestRateProperty)));

        // Cancel Button
        Button cancelButton = new Button("Cancel");
        cancelButton.setOnAction(x -> UIHelpers.navigateBackToAccountManagement());
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
        ComboBox checkingAccountTypeBox = UIHelpers.createComboBox(UIHelpers.checkingAccountTypes, checkingAccountTypeProperty);
        checkingAccountFieldsVBox.getChildren().add(UIHelpers.createHBox("Checking Account Type:", checkingAccountTypeBox));

        ComboBox overdraftProtectionCombo = UIHelpers.createComboBox(
                FXCollections.observableArrayList("True", "False"), overdraftProtectionProperty);
        checkingAccountFieldsVBox.getChildren().add(UIHelpers.createHBox(
                "Overdraft Protection:", overdraftProtectionCombo));
    }

    // Creates fields used by loans.
    private void createLoanFields() {
        ComboBox loanTypeBox = UIHelpers.createComboBox(UIHelpers.loanTypes, loanTypeProperty);
        loanFieldsVBox.getChildren().add(UIHelpers.createHBox("Loan Type:", loanTypeBox));

        DatePicker datePicker = UIHelpers.createDatePicker(datePaymentDueProperty);
        loanFieldsVBox.getChildren().add(UIHelpers.createHBox("Date Payment Due:", datePicker));
    }

    // Creates fields used by loans.
    private void createCdFields() {
        DatePicker datePicker = UIHelpers.createDatePicker(dateCDDueProperty);
        cdFieldsVBox.getChildren().add(UIHelpers.createHBox("Date CD Due:", datePicker));
    }

    // Runs when the "Save" button is pressed.
    private void saveAccount() throws IOException {
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
            UIHelpers.showAlert(Alert.AlertType.ERROR, errorMessage);
        } else {
            if (editedAccount instanceof CheckingAccount) {
                CheckingAccount.checkingAccounts.remove(editedAccount);
            } else if (editedAccount instanceof CD) {
                CD.cds.remove(editedAccount);
            } else if (editedAccount instanceof SavingAccount) {
                SavingAccount.savingAccounts.remove(editedAccount);
            } else if (editedAccount instanceof LoanAccount) {
                LoanAccount.loans.remove(editedAccount);
            }
            switch (UIHelpers.accountTypes.indexOf(accountTypeProperty.get())) {
                case 0:
                    SavingAccount savingAccount = new SavingAccount(
                            customerId,
                            accountBalance,
                            Double.parseDouble(interestRateProperty.get()),
                            new Date()
                    );
                    SavingAccount.savingAccounts.add(savingAccount);
                    SavingAccount.exportFile();
                    break;
                case 1:
                    CheckingAccount checkingAccount = new CheckingAccount(
                            customerId,
                            accountBalance,
                            overdraftProtectionProperty.get().equals("True"),
                            false,
                            editedAccount == null ? 0 : ((CheckingAccount)editedAccount).getOverdraftsThisMonth(),
                            new Date()
                    );
                    CheckingAccount.checkingAccounts.add(checkingAccount);
                    CheckingAccount.exportFile();
                    break;
                case 2:
                    String loanType = loanTypeProperty.get();
                    LoanAccount loanAccount = new LoanAccount(
                            customerId,
                            accountBalance,
                            Double.parseDouble(interestRateProperty.get()),
                            loanType.toLowerCase()
                    );
                    Date datePaymentDue = Date.from(datePaymentDueProperty.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
                    loanAccount.setDatePaymentDue(datePaymentDue);
                    LoanAccount.loans.add(loanAccount);
                    LoanAccount.exportFile();
                    break;
                case 3:
                    CD cd = new CD(
                            customerId,
                            accountBalance,
                            Double.parseDouble(interestRateProperty.get()),
                            new Date(),
                            Date.from(dateCDDueProperty.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()),
                            CD.cds.get(CD.cds.size() - 1).cdNumber + 1
                    );
                    CD.cds.add(cd);
                    CD.exportFile();
                    break;
            }
            UIHelpers.showAlert(Alert.AlertType.INFORMATION, "The account has been saved successfully.");

            UIHelpers.navigateBackToAccountManagement();
        }
    }

    // Runs when Account Type is changed to add fields related to account type and remove fields of other account types.
    private void updateAccountType(String newValue) {
        if (newValue.equals(AccountType.SAVING.name)) {
            fieldVBox.getChildren().add(savingsAccountFieldsVBox);
            fieldVBox.getChildren().remove(checkingAccountFieldsVBox);
            fieldVBox.getChildren().remove(loanFieldsVBox);
            fieldVBox.getChildren().remove(cdFieldsVBox);
        } else if (newValue.equals(AccountType.CHECKING.name)) {
            fieldVBox.getChildren().remove(savingsAccountFieldsVBox);
            fieldVBox.getChildren().add(checkingAccountFieldsVBox);
            fieldVBox.getChildren().remove(loanFieldsVBox);
            fieldVBox.getChildren().remove(cdFieldsVBox);
        } else if (newValue.equals(AccountType.LOAN.name)) {
            fieldVBox.getChildren().remove(savingsAccountFieldsVBox);
            fieldVBox.getChildren().remove(checkingAccountFieldsVBox);
            fieldVBox.getChildren().add(loanFieldsVBox);
            fieldVBox.getChildren().remove(cdFieldsVBox);
        } else if (newValue.equals(AccountType.CD.name)) {
            fieldVBox.getChildren().remove(savingsAccountFieldsVBox);
            fieldVBox.getChildren().remove(checkingAccountFieldsVBox);
            fieldVBox.getChildren().remove(loanFieldsVBox);
            fieldVBox.getChildren().add(cdFieldsVBox);
        }
        fieldVBox.getChildren().remove(buttonHBox);
        fieldVBox.getChildren().add(buttonHBox);
    }
}
