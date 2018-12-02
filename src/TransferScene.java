import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 * Allows transferring of funds between a saving and checking account.
 *
 * @author  Hunter Berten
 */

public class TransferScene {
    // "fieldVBox" stacks all UI elements of the scene vertically.
    // "root" contains all UI of the scene (this is transferred on navigation to another page).
    // "buttonHBox" holds the buttons at the bottom of the scene.
    private VBox fieldVBox = new VBox();
    StackPane root = new StackPane(fieldVBox);
    private HBox buttonHBox = new HBox();

    private SavingAccount savingAccount; // The customer's saving account.
    private CheckingAccount checkingAccount; // The customer's checking account.

    public TransferScene(SavingAccount savingAccount, CheckingAccount checkingAccount) {
        this.savingAccount = savingAccount;
        this.checkingAccount = checkingAccount;

        // Sets base scene settings (padding, etc.).
        UIHelpers.setBaseSceneSettings(root, fieldVBox);
        UIHelpers.setButtonSettings(buttonHBox);
    }

}
