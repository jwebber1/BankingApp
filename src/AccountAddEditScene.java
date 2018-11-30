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
 * @author Hunter Berten
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
//    private final Property<LocalDate> datePaymentDueProperty = new SimpleObjectProperty<>();

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

        if (editedAccount instanceof CheckingAccount) {
            checkingAccountTypeProperty.set(((CheckingAccount) editedAccount).accountType);
        } else if (editedAccount instanceof LoanAccount) {
            loanTypeProperty.set(((LoanAccount) editedAccount).accountType);
            if (editedAccount.accountType.equalsIgnoreCase("LT")) {
                loanTypeProperty.set("Long Term");
            } else if (editedAccount.accountType.equalsIgnoreCase("ST")) {
                loanTypeProperty.set("Short Term");
            }
            interestRateProperty.set(Double.toString(((LoanAccount) editedAccount).getInterestRate()));
        } else if (editedAccount instanceof SavingAccount) {
            interestRateProperty.set(Double.toString(((SavingAccount) editedAccount).getCurrentInterestRate()));
        } else if (editedAccount instanceof CD) {
            interestRateProperty.set(Double.toString(((CD) editedAccount).getCurrentInterestRate()));
            LocalDate dateCDDue = ((CD) editedAccount).getDateCDDue().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
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

        accountTypeProperty.set(UIHelpers.selectedAccountType.name);
        updateAccountType(UIHelpers.selectedAccountType.name);

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

//        DatePicker datePicker = UIHelpers.createDatePicker(datePaymentDueProperty);
//        loanFieldsVBox.getChildren().add(UIHelpers.createHBox("Date Payment Due:", datePicker));
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

        switch (UIHelpers.selectedAccountType) {
            case CD:
                if (accountBalance <= 0.01) {
                    errorMessage += "CD balance cannot be less than $0.01.\n";
                }
                break;
            case LOAN:
                if (loanTypeProperty.get().equalsIgnoreCase("credit card")) {
                    if (accountBalance < 0) {
                        errorMessage += "Credit card balance cannot be less than $0.00.\n";
                    }
                } else {
                    if (accountBalance <= 0.01) {
                        errorMessage += "Loan account balance cannot be less than $0.01.\n";
                    }
                }
                break;
            case SAVING:
                if (accountBalance < 0) {
                    errorMessage += "Savings account balance cannot be less than $0.00.\n";
                }
                break;
        }

        if (accountTypeProperty.get().isEmpty()) {
            errorMessage += "Account Type must be selected.\n";
        }


        if (!errorMessage.isEmpty()) {
            UIHelpers.showAlert(Alert.AlertType.ERROR, errorMessage);
        } else {
            switch (UIHelpers.selectedAccountType) {
                case SAVING:
                    if (editedAccount != null) CheckingAccount.checkingAccounts.remove(editedAccount);
                    else if (SavingAccount.search(customerId) != null) {
                        UIHelpers.showAlert(Alert.AlertType.INFORMATION,
                                "This user already has a Savings account. Are you sure you selected the correct SSN?");
                        return;
                    }
                    break;
                case CHECKING:
                    if (editedAccount != null) SavingAccount.savingAccounts.remove(editedAccount);
                    else if (CheckingAccount.search(customerId) != null) {
                        UIHelpers.showAlert(Alert.AlertType.INFORMATION,
                                "This user already has a Checking account. Are you sure you selected the correct SSN?");
                        return;
                    }
                    break;
                case LOAN:
                    if (editedAccount != null) LoanAccount.loans.remove(editedAccount);
                    else if (LoanAccount.search(customerId, loanTypeProperty.get()) != null) {
                        UIHelpers.showAlert(Alert.AlertType.INFORMATION,
                                "This user already has a Loan of this type. Are you sure you selected the correct SSN?");
                        return;
                    }
                    break;
                case CD:
                    if (editedAccount != null) CD.cds.remove(editedAccount);
                    break;
            }

            switch (UIHelpers.selectedAccountType) {
                case SAVING:
                    if (overdraftProtectionProperty.get().equals("True") && SavingAccount.search(customerId) == null) {
                        UIHelpers.showAlert(Alert.AlertType.INFORMATION, "This account cannot have overdraft" +
                                "protection because this customer does not have a savings account.");
                    }

                    double savingInterestRate;
                    try {
                        savingInterestRate = Double.parseDouble(interestRateProperty.get());
                    } catch (NumberFormatException | NullPointerException e) {
                        UIHelpers.showAlert(Alert.AlertType.INFORMATION, "Interest rate must be a decimal " +
                                "number (such as \"0.2\" for 20%).");
                        return;
                    }
                    if (savingInterestRate < 0.001 || savingInterestRate > 1.0) {
                        UIHelpers.showAlert(Alert.AlertType.INFORMATION, "Interest rate must be greater than or equal to 0.001 and less than or equal to 1.0.");
                        return;
                    }
                    SavingAccount savingAccount = new SavingAccount(
                            customerId,
                            accountBalance,
                            savingInterestRate,
                            new Date()
                    );
                    SavingAccount.savingAccounts.add(savingAccount);
                    SavingAccount.exportFile();
                    break;
                case CHECKING:
                    if (overdraftProtectionProperty.get().equals("True") && SavingAccount.search(customerId) == null) {
                        UIHelpers.showAlert(Alert.AlertType.INFORMATION, "This account cannot have overdraft" +
                                "protection because this customer does not have a savings account.");
                    }
                    CheckingAccount checkingAccount = new CheckingAccount(
                            customerId,
                            accountBalance,
                            overdraftProtectionProperty.get().equals("True"),
                            false,
                            editedAccount == null ? 0 : ((CheckingAccount) editedAccount).getOverdraftsThisMonth(),
                            editedAccount == null ? new Date() : editedAccount.getDateAccountOpened()
                    );
                    CheckingAccount.checkingAccounts.add(checkingAccount);
                    CheckingAccount.exportFile();
                    break;
                case LOAN:
                    double loanInterestRate;
                    try {
                        loanInterestRate = Double.parseDouble(interestRateProperty.get());
                    } catch (NumberFormatException | NullPointerException e) {
                        UIHelpers.showAlert(Alert.AlertType.INFORMATION, "Interest rate must be a decimal " +
                                "number (such as \"0.2\" for 20%).");
                        return;
                    }
                    if (loanInterestRate <= 0.001 || loanInterestRate > 1.0) {
                        UIHelpers.showAlert(Alert.AlertType.INFORMATION, "Interest rate must be greater than or equal to 0.001.");
                        return;
                    }
                    String loanType = loanTypeProperty.get();
                    LoanAccount loanAccount;
                    if (editedAccount != null) {
                        loanAccount = new LoanAccount(
                                customerId,
                                accountBalance,
                                ((LoanAccount)editedAccount).getPaymentsMade(),
                                ((LoanAccount)editedAccount).getInitialAmount(),
                                ((LoanAccount)editedAccount).getCurrentPaymentDue(),
                                loanInterestRate,
                                editedAccount.getDateAccountOpened(),
                                ((LoanAccount)editedAccount).getDatePaymentDue(),
                                ((LoanAccount)editedAccount).getLastPaymentDate(),
                                ((LoanAccount)editedAccount).getMissedPaymentFlag(),
                                loanType.toLowerCase()
                        );
                    } else {
                        loanAccount = new LoanAccount(
                                customerId,
                                accountBalance,
                                loanInterestRate,
                                loanType.toLowerCase()
                        );
                    }
                    LoanAccount.loans.add(loanAccount);
                    LoanAccount.exportFile();
                    break;
                case CD:
                    double cdInterestRate;
                    if (dateCDDueProperty.getValue() == null) {
                        UIHelpers.showAlert(Alert.AlertType.INFORMATION, "A CD must have a date due.");
                        return;
                    }
                    try {
                        cdInterestRate = Double.parseDouble(interestRateProperty.get());
                    } catch (NumberFormatException | NullPointerException e) {
                        UIHelpers.showAlert(Alert.AlertType.INFORMATION, "Interest rate must be a decimal " +
                                "number (such as \"0.2\" for 20%).");
                        return;
                    }
                    if (cdInterestRate <= 0.001 || cdInterestRate > 1.0) {
                        UIHelpers.showAlert(Alert.AlertType.INFORMATION, "Interest rate must be greater than or equal to 0.001.");
                        return;
                    }
                    CD cd = new CD(
                            customerId,
                            accountBalance,
                            cdInterestRate,
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
