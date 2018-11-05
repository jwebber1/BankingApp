import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.*;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Main extends Application {
    //used to format money, will probably need to be moved to "controller" for the GUI
    static NumberFormat moneyFormat = NumberFormat.getCurrencyInstance();

    static ArrayList<Person> people;
    static ArrayList<CheckingAccount> checkingAccounts;
    static ArrayList<Check> checks;
    static ArrayList<CD> cds;

    static {
        try {
            people = Person.importFile();
            checkingAccounts = CheckingAccount.importFile();
            checks = Check.importFile();
            cds = CD.importFile();
        }
        catch (IOException e) {e.printStackTrace();}
        catch (ParseException e) {e.printStackTrace();}
    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Banking App");
        primaryStage.setScene(new Scene(root, 640, 480));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}//end of main
