import Enums.AccountType;
import javafx.beans.property.Property;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

/**
 * Assists in creation, navigation, and management of UI Scenes.
 *
 * @author  Hunter Berten
 */

class UIHelpers {
    static int currentUserLevel = -1;
    static Person currentUser;
    private static double fieldWidth = 140;
    private static double fieldHeight = 20;

    static AccountType selectedAccountType;
    static ArrayList<Account> selectedAccounts;

    static ObservableList<String> accountTypes = FXCollections.observableArrayList();

    static ObservableList<String> loanTypes = FXCollections.observableArrayList(
            "Long Term",
            "Short Term",
            "Credit Card"
    );
    static ObservableList<String> checkingAccountTypes = FXCollections.observableArrayList(
            "That's My Bank",
            "Gold"
    );

    static ObservableList<String> userLevels = FXCollections.observableArrayList(
            "Customer",
            "Teller",
            "Manager"
    );

    static void setBaseSceneSettings(Pane root, VBox fieldVBox) {
        fieldVBox.setSpacing(8);
        root.setPadding(new Insets(20));
    }

    // Creates a field's HBox (i.e. a field of any type, and a label for that field).
    static HBox createHBox(String labelText, Node node) {
        Label label = new Label(labelText);
        label.setPrefSize(fieldWidth, fieldHeight + 10);
        label.setAlignment(Pos.BASELINE_LEFT);
        HBox hBox = new HBox();
        hBox.getChildren().addAll(label, node);
        hBox.setSpacing(10);
        return hBox;
    }

    static TextField createBalanceField(StringProperty property) {
        TextField textField = new TextField();
        textField.textProperty().bindBidirectional(property);
        textField.setPrefSize(fieldWidth, fieldHeight);

        Pattern validEditingState = Pattern.compile("\\$-?(([1-9][0-9]*)|0)?(\\.[0-9]*)?");
        UnaryOperator<TextFormatter.Change> filter = c -> {
            String text = c.getControlNewText();
            if (validEditingState.matcher(text).matches()) {
                return c;
            } else {
                return null;
            }
        };

        StringConverter<Double> converter = new StringConverter<Double>() {
            @Override
            public Double fromString(String s) {
                if (s.isEmpty() || "-".equals(s) || ".".equals(s) || "-.".equals(s)) {
                    return 0.0;
                } else {
                    return Double.valueOf(s.replace("$", ""));
                }
            }

            @Override
            public String toString(Double d) {
                return "$" + d.toString();
            }
        };

        TextFormatter<Double> textFormatter = new TextFormatter<>(converter, 0.0, filter);
        textField.setTextFormatter(textFormatter);

        return textField;
    }

    // Creates a combobox with Label having text "labelText" and containing the options passed in "options." The text
    // in the field is bound bidirectionally to the StringProperty "property."
    static ComboBox createComboBox(HashMap<Integer, String> options, StringProperty property) {
        ObservableList<String> values = FXCollections.observableArrayList();
        values.addAll(options.values());

        ComboBox<String> comboBox = new ComboBox<>(values);
        comboBox.valueProperty().bindBidirectional(property);
        comboBox.setPrefSize(fieldWidth, fieldHeight);
        return comboBox;
    }

    // Creates a combobox with Label having text "labelText" and containing the options passed in "options." The text
    // in the field is bound bidirectionally to the StringProperty "property."
    static ComboBox createComboBox(ObservableList<String> options, StringProperty property) {
        ComboBox<String> comboBox = new ComboBox<>(options);
        comboBox.valueProperty().bindBidirectional(property);
        comboBox.setPrefSize(fieldWidth, fieldHeight);
        return comboBox;
    }

    static DatePicker createDatePicker(Property<LocalDate> date) {
        DatePicker datePicker = new DatePicker();
        datePicker.setPrefSize(fieldWidth, fieldHeight);
        datePicker.valueProperty().bindBidirectional(date);
        return datePicker;
    }

    // Creates a text field with Label having text "labelText." The text in the field is bound bidirectionally to the
    // StringProperty "property."
    static TextField createTextField(StringProperty property) {
        TextField textField = new TextField();
        textField.setPrefSize(fieldWidth, fieldHeight);
        textField.textProperty().bindBidirectional(property);
        return textField;
    }

    static String checkNumberField(String fieldName, StringProperty property) {
        String errorMessage = "";

        // Contains ONLY Numbers
//        if (property.get().matches("[0-9]+")) {
//            errorMessage += fieldName + " field must contain only numbers.\n";
//        }

        return errorMessage;
    }

    // Checks basic text fields to ensure that they contain text and only valid text (for instance, no numbers).
    static String checkTextField(String fieldName, StringProperty property) {
        // TODO: Implement any further checks- like checking for special characters.
        String errorMessage = "";

        // Field Length
        if (property.get().length() == 0 || property.get().length() > 25) {
            errorMessage += fieldName + " field must contain between 1 and 25 characters.\n";
        }

        // Contains Numbers
        if (property.get().matches(".*\\d+.*")) {
            errorMessage += fieldName + " field cannot contain numbers.\n";
        }

        return errorMessage;
    }

    public static Button createButton(String buttonText, VBox fieldVBox, EventHandler<ActionEvent> lambda) {
        Button button = new Button(buttonText);
        button.setOnAction(lambda);
        fieldVBox.getChildren().add(button);
        return button;
    }

    public static void navigateToScene(Parent root) {
        Scene scene = new Scene(root, 640, 480);
        scene.getStylesheets().add("stylesheet.css");
        Main.primaryStage.setScene(scene);
    }

    public static void showAlert(Alert.AlertType alertType, String message) {
        Alert errorAlert = new Alert(alertType);
        if (alertType == Alert.AlertType.ERROR) {
            errorAlert.setHeaderText("Error");
        } else {
            errorAlert.setHeaderText("Information");
        }
        errorAlert.setContentText(message);
        errorAlert.showAndWait();
    }

    public static void setButtonSettings(HBox buttonHBox) {
        buttonHBox.setSpacing(10);
        buttonHBox.setAlignment(Pos.BASELINE_RIGHT);
    }

    public static void navigateToAccountManagementScene(AccountType type) {
        selectedAccountType = type;
        switch (type) {
            case CD:
                if (currentUserLevel == 0) {
                    selectedAccounts = new ArrayList<>(CD.search(currentUser.id));
                } else {
                    selectedAccounts = new ArrayList<>(CD.cds);
                }
                break;
            case LOAN:
                if (currentUserLevel == 0) {
                    selectedAccounts = new ArrayList<>(LoanAccount.search(currentUser.id));
                } else {
                    selectedAccounts = new ArrayList<>(LoanAccount.loans);
                }
                break;
            case SAVING:
                if (currentUserLevel == 0) {
                    selectedAccounts = new ArrayList<>();
                    selectedAccounts.add(SavingAccount.search(currentUser.id));
                } else {
                    selectedAccounts = new ArrayList<>(SavingAccount.savingAccounts);
                }
                break;
            case CHECKING:
                if (currentUserLevel == 0) {
                    selectedAccounts = new ArrayList<>();
                    selectedAccounts.add(CheckingAccount.search(currentUser.id));
                } else {
                    selectedAccounts = new ArrayList<>(CheckingAccount.checkingAccounts);
                }
                break;
        }
        navigateToScene(new AccountManagementScene(selectedAccounts).root);
    }

    public static void navigateBackToAccountManagement() {
        switch (UIHelpers.selectedAccountType) {
            case CHECKING:
                UIHelpers.navigateToAccountManagementScene(AccountType.CHECKING);
                break;
            case SAVING:
                UIHelpers.navigateToAccountManagementScene(AccountType.SAVING);
                break;
            case LOAN:
                UIHelpers.navigateToAccountManagementScene(AccountType.LOAN);
                break;
            case CD:
                UIHelpers.navigateToAccountManagementScene(AccountType.CD);
                break;
        }
    }
}
