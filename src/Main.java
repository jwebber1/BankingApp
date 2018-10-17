import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Banking App");
        primaryStage.setScene(new Scene(root, 640, 480));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);   //may have to go between


        /*

        importing data like below

        ArrayList<LoanAccount> loans = new ArrayList<>();

        try {
            loans = importFile();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        for(LoanAccount loan: loans){
            loan.print(loan.getCustomerID());
        }
        */
    }

    //current method to grab data from the textfile in "memory"
    public static ArrayList<LoanAccount> importFile() throws IOException, ParseException {

        //creates a file referencing the text file in the memory folder
        File fileIn = new File("memory/Team3_with_bars.txt");

        //creates a bufferedreader to read from a file
        BufferedReader br = null;
        br = new BufferedReader(new InputStreamReader(new FileInputStream(fileIn)));

        //buffer string to temporarily hold the line retrieved
        String line;

        //creates the ArrayList of data (currently loans, though this will change)
        ArrayList<LoanAccount> importLoans = new ArrayList<>();

        //generic counter to know the line currenly on
        int lineNum = 0;

        //while loop to go through the file
        while ((line = br.readLine()) != null) {

            //if the file has a header, this if statement is to avoid that
            //remove "if" if final file has no header
            if(lineNum > 0) {

                //split the line into an array of strings
                String[] splitLine = line.split("\\|");

                //create temp variable to hold info from the split lines
                int cusID = Integer.parseInt(splitLine[0]);
                double balance = Double.parseDouble(splitLine[1]);
                double rate = Double.parseDouble(splitLine[2]);
                System.out.println(splitLine[3]);
                Date payDueDate = new SimpleDateFormat("MM/dd/yyyy").parse(splitLine[3]);
                Date notifyPayDate = new SimpleDateFormat("MM/dd/yyyy").parse(splitLine[4]);
                double currPayDue = Double.parseDouble(splitLine[5]);
                Date lastPayDate = new SimpleDateFormat("MM/dd/yyyy").parse(splitLine[6]);
                Byte missedPay = Byte.parseByte(splitLine[7]);
                String accountType = splitLine[8];

                //add the new data (in our case loan) to the ArrayList
                importLoans.add(new LoanAccount(cusID, balance, rate, payDueDate, currPayDue, notifyPayDate, lastPayDate, missedPay, accountType));
            }

            //increment the line number
            lineNum++;
        }

        //close the bufferfile and return the ArrayList
        br.close();
        return importLoans;

    }//end of data import method
}