import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.io.FileNotFoundException;

class WithdrawOrDepositScene {
    private boolean isWithdraw;
    private Account editedAccount;
    private final StringProperty accountBalanceProperty = new SimpleStringProperty("$");

    private VBox fieldVBox = new VBox();
    StackPane root = new StackPane(fieldVBox);

    private HBox buttonHBox = new HBox();

    WithdrawOrDepositScene(Account editedAccount, boolean isWithdraw) {
        this.isWithdraw = isWithdraw;
        this.editedAccount = editedAccount;

        UICreationHelpers.setBaseSceneSettings(root, fieldVBox);
        UICreationHelpers.setButtonSettings(buttonHBox);

        Label balanceLabel = new Label("Balance: $" + editedAccount.accountBalance);
        HBox amountField = UICreationHelpers.createHBox("Amount:", UICreationHelpers.createBalanceField(accountBalanceProperty));

        // Save Button
        Button withdrawOrDepositButton;
        if (isWithdraw) {
            withdrawOrDepositButton = new Button("Withdraw");
        } else {
            withdrawOrDepositButton = new Button("Deposit");
        }
        withdrawOrDepositButton.setOnAction(x -> {
            try {
                saveAccount();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        buttonHBox.getChildren().add(withdrawOrDepositButton);

        fieldVBox.getChildren().addAll(balanceLabel, amountField, buttonHBox);
    }

    void saveAccount() {
        if (isWithdraw) {
            editedAccount.accountBalance -= Double.parseDouble(accountBalanceProperty.get().replace("$", ""));
        } else {
            editedAccount.accountBalance += Double.parseDouble(accountBalanceProperty.get().replace("$", ""));
        }
        try {
            if (editedAccount instanceof CheckingAccount) {
    //            CheckingAccount.checkingAccounts.remove(editedAccount);
                    CheckingAccount.exportFile();
            } else if (editedAccount instanceof LoanAccount) {
    //            LoanAccount.loans.remove(editedAccount);
                LoanAccount.exportFile();
            }
            UICreationHelpers.navigateToScene(new AccountManagementScene().root);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
