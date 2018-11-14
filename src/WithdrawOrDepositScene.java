import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.io.FileNotFoundException;
import java.util.ArrayList;

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

        // Cancel Button
        Button cancelButton = new Button("Cancel");
        cancelButton.setOnAction(x -> {
            try {
                navigateBack();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        buttonHBox.getChildren().addAll(cancelButton, withdrawOrDepositButton);
        fieldVBox.getChildren().addAll(balanceLabel, amountField, buttonHBox);
    }

    private void saveAccount() {

        try {
            if (editedAccount instanceof CheckingAccount) {
    //            CheckingAccount.checkingAccounts.remove(editedAccount);
                ((CheckingAccount)editedAccount).withdraw(editedAccount.accountBalance);
                CheckingAccount.exportFile();
//                if (isWithdraw) ((CheckingAccount) editedAccount).withdraw(editedAccount, editedAccount.accountBalance);
            } else if (editedAccount instanceof LoanAccount) {
//                LoanAccount.loans.remove(editedAccount);
                ((LoanAccount)editedAccount).makePayment(Double.parseDouble(accountBalanceProperty.get().replace("$", "")));
                LoanAccount.exportFile();
            } else if (editedAccount instanceof SavingAccount) {
//                LoanAccount.loans.remove(editedAccount);
                if (isWithdraw) {
                    editedAccount.accountBalance -= Double.parseDouble(accountBalanceProperty.get().replace("$", ""));
                } else {
                    editedAccount.accountBalance += Double.parseDouble(accountBalanceProperty.get().replace("$", ""));
                }
//                ((SavingAccount)editedAccount).(Double.parseDouble(accountBalanceProperty.get().replace("$", "")));
                LoanAccount.exportFile();
            }
            navigateBack();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void navigateBack() {
        ArrayList<Account> accounts = new ArrayList<>();
        if (editedAccount instanceof CheckingAccount) {
            accounts.add(CheckingAccount.search(UICreationHelpers.currentUser.id));
        } else if (editedAccount instanceof LoanAccount) {
            accounts.addAll(LoanAccount.search(UICreationHelpers.currentUser.id));
        } else if (editedAccount instanceof CD) {
            accounts.addAll(CD.search(UICreationHelpers.currentUser.id));
        } else if (editedAccount instanceof SavingAccount) {
//            accounts.addAll(SavingAccount.search(UICreationHelpers.currentUser.id));
        }
        UICreationHelpers.navigateToScene(new AccountManagementScene(accounts).root);
    }
}
