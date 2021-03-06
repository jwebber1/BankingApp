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
    // "fieldVBox" stacks all UI elements of the scene vertically.
    // "root" contains all UI of the scene (this is transferred on navigation to another page).
    // "buttonHBox" holds the buttons at the bottom of the scene.
    private VBox fieldVBox = new VBox();
    StackPane root = new StackPane(fieldVBox);
    private HBox buttonHBox = new HBox();

    private boolean isWithdraw; // Whether a withdraw is occurring (if not, it is a deposit).
    private Account editedAccount; // The account being deposited into or withdrawn from.

    // The amount being deposited or withdrawn.
    private final StringProperty depositOrWithdrawAmount = new SimpleStringProperty("$");

    // Constructor
    DepositWithdrawScene(Account editedAccount, boolean isWithdraw) {
        this.isWithdraw = isWithdraw;
        this.editedAccount = editedAccount;

        // Sets base scene settings (padding, etc.).
        UIHelpers.setBaseSceneSettings(root, fieldVBox);
        UIHelpers.setButtonSettings(buttonHBox);

        // Balance Label
        Label balanceLabel = new Label("Balance: $" + editedAccount.accountBalance);
        HBox amountField = UIHelpers.createHBox("Amount:", UIHelpers.createBalanceField(depositOrWithdrawAmount));
        fieldVBox.getChildren().add(balanceLabel);

        // Payment Due Label (For Loans)
        if (editedAccount instanceof LoanAccount) {
            if (!editedAccount.accountType.equalsIgnoreCase("credit card")) {
                // Payment Due
                Label paymentDueLabel = new Label("Payment Due: $" + ((LoanAccount) editedAccount).getCurrentPaymentDue());
                fieldVBox.getChildren().add(paymentDueLabel);

                // Complete Payoff Amount
                Label completePayoffAmountLabel = new Label("Complete Payoff Amount: $" + ((LoanAccount) editedAccount).calcPayOff());
                fieldVBox.getChildren().add(completePayoffAmountLabel);
            }
        }

        // Withdraw or Deposit Button
        Button withdrawOrDepositButton;
        if (isWithdraw) {
            withdrawOrDepositButton = new Button("Withdraw");
        } else {
            withdrawOrDepositButton = new Button("Deposit");
        }
        withdrawOrDepositButton.setOnAction(x -> {
            try {
                withdrawOrDeposit();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        // Cancel Button
        Button cancelButton = new Button("Cancel");
        cancelButton.setOnAction(x -> UIHelpers.navigateBackToAccountManagement());

        buttonHBox.getChildren().addAll(cancelButton, withdrawOrDepositButton);
        fieldVBox.getChildren().addAll(amountField, buttonHBox);
    } // End of Constructor

    // The "Withdraw" and "Deposit" buttons click events.
    private void withdrawOrDeposit() {
        try {
            // Get amount and validate it.
            double amount = Double.parseDouble(depositOrWithdrawAmount.get().replace("$", ""));
            if (amount <= 0.01) {
                UIHelpers.showAlert(Alert.AlertType.INFORMATION, "You cannot enter an amount less than $0.01.");
                return;
            }
            if (amount >= 999999) {
                UIHelpers.showAlert(Alert.AlertType.INFORMATION, "The amount you've entered is too large!");
                return;
            }

            // Extra checks for Checking if Customer user level and withdrawing.
            if (editedAccount instanceof CheckingAccount) {
                // Checking Account
                if (isWithdraw) {
                    if (UIHelpers.currentUserLevel == 0) {
                        if( amount > 500) {
                            UIHelpers.showAlert(Alert.AlertType.INFORMATION, "As a customer, you cannot withdraw " +
                                    "more than $500 at a time from a checking account.");
                            return;
                        }
                        if (((CheckingAccount) editedAccount).getWithdrawsToday() > 2) {
                            UIHelpers.showAlert(Alert.AlertType.INFORMATION, "As a customer, you cannot withdraw " +
                                    "more than twice a day.");
                            return;
                        }
                    }
                    ((CheckingAccount)editedAccount).withdraw(amount);
                }
                else ((CheckingAccount)editedAccount).deposit(amount);
                CheckingAccount.exportFile();
            } else if (editedAccount instanceof LoanAccount) {
                // Extra checks if Loan Account.
                if (amount > ((LoanAccount) editedAccount).calcPayOff()) {
                    UIHelpers.showAlert(Alert.AlertType.INFORMATION, "You cannot make a payment larger than " +
                            "a loan's payoff amount.");
                    return;
                }

                if (((LoanAccount)editedAccount).getCurrentPaymentDue() > amount) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                            "This amount does not fully cover this month's payment ($" +((LoanAccount) editedAccount).getCurrentPaymentDue()
                                    + "). Additional fees may be added to your loan. Continue?", ButtonType.YES, ButtonType.NO);
                    alert.showAndWait();

                    if (alert.getResult() == ButtonType.YES) {
                        ((LoanAccount)editedAccount).makePayment(amount);
                        LoanAccount.exportFile();
                    } else {
                        return;
                    }
                } else {
                    // Make payment and export loans.
                    ((LoanAccount)editedAccount).makePayment(amount);
                    if (editedAccount.accountBalance <= 0) {
                        UIHelpers.showAlert(Alert.AlertType.INFORMATION, "This loan has been fully paid off, and will be closed.");
                        LoanAccount.loans.remove(editedAccount);
                    }
                    LoanAccount.exportFile();
                }
            } else if (editedAccount instanceof SavingAccount) {
                if (isWithdraw && amount > editedAccount.accountBalance) {
                    UIHelpers.showAlert(Alert.AlertType.INFORMATION, "You cannot withdraw more money than is in the account.");
                    return;
                }

                // Deposit/Withdraw and update Savings.
                if (isWithdraw) ((SavingAccount) editedAccount).withdraw(amount);
                else ((SavingAccount) editedAccount).deposit(amount);
                LoanAccount.exportFile();
            }
            UIHelpers.navigateBackToAccountManagement(); // Always navigate back when process fully completes.
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
} // End of DepositWithdrawScene