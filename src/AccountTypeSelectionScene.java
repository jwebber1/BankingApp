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
            ArrayList<Account> checkingAccounts = new ArrayList<>(CheckingAccount.searchCheckingAccountsByCustomerID(customerId));
            if (!checkingAccounts.isEmpty()) {
                UICreationHelpers.createButton("Checking", fieldVBox, x ->
                        UICreationHelpers.navigateToAccountManagmentScene(AccountType.CHECKING));
            }
            ArrayList<Account> loanAccounts = new ArrayList<>(LoanAccount.search(customerId));
            if (!loanAccounts.isEmpty()) {
                UICreationHelpers.createButton("Loan", fieldVBox, x ->
                        UICreationHelpers.navigateToAccountManagmentScene(AccountType.LOAN));
            }
//            ArrayList<Account> cds = new ArrayList<>(SavingAccount.search(customerId));
//            if (!cds.isEmpty()) {
//                UICreationHelpers.createButton("CD", fieldVBox, x ->
//                        UICreationHelpers.navigateToAccountManagmentScene(AccountType.CD));
//            }
            ArrayList<Account> cds = new ArrayList<>(CD.search(customerId));
            if (!cds.isEmpty()) {
                UICreationHelpers.createButton("CD", fieldVBox, x ->
                        UICreationHelpers.navigateToAccountManagmentScene(AccountType.CD));
            }
        } else {
            UICreationHelpers.createButton("Checkings", fieldVBox, x ->
                    UICreationHelpers.navigateToAccountManagmentScene(AccountType.CHECKING));
            UICreationHelpers.createButton("Loans", fieldVBox, x ->
                    UICreationHelpers.navigateToAccountManagmentScene(AccountType.LOAN));
            UICreationHelpers.createButton("Savings", fieldVBox, x ->
                    UICreationHelpers.navigateToAccountManagmentScene(AccountType.SAVING));
            UICreationHelpers.createButton("CDs", fieldVBox, x ->
                    UICreationHelpers.navigateToAccountManagmentScene(AccountType.CD));
        }
        UICreationHelpers.createButton("Back", fieldVBox, x ->
                UICreationHelpers.navigateToScene(new NavigationScene().root));
    }

    private void goToAccountManagementScene(ArrayList<Account> accounts) {
        UICreationHelpers.selectedAccounts = accounts;
        UICreationHelpers.navigateToScene(new AccountManagementScene(accounts).root);
    }
}
