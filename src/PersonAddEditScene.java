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

/**
 * Allows the adding and editing of a person and all their fields.
 *
 * @author  Hunter Berten
 */

public class PersonAddEditScene {
    // "fieldVBox" stacks all UI elements of the scene vertically.
    // "root" contains all UI of the scene (this is transferred on navigation to another page).
    // "buttonHBox" holds the buttons at the bottom of the scene.
    private VBox fieldVBox = new VBox();
    private HBox buttonHBox = new HBox();
    StackPane root = new StackPane(fieldVBox, buttonHBox);

    Person editedPerson; // The person being edited (or null, if no person is being edited).

    // Field properties (stores values of the fields).
    private final StringProperty socialSecurityProperty = new SimpleStringProperty("");
    private final StringProperty streetAddressProperty = new SimpleStringProperty("");
    private final StringProperty cityProperty = new SimpleStringProperty("");
    private final StringProperty stateProperty = new SimpleStringProperty("");
    private final StringProperty zipCodeProperty = new SimpleStringProperty("");
    private final StringProperty firstNameProperty = new SimpleStringProperty("");
    private final StringProperty lastNameProperty = new SimpleStringProperty("");
    private final StringProperty userLevelProperty = new SimpleStringProperty("");

    // The states selectable in the state box.
    private ObservableList<String> states = FXCollections.observableArrayList(
            "Alabama", "Alaska", "Arizona", "Arkansas", "California", "Colorado", "Connecticut", "Delaware",
            "District of Columbia", "Florida", "Georgia", "Hawaii", "Idaho", "Illinois", "Indiana", "Iowa", "Kansas",
            "Kentucky", "Louisiana", "Maine", "Maryland", "Massachusetts", "Michigan", "Minnesota", "Mississippi",
            "Missouri", "Montana", "Nebraska", "Nevada", "New Hampshire", "New Jersey", "New Mexico", "New York",
            "North Carolina", "North Dakota", "Ohio", "Oklahoma", "Oregon", "Pennsylvania", "Rhode Island",
            "South Carolina", "South Dakota", "Tennessee", "Texas", "Utah", "Vermont", "Virginia", "Washington",
            "West Virginia", "Wisconsin", "Wyoming"
    );

    // Constructors
    public PersonAddEditScene() {
        UIHelpers.setBaseSceneSettings(root, fieldVBox);
        createBaseCustomerCreationNodes();
    }
    public PersonAddEditScene(Person editedPerson) {
        this.editedPerson = editedPerson;

        // Sets base scene settings (padding, etc.).
        UIHelpers.setBaseSceneSettings(root, fieldVBox);
        createBaseCustomerCreationNodes();

        // Sets the fields to the passed person's values.
        socialSecurityProperty.set(String.valueOf(editedPerson.id));
        streetAddressProperty.set(editedPerson.streetAddress);
        cityProperty.set(editedPerson.city);
        stateProperty.set(editedPerson.state);
        zipCodeProperty.set(editedPerson.zipCode);
        firstNameProperty.set(editedPerson.firstName);
        lastNameProperty.set(editedPerson.lastName);
        userLevelProperty.set(UIHelpers.userLevels.get(editedPerson.userLevel));
    }

    // Creates base fields that are used by all account types.
    private void createBaseCustomerCreationNodes() {
        TextField socialSecurityField = UIHelpers.createTextField(socialSecurityProperty);
        fieldVBox.getChildren().add(UIHelpers.createHBox("Social Security #:", socialSecurityField));

        TextField streetAddressField = UIHelpers.createTextField(streetAddressProperty);
        fieldVBox.getChildren().add(UIHelpers.createHBox("Street Address:", streetAddressField));

        TextField cityField = UIHelpers.createTextField(cityProperty);
        fieldVBox.getChildren().add(UIHelpers.createHBox("City:", cityField));

        ComboBox stateField = UIHelpers.createComboBox(states, stateProperty);
        fieldVBox.getChildren().add(UIHelpers.createHBox("State:", stateField));

        TextField zipCodeField = UIHelpers.createTextField(zipCodeProperty);
        fieldVBox.getChildren().add(UIHelpers.createHBox("Zip Code:", zipCodeField));

        TextField firstNameField = UIHelpers.createTextField(firstNameProperty);
        fieldVBox.getChildren().add(UIHelpers.createHBox("First Name:", firstNameField));

        TextField lastNameField = UIHelpers.createTextField(lastNameProperty);
        fieldVBox.getChildren().add(UIHelpers.createHBox("Last Name:", lastNameField));

        ComboBox userLevelField = UIHelpers.createComboBox(UIHelpers.userLevels, userLevelProperty);
        fieldVBox.getChildren().add(UIHelpers.createHBox("User Level:", userLevelField));

        // Cancel Button
        Button cancelButton = new Button("Cancel");
        cancelButton.setOnAction(x -> {
            try {
                UIHelpers.navigateToScene(new PersonManagementScene().root);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        buttonHBox.getChildren().add(cancelButton);

        // Save Button
        Button saveButton = new Button("Save");
        saveButton.setOnAction(x -> savePerson());
        buttonHBox.getChildren().add(saveButton);

        buttonHBox.setSpacing(10);
        buttonHBox.setAlignment(Pos.BASELINE_RIGHT);
        fieldVBox.getChildren().add(buttonHBox);
    }

    // Runs when the "Save" button is pressed.
    private void savePerson() {
        String errorMessage = "";

        errorMessage += UIHelpers.checkNumberField("Social Security #", socialSecurityProperty);
        if (socialSecurityProperty.get().length() != 9 || !UIHelpers.checkNumberField("SSN", socialSecurityProperty).isEmpty()) {
            errorMessage += "Social Security # field must contain exactly nine digits.\n";
        } else if (Person.searchPeopleByCustomerID(Integer.parseInt(socialSecurityProperty.get())) != null) {
            errorMessage += "A user with this SSN already exists.\n";
        }

        errorMessage += UIHelpers.checkTextField("Street Address", streetAddressProperty);
        errorMessage += UIHelpers.checkTextField("City", cityProperty);
        errorMessage += UIHelpers.checkTextField("State", stateProperty);
        errorMessage += UIHelpers.checkNumberField("Zip Code", zipCodeProperty);
        if (zipCodeProperty.get().length() < 5) {
            errorMessage += "Zip Code must contain five or greater digits.";
        }
        errorMessage += UIHelpers.checkTextField("First Name", firstNameProperty);
        errorMessage += UIHelpers.checkTextField("Last Name", lastNameProperty);
        errorMessage += UIHelpers.checkTextField("User Level", userLevelProperty);
        errorMessage += UIHelpers.checkTextField("Street Address", streetAddressProperty);

        if (!errorMessage.isEmpty()) {
            UIHelpers.showAlert(Alert.AlertType.ERROR, errorMessage);
        } else {
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
                        UIHelpers.userLevels.indexOf(userLevelProperty.get())
                );
                if (editedPerson != null) {
                    Person.people.remove(editedPerson);
                }
                Person.people.add(person);
                Person.exportFile();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            UIHelpers.showAlert(Alert.AlertType.INFORMATION, "The user has been saved successfully.");
        }
    }
}
