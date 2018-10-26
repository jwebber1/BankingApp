import UI.AccountCreationScene;
import UI.CustomerCreationScene;
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
        primaryStage.setTitle("Banking App");
        primaryStage.setScene(new Scene(new CustomerCreationScene().getRoot(), 640, 480));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);

        //importing data

        //creating arraylists to hold the objects
        ArrayList<Person> persons = new ArrayList<>();
        ArrayList<LoanAccount> loans = new ArrayList<>();
        ArrayList<CheckingAccount> checkings = new ArrayList<>();
        ArrayList<SavingAccount> savings = new ArrayList<>();

        //attempting to import the data into the arraylists
        try {
            persons = personsImportFile();
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
        for(Person person: persons){System.out.println(person.toString());}
        for(LoanAccount loan: loans){System.out.println(loan.toString());}
        for(CheckingAccount checking: checkings){System.out.println(checking.toString());}
        for(SavingAccount saving: savings){System.out.println(saving.toString());}
        */

        //exporting data
        /*
        try {a
            exportPersons(persons);
            exportLoans(loans);
            exportCheckings(checkings);
            exportSavings(savings);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        */



        //debugging the SearchAccounts method
        ArrayList<ArrayList> searchResults = SearchAccounts(423453245, savings, checkings, loans);
        for (int i = 0; i<3; i++){
            for(int j=0; j<searchResults.get(i).size(); j++){
                System.out.println(searchResults.get(i).get(j).toString());
            }
        }


        System.exit(0); //currently stops the program, may want to remove later
    }

    //current method to grab data from the customers textfile in "memory"
    public static ArrayList<Person> personsImportFile() throws IOException, ParseException {

        //creates a file referencing the text file in the memory folder
        File personsFileIn = new File("memory/persons.txt");

        //creates a bufferedreader to read from a file
        BufferedReader personsBR = null;
        personsBR = new BufferedReader(new InputStreamReader(new FileInputStream(personsFileIn)));

        //buffer string to temporarily hold the line retrieved
        String line;

        //creates the ArrayList of data
        ArrayList<Person> importPerson = new ArrayList<>();

        //generic counter to know the line currently on
        int lineNum = 0;

        //while loop to go through the file
        while ((line = personsBR.readLine()) != null) {

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
                int userLevel = 1;  //default to customer
                //hard code userLevel to ssn
                if (socialSecurityNumber == 000000002 || socialSecurityNumber == 000000001){userLevel = 2;}
                else if (socialSecurityNumber == 000000000){userLevel = 3;}


                //add the new data (in our case checking) to the ArrayList
                importPerson.add(new Person(socialSecurityNumber, streetAddress, city, state, zipCode, firstName, lastName, userLevel));
            }

            //increment the line number
            lineNum++;
        }

        //close the bufferfile and return the ArrayList
        personsBR.close();
        return importPerson;
    }//end of customer data import method

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
    public static void exportPersons(ArrayList<Person> persons) throws FileNotFoundException {
        //create a new PrintWriter to write to a file
        PrintWriter writer = new PrintWriter("memory/persons"+String.valueOf(System.currentTimeMillis())+".txt");

        //printing the headers of the file
        writer.println("SocialSecurityNumber,Address,City,State,ZIP,FirstName,LastName,");

        //print the info for each customer
        for(Person person: persons) {
            writer.println(person.getId() + "," + person.getStreetAddress() + "," +
                    person.getCity() + "," + person.getState() + "," + person.getZipCode() + "," +
                    person.getfName() + "," + person.getlName() + ",");
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
    public static ArrayList<ArrayList> SearchAccounts(int custID, ArrayList<SavingAccount> savings, ArrayList<CheckingAccount> checkings, ArrayList<LoanAccount> loans){

        //create an arraylist for any account arraylist type
        ArrayList<ArrayList> searchResults = new ArrayList<>();

        //create temporary arraylists to put the found info into
        ArrayList<SavingAccount> personSavingsAccounts = new ArrayList<>();
        ArrayList<CheckingAccount> personCheckingsAccounts = new ArrayList<>();
        ArrayList<LoanAccount> personLoansAccounts = new ArrayList<>();

        for(int i=0; i<savings.size()-1; i++){
            if(savings.get(i).getCustomerID()==custID){
                personSavingsAccounts.add(savings.get(i));
            }
        }
        searchResults.add(0, personSavingsAccounts);

        for(int i=0; i<checkings.size()-1; i++){
            if(checkings.get(i).getCustomerID()==custID){
                personCheckingsAccounts.add(checkings.get(i));
            }
        }
        searchResults.add(1, personCheckingsAccounts);

        for(int i=0; i<loans.size()-1; i++){
            if(loans.get(i).getCustomerID()==custID){
                personLoansAccounts.add(loans.get(i));
            }
        }
        searchResults.add(2, personLoansAccounts);


        //will return an arraylist in the format of searchResult = (savingsArrayList, checkingArrayList, loanArrayList)
        //each of these individual array lists may have between 0 to many accounts in them
        return searchResults;
    }








}//end of main
