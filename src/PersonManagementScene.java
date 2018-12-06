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

/**
 * Allows the viewing, creation, and editing of users.
 *
 * @author  Hunter Berten
 */

class PersonManagementScene {
    // "fieldVBox" stacks all UI elements of the scene vertically.
    // "root" contains all UI of the scene (this is transferred on navigation to another page).
    // "buttonHBox" holds the buttons at the bottom of the scene.
    private VBox fieldVBox = new VBox();
    StackPane root = new StackPane(fieldVBox);
    private HBox buttonHBox = new HBox();

    TableView<Person> personTable = new TableView<>(); // The table that displays the Persons.

    // Constructor
    public PersonManagementScene() {
        // Sets base scene settings (padding, etc.).
        UIHelpers.setBaseSceneSettings(root, fieldVBox);
        UIHelpers.setButtonSettings(buttonHBox);

        setupPersonTable(); // Initializes person table.

        // Edit Button
        Button addButton = new Button("Add");
        addButton.setOnAction(x -> UIHelpers.navigateToScene(new PersonAddEditScene().root));
        buttonHBox.getChildren().add(addButton);

        // Edit Button
        Button editButton = new Button("Edit");
        editButton.setOnAction(x -> editPerson());
        buttonHBox.getChildren().add(editButton);

        // Cancel Button
        Button cancelButton = new Button("Cancel");
        cancelButton.setOnAction(x -> {
            try {
                UIHelpers.navigateToScene(new NavigationScene().root);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        buttonHBox.getChildren().add(cancelButton);

        fieldVBox.getChildren().addAll(personTable, buttonHBox);
    } // End of Constructor

    // Injects fields into person table and binds the data to it.
    public void setupPersonTable() {
        // Creates columns and binds data to them.
        TableColumn<Person, String> id = new TableColumn<>("SSN");
        TableColumn<Person, String> fName = new TableColumn<>("First Name");
        TableColumn<Person, String> lName = new TableColumn<>("Last Name");
        TableColumn<Person, String> city = new TableColumn<>("City");
        TableColumn<Person, String> state = new TableColumn<>("State");
        TableColumn<Person, String> address = new TableColumn<>("Address");
        TableColumn<Person, String> zipCode = new TableColumn<>("Zip");
        ObservableList<Person> list= FXCollections.observableArrayList(Person.people);

        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        fName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        city.setCellValueFactory(new PropertyValueFactory<>("city"));
        state.setCellValueFactory(new PropertyValueFactory<>("state"));
        address.setCellValueFactory(new PropertyValueFactory<>("streetAddress"));
        zipCode.setCellValueFactory(new PropertyValueFactory<>("zipCode"));

        // Injects columns into table and sets source for the table.
        personTable.getColumns().addAll(id, fName, lName, city, state, zipCode, address);
        personTable.setItems(list);
    }

    // The "Edit" button's click event. Navigates to PersonAddEditScene (or displays an error if no person is selected).
    public void editPerson() {
        Person selectedPerson = personTable.getSelectionModel().getSelectedItem();
        if (selectedPerson == null) {
            UIHelpers.showAlert(Alert.AlertType.INFORMATION, "You must select an person to edit.");
            return;
        }
        UIHelpers.navigateToScene(new PersonAddEditScene(selectedPerson).root);
    }
} // End of PersonManagementScene