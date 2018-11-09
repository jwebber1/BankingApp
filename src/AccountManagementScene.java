import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.io.FileNotFoundException;

class AccountManagementScene {
    private VBox fieldVBox = new VBox();
    StackPane root = new StackPane(fieldVBox);
    private TableView<Account> accountTable = new TableView<>();

    private HBox buttonHBox = new HBox();

    AccountManagementScene() {
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
                    UICreationHelpers.navigateToScene(new NavigationScene().root);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            buttonHBox.getChildren().addAll(addButton, editButton, closeButton, cancelButton);
        }

        fieldVBox.getChildren().addAll(accountTable, buttonHBox);
    }

    private void setupAccountTable() {
        TableColumn<Account, String> type = new TableColumn<>("Type");
        TableColumn<Account, String> dateOpened = new TableColumn<>("Date Opened");
        TableColumn<Account, String> balance = new TableColumn<>("Balance");

        type.setCellValueFactory(new PropertyValueFactory<>("mainAccountType"));
        dateOpened.setCellValueFactory(new PropertyValueFactory<>("dateAccountOpened"));
        balance.setCellValueFactory(new PropertyValueFactory<>("accountBalance"));

        setAccountTableItems();

        if (UICreationHelpers.currentUserLevel != 0) {
            TableColumn<Account, String> ssn = new TableColumn<>("SSN");
            ssn.setCellValueFactory(new PropertyValueFactory<>("customerID"));
            accountTable.getColumns().add(ssn);
        }

        accountTable.getColumns().addAll(type, dateOpened, balance);
    }

    private void editAccount() {
        Account selectedAccount = accountTable.getSelectionModel().getSelectedItem();
        if (selectedAccount == null) {
            return;
        }
        UICreationHelpers.navigateToScene(new AccountCreationScene(selectedAccount).root);
    }

    private void closeAccount() {
        Account selectedAccount = accountTable.getSelectionModel().getSelectedItem();
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
                    SavingAccount.exportFile(SavingAccount.savingAccounts);
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
        // TODO: Get other accounts.
        accountTable.setItems(FXCollections.observableArrayList());
        ObservableList<Account> accounts = FXCollections.observableArrayList();
        if (UICreationHelpers.currentUserLevel == 0) {
            accounts.addAll(CheckingAccount.searchCheckingAccountsByCustomerID(UICreationHelpers.currentUser.id));
            accounts.addAll(LoanAccount.search(UICreationHelpers.currentUser.id));
            accounts.addAll(CD.search(UICreationHelpers.currentUser.id));
        } else {
            accounts.addAll(CheckingAccount.checkingAccounts);
            accounts.addAll(LoanAccount.loans);
            accounts.addAll(CD.cds);
        }
        accountTable.setItems(accounts);
        accountTable.refresh();
    }

    private void withdrawOrDeposit(boolean isWithdraw) {
        Account selectedAccount = accountTable.getSelectionModel().getSelectedItem();
        if (selectedAccount == null) return;
        try {
            if (selectedAccount instanceof LoanAccount) {
                if (isWithdraw) {
                    UICreationHelpers.showAlert(Alert.AlertType.INFORMATION, "You cannot withdraw from a loan account.");
                } else {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                            "Would you like to make a payment on this loan?", ButtonType.YES, ButtonType.NO);
                    alert.showAndWait();

                    if (alert.getResult() == ButtonType.YES) {
                        ((LoanAccount) selectedAccount).makeLoanPayment();

                        if (selectedAccount.accountBalance <= 0) {
                            UICreationHelpers.showAlert(Alert.AlertType.INFORMATION,
                                    "The loan has been paid off in full, and will now be closed.");
                            LoanAccount.loans.remove(selectedAccount);
                        }
                        LoanAccount.exportFile();
                        setAccountTableItems();
                    }
                }
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
