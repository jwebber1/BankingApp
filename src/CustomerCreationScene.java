import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.io.FileNotFoundException;
import java.util.ArrayList;

public class CustomerCreationScene {
    private final StringProperty socialSecurityProperty = new SimpleStringProperty("");
    private final StringProperty streetAddressProperty = new SimpleStringProperty("");
    private final StringProperty cityProperty = new SimpleStringProperty("");
    private final StringProperty stateProperty = new SimpleStringProperty("");
    private final StringProperty zipCodeProperty = new SimpleStringProperty("");
    private final StringProperty firstNameProperty = new SimpleStringProperty("");
    private final StringProperty lastNameProperty = new SimpleStringProperty("");
    private final StringProperty userLevelProperty = new SimpleStringProperty("");

    private VBox fieldVBox = new VBox();
    private HBox buttonHBox = new HBox();

    private StackPane root = new StackPane(fieldVBox, buttonHBox);

    private ObservableList<String> states = FXCollections.observableArrayList(
            "Alabama", "Alaska", "Arizona", "Arkansas", "California", "Colorado", "Connecticut", "Delaware",
            "District of Columbia", "Florida", "Georgia", "Hawaii", "Idaho", "Illinois", "Indiana", "Iowa", "Kansas",
            "Kentucky", "Louisiana", "Maine", "Maryland", "Massachusetts", "Michigan", "Minnesota", "Mississippi",
            "Missouri", "Montana", "Nebraska", "Nevada", "New Hampshire", "New Jersey", "New Mexico", "New York",
            "North Carolina", "North Dakota", "Ohio", "Oklahoma", "Oregon", "Pennsylvania", "Rhode Island",
            "South Carolina", "South Dakota", "Tennessee", "Texas", "Utah", "Vermont", "Virginia", "Washington",
            "West Virginia", "Wisconsin", "Wyoming"
    );

    public CustomerCreationScene() {
        UICreationHelpers.setBaseSceneSettings(root, fieldVBox);
        createBaseCustomerCreationNodes();
    }

    public StackPane getRoot() {
        return root;
    }

    // Creates base fields that are used by all account types.
    private void createBaseCustomerCreationNodes() {
        TextField socialSecurityField = UICreationHelpers.createTextField(socialSecurityProperty);
        fieldVBox.getChildren().add(UICreationHelpers.createHBox("Social Security #:", socialSecurityField));

        TextField streetAddressField = UICreationHelpers.createTextField(streetAddressProperty);
        fieldVBox.getChildren().add(UICreationHelpers.createHBox("Street Address:", streetAddressField));

        TextField cityField = UICreationHelpers.createTextField(cityProperty);
        fieldVBox.getChildren().add(UICreationHelpers.createHBox("City:", cityField));

        ComboBox stateField = UICreationHelpers.createComboBox(states, stateProperty);
        fieldVBox.getChildren().add(UICreationHelpers.createHBox("State:", stateField));

        TextField zipCodeField = UICreationHelpers.createTextField(zipCodeProperty);
        fieldVBox.getChildren().add(UICreationHelpers.createHBox("Zip Code:", zipCodeField));

        TextField firstNameField = UICreationHelpers.createTextField(firstNameProperty);
        fieldVBox.getChildren().add(UICreationHelpers.createHBox("First Name:", firstNameField));

        TextField lastNameField = UICreationHelpers.createTextField(lastNameProperty);
        fieldVBox.getChildren().add(UICreationHelpers.createHBox("Last Name:", lastNameField));

        ComboBox userLevelField = UICreationHelpers.createComboBox(UICreationHelpers.userLevels, userLevelProperty);
        fieldVBox.getChildren().add(UICreationHelpers.createHBox("User Level:", userLevelField));

        // Save Button
        Button saveButton = new Button("Save");
        saveButton.setOnAction(x -> saveAccount());
        buttonHBox.getChildren().add(saveButton);
        buttonHBox.setAlignment(Pos.BASELINE_RIGHT);
        fieldVBox.getChildren().add(buttonHBox);
    }

    // Runs when the "Save" button is pressed.
    private void saveAccount() {
        String errorMessage = "";

        errorMessage += UICreationHelpers.checkNumberField("Social Security #", socialSecurityProperty);
        if (socialSecurityProperty.get().length() != 9) {
            errorMessage += "Social Security # field must contain exactly nine digits.\n";
        }

        errorMessage += UICreationHelpers.checkTextField("Street Address", streetAddressProperty);
        errorMessage += UICreationHelpers.checkTextField("City", cityProperty);
        errorMessage += UICreationHelpers.checkTextField("State", stateProperty);
        errorMessage += UICreationHelpers.checkNumberField("Zip Code", zipCodeProperty);
        errorMessage += UICreationHelpers.checkTextField("First Name", firstNameProperty);
        errorMessage += UICreationHelpers.checkTextField("Last Name", lastNameProperty);
        errorMessage += UICreationHelpers.checkTextField("User Level", userLevelProperty);
        errorMessage += UICreationHelpers.checkTextField("Street Address", streetAddressProperty);

        if (!errorMessage.isEmpty()) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("Input Not Valid");
            errorAlert.setContentText(errorMessage);
            errorAlert.showAndWait();
        } else {
            // TODO: Save to file.
            try {
                ArrayList<ArrayList> accounts = new ArrayList<>();
                accounts.add(new ArrayList());
                accounts.add(new ArrayList());
                accounts.add(new ArrayList());
                Person person = new Person(
                        Integer.parseInt(socialSecurityProperty.get()),
                        streetAddressProperty.get(),
                        cityProperty.get(),
                        stateProperty.get(),
                        zipCodeProperty.get(),
                        firstNameProperty.get(),
                        lastNameProperty.get(),
                        UICreationHelpers.userLevels.indexOf(userLevelProperty.get()),
                        accounts
                );
                Main.persons.add(person);
                Main.exportToFile(Main.persons);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            Alert successfulAlert = new Alert(Alert.AlertType.INFORMATION);
            successfulAlert.setHeaderText("Save Successful");
            successfulAlert.setContentText("The user has been saved successfully.");
            successfulAlert.showAndWait();
        }
    }

}
