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

        // Edit Button
        Button addButton = new Button("Add");
        addButton.setOnAction(x -> UICreationHelpers.navigateToScene(new AccountCreationScene().root));
        buttonHBox.getChildren().add(addButton);

        // Edit Button
        Button editButton = new Button("Edit");
        editButton.setOnAction(x -> editAccount());
        buttonHBox.getChildren().add(editButton);

        // Cancel Button
        Button cancelButton = new Button("Cancel");
        cancelButton.setOnAction(x -> {
            try {
                UICreationHelpers.navigateToScene(new NavigationScene().root);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        buttonHBox.getChildren().add(cancelButton);

        fieldVBox.getChildren().addAll(accountTable, buttonHBox);
    }

    public void setupAccountTable() {
        TableColumn<Account, String> type = new TableColumn<>("Type");
        TableColumn<Account, String> dateOpened = new TableColumn<>("Date Opened");
        TableColumn<Account, String> balance = new TableColumn<>("Balance");
        ObservableList<Account> list = FXCollections.observableArrayList();
        list.addAll(CheckingAccount.checkingAccounts);
        list.addAll(LoanAccount.loans);
//        list.addAll(SavingAccount);

        type.setCellValueFactory(new PropertyValueFactory<>("accountType"));
        dateOpened.setCellValueFactory(new PropertyValueFactory<>("dateAccountOpened"));
        balance.setCellValueFactory(new PropertyValueFactory<>("accountBalance"));
        accountTable.getColumns().addAll(type, dateOpened, balance);
        accountTable.setItems(list);
    }

    public void editAccount() {
        Account selectedAccount = accountTable.getSelectionModel().getSelectedItem();
        if (selectedAccount == null) {
            return;
        }
        UICreationHelpers.navigateToScene(new AccountCreationScene(selectedAccount).root);
    }
}
