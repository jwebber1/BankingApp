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

    private final StringProperty costProperty = new SimpleStringProperty("");
    private final StringProperty descriptionProperty = new SimpleStringProperty("");
    private final StringProperty payToProperty = new SimpleStringProperty("");
    private final Property<LocalDate> dateProperty = new SimpleObjectProperty<>();

    private Label balanceLabel = new Label("Account Balance: $????");

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

                CheckingAccount account = CheckingAccount.search(id);
                if (account == null) {
                    balanceLabel.textProperty().set("Account Balance: $????");
                    return;
                }
                balanceLabel.textProperty().set("Account Balance: $" + account.accountBalance);
            });
        } else {
            balanceLabel.textProperty().set("Account Balance: $" + CheckingAccount.search(customerId).accountBalance);
        }
        fieldVBox.getChildren().add(balanceLabel);

        setupCheckTable();
        fieldVBox.getChildren().add(checkTable);

        if (UIHelpers.currentUserLevel != 0) {
            Label makePurchaseLabel = new Label("Make Purchase");
            fieldVBox.getChildren().add(makePurchaseLabel);

            HBox balanceField = UIHelpers.createHBox("Cost:", UIHelpers.createBalanceField(costProperty));
            fieldVBox.getChildren().add(balanceField);

            TextField payToField = UIHelpers.createTextField(payToProperty);
            fieldVBox.getChildren().add(UIHelpers.createHBox("Pay To:", payToField));

            TextField descriptionField = UIHelpers.createTextField(descriptionProperty);
            fieldVBox.getChildren().add(UIHelpers.createHBox("Description:", descriptionField));

            DatePicker datePicker = UIHelpers.createDatePicker(dateProperty);
            fieldVBox.getChildren().add(UIHelpers.createHBox("Date:", datePicker));

            Button saveButton = new Button("Save Purchase");
            saveButton.setOnAction(x -> savePurchase());
            buttonHBox.getChildren().add(saveButton);

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
        fieldVBox.getChildren().add(buttonHBox);
    } // End of Constructor

    // The "Save Purchase" click event.
    private void savePurchase() {
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
        String errorMessage = "";

        CheckingAccount account = CheckingAccount.search(id);
        if (account == null) {
            UIHelpers.showAlert(Alert.AlertType.INFORMATION, "The selected customer does not have a Checking account.");
            return;
        }

        double cost = Double.parseDouble(costProperty.get().replace("$", ""));
        if (cost <= 0) {
            errorMessage += "A purchase must be greater than $0.00.\n";
        }
        if (dateProperty.getValue() == null) {
            errorMessage += "You must enter a date.\n";
        }
        String desc = descriptionProperty.get();
        if (desc.isEmpty() || desc.length() > 255) {
            errorMessage += "A description must not be empty or exceed a length of 255 characters.\n";
        }
        if (payToProperty.get().isEmpty() || payToProperty.get().length() > 255) {
            errorMessage += "Pay To must not be empty or exceed a length of 255 characters.\n";
        }

        if (!errorMessage.isEmpty()) {
            UIHelpers.showAlert(Alert.AlertType.INFORMATION, errorMessage);
            return;
        }

        // Gets the largest checkID (this value is incremented for the newest check).
        Date date = Date.from(dateProperty.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
        int checkId = 0;
        for (Check check : Check.checks) {
            if (check.checkID > checkId) { checkId = check.checkID; }
        }

        double balance = account.accountBalance;
        if (account.getHasOverdraftProtection()) balance += SavingAccount.search(id).accountBalance;
        if (balance < cost) {
            UIHelpers.showAlert(Alert.AlertType.INFORMATION,
                    "You've overdrawn from this account and will be charged a fee.");
            return;
        }

        // Save Check and Export
        Check.checks.add(new Check(id, checkId+1, cost, payToProperty.get(), date, desc, false));
        try {
            Check.exportFile();
            checkTable.setItems(FXCollections.observableArrayList(Check.searchChecksByCustomerID(id)));
            checkTable.refresh();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    // Injects the correct fields into the check table and binds the data to it.
    private void setupCheckTable() {
        // Creates check fields for table and binds data to fields.
        TableColumn<Check, String> checkNum = new TableColumn<>("Check #");
        TableColumn<Check, String> amount = new TableColumn<>("Amount");
        TableColumn<Check, String> dateCheck = new TableColumn<>("Date");
        TableColumn<Check, String> dateHonored = new TableColumn<>("Date Honored");
        TableColumn<Check, String> memo = new TableColumn<>("Details");
        TableColumn<Check, String> payTo = new TableColumn<>("Pay To");
        TableColumn<Check, String> isStopped = new TableColumn<>("Stopped?");

        checkNum.setCellValueFactory(new PropertyValueFactory<>("checkID"));
        amount.setCellValueFactory(new PropertyValueFactory<>("checkAmt"));
        dateCheck.setCellValueFactory(new PropertyValueFactory<>("dateCheck"));
        dateHonored.setCellValueFactory(new PropertyValueFactory<>("dateHonored"));
        memo.setCellValueFactory(new PropertyValueFactory<>("memo"));
        payTo.setCellValueFactory(new PropertyValueFactory<>("payTo"));
        isStopped.setCellValueFactory(new PropertyValueFactory<>("isStopped"));

        // Injects check fields into table.
        checkTable.getColumns().addAll(checkNum, amount, dateCheck, dateHonored, memo, payTo, isStopped);

        // Updates table source.
        if (customerId != 0) {
            ObservableList checks = FXCollections.observableArrayList(Check.searchChecksByCustomerID(customerId));
            checkTable.setItems(checks);
        }
    }

    // The "Honor Check" button's click event. Honors the check and does any necessary error checking.
    private void honorCheck() {
        // Error checking.
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
        if (check.isStopped) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                    "This check is stopped and cannot be honored. Would you like to unstop it and honor it?",
                    ButtonType.YES, ButtonType.NO);
            alert.showAndWait();
            if (alert.getResult() == ButtonType.NO) {
                return;
            }
            check.isStopped = false;
        }

        // Honor and export check.
        check.honorCheck();
        try {
            Check.exportFile();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // Update table.
        int id = customerId;
        if (customerId == 0) {
            int selectedCustomerId = customerBox.getSelectionModel().getSelectedIndex();
            Person selectedCustomer = Person.people.get(selectedCustomerId);
            id = selectedCustomer.id;
        }
        ObservableList checks = FXCollections.observableArrayList(Check.searchChecksByCustomerID(id));
        checkTable.setItems(checks);
        checkTable.refresh();

        int selectedCustomerId = customerBox.getSelectionModel().getSelectedIndex();
        Person selectedCustomer = Person.people.get(selectedCustomerId);
        int cusId = selectedCustomer.id;
        CheckingAccount account = CheckingAccount.search(cusId);

        // Update Account Balance field.
        try {
            CheckingAccount.exportFile();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        balanceLabel.textProperty().set("Account Balance: $" + account.accountBalance);
    }

    // The "Stop Check" button's click event. Stops the check and does any necessary error checking.
    private void stopCheck() {
        // Error checking.
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

        // Stop check and export.
        Check.stopCheck(check.checkID);
        try {
            Check.exportFile();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // Update check table.
        checkTable.setItems(FXCollections.observableArrayList());
        int id = customerId;
        if (customerId == 0) {
            int selectedCustomerId = customerBox.getSelectionModel().getSelectedIndex();
            Person selectedCustomer = Person.people.get(selectedCustomerId);
            id = selectedCustomer.id;
        }
        ObservableList checks = FXCollections.observableArrayList(Check.searchChecksByCustomerID(id));
        checkTable.setItems(checks);
        checkTable.refresh();
    }
} // End of CheckViewScene
