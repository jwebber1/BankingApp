import Enums.AccountType;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

public class AccountTypeSelectionScene {
    private VBox fieldVBox = new VBox();
    StackPane root = new StackPane(fieldVBox);

    AccountTypeSelectionScene() {
        UICreationHelpers.setBaseSceneSettings(root, fieldVBox);

        if (UICreationHelpers.currentUserLevel == 0) {
            int customerId = UICreationHelpers.currentUser.id;
            if (CheckingAccount.search(customerId) != null) {
                UICreationHelpers.createButton("Checking", fieldVBox, x -> UICreationHelpers.navigateToAccountManagementScene(AccountType.CHECKING));
            }
            if (!new ArrayList<>(LoanAccount.search(customerId)).isEmpty()) {
                UICreationHelpers.createButton("Loan", fieldVBox, x -> UICreationHelpers.navigateToAccountManagementScene(AccountType.LOAN));
            }
            if (SavingAccount.search(customerId) != null) {
                UICreationHelpers.createButton("Saving", fieldVBox, x -> UICreationHelpers.navigateToAccountManagementScene(AccountType.SAVING));
            }
            if (!new ArrayList<>(CD.search(customerId)).isEmpty()) {
                UICreationHelpers.createButton("CD", fieldVBox, x -> UICreationHelpers.navigateToAccountManagementScene(AccountType.CD));
            }
        } else {
            UICreationHelpers.createButton("Checkings", fieldVBox, x -> UICreationHelpers.navigateToAccountManagementScene(AccountType.CHECKING));
            UICreationHelpers.createButton("Loans", fieldVBox, x -> UICreationHelpers.navigateToAccountManagementScene(AccountType.LOAN));
            UICreationHelpers.createButton("Savings", fieldVBox, x -> UICreationHelpers.navigateToAccountManagementScene(AccountType.SAVING));
            UICreationHelpers.createButton("CDs", fieldVBox, x -> UICreationHelpers.navigateToAccountManagementScene(AccountType.CD));
        }
        UICreationHelpers.createButton("Back", fieldVBox, x ->
                UICreationHelpers.navigateToScene(new NavigationScene().root));
    }
}
