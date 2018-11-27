import Enums.AccountType;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Date;

/**
 * Allows viewing of all types of accounts, and allows navigation to adding accounts, allows selection of an account to
 * edit, close, view checks for, withdraw from, and deposit into.
 *
 * @author  Hunter Berten
 */

class AccountManagementScene {
    // "fieldVBox" stacks all UI elements of the scene vertically.
    // "root" contains all UI of the scene (this is transferred on navigation to another page).
    // "buttonHBox" holds the buttons at the bottom of the scene.
    private VBox fieldVBox = new VBox();
    StackPane root = new StackPane(fieldVBox);
    private HBox buttonHBox = new HBox();

    private TableView<SavingAccount> savingsTable = new TableView<>(); // The table that holds savings accounts.
    private TableView<LoanAccount> loanTable = new TableView<>(); // The table that holds loans.
    private TableView<CheckingAccount> checkingTable = new TableView<>(); // The table that holds checking accounts.
    private TableView<CD> cdTable = new TableView<>(); // The table that holds cds.

    private ComboBox customerBox; // The customer selection box (which allows searching by customer).

    // Stores the "customerBox"'s selected customer.
    private final StringProperty customerProperty = new SimpleStringProperty("");

    ObservableList<Account> accounts; // The displayed accounts.

    // Constructor
    AccountManagementScene(ArrayList<Account> accounts) {
        this.accounts = FXCollections.observableArrayList(accounts);

        // Sets base scene settings (padding, etc.).
        UIHelpers.setBaseSceneSettings(root, fieldVBox);
        UIHelpers.setButtonSettings(buttonHBox);

        ObservableList<String> personNames = FXCollections.observableArrayList();
        for (Person person : Person.people) {
            personNames.add(person.lastName + ", " + person.firstName);
        }
        customerBox = UIHelpers.createComboBox(personNames, customerProperty);
        fieldVBox.getChildren().add(UIHelpers.createHBox("Customer:", customerBox));
        customerBox.getSelectionModel().selectedIndexProperty().addListener(x -> changeSelectedCustomer());

        initializeButtonBox();
        setupAccountTable();
    }

    // Injects the correct buttons into the buttonBox based on the account type.
    private void initializeButtonBox() {
        buttonHBox.getChildren().clear();
        if (UIHelpers.selectedAccountType == AccountType.LOAN) {
            // Make Payment Button
            Button makePaymentButton = new Button("Make Payment");
            makePaymentButton.setOnAction(x -> withdrawOrDeposit(false));
            buttonHBox.getChildren().addAll(makePaymentButton);

            // C.C. Purchases Button
            Button ccPurchasesButton = new Button("C.C. Purchases");
            ccPurchasesButton.setOnAction(x -> ccPurchases());
            buttonHBox.getChildren().addAll(ccPurchasesButton);
        } else if (!(UIHelpers.selectedAccountType == AccountType.CD)) {
            // If not viewing CDs (since CDs can just be closed- which withdraws their money).

            // Deposit Button
            Button depositButton = new Button("Deposit");
            depositButton.setOnAction(x -> withdrawOrDeposit(false));

            // Withdraw Button
            Button withdrawButton = new Button("Withdraw");
            withdrawButton.setOnAction(x -> withdrawOrDeposit(true));
            buttonHBox.getChildren().addAll(depositButton, withdrawButton);
        }

        if (UIHelpers.currentUserLevel != 0) {
            // Add Button
            Button addButton = new Button("Add");
            addButton.setOnAction(x -> UIHelpers.navigateToScene(new AccountAddEditScene().root));

            // Edit Button
            Button editButton = new Button("Edit");
            editButton.setOnAction(x -> editAccount());

            // Close Button
            Button closeButton = new Button("Close");
            closeButton.setOnAction(x -> closeAccount());

            buttonHBox.getChildren().addAll(addButton, editButton, closeButton);
        }
    }

    // Filters the account box by the selected customer.
    private void changeSelectedCustomer() {
        int selectedCustomerId = customerBox.getSelectionModel().getSelectedIndex();
        Person selectedCustomer = Person.people.get(selectedCustomerId);
        int customerId = selectedCustomer.id;
        setupAccountTable(customerId);
    }

    // Initializes the account table for the current account type.
    private void setupAccountTable() {
        setupAccountTable(0);
    }
    private void setupAccountTable(int searchId) {
        initializeButtonBox(); // Initializes the button box with the correct buttons.

        // Removes all previous tables and buttons.
        fieldVBox.getChildren().removeAll(savingsTable, checkingTable, cdTable, loanTable, buttonHBox);

        // Ensures the correct accounts are displayed (the selected type and only the current user's if they are only a
        // customer level user).
        if (searchId != 0) {
            switch (UIHelpers.selectedAccountType) {
                case CHECKING:
                    accounts = FXCollections.observableArrayList(CheckingAccount.search(searchId));
                    break;
                case LOAN:
                    accounts = FXCollections.observableArrayList(LoanAccount.search(searchId));
                    break;
                case SAVING:
                    accounts = FXCollections.observableArrayList(SavingAccount.search(searchId));
                    break;
                case CD:
                    accounts = FXCollections.observableArrayList(CD.search(searchId));
                    break;
            }
        } else if (UIHelpers.currentUserLevel != 0) {
            switch (UIHelpers.selectedAccountType) {
                case CHECKING:
                    accounts = FXCollections.observableArrayList(CheckingAccount.checkingAccounts);
                    break;
                case LOAN:
                    accounts = FXCollections.observableArrayList(LoanAccount.loans);
                    break;
                case SAVING:
                    accounts = FXCollections.observableArrayList(SavingAccount.savingAccounts);
                    break;
                case CD:
                    accounts = FXCollections.observableArrayList(CD.cds);
                    break;
            }
        }

        // Adds the correct fields to the table for the selected account type.
        if (!accounts.isEmpty() && accounts.get(0) != null) {
            switch (UIHelpers.selectedAccountType) {
                case CHECKING: {
                    checkingTable.getColumns().clear();
                    if (UIHelpers.currentUserLevel != 0) {
                        TableColumn<CheckingAccount, String> ssn = new TableColumn<>("SSN");
                        ssn.setCellValueFactory(new PropertyValueFactory<>("customerID"));
                        checkingTable.getColumns().add(ssn);
                    }

                    TableColumn<CheckingAccount, String> type = new TableColumn<>("Type");
                    TableColumn<CheckingAccount, String> dateOpened = new TableColumn<>("Date Opened");
                    TableColumn<CheckingAccount, String> balance = new TableColumn<>("Balance");

                    type.setCellValueFactory(new PropertyValueFactory<>("accountType"));
                    dateOpened.setCellValueFactory(new PropertyValueFactory<>("dateAccountOpened"));
                    balance.setCellValueFactory(new PropertyValueFactory<>("accountBalance"));

                    checkingTable.getColumns().addAll(type, dateOpened, balance);
                    fieldVBox.getChildren().addAll(checkingTable, buttonHBox);

                    Button viewChecksButton = new Button("View Checks");
                    viewChecksButton.setOnAction(x -> {
                        int customerId = UIHelpers.currentUser.id;
                        if (customerId <= 1) {
                            CheckingAccount account = checkingTable.getSelectionModel().getSelectedItem();
                            if (account == null) {
                                return;
                            }
                            customerId = account.customerID;
                        }
                        UIHelpers.navigateToScene(new CheckViewScene(customerId).root);
                    });

                    buttonHBox.getChildren().add(viewChecksButton);
                    break;
                }
                case SAVING: {
                    savingsTable.getColumns().clear();
                    if (UIHelpers.currentUserLevel != 0) {
                        TableColumn<SavingAccount, String> ssn = new TableColumn<>("SSN");
                        ssn.setCellValueFactory(new PropertyValueFactory<>("customerID"));
                        savingsTable.getColumns().add(ssn);
                    }

                    TableColumn<SavingAccount, String> dateOpened = new TableColumn<>("Date Opened");
                    TableColumn<SavingAccount, String> balance = new TableColumn<>("Balance");
                    TableColumn<SavingAccount, String> currentInterestRate = new TableColumn<>("Interest");

                    dateOpened.setCellValueFactory(new PropertyValueFactory<>("dateAccountOpened"));
                    balance.setCellValueFactory(new PropertyValueFactory<>("accountBalance"));
                    currentInterestRate.setCellValueFactory(new PropertyValueFactory<>("currentInterestRate"));

                    savingsTable.getColumns().addAll(dateOpened, balance, currentInterestRate);
                    fieldVBox.getChildren().addAll(savingsTable, buttonHBox);
                    break;
                }
                case LOAN: {
                    loanTable.getColumns().clear();
                    if (UIHelpers.currentUserLevel != 0) {
                        TableColumn<LoanAccount, String> ssn = new TableColumn<>("SSN");
                        ssn.setCellValueFactory(new PropertyValueFactory<>("customerID"));
                        loanTable.getColumns().add(ssn);
                    }

                    TableColumn<LoanAccount, String> type = new TableColumn<>("Type");
                    TableColumn<LoanAccount, String> dateOpened = new TableColumn<>("Date Opened");
                    TableColumn<LoanAccount, String> balance = new TableColumn<>("Balance");
                    TableColumn<LoanAccount, String> calculatedBalance = new TableColumn<>("Initial Amount");
                    TableColumn<LoanAccount, String> currentPayment = new TableColumn<>("Current Payment");
                    TableColumn<LoanAccount, String> interestDue = new TableColumn<>("Interest Due");

                    type.setCellValueFactory(new PropertyValueFactory<>("accountType"));
                    dateOpened.setCellValueFactory(new PropertyValueFactory<>("dateAccountOpened"));
                    balance.setCellValueFactory(new PropertyValueFactory<>("accountBalance"));
                    calculatedBalance.setCellValueFactory(new PropertyValueFactory<>("initialAmount"));
                    currentPayment.setCellValueFactory(new PropertyValueFactory<>("currentPaymentDue"));
                    interestDue.setCellValueFactory(new PropertyValueFactory<>("interestDue"));

                    TableColumn<LoanAccount, String> interestRate = new TableColumn<>("Interest Rate");
                    interestRate.setCellValueFactory(new PropertyValueFactory<>("interestRate"));

                    TableColumn<LoanAccount, String> datePaymentDue = new TableColumn<>("Date Payment Due");
                    datePaymentDue.setCellValueFactory(new PropertyValueFactory<>("datePaymentDue"));

                    TableColumn<LoanAccount, String> paymentsLeft = new TableColumn<>("Payments Made");
                    paymentsLeft.setCellValueFactory(new PropertyValueFactory<>("paymentsMade"));

                    loanTable.getColumns().addAll(type, dateOpened, balance, calculatedBalance, interestRate, interestDue, datePaymentDue, currentPayment, paymentsLeft);
                    fieldVBox.getChildren().addAll(loanTable, buttonHBox);
                    break;
                }
                case CD: {
                    cdTable.getColumns().clear();
                    if (UIHelpers.currentUserLevel != 0) {
                        TableColumn<CD, String> ssn = new TableColumn<>("SSN");
                        ssn.setCellValueFactory(new PropertyValueFactory<>("customerID"));
                        cdTable.getColumns().add(ssn);
                    }

                    TableColumn<CD, String> type = new TableColumn<>("Type");
                    TableColumn<CD, String> dateOpened = new TableColumn<>("Date Opened");
                    TableColumn<CD, String> balance = new TableColumn<>("Balance");

                    type.setCellValueFactory(new PropertyValueFactory<>("mainAccountType"));
                    dateOpened.setCellValueFactory(new PropertyValueFactory<>("dateAccountOpened"));
                    balance.setCellValueFactory(new PropertyValueFactory<>("accountBalance"));

                    TableColumn<CD, String> dateCDDue = new TableColumn<>("Date Due");
                    dateCDDue.setCellValueFactory(new PropertyValueFactory<>("dateCDDue"));

                    cdTable.getColumns().addAll(type, dateOpened, balance, dateCDDue);
                    fieldVBox.getChildren().addAll(cdTable, buttonHBox);
                    break;
                }
            }
        } else {
            fieldVBox.getChildren().add(buttonHBox);
        }

        setAccountTableItems(FXCollections.observableArrayList(accounts));

        // Back Button
        Button backButton = new Button("Back");
        backButton.setOnAction(x -> {
            UIHelpers.selectedAccountType = null;
            try {
                UIHelpers.navigateToScene(new AccountTypeSelectionScene().root);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        buttonHBox.getChildren().add(backButton);
    }

    // Selects an account when it is clicked on in its table.
    private Account getSelectedAccount() {
        Account selectedAccount = null;
        if (!accounts.isEmpty()) {
            if (accounts.get(0) instanceof CheckingAccount) {
                selectedAccount = checkingTable.getSelectionModel().getSelectedItem();
            } else if (accounts.get(0) instanceof LoanAccount) {
                selectedAccount = loanTable.getSelectionModel().getSelectedItem();
            } else if (accounts.get(0) instanceof CD) {
                selectedAccount = cdTable.getSelectionModel().getSelectedItem();
            } else if (accounts.get(0) instanceof SavingAccount) {
                selectedAccount = savingsTable.getSelectionModel().getSelectedItem();
            }
        }
        return selectedAccount;
    }

    // The "Edit" button click event. Navigates to AccountAddEditScreen with the selected account (if an account is
    // selected- otherwise, displays an error message).
    private void editAccount() {
        Account selectedAccount = getSelectedAccount();
        if (selectedAccount == null) {
            UIHelpers.showAlert(Alert.AlertType.INFORMATION,
                    "You must select an account to edit.");
            return;
        }
        UIHelpers.navigateToScene(new AccountAddEditScene(selectedAccount).root);
    }

    // The "Close" button click event. Closes the selected account (displays error if no account selected).
    private void closeAccount() {
        Account selectedAccount = getSelectedAccount();
        if (selectedAccount == null) {
            UIHelpers.showAlert(Alert.AlertType.INFORMATION,
                    "You must select an account to close.");
            return;
        }
        if (selectedAccount instanceof CD) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                    ((CD) selectedAccount).getDateCDDue().compareTo(new Date()) > 0 ?
                            "This CD is not fully mature, and you will only be able to receive the initial deposit " +
                            "($" + ((CD) selectedAccount).withdraw() + "). Continue?" :
                            "Would you like to close this CD (withdrawing $" + ((CD) selectedAccount).withdraw() +
                            " money in the process)?",

                    ButtonType.YES, ButtonType.NO);
            alert.showAndWait();

            if (alert.getResult() == ButtonType.YES) {
                ((CD) selectedAccount).withdraw();
                CD.cds.remove(selectedAccount);
                try {
                    CD.exportFile();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                setAccountTableItems();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                    "Close account? (This action cannot be undone.)", ButtonType.YES, ButtonType.NO);
            alert.showAndWait();

            if (alert.getResult() == ButtonType.YES) {
                try {
                    if (selectedAccount instanceof LoanAccount) {
                        Alert payOffAlert = new Alert(Alert.AlertType.CONFIRMATION,
                            "This loan has not been payed off in full- closing it will pay off it's remaining balance. " +
                                        "Are you sure you wish to close this account?", ButtonType.YES, ButtonType.NO);
                        payOffAlert.showAndWait();
                        if (payOffAlert.getResult() == ButtonType.YES) {
                            ((LoanAccount) selectedAccount).payOffLoan();
                            LoanAccount.loans.remove(selectedAccount);
                            LoanAccount.exportFile();
                        }
                    } else if (selectedAccount instanceof SavingAccount) {
                        SavingAccount.savingAccounts.remove(selectedAccount);
                        SavingAccount.exportFile();
                    } else if (selectedAccount instanceof CheckingAccount) {
                        CheckingAccount.checkingAccounts.remove(selectedAccount);
                        CheckingAccount.exportFile();
                    }
                    setAccountTableItems();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // Refreshes an account table when changes are made.
    private void setAccountTableItems() {
        setAccountTableItems(FXCollections.observableArrayList());
    }
    private void setAccountTableItems(ObservableList<Account> accounts) {
        if (accounts.isEmpty()) {
            if (UIHelpers.currentUserLevel == 0) {
                accounts.addAll(CheckingAccount.search(UIHelpers.currentUser.id));
                accounts.addAll(LoanAccount.search(UIHelpers.currentUser.id));
                accounts.addAll(CD.search(UIHelpers.currentUser.id));
            }
        }
        if (!accounts.isEmpty()) {
            if (accounts.get(0) instanceof CheckingAccount) {
                ObservableList<CheckingAccount> checkingAccounts = FXCollections.observableArrayList();
                for (Account account : accounts) {
                    checkingAccounts.add((CheckingAccount) account);
                }
                checkingTable.setItems(checkingAccounts);
                checkingTable.refresh();
            } else if (accounts.get(0) instanceof LoanAccount) {
                ObservableList<LoanAccount> loanAccounts = FXCollections.observableArrayList();
                for (Account account : accounts) {
                    loanAccounts.add((LoanAccount) account);
                }
                loanTable.setItems(loanAccounts);
                loanTable.refresh();
            } else if (accounts.get(0) instanceof CD) {
                ObservableList<CD> cds = FXCollections.observableArrayList();
                for (Account account : accounts) {
                    cds.add((CD) account);
                }
                cdTable.setItems(cds);
                cdTable.refresh();
            } else if (accounts.get(0) instanceof SavingAccount) {
                ObservableList<SavingAccount> savingAccounts = FXCollections.observableArrayList();
                for (Account account : accounts) {
                    savingAccounts.add((SavingAccount) account);
                }
                savingsTable.setItems(savingAccounts);
                savingsTable.refresh();
            }
        }
    }

    // Both the "Deposit" and "Withdraw" button click events. Navigates to the DepositWithdrawScene in deposit or
    // withdraw mode respectively (or displays an error if no account is selected).
    private void withdrawOrDeposit(boolean isWithdraw) {
        Account selectedAccount = getSelectedAccount();
        if (selectedAccount == null) {
            UIHelpers.showAlert(Alert.AlertType.INFORMATION,
                    "You must select an account to " + (isWithdraw ? "withdraw from." : "deposit into."));
            return;
        }
        UIHelpers.navigateToScene(new DepositWithdrawScene(selectedAccount, isWithdraw).root);
    }

    // The "C.C. Purchases" button's click event. Navigates to the CreditCardViewScene if a credit card loan selected.
    private void ccPurchases() {
        Account selectedAccount = getSelectedAccount();
        if (selectedAccount == null || !selectedAccount.accountType.equalsIgnoreCase("credit card")) {
            UIHelpers.showAlert(Alert.AlertType.INFORMATION,
                    "You must select a credit card loan to view its purchases.");
            return;
        }
        UIHelpers.navigateToScene(new CreditCardViewScene(selectedAccount.customerID).root);
    }
}
