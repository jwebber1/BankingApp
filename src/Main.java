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
        //launch(args);

        //importing data

        //creating arraylists to hold the objects
        ArrayList<Customer> customers = new ArrayList<>();
        ArrayList<LoanAccount> loans = new ArrayList<>();
        ArrayList<CheckingAccount> checkings = new ArrayList<>();
        ArrayList<SavingAccount> savings = new ArrayList<>();

        //attempting to import the data into the arraylists
        try {
            customers = customerImportFile();
            loans = loansImportFile();
            checkings = checkingsImportFile();
            savings = savingsImportFile();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }


        /*
        //debugging the import process

        for(Customer customer: customers){
            System.out.println(customer.toString());
        }
        for(LoanAccount loan: loans){
            System.out.println(loan.toString());
        }
        for(CheckingAccount checking: checkings){
            System.out.println(checking.toString());
        }
        for(SavingAccount saving: savings){
            System.out.println(saving.toString());
        }
        */

        //exporting data
        try {
            //exportCustomers(customers);
            exportLoans(loans);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        System.exit(0);
    }

    //current method to grab data from the customers textfile in "memory"
    public static ArrayList<Customer> customerImportFile() throws IOException, ParseException {

        //creates a file referencing the text file in the memory folder
        File customersFileIn = new File("memory/customers.txt");

        //creates a bufferedreader to read from a file
        BufferedReader customersBR = null;
        customersBR = new BufferedReader(new InputStreamReader(new FileInputStream(customersFileIn)));

        //buffer string to temporarily hold the line retrieved
        String line;

        //creates the ArrayList of data
        ArrayList<Customer> importCustomer = new ArrayList<>();

        //generic counter to know the line currently on
        int lineNum = 0;

        //while loop to go through the file
        while ((line = customersBR.readLine()) != null) {

            //if the file has a header, this if statement is to avoid that
            //remove "if" if final file has no header
            if(lineNum > 0) {

                //split the line into an array of strings
                String[] splitLine = line.split(",");

                //create temp variable to hold info from the split lines
                int socialSecurityNumber = Integer.parseInt(splitLine[0]);//customerID in account classes
                String streetAddress = splitLine[1];
                String city = splitLine[2];
                String state = splitLine[3];
                String zipCode = splitLine[4];
                String firstName = splitLine[5];
                String lastName = splitLine[6];

                //add the new data (in our case checking) to the ArrayList
                importCustomer.add(new Customer(socialSecurityNumber, streetAddress, city, state, zipCode, firstName, lastName));
            }

            //increment the line number
            lineNum++;
        }

        //close the bufferfile and return the ArrayList
        customersBR.close();
        return importCustomer;
    }//end of checking data import method

    //current method to grab data from the loans textfile in "memory"
    public static ArrayList<LoanAccount> loansImportFile() throws IOException, ParseException {

        //creates a file referencing the text file in the memory folder
        File loansFileIn = new File("memory/loans.txt");

        //creates a bufferedreader to read from a file
        BufferedReader loansBR = null;
        loansBR = new BufferedReader(new InputStreamReader(new FileInputStream(loansFileIn)));

        //buffer string to temporarily hold the line retrieved
        String line;

        //creates the ArrayList of data
        ArrayList<LoanAccount> importLoans = new ArrayList<>();

        //generic counter to know the line currenly on
        int lineNum = 0;

        //while loop to go through the file
        while ((line = loansBR.readLine()) != null) {

            //if the file has a header, this if statement is to avoid that
            //remove "if" if final file has no header
            if(lineNum > 0) {

                //split the line into an array of strings
                String[] splitLine = line.split(",");

                //create temp variable to hold info from the split lines
                int cusID = Integer.parseInt(splitLine[0]);
                String description = splitLine[1];
                double balance = Double.parseDouble(splitLine[2]);
                double rate = Double.parseDouble(splitLine[3]);
                Date payDueDate = new SimpleDateFormat("MM/dd/yyyy").parse(splitLine[4]);
                Date notifyPayDate = new SimpleDateFormat("MM/dd/yyyy").parse(splitLine[5]);
                double currPayDue = Double.parseDouble(splitLine[6]);
                Date lastPayDate = new SimpleDateFormat("MM/dd/yyyy").parse(splitLine[7]);
                Byte missedPay = Byte.parseByte(splitLine[8]);
                String accountType = splitLine[9];

                //add the new data (in our case loan) to the ArrayList
                importLoans.add(new LoanAccount(cusID, description, balance, rate, payDueDate, currPayDue, notifyPayDate, lastPayDate, missedPay, accountType));
            }

            //increment the line number
            lineNum++;
        }

        //close the bufferfile and return the ArrayList
        loansBR.close();
        return importLoans;

    }//end of loans data import method

    //current method to grab data from the checkings textfile in "memory"
    public static ArrayList<CheckingAccount> checkingsImportFile() throws IOException, ParseException {

        //creates a file referencing the text file in the memory folder
        File checkingsFileIn = new File("memory/checkings.txt");

        //creates a bufferedreader to read from a file
        BufferedReader checkingsBR = null;
        checkingsBR = new BufferedReader(new InputStreamReader(new FileInputStream(checkingsFileIn)));

        //buffer string to temporarily hold the line retrieved
        String line;

        //creates the ArrayList of data
        ArrayList<CheckingAccount> importChecking = new ArrayList<>();

        //generic counter to know the line currently on
        int lineNum = 0;

        //while loop to go through the file
        while ((line = checkingsBR.readLine()) != null) {

            //if the file has a header, this if statement is to avoid that
            //remove "if" if final file has no header
            if(lineNum > 0) {

                //split the line into an array of strings
                String[] splitLine = line.split(",");

                //create temp variable to hold info from the split lines
                int cusID = Integer.parseInt(splitLine[0]);
                double balance = Double.parseDouble(splitLine[1]);
                String accountType = splitLine[2];
                Byte indicatedOverdraftProtection = Byte.parseByte(splitLine[3]);    // 1=true   0=false
                int overdraftsThisMonth = Integer.parseInt(splitLine[4]);
                Date dateAccountOpened = new SimpleDateFormat("MM/dd/yyyy").parse(splitLine[5]);

                //add the new data (in our case checking) to the ArrayList
                importChecking.add(new CheckingAccount(cusID, balance, accountType, indicatedOverdraftProtection, overdraftsThisMonth, dateAccountOpened));
            }

            //increment the line number
            lineNum++;
        }

        //close the bufferfile and return the ArrayList
        checkingsBR.close();
        return importChecking;
    }//end of checking data import method

    //current method to grab data from the checkings textfile in "memory"
    public static ArrayList<SavingAccount> savingsImportFile() throws IOException, ParseException {

        //creates a file referencing the text file in the memory folder
        File savingsFileIn = new File("memory/savings.txt");

        //creates a bufferedreader to read from a file
        BufferedReader savingsBR = null;
        savingsBR = new BufferedReader(new InputStreamReader(new FileInputStream(savingsFileIn)));

        //buffer string to temporarily hold the line retrieved
        String line;

        //creates the ArrayList of data
        ArrayList<SavingAccount> importSaving = new ArrayList<>();

        //generic counter to know the line currently on
        int lineNum = 0;

        //while loop to go through the file
        while ((line = savingsBR.readLine()) != null) {

            //if the file has a header, this if statement is to avoid that
            //remove "if" if final file has no header
            if(lineNum > 0) {

                //split the line into an array of strings
                String[] splitLine = line.split(",", -1);

                //create temp variable to hold info from the split lines
                int cusID = Integer.parseInt(splitLine[0]);
                double balance = Double.parseDouble(splitLine[1]);
                double currentInterestRate = Double.parseDouble(splitLine[2]);
                Date dateAccountOpened = new SimpleDateFormat("MM/dd/yyyy").parse(splitLine[3]);

                if(splitLine[4].equals("")) {
                    //add the new data (in our case savings) to the ArrayList
                    importSaving.add(new SavingAccount(cusID, balance, currentInterestRate, dateAccountOpened));

                }
                else{
                    //if it is a CD...
                    Date dateCDDue = new SimpleDateFormat("MM/dd/yyyy").parse(splitLine[4]);
                    importSaving.add(new SavingAccount(cusID, balance, currentInterestRate, dateAccountOpened, dateCDDue));

                }
            }

            //increment the line number
            lineNum++;
        }

        //close the bufferfile and return the ArrayList
        savingsBR.close();
        return importSaving;

    }//end of checking data import method

    //method to export customers data to a file
    public static void exportCustomers(ArrayList<Customer> customers) throws FileNotFoundException {
        //create a new PrintWriter to write to a file
        PrintWriter writer = new PrintWriter("memory/customers"+String.valueOf(System.currentTimeMillis())+".txt");

        //printing the headers of the file
        writer.println("SocialSecurityNumber,Address,City,State,ZIP,FirstName,LastName,");

        //print the info for each customer
        for(Customer customer: customers) {
            writer.println(customer.getSocialSecurityNumber() + "," + customer.getStreetAddress() + "," +
                    customer.getCity() + "," + customer.getState() + "," + customer.getZipCode() + "," +
                    customer.getFirstName() + "," + customer.getLastName() + ",");
        }

        //close the PrintWriter object
        writer.close();
    }

    //method to export loans data to a file
    public static void exportLoans(ArrayList<LoanAccount> loans) throws FileNotFoundException {
        //create a new PrintWriter to write to a file
        PrintWriter writer = new PrintWriter("memory/loans"+String.valueOf(System.currentTimeMillis())+".txt");

        //printing the headers of the file
        writer.println("CustomerID,Description,CurrentBalance,CurrentInterestRate,DatePaymentDue,DateNotifiedOfPayment,CurrentPaymentDue,DateSinceLastPaymentMade,MissedPaymentFlag,AccountType,");

        //print the info for each customer
        for(LoanAccount loan: loans) {
            writer.println(loan.getCustomerID() + "," + loan.getDescription() + "," + loan.getAccountBalance() + "," +
                    loan.getCurrentInterestRate() + "," + loan.getDatePaymentDue() + "," +
                    loan.getDatePaymentNotified() + "," + loan.getCurrentPaymentDue() + "," +
                    loan.getLastPaymentDate() + "," + loan.getMissedPaymentFlag() + "," + loan.getLoanType() + ",");
        }

        //close the PrintWriter object
        writer.close();
    }

}