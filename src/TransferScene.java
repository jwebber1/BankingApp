import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.io.FileNotFoundException;

/**
 * Allows transferring of funds between a saving and checking account.
 *
 * @author  Hunter Berten
 */

public class TransferScene {
    // "fieldVBox" stacks all UI elements of the scene vertically.
    // "buttonHBox" holds the buttons at the bottom of the scene.
    // "root" contains all UI of the scene (this is transferred on navigation to another page).
    private VBox fieldVBox = new VBox();
    private HBox buttonHBox = new HBox();
    StackPane root = new StackPane(fieldVBox);

    private SavingAccount savingAccount; // The customer's saving account.
    private CheckingAccount checkingAccount; // The customer's checking account.

    // Stores the type of transfer the user wishes to do.
    private final StringProperty transferTypeProperty = new SimpleStringProperty("Checking to Savings");

    // The amount the user wants to transfer.
    private final StringProperty amountProperty = new SimpleStringProperty("$");

    public TransferScene(SavingAccount savingAccount, CheckingAccount checkingAccount) {
        this.savingAccount = savingAccount;
        this.checkingAccount = checkingAccount;

        // Sets base scene settings (padding, etc.).
        UIHelpers.setBaseSceneSettings(root, fieldVBox);
        UIHelpers.setButtonSettings(buttonHBox);

        // Checking Balance Label
        Label checkingBalanceLabel = new Label("Checking Balance: $" + checkingAccount.accountBalance);
        fieldVBox.getChildren().add(checkingBalanceLabel);

        // Saving Balance Label
        Label savingBalanceLabel = new Label("Saving Balance: $" + savingAccount.accountBalance);
        fieldVBox.getChildren().add(savingBalanceLabel);

        // Transfer Type ComboBox
        ComboBox transferTypeField = UIHelpers.createComboBox(FXCollections.observableArrayList("Checking to Savings",
                "Savings to Checking"), transferTypeProperty);
        fieldVBox.getChildren().add(UIHelpers.createHBox("Transfer Type:", transferTypeField));

        HBox amountField = UIHelpers.createHBox("Amount:", UIHelpers.createBalanceField(amountProperty));
        fieldVBox.getChildren().add(amountField);

        // Save Button
        Button saveButton = new Button("Save");
        saveButton.setOnAction(x -> transfer());
        buttonHBox.getChildren().add(saveButton);

        // Cancel Button
        Button cancelButton = new Button("Cancel");
        cancelButton.setOnAction(x -> UIHelpers.navigateBackToAccountManagement());
        buttonHBox.getChildren().add(cancelButton);

        fieldVBox.getChildren().add(buttonHBox);
    }

    // The "Save" button click event. Performs the actual transfer.
    private void transfer() {
        double amount = Double.parseDouble(amountProperty.get().replace("$", ""));
        if (transferTypeProperty.get().equals("Checking to Savings")) {
            if (amount < 0.01 || amount > checkingAccount.accountBalance) {
                UIHelpers.showAlert(Alert.AlertType.INFORMATION,
                        "Amount must be greater than $0.00 and less than the total balance of the account being " +
                                "transferred from.");
                return;
            }
            checkingAccount.accountBalance -= amount;
            savingAccount.accountBalance += amount;
        } else {
            if (amount < 0.01 || amount > savingAccount.accountBalance) {
                UIHelpers.showAlert(Alert.AlertType.INFORMATION,
                        "Amount must be greater than $0.00 and less than the total balance of the account being " +
                                "transferred from.");
                return;
            }
            savingAccount.accountBalance -= amount;
            checkingAccount.accountBalance += amount;
        }
        try {
            SavingAccount.exportFile();
            CheckingAccount.exportFile();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        UIHelpers.navigateBackToAccountManagement();
    }
}
