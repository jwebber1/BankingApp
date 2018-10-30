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
    NumberFormat moneyFormat = NumberFormat.getCurrencyInstance();

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



        //debugging the import process
        /*
        for(Customer customer: customers){System.out.println(customer.toString());}
        for(LoanAccount loan: loans){System.out.println(loan.toString());}
        for(CheckingAccount checking: checkings){System.out.println(checking.toString());}
        for(SavingAccount saving: savings){System.out.println(saving.toString());}
        */

        //exporting data
        /*
<<<<<<< HEAD
        try {
            exportCustomers(customers);
=======
        try {a
>>>>>>> 736bc916e3ffe68950eeeb99f2c258c398c2a640
            exportLoans(loans);
            exportCheckings(checkings);
            exportSavings(savings);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        */

<<<<<<< HEAD
        ArrayList<Account> searchResults = SearchAccounts(423453245, savings, checkings, loans);
        for (Account account: searchResults) {
            System.out.println(account.toString());
        }
=======
>>>>>>> 736bc916e3ffe68950eeeb99f2c258c398c2a640

        System.exit(0); //currently stops the program, may want to remove later
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
        ArrayList<Person> importPerson = new ArrayList<>();

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
<<<<<<< HEAD
                String pin = splitLine[7];

                //add the new data (in our case checking) to the ArrayList
                importCustomer.add(new Customer(socialSecurityNumber, streetAddress, city, state, zipCode, firstName, lastName, pin));
=======
>>>>>>> 736bc916e3ffe68950eeeb99f2c258c398c2a640
            }

            //increment the line number
            lineNum++;
        }

        //close the bufferfile and return the ArrayList
        customersBR.close();
        return importCustomer;
    }//end of customer data import method

    //current method to grab data from the tellers textfile in "memory"
    public static ArrayList<Teller> tellerImportFile() throws IOException, ParseException {

        //creates a file referencing the text file in the memory folder
        File tellersFileIn = new File("memory/tellers.txt");

        //creates a bufferedreader to read from a file
        BufferedReader tellersBR = null;
        tellersBR = new BufferedReader(new InputStreamReader(new FileInputStream(tellersFileIn)));

        //buffer string to temporarily hold the line retrieved
        String line;

        //creates the ArrayList of data
        ArrayList<Teller> importTeller = new ArrayList<>();

        //generic counter to know the line currently on
        int lineNum = 0;

        //while loop to go through the file
        while ((line = tellersBR.readLine()) != null) {

            //if the file has a header, this if statement is to avoid that
            //remove "if" if final file has no header
            if(lineNum > 0) {

                //split the line into an array of strings
                String[] splitLine = line.split(",");

                //create temp variable to hold info from the split lines
                int employeeID = Integer.parseInt(splitLine[0]);

                String firstName = splitLine[1];
                String lastName = splitLine[2];
                String password = splitLine[3];

                //add the new data (in our case checking) to the ArrayList
                importTeller.add(new Teller(employeeID, firstName, lastName, password));
            }

            //increment the line number
            lineNum++;
        }

        //close the bufferfile and return the ArrayList
        tellersBR.close();
        return importTeller;
    }//end of teller data import method

    //current method to grab data from the customers textfile in "memory"
    public static ArrayList<Manager> managerImportFile() throws IOException, ParseException {

        //creates a file referencing the text file in the memory folder
        File managersFileIn = new File("memory/managers.txt");

        //creates a bufferedreader to read from a file
        BufferedReader managersBR = null;
        managersBR = new BufferedReader(new InputStreamReader(new FileInputStream(managersFileIn)));

        //buffer string to temporarily hold the line retrieved
        String line;

        //creates the ArrayList of data
        ArrayList<Manager> importManager = new ArrayList<>();

        //generic counter to know the line currently on
        int lineNum = 0;

        //while loop to go through the file
        while ((line = managersBR.readLine()) != null) {

            //if the file has a header, this if statement is to avoid that
            //remove "if" if final file has no header
            if(lineNum > 0) {

                //split the line into an array of strings
                String[] splitLine = line.split(",");

                //create temp variable to hold info from the split lines
                int employeeID = Integer.parseInt(splitLine[0]);
                String firstName = splitLine[1];
                String lastName = splitLine[2];
                String password = splitLine[3];

                //add the new data (in our case checking) to the ArrayList
                importManager.add(new Manager(employeeID, firstName, lastName, password));
            }

            //increment the line number
            lineNum++;
        }

        //close the bufferfile and return the ArrayList
        managersBR.close();
        return importManager;
    }//end of manager data import method

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
                Date payDueDate =   new SimpleDateFormat("MM/dd/yyyy").parse(splitLine[4]);
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

    //current method to grab data from the savings textfile in "memory"
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

    }//end of saving data import method

    //method to export customers data to a file
    public static void exportCustomers(ArrayList<Customer> customers) throws FileNotFoundException {
        //create a new PrintWriter to write to a file
        PrintWriter writer = new PrintWriter("memory/customers"+String.valueOf(System.currentTimeMillis())+".txt");

        //printing the headers of the file
        writer.println("SocialSecurityNumber,Address,City,State,ZIP,FirstName,LastName,");

        //print the info for each customer
<<<<<<< HEAD
        for(Customer customer: customers) {
            writer.println(customer.getId() + "," + customer.getStreetAddress() + "," +
                    customer.getCity() + "," + customer.getState() + "," + customer.getZipCode() + "," +
                    customer.getfName() + "," + customer.getlName() + ",");
=======
>>>>>>> 736bc916e3ffe68950eeeb99f2c258c398c2a640
        }

        //close the PrintWriter object
        writer.close();

    }//end of exportCustomers

    //method to export loans data to a file
    public static void exportLoans(ArrayList<LoanAccount> loans) throws FileNotFoundException {
        //create a new PrintWriter to write to a file
        PrintWriter writer = new PrintWriter("memory/loans"+String.valueOf(System.currentTimeMillis())+".txt");

        //printing the headers of the file
        writer.println("CustomerID,Description,CurrentBalance,CurrentInterestRate,DatePaymentDue,DateNotifiedOfPayment,CurrentPaymentDue,DateSinceLastPaymentMade,MissedPaymentFlag,AccountType,");

        //print the info for each loan
        for(LoanAccount loan: loans) {
            writer.println(loan.getCustomerID() + "," + loan.getDescription() + "," + loan.getAccountBalance() + "," +
                    loan.getCurrentInterestRate() + "," +
                    new SimpleDateFormat("MM/dd/yyy").format(loan.getDatePaymentDue()) + "," +
                    new SimpleDateFormat("MM/dd/yyy").format(loan.getDatePaymentNotified()) + "," +
                    new SimpleDateFormat("MM/dd/yyy").format(loan.getCurrentPaymentDue()) + "," +
                    new SimpleDateFormat("MM/dd/yyy").format(loan.getLastPaymentDate()) + "," +
                    loan.getMissedPaymentFlag() + "," + loan.getAccountType() + ",");
        }

        //close the PrintWriter object
        writer.close();

    }//end of exportLoans

    //method to export checkings data to a file
    public static void exportCheckings(ArrayList<CheckingAccount> checkings) throws FileNotFoundException {
        //create a new PrintWriter to write to a file
        PrintWriter writer = new PrintWriter("memory/checkings"+String.valueOf(System.currentTimeMillis())+".txt");

        //printing the headers of the file
        writer.println("CustomerID,CheckingBalance,CheckingAccountType,hasBackup,Overdrafts,DateOpened,");

        //print the info for each checking
        for(CheckingAccount checking: checkings) {
            writer.println(checking.getCustomerID() + "," + checking.getAccountBalance() + "," +
                    checking.getAccountType() + "," + checking.getIndicatedOverdraftProtection() + "," +
                    checking.getOverdraftsThisMonth() + "," +
                    new SimpleDateFormat("MM/dd/yyy").format(checking.getDateAccountOpened()) + ",");
        }

        //close the PrintWriter object
        writer.close();

    }//end of exportCheckings

    //method to export savings data to a file
    public static void exportSavings(ArrayList<SavingAccount> savings) throws FileNotFoundException {
        //create a new PrintWriter to write to a file
        PrintWriter writer = new PrintWriter("memory/savings"+String.valueOf(System.currentTimeMillis())+".txt");

        //printing the headers of the file
        writer.println("CustomerID,AccountBalance,CurrInterestRate,DateAccOpened,DateCDDue,");

        //print the info for each saving
        for(SavingAccount saving: savings) {

            //check to see if it is a CD just a regular savings account
            if(saving.getDateCDDue()==null) {
                writer.println(saving.getCustomerID() + "," + saving.getAccountBalance() + "," +
                        saving.getCurrentInterestRate() + "," + saving.getDateAccountOpened() + ",,");
            }
            else{
                writer.println(saving.getCustomerID() + "," + saving.getAccountBalance() + "," +
                        saving.getCurrentInterestRate() + "," +
                        new SimpleDateFormat("MM/dd/yyy").format(saving.getDateAccountOpened()) + "," +
                        new SimpleDateFormat("MM/dd/yyy").format(saving.getDateCDDue()) + ",");
            }
        }

        //close the PrintWriter object
        writer.close();

    }//end of exportSavings

    //search all the savingaccounts for a matching customerID
<<<<<<< HEAD
    public static ArrayList<Account> SearchAccounts(int custID, ArrayList<SavingAccount> savings, ArrayList<CheckingAccount> checkings, ArrayList<LoanAccount> loans){

        //create an arraylist for any account type
        ArrayList<Account> searchResults = new ArrayList<>();

        for(int i=0; i<savings.size()-1; i++){
            if(savings.get(i).getCustomerID()==custID){
                searchResults.add(savings.get(i));
            }
        }
        for(int i=0; i<checkings.size()-1; i++){
            if(checkings.get(i).getCustomerID()==custID){
                searchResults.add(checkings.get(i));
            }
        }
        for(int i=0; i<loans.size()-1; i++){
            if(loans.get(i).getCustomerID()==custID){
                searchResults.add(loans.get(i));
            }
        }

        return searchResults;
    }

}//end of main>>>>>>> 736bc916e3ffe68950eeeb99f2c258c398c2a640
