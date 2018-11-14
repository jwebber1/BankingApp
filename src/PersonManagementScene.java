import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

class PersonManagementScene {
    private VBox fieldVBox = new VBox();
    StackPane root = new StackPane(fieldVBox);
    TableView<Person> personTable = new TableView<>();

    private HBox buttonHBox = new HBox();

    public PersonManagementScene() {
        UIHelpers.setBaseSceneSettings(root, fieldVBox);
        UIHelpers.setButtonSettings(buttonHBox);

        setupPersonTable();

        // Edit Button
        Button addButton = new Button("Add");
        addButton.setOnAction(x -> UIHelpers.navigateToScene(new PersonCreationScene().root));
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
    }

    public void setupPersonTable() {
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
        personTable.getColumns().addAll(id, fName, lName, city, state, zipCode, address);
        personTable.setItems(list);
    }

    public void editPerson() {
        Person selectedPerson = personTable.getSelectionModel().getSelectedItem();
        if (selectedPerson == null) {
            return;
        }
        UIHelpers.navigateToScene(new PersonCreationScene(selectedPerson).root);
    }
}
