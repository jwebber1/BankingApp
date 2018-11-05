import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class AccountEditScene {
    private Account selectedAccount;

    private final StringProperty accountBalanceProperty = new SimpleStringProperty("$");

    private VBox fieldVBox = new VBox();
    private StackPane root = new StackPane(fieldVBox);

    public AccountEditScene(Account selectedAccount) {
        UICreationHelpers.setBaseSceneSettings(root, fieldVBox);

        this.selectedAccount = selectedAccount;

        HBox hBox = UICreationHelpers.createHBox(
                "Account Balance:", UICreationHelpers.createBalanceField(accountBalanceProperty));
        fieldVBox.getChildren().add(hBox);
        UICreationHelpers.createBalanceField(accountBalanceProperty);
    }

    public StackPane getRoot() {
        return root;
    }
}
