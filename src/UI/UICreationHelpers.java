package UI;

import javafx.beans.property.Property;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.util.StringConverter;

import java.time.LocalDate;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

/**
 * This Helper class allows static creation of fields anywhere in the application.
 */

class UICreationHelpers {
    private static double fieldWidth = 140;
    private static double fieldHeight = 20;

    // Creates a field's HBox (i.e. a field of any type, and a label for that field).
    static HBox createHBox(String labelText, Node node) {
        Label label = new Label(labelText);
        label.setPrefSize(fieldWidth, fieldHeight);
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
                    return Double.valueOf(s);
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
        if (property.get().matches("[0-9]+")) {
            errorMessage += fieldName + " field must contain only numbers.\n";
        }

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

}
