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

/**
 * Allows viewing and stopping of checks for an account.
 *
 * @author  Hunter Berten
 */

public class CheckViewScene {
    private VBox fieldVBox = new VBox();
    StackPane root = new StackPane(fieldVBox);

    private int customerId;

    private TableView<Check> checkTable = new TableView<>();

    private HBox buttonHBox = new HBox();

    public CheckViewScene(int customerId) {
        this.customerId = customerId;

        UIHelpers.setBaseSceneSettings(root, fieldVBox);
        UIHelpers.setButtonSettings(buttonHBox);

        setupCheckTable();

        // Stop Check Button
        Button stopCheckButton = new Button("Stop Check");
        stopCheckButton.setOnAction(x -> stopCheck());

        // Back Button
        Button backButton = new Button("Back");
        backButton.setOnAction(x -> UIHelpers.navigateBackToAccountManagement());

        buttonHBox.getChildren().addAll(stopCheckButton, backButton);
        fieldVBox.getChildren().addAll(checkTable, buttonHBox);
    }

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

        ObservableList checks = FXCollections.observableArrayList(Check.searchChecksByCustomerID(customerId));
        checkTable.setItems(checks);
    }

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
