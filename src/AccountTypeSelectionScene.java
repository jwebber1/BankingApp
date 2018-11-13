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
                        UICreationHelpers.navigateToScene(new AccountManagementScene(checkingAccounts).root));
            }
            ArrayList<Account> loanAccounts = new ArrayList<>(LoanAccount.search(customerId));
            if (!loanAccounts.isEmpty()) {
                UICreationHelpers.createButton("Loan", fieldVBox, x ->
                        UICreationHelpers.navigateToScene(new AccountManagementScene(loanAccounts).root));
            }
            // TODO: Savings Accounts
//            ArrayList<Account> savingsAccounts = new ArrayList<>(SavingAccount.s(customerId));
//            if (!loanAccounts.isEmpty()) {
//                UICreationHelpers.createButton("Account Management", fieldVBox, x ->
//                        UICreationHelpers.navigateToScene(new AccountManagementScene(loanAccounts).root));
//            }
            ArrayList<Account> cds = new ArrayList<>(CD.search(customerId));
            if (!cds.isEmpty()) {
                UICreationHelpers.createButton("CD", fieldVBox, x ->
                    UICreationHelpers.navigateToScene(new AccountManagementScene(cds).root));
            }
        } else {
            UICreationHelpers.createButton("Checkings", fieldVBox, x ->{
                ArrayList<Account> accounts = new ArrayList<>(CheckingAccount.checkingAccounts);
                UICreationHelpers.navigateToScene(new AccountManagementScene(accounts).root);
            });
            UICreationHelpers.createButton("Loans", fieldVBox, x ->{
                ArrayList<Account> accounts = new ArrayList<>(LoanAccount.loans);
                UICreationHelpers.navigateToScene(new AccountManagementScene(accounts).root);
            });
            UICreationHelpers.createButton("Savings", fieldVBox, x ->{
                ArrayList<Account> accounts = new ArrayList<>(SavingAccount.savingAccounts);
                UICreationHelpers.navigateToScene(new AccountManagementScene(accounts).root);
            });
            UICreationHelpers.createButton("CDs", fieldVBox, x ->{
                ArrayList<Account> accounts = new ArrayList<>(CD.cds);
                UICreationHelpers.navigateToScene(new AccountManagementScene(accounts).root);
            });
            UICreationHelpers.createButton("Back", fieldVBox, x ->
                    UICreationHelpers.navigateToScene(new NavigationScene().root));
        }
    }
}