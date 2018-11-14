import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

class AccountManagementScene {
    private VBox fieldVBox = new VBox();
    StackPane root = new StackPane(fieldVBox);

    private TableView<SavingAccount> savingsTable = new TableView<>();
    private TableView<LoanAccount> loanTable = new TableView<>();
    private TableView<CheckingAccount> checkingTable = new TableView<>();
    private TableView<CD> cdTable = new TableView<>();

    private HBox buttonHBox = new HBox();

    ObservableList<Account> accounts;

    AccountManagementScene() {
        this(new ArrayList<>());
    }

    AccountManagementScene(ArrayList<Account> accounts) {
        this.accounts = FXCollections.observableArrayList(accounts);

        UICreationHelpers.setBaseSceneSettings(root, fieldVBox);
        UICreationHelpers.setButtonSettings(buttonHBox);

        setupAccountTable();

        if (UICreationHelpers.currentUserLevel == 0) {
            // Deposit Button
            Button depositButton = new Button("Deposit");
            depositButton.setOnAction(x -> withdrawOrDeposit(false));

            // Withdraw Button
            Button withdrawButton = new Button("Withdraw");
            withdrawButton.setOnAction(x -> withdrawOrDeposit(true));

            buttonHBox.getChildren().addAll(depositButton, withdrawButton);
        } else {
            // Add Button
            Button addButton = new Button("Add");
            addButton.setOnAction(x -> UICreationHelpers.navigateToScene(new AccountCreationScene().root));

            // Edit Button
            Button editButton = new Button("Edit");
            editButton.setOnAction(x -> editAccount());

            // Close Button
            Button closeButton = new Button("Close");
            closeButton.setOnAction(x -> closeAccount());

            // Cancel Button
            Button cancelButton = new Button("Cancel");
            cancelButton.setOnAction(x -> {
                try {
                    UICreationHelpers.navigateToScene(new AccountTypeSelectionScene().root);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            buttonHBox.getChildren().addAll(addButton, editButton, closeButton, cancelButton);
        }
    }

    private void setupAccountTable() {
        if (!accounts.isEmpty()) {
            if (accounts.get(0) instanceof CheckingAccount) {
                TableColumn<CheckingAccount, String> type = new TableColumn<>("Type");
                TableColumn<CheckingAccount, String> dateOpened = new TableColumn<>("Date Opened");
                TableColumn<CheckingAccount, String> balance = new TableColumn<>("Balance");

                type.setCellValueFactory(new PropertyValueFactory<>("mainAccountType"));
                dateOpened.setCellValueFactory(new PropertyValueFactory<>("dateAccountOpened"));
                balance.setCellValueFactory(new PropertyValueFactory<>("accountBalance"));

                checkingTable.getColumns().addAll(type, dateOpened, balance);
                fieldVBox.getChildren().addAll(checkingTable, buttonHBox);

                Button viewChecksButton = new Button("View Checks");
                viewChecksButton.setOnAction(x -> {
                    int customerId = UICreationHelpers.currentUser.id;
                    if (customerId <= 1) {
                        CheckingAccount account = checkingTable.getSelectionModel().getSelectedItem();
                        if (account == null) {
                            return;
                        }
                        customerId = account.customerID;
                    }
                    UICreationHelpers.navigateToScene(new CheckViewScene(customerId).root);
                });

                buttonHBox.getChildren().add(viewChecksButton);
            } else if (accounts.get(0) instanceof SavingAccount) {
                TableColumn<SavingAccount, String> type = new TableColumn<>("Type");
                TableColumn<SavingAccount, String> dateOpened = new TableColumn<>("Date Opened");
                TableColumn<SavingAccount, String> balance = new TableColumn<>("Balance");

                type.setCellValueFactory(new PropertyValueFactory<>("mainAccountType"));
                dateOpened.setCellValueFactory(new PropertyValueFactory<>("dateAccountOpened"));
                balance.setCellValueFactory(new PropertyValueFactory<>("accountBalance"));

                savingsTable.getColumns().addAll(type, dateOpened, balance);
                fieldVBox.getChildren().addAll(savingsTable, buttonHBox);
            } else if (accounts.get(0) instanceof LoanAccount) {
                if (UICreationHelpers.currentUserLevel != 0) {
                    TableColumn<LoanAccount, String> ssn = new TableColumn<>("SSN");
                    ssn.setCellValueFactory(new PropertyValueFactory<>("customerID"));
                    loanTable.getColumns().add(ssn);
                }

                TableColumn<LoanAccount, String> type = new TableColumn<>("Type");
                TableColumn<LoanAccount, String> dateOpened = new TableColumn<>("Date Opened");
                TableColumn<LoanAccount, String> balance = new TableColumn<>("Balance");

                type.setCellValueFactory(new PropertyValueFactory<>("mainAccountType"));
                dateOpened.setCellValueFactory(new PropertyValueFactory<>("dateAccountOpened"));
                balance.setCellValueFactory(new PropertyValueFactory<>("accountBalance"));

                TableColumn<LoanAccount, String> interestRate = new TableColumn<>("Interest Rate");
                interestRate.setCellValueFactory(new PropertyValueFactory<>("interestRate"));

                TableColumn<LoanAccount, String> datePaymentDue = new TableColumn<>("Date Payment Due");
                datePaymentDue.setCellValueFactory(new PropertyValueFactory<>("datePaymentDue"));

                TableColumn<LoanAccount, String> paymentsLeft = new TableColumn<>("Payments Remaining");
                paymentsLeft.setCellValueFactory(new PropertyValueFactory<>("paymentsLeft"));

                loanTable.getColumns().addAll(type, dateOpened, balance, interestRate, datePaymentDue, paymentsLeft);
                fieldVBox.getChildren().addAll(loanTable, buttonHBox);
            } else if (accounts.get(0) instanceof CD) {
                if (UICreationHelpers.currentUserLevel != 0) {
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
            }
        }

        setAccountTableItems(FXCollections.observableArrayList(accounts));
    }

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

    private void editAccount() {
        Account selectedAccount = getSelectedAccount();
        if (selectedAccount == null) {
            return;
        }
        UICreationHelpers.navigateToScene(new AccountCreationScene(selectedAccount).root);
    }

    private void closeAccount() {
        Account selectedAccount = getSelectedAccount();
        if (selectedAccount == null) {
            return;
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                "Close account? (This action cannot be undone.)", ButtonType.YES, ButtonType.NO);
        alert.showAndWait();

        if (alert.getResult() == ButtonType.YES) {
            try {
                if (selectedAccount instanceof CD) {
                    CD.cds.remove(selectedAccount);
                    CD.exportFile();
                } else if (selectedAccount instanceof LoanAccount) {
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

    private void setAccountTableItems() {
        setAccountTableItems(FXCollections.observableArrayList());
    }
    private void setAccountTableItems(ObservableList<Account> accounts) {
        if (accounts.isEmpty()) {
            if (UICreationHelpers.currentUserLevel == 0) {
                accounts.addAll(CheckingAccount.search(UICreationHelpers.currentUser.id));
                accounts.addAll(LoanAccount.search(UICreationHelpers.currentUser.id));
                accounts.addAll(CD.search(UICreationHelpers.currentUser.id));
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

    private void withdrawOrDeposit(boolean isWithdraw) {
        Account selectedAccount = getSelectedAccount();
        if (selectedAccount == null) return;
        try {
            if (selectedAccount instanceof LoanAccount) {
                if (isWithdraw) {
                    UICreationHelpers.showAlert(Alert.AlertType.INFORMATION, "You cannot withdraw from a loan account.");
                }
//                else {
//                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
//                            "Would you like to make a payment on this loan?", ButtonType.YES, ButtonType.NO);
//                    alert.showAndWait();
//
//                    if (alert.getResult() == ButtonType.YES) {
//                        ((LoanAccount) selectedAccount).makeLoanPayment();
//
//                        if (selectedAccount.accountBalance <= 0) {
//                            UICreationHelpers.showAlert(Alert.AlertType.INFORMATION,
//                                    "The loan has been paid off in full, and will now be closed.");
//                            LoanAccount.loans.remove(selectedAccount);
//                        }
//                        LoanAccount.exportFile();
//                        setAccountTableItems();
//                    }
//                }
            } else if (selectedAccount instanceof CD) {
                if (isWithdraw) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                            "Would you like to withdraw all money in this CD (closing it in the process)?",
                            ButtonType.YES, ButtonType.NO);
                    alert.showAndWait();

                    if (alert.getResult() == ButtonType.YES) {
                        ((CD) selectedAccount).withdraw();
                        CD.cds.remove(selectedAccount);
                        CD.exportFile();
                        setAccountTableItems();
                    }
                } else {
                    UICreationHelpers.showAlert(Alert.AlertType.INFORMATION, "You cannot deposit into a CD.");
                }
            } else {
                UICreationHelpers.navigateToScene(new WithdrawOrDepositScene(selectedAccount, isWithdraw).root);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
