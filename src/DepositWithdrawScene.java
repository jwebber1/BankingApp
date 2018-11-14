import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.io.FileNotFoundException;

/**
 * Allows deposit or withdrawal of specified amount for an account.
 *
 * @author  Hunter Berten
 */

class DepositWithdrawScene {
    private boolean isWithdraw;
    private Account editedAccount;
    private final StringProperty accountBalanceProperty = new SimpleStringProperty("$");

    private VBox fieldVBox = new VBox();
    StackPane root = new StackPane(fieldVBox);

    private HBox buttonHBox = new HBox();

    DepositWithdrawScene(Account editedAccount, boolean isWithdraw) {
        this.isWithdraw = isWithdraw;
        this.editedAccount = editedAccount;

        UIHelpers.setBaseSceneSettings(root, fieldVBox);
        UIHelpers.setButtonSettings(buttonHBox);

        Label balanceLabel = new Label("Balance: $" + editedAccount.accountBalance);
        HBox amountField = UIHelpers.createHBox("Amount:", UIHelpers.createBalanceField(accountBalanceProperty));

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
        cancelButton.setOnAction(x -> UIHelpers.navigateBackToAccountManagement());

        buttonHBox.getChildren().addAll(cancelButton, withdrawOrDepositButton);
        fieldVBox.getChildren().addAll(balanceLabel, amountField, buttonHBox);
    }

    private void saveAccount() {
        try {
            double amount = Double.parseDouble(accountBalanceProperty.get().replace("$", ""));
            if (editedAccount instanceof CheckingAccount) {
                if (isWithdraw) ((CheckingAccount)editedAccount).withdraw(amount);
                else ((CheckingAccount)editedAccount).deposit(amount);
                ((CheckingAccount)editedAccount).withdraw(amount);
                CheckingAccount.exportFile();
            } else if (editedAccount instanceof LoanAccount) {
                if (((LoanAccount)editedAccount).getCurrentPaymentDue() > amount) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                            "This amount does not fully cover this month's payment. Additional fees may be " +
                                    "added to your loan. Continue?", ButtonType.YES, ButtonType.NO);
                    alert.showAndWait();

                    if (alert.getResult() == ButtonType.YES) {
                        ((LoanAccount)editedAccount).makePayment(amount);
                        LoanAccount.exportFile();
                    } else {
                        return;
                    }
                }
            } else if (editedAccount instanceof SavingAccount) {
                if (isWithdraw) ((SavingAccount) editedAccount).withdraw(amount);
                else ((SavingAccount) editedAccount).deposit(amount);
                LoanAccount.exportFile();
            }
            UIHelpers.navigateBackToAccountManagement();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
