import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class AccountEditScene {
    private Account selectedAccount;

    private final StringProperty accountBalanceProperty = new SimpleStringProperty("$");

    private VBox fieldVBox = new VBox();
    StackPane root = new StackPane(fieldVBox);

    public AccountEditScene(Account selectedAccount) {
        UIHelpers.setBaseSceneSettings(root, fieldVBox);

        this.selectedAccount = selectedAccount;

        HBox hBox = UIHelpers.createHBox(
                "Account Balance:", UIHelpers.createBalanceField(accountBalanceProperty));
        fieldVBox.getChildren().add(hBox);
        UIHelpers.createBalanceField(accountBalanceProperty);
    }
}
