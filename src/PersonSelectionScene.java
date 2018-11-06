import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class PersonSelectionScene {
    private VBox fieldVBox = new VBox();
    StackPane root = new StackPane(fieldVBox);
    TableView<Person> personTable = new TableView<>();

    private HBox buttonHBox = new HBox();

    public PersonSelectionScene() {
        UICreationHelpers.setBaseSceneSettings(root, fieldVBox);

        TableColumn<Person, String> id = new TableColumn<>("SSN");
        TableColumn<Person, String> fName = new TableColumn<>("First Name");
        TableColumn<Person, String> lName = new TableColumn<>("Last Name");
        TableColumn<Person, String> city = new TableColumn<>("City");
        TableColumn<Person, String> state = new TableColumn<>("State");
        TableColumn<Person, String> address = new TableColumn<>("Address");
        TableColumn<Person, String> zipCode = new TableColumn<>("Zip");
        ObservableList<Person> list=FXCollections.observableArrayList(Person.people);

        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        fName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        city.setCellValueFactory(new PropertyValueFactory<>("city"));
        state.setCellValueFactory(new PropertyValueFactory<>("state"));
        address.setCellValueFactory(new PropertyValueFactory<>("streetAddress"));
        zipCode.setCellValueFactory(new PropertyValueFactory<>("zipCode"));
        personTable.getColumns().addAll(id, fName, lName, city, state, zipCode, address);
        personTable.setItems(list);

        // Cancel Button
        Button cancelButton = new Button("Cancel");
        cancelButton.setOnAction(x -> {
            try {
                UICreationHelpers.navigateToScene(new NavigationScene().root);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        buttonHBox.getChildren().add(cancelButton);

        // Edit Button
        Button editButton = new Button("Edit");
        editButton.setOnAction(x -> editPerson());
        buttonHBox.getChildren().add(editButton);

        fieldVBox.getChildren().addAll(personTable, buttonHBox);
    }

    public void editPerson() {
        Person selectedPerson = personTable.getSelectionModel().getSelectedItem();
        UICreationHelpers.navigateToScene(new PersonCreationScene(selectedPerson).root);
    }
}
