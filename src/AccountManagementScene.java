import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class AccountManagementScene {
    private VBox fieldVBox = new VBox();
    StackPane root = new StackPane(fieldVBox);
    TableView<Account> accountTable = new TableView<>();

    private HBox buttonHBox = new HBox();

    public AccountManagementScene() {
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

            // Cancel Button
            Button cancelButton = new Button("Cancel");
            cancelButton.setOnAction(x -> {
                try {
                    UICreationHelpers.navigateToScene(new NavigationScene().root);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            buttonHBox.getChildren().addAll(addButton, editButton, cancelButton);
        }

        fieldVBox.getChildren().addAll(accountTable, buttonHBox);
    }

    public void setupAccountTable() {
        TableColumn<Account, String> type = new TableColumn<>("Type");
        TableColumn<Account, String> dateOpened = new TableColumn<>("Date Opened");
        TableColumn<Account, String> balance = new TableColumn<>("Balance");
        ObservableList<Account> accounts = FXCollections.observableArrayList();
        // TODO: Get other accounts.
        if (UICreationHelpers.currentUserLevel == 0) {
            accounts.addAll(CheckingAccount.searchCheckingAccountsByCustomerID(UICreationHelpers.currentUser.id));
            accounts.addAll(LoanAccount.search(UICreationHelpers.currentUser.id));
        } else {
            accounts.addAll(CheckingAccount.checkingAccounts);
            accounts.addAll(LoanAccount.loans);
        }

        type.setCellValueFactory(new PropertyValueFactory<>("mainAccountType"));
        dateOpened.setCellValueFactory(new PropertyValueFactory<>("dateAccountOpened"));
        balance.setCellValueFactory(new PropertyValueFactory<>("accountBalance"));
        accountTable.getColumns().addAll(type, dateOpened, balance);
        accountTable.setItems(accounts);
    }

    public void editAccount() {
        Account selectedAccount = accountTable.getSelectionModel().getSelectedItem();
        if (selectedAccount == null) {
            return;
        }
        UICreationHelpers.navigateToScene(new AccountCreationScene(selectedAccount).root);
    }

    public void withdrawOrDeposit(boolean isWithdraw) {
        Account selectedAccount = accountTable.getSelectionModel().getSelectedItem();
        if (selectedAccount == null) {
            return;
        }
        UICreationHelpers.navigateToScene(new WithdrawOrDepositScene(selectedAccount, isWithdraw).root);
    }
}
