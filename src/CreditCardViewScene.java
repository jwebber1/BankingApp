import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.io.FileNotFoundException;
import java.util.Date;

/**
 * Allows viewing and stopping of checks for an account.
 *
 * @author  Hunter Berten
 */

public class CreditCardViewScene {
    private VBox fieldVBox = new VBox();
    StackPane root = new StackPane(fieldVBox);

    private int customerId;

    private TableView<CreditCardPurchase> creditCardPurchaseTable = new TableView<>();

    private HBox buttonHBox = new HBox();

    public CreditCardViewScene(int customerId) {
        this.customerId = customerId;

        UIHelpers.setBaseSceneSettings(root, fieldVBox);
        UIHelpers.setButtonSettings(buttonHBox);

        setupCreditCardPurchaseTable();

        // Back Button
        Button backButton = new Button("Back");
        backButton.setOnAction(x -> UIHelpers.navigateBackToAccountManagement());

        buttonHBox.getChildren().add(backButton);
        fieldVBox.getChildren().addAll(creditCardPurchaseTable, buttonHBox);
    }

    private void setupCreditCardPurchaseTable() {
        TableColumn<CreditCardPurchase, String> description = new TableColumn<>("Description");
        TableColumn<CreditCardPurchase, String> price = new TableColumn<>("Price");
        TableColumn<CreditCardPurchase, String> dateOfPurchase = new TableColumn<>("Date Of Purchase");

        description.setCellValueFactory(new PropertyValueFactory<>("description"));
        price.setCellValueFactory(new PropertyValueFactory<>("price"));
        dateOfPurchase.setCellValueFactory(new PropertyValueFactory<>("dateOfPurchase"));

        creditCardPurchaseTable.getColumns().addAll(description, price, dateOfPurchase);

        ObservableList purchases = FXCollections.observableArrayList(CreditCardPurchase.search(customerId));
        creditCardPurchaseTable.setItems(purchases);
    }
}
