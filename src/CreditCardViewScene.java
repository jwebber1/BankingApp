import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.io.FileNotFoundException;
import java.time.LocalDate;
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

    private ComboBox customerBox; // The customer selection box (which allows searching by customer).
    // Stores the "customerBox"'s selected customer.
    private final StringProperty customerProperty = new SimpleStringProperty("");

    private final StringProperty costProperty = new SimpleStringProperty("");
    private final StringProperty descriptionProperty = new SimpleStringProperty("");
    private final Property<LocalDate> dateProperty = new SimpleObjectProperty<>();

    public CreditCardViewScene(int customerId) {
        this.customerId = customerId;

        UIHelpers.setBaseSceneSettings(root, fieldVBox);
        UIHelpers.setButtonSettings(buttonHBox);

        if (customerId == 0) {
            ObservableList<String> personNames = FXCollections.observableArrayList();
            for (Person person : Person.people) {
                personNames.add(person.lastName + ", " + person.firstName);
            }
            customerBox = UIHelpers.createComboBox(personNames, customerProperty);
            fieldVBox.getChildren().add(UIHelpers.createHBox("Customer:", customerBox));
            customerBox.getSelectionModel().selectedIndexProperty().addListener(x -> {
                int selectedCustomerId = customerBox.getSelectionModel().getSelectedIndex();
                Person selectedCustomer = Person.people.get(selectedCustomerId);
                int id = selectedCustomer.id;
                ObservableList purchases = FXCollections.observableArrayList(
                        CreditCardPurchase.search(id)
                );
                creditCardPurchaseTable.setItems(purchases);
                creditCardPurchaseTable.refresh();
            });
        }

        setupCreditCardPurchaseTable();

        if (UIHelpers.currentUserLevel != 0) {
            Label makePurchaseLabel = new Label("Make Purchase");
            fieldVBox.getChildren().add(makePurchaseLabel);

            HBox balanceField = UIHelpers.createHBox("Cost:", UIHelpers.createBalanceField(costProperty));
            fieldVBox.getChildren().add(balanceField);

            TextField descriptionField = UIHelpers.createTextField(descriptionProperty);
            fieldVBox.getChildren().add(UIHelpers.createHBox("Description:", descriptionField));

            DatePicker datePicker = UIHelpers.createDatePicker(dateProperty);
            fieldVBox.getChildren().add(UIHelpers.createHBox("Date:", datePicker));

            Button backButton = new Button("Save");
            backButton.setOnAction(x -> {
                LoanAccount.
            });
        }

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

        if (customerId != 0) {
            ObservableList purchases = FXCollections.observableArrayList(CreditCardPurchase.search(customerId));
            creditCardPurchaseTable.setItems(purchases);
        }
    }
}
