import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;

public class Main extends Application {
    //used to format money, will probably need to be moved to "controller" for the GUI
    static NumberFormat moneyFormat = NumberFormat.getCurrencyInstance();

    static Stage primaryStage;
    static ArrayList<Person> people;
    static ArrayList<CheckingAccount> checkingAccounts;
    static ArrayList<Check> checks;

    static {
        try {
            people = Person.importFile();
            checkingAccounts = CheckingAccount.importFile();
            checks = Check.importFile();
        }
        catch (IOException e) {e.printStackTrace();}
        catch (ParseException e) {e.printStackTrace();}
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Main.primaryStage = primaryStage;
        primaryStage.setTitle("Banking App");
        UICreationHelpers.navigateToScene(new LoginScene().getRoot());
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);

        Person.debugImport();
        CheckingAccount.debugImport();
        Check.debugImport();


        try {
            Person.exportFile(people);
            CheckingAccount.exportFile(checkingAccounts);
            Check.exportFile(checks);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

}//end of main
