import Enums.AccountType;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

/**
 * Allows user to select an account type, after which the system will navigate to the AccountManagementScene and
 * display that account type.
 *
 * @author  Hunter Berten
 */

public class AccountTypeSelectionScene {
    // "fieldVBox" stacks all UI elements of the scene vertically.
    // "root" contains all UI of the scene (this is transferred on navigation to another page).
    private VBox fieldVBox = new VBox();
    StackPane root = new StackPane(fieldVBox);

    // Constructor
    AccountTypeSelectionScene() {
        UIHelpers.setBaseSceneSettings(root, fieldVBox);

        if (UIHelpers.currentUserLevel == 0) {
            int customerId = UIHelpers.currentUser.id;
            if (CheckingAccount.search(customerId) != null) {
                UIHelpers.createButton("Checking", fieldVBox, x -> UIHelpers.navigateToAccountManagementScene(AccountType.CHECKING));
            }
            if (!new ArrayList<>(LoanAccount.search(customerId)).isEmpty()) {
                UIHelpers.createButton("Loan", fieldVBox, x -> UIHelpers.navigateToAccountManagementScene(AccountType.LOAN));
            }
            if (SavingAccount.search(customerId) != null) {
                UIHelpers.createButton("Saving", fieldVBox, x -> UIHelpers.navigateToAccountManagementScene(AccountType.SAVING));
            }
            if (!new ArrayList<>(CD.search(customerId)).isEmpty()) {
                UIHelpers.createButton("CD", fieldVBox, x -> UIHelpers.navigateToAccountManagementScene(AccountType.CD));
            }
        } else {
            UIHelpers.createButton("Checkings", fieldVBox, x -> UIHelpers.navigateToAccountManagementScene(AccountType.CHECKING));
            UIHelpers.createButton("Loans", fieldVBox, x -> UIHelpers.navigateToAccountManagementScene(AccountType.LOAN));
            UIHelpers.createButton("Savings", fieldVBox, x -> UIHelpers.navigateToAccountManagementScene(AccountType.SAVING));
            UIHelpers.createButton("CDs", fieldVBox, x -> UIHelpers.navigateToAccountManagementScene(AccountType.CD));
        }
        UIHelpers.createButton("Back", fieldVBox, x ->
                UIHelpers.navigateToScene(new NavigationScene().root));
    }
}
