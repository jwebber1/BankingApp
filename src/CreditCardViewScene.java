import Enums.AccountType;
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
import java.time.ZoneId;
import java.util.Collections;
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

    private Label balanceLabel = new Label("Account Balance: $????");

    public CreditCardViewScene(int customerId) {
        this.customerId = customerId;

        UIHelpers.setBaseSceneSettings(root, fieldVBox);
        UIHelpers.setButtonSettings(buttonHBox);

        if (customerId == 0) {
            // Create customer field and its change event.
            ObservableList<String> personNames = FXCollections.observableArrayList();
            for (Person person : Person.people) {
                personNames.add(person.lastName + ", " + person.firstName);
            }
            Collections.sort(personNames);
            customerBox = UIHelpers.createComboBox(personNames, customerProperty);
            fieldVBox.getChildren().add(UIHelpers.createHBox("Customer:", customerBox));
            customerBox.getSelectionModel().selectedIndexProperty().addListener(x -> {
                // Set shown items.
                int selectedCustomerId = customerBox.getSelectionModel().getSelectedIndex();
                Person selectedCustomer = Person.people.get(selectedCustomerId);
                int id = selectedCustomer.id;
                ObservableList purchases = FXCollections.observableArrayList(
                        CreditCardPurchase.search(id)
                );
                creditCardPurchaseTable.setItems(purchases);
                creditCardPurchaseTable.refresh();

                // Set Balance Label
                LoanAccount account = LoanAccount.search(id, "credit card");
                if (account == null) {
                    balanceLabel.textProperty().set("Account Balance: $????");
                    return;
                }
                balanceLabel.textProperty().set("Account Balance: $" + account.accountBalance);
            });
        } else {
            balanceLabel.textProperty().set("Account Balance: $" + LoanAccount.search(customerId, "credit card").accountBalance);
        }
        fieldVBox.getChildren().add(balanceLabel);

        // Basic table setup.
        setupCreditCardPurchaseTable();
        fieldVBox.getChildren().add(creditCardPurchaseTable);

        // If not user, allows for purchase creation.
        if (UIHelpers.currentUserLevel != 0) {
            Label makePurchaseLabel = new Label("Make Purchase");
            fieldVBox.getChildren().add(makePurchaseLabel);

            HBox balanceField = UIHelpers.createHBox("Cost:", UIHelpers.createBalanceField(costProperty));
            fieldVBox.getChildren().add(balanceField);

            TextField descriptionField = UIHelpers.createTextField(descriptionProperty);
            fieldVBox.getChildren().add(UIHelpers.createHBox("Description:", descriptionField));

            DatePicker datePicker = UIHelpers.createDatePicker(dateProperty);
            fieldVBox.getChildren().add(UIHelpers.createHBox("Date:", datePicker));

            Button saveButton = new Button("Save Purchase");
            saveButton.setOnAction(x -> saveCreditCardPurchase());
            buttonHBox.getChildren().add(saveButton);
        }

        // Back Button
        Button backButton = new Button("Back");
        backButton.setOnAction(x ->  {
            if (UIHelpers.selectedAccountType == null) {
                UIHelpers.navigateToScene(new NavigationScene().root);
            } else {
                UIHelpers.navigateBackToAccountManagement();
            }
        });
        buttonHBox.getChildren().add(backButton);

        fieldVBox.getChildren().add(buttonHBox);
    }

    // Runs on clicking the "Save Purchase" button. Saves the purchase created.
    private void saveCreditCardPurchase() {
        // Detailed error checking.
        int id = customerId;
        if (customerId == 0) {
            if (customerProperty.get() == null) {
                UIHelpers.showAlert(Alert.AlertType.INFORMATION, "A customer must be selected.");
                return;
            }
            int selectedCustomerId = customerBox.getSelectionModel().getSelectedIndex();
            if (selectedCustomerId == -1) {
                UIHelpers.showAlert(Alert.AlertType.INFORMATION, "A customer must be selected.");
                return;
            }
            Person selectedCustomer = Person.people.get(selectedCustomerId);
            id = selectedCustomer.id;
        }
        LoanAccount loan = LoanAccount.search(id, "credit card");
        if (loan == null) {
            UIHelpers.showAlert(Alert.AlertType.INFORMATION, "The selected customer does not have a CC account.");
            return;
        }
        String errorMessage = "";

        double cost = Double.parseDouble(costProperty.get().replace("$", ""));
        if (loan.ccPurchaseIsTooMuch(cost)) {
            errorMessage += "This purchase goes beyond this CC account's limit, and has been denied.";
        }
        if (cost <= 0) {
            errorMessage += "A purchase must be greater than $0.00.";
        }
        if (dateProperty.getValue() == null) {
            errorMessage += "You must enter a date.";
        }
        String desc = descriptionProperty.get();
        if (desc.isEmpty() || desc.length() > 255) {
            errorMessage += "A description must not be empty or exceed a length of 255 characters.";
        }

        if (!errorMessage.isEmpty()) {
            UIHelpers.showAlert(Alert.AlertType.INFORMATION, errorMessage);
            return;
        }

        Date date = Date.from(dateProperty.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());

        // Actual purchase creation and saving/exporting.
        loan.makeCCPurchase(cost, descriptionProperty.get(), date);
        try {
            LoanAccount.exportFile();
            CreditCardPurchase.exportFile();

            // Update UI.
            creditCardPurchaseTable.setItems(FXCollections.observableArrayList(CreditCardPurchase.search(id)));
            creditCardPurchaseTable.refresh();

            int selectedCustomerId = customerBox.getSelectionModel().getSelectedIndex();
            Person selectedCustomer = Person.people.get(selectedCustomerId);
            int cusId = selectedCustomer.id;
            LoanAccount account = LoanAccount.search(cusId, "credit card");
            if (account == null) {
                balanceLabel.textProperty().set("Account Balance: $????");
                return;
            }
            balanceLabel.textProperty().set("Account Balance: $" + account.accountBalance);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    // Creates the credit card purchase table, injecting all necessary fields and tying information to them.
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
