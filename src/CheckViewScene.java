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

/**
 * Allows viewing and stopping of checks for an account.
 *
 * @author  Hunter Berten
 */

public class CheckViewScene {
    // "fieldVBox" stacks all UI elements of the scene vertically.
    // "root" contains all UI of the scene (this is transferred on navigation to another page).
    private VBox fieldVBox = new VBox();
    StackPane root = new StackPane(fieldVBox);

    private int customerId; // The id of the customer whose checks are being viewed.
    private TableView<Check> checkTable = new TableView<>(); // The table of checks being viewed.
    private HBox buttonHBox = new HBox(); // Contains the buttons at the bottom of the scene.

    private ComboBox customerBox; // The customer selection box (which allows searching by customer).

    // Stores the "customerBox"'s selected customer.
    private final StringProperty customerProperty = new SimpleStringProperty("");

    // Constructor
    public CheckViewScene(int customerId) {
        this.customerId = customerId;

        // Sets base scene settings (padding, etc.).
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
                ObservableList checks = FXCollections.observableArrayList(
                        Check.searchChecksByCustomerID(id)
                );
                checkTable.setItems(checks);
                checkTable.refresh();
            });
        }

        setupCheckTable();

        if (UIHelpers.currentUserLevel != 0) {
            // Honor Check Button
            Button honorCheckButton = new Button("Honor Check");
            honorCheckButton.setOnAction(x -> honorCheck());

            // Stop Check Button
            Button stopCheckButton = new Button("Stop Check");
            stopCheckButton.setOnAction(x -> stopCheck());

            buttonHBox.getChildren().addAll(honorCheckButton, stopCheckButton);
        }

        // Back Button
        Button backButton = new Button("Back");
        backButton.setOnAction(x -> {
            if (UIHelpers.selectedAccountType != null) {
                UIHelpers.navigateBackToAccountManagement();
            } else {
                UIHelpers.navigateToScene(new NavigationScene().root);
            }
        });

        buttonHBox.getChildren().add(backButton);
        fieldVBox.getChildren().addAll(checkTable, buttonHBox);
    }

    // Injects the correct fields into the check table and binds the data to it.
    private void setupCheckTable() {
        TableColumn<Check, String> amount = new TableColumn<>("Amount");
        TableColumn<Check, String> dateCheck = new TableColumn<>("Date");
        TableColumn<Check, String> dateHonored = new TableColumn<>("Date Honored");
        TableColumn<Check, String> memo = new TableColumn<>("Details");
        TableColumn<Check, String> payTo = new TableColumn<>("Pay To");

        amount.setCellValueFactory(new PropertyValueFactory<>("checkAmt"));
        dateCheck.setCellValueFactory(new PropertyValueFactory<>("dateCheck"));
        dateHonored.setCellValueFactory(new PropertyValueFactory<>("dateHonored"));
        memo.setCellValueFactory(new PropertyValueFactory<>("memo"));
        payTo.setCellValueFactory(new PropertyValueFactory<>("payTo"));

        checkTable.getColumns().addAll(amount, dateCheck, dateHonored, memo, payTo);

        if (customerId != 0) {
            ObservableList checks = FXCollections.observableArrayList(Check.searchChecksByCustomerID(customerId));
            checkTable.setItems(checks);
        }
    }

    // The "Honor Check" button's click event. Honors the check and does any necessary error checking.
    private void honorCheck() {
        Check check = checkTable.getSelectionModel().getSelectedItem();
        if (check == null) {
            UIHelpers.showAlert(Alert.AlertType.INFORMATION,
                    "You must select a check to honor.");
            return;
        }
        if (check.dateHonored != null) {
            UIHelpers.showAlert(Alert.AlertType.INFORMATION,
                    "This check has already been honored.");
            return;
        }
        check.honorCheck();
        try {
            Check.exportFile();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        checkTable.setItems(FXCollections.observableArrayList());
        ObservableList checks = FXCollections.observableArrayList(Check.searchChecksByCustomerID(customerId));
        checkTable.setItems(checks);
        checkTable.refresh();
    }

    // The "Stop Check" button's click event. Stops the check and does any necessary error checking.
    private void stopCheck() {
        Check check = checkTable.getSelectionModel().getSelectedItem();
        if (check == null) {
            UIHelpers.showAlert(Alert.AlertType.INFORMATION,
                    "You must select a check to stop.");
            return;
        }
        if (check.dateHonored != null) {
            UIHelpers.showAlert(Alert.AlertType.INFORMATION,
                    "This check has already been honored, so it cannot be stopped.");
            return;
        }
        Check.stopCheck(check.checkID);
        try {
            Check.exportFile();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        checkTable.setItems(FXCollections.observableArrayList());
        ObservableList checks = FXCollections.observableArrayList(Check.searchChecksByCustomerID(customerId));
        checkTable.setItems(checks);
        checkTable.refresh();
    }
}
