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

        ArrayList<Person> persons = new ArrayList<>();

        //attempting to import the data into the arraylists
        try {
            persons = importFromFiles();
        }
        catch (IOException e) {e.printStackTrace();}
        catch (ParseException e) {e.printStackTrace();}



        //debugging the import process
        //debugImport(persons);
        /**/
        //persons.get(0).setfName("Hunter");
        //persons.get(0).setlName("Rummington");

        //exporting data

        try {
            exportToFile(persons);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


//used to format money, will probably need to be moved to "controller" for the GUI
        NumberFormat moneyFormat = NumberFormat.getCurrencyInstance();

        //debugging debit withdraw/deposit methods
/*
        CheckingAccount tempChecking = (CheckingAccount) persons.get(0).getAccounts().get(1).get(0);
        SavingAccount tempSaving = (SavingAccount) persons.get(0).getAccounts().get(0).get(0);

        System.out.println(moneyFormat.format(tempChecking.getAccountBalance()));
        tempChecking.deposit(100.00);
        System.out.println(moneyFormat.format(tempChecking.getAccountBalance()));
        tempChecking.withdraw(tempSaving, 200.00);
        System.out.println(moneyFormat.format(tempChecking.getAccountBalance()));
        */



        //currently stops the program, may want to remove later
        System.exit(0);
    }

    //importing data
    public static ArrayList<Person> importFromFiles() throws IOException, ParseException {

        //create the array of persons and fill it from the file persons.txt
        ArrayList<Person> persons = new ArrayList<>();
        //todo- CCs will go into loansImportFile                                                                 \/
        persons = personsImportFile(savingsImportFile(), checkingsImportFile(checksImportFile()), loansImportFile());

        //return the array
        return persons;
    }

    //current method to grab data from the persons textfile in "memory"
    public static ArrayList<Person> personsImportFile(ArrayList<SavingAccount> savingsIn, ArrayList<CheckingAccount> checkingIn, ArrayList<LoanAccount> loansIn) throws IOException, ParseException {

        //creates a file referencing the text file in the memory folder
        File personsFileIn = new File("memory/persons.txt");

        //creates a bufferedreader to read from a file
        BufferedReader personsBR = null;
        personsBR = new BufferedReader(new InputStreamReader(new FileInputStream(personsFileIn)));

        //buffer string to temporarily hold the line retrieved
        String line;

        //creates the ArrayList of data
        ArrayList<Person> importPerson = new ArrayList<>();
        ArrayList<ArrayList> accounts = new ArrayList<>();

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
                accounts = searchAccounts(socialSecurityNumber, savingsIn, checkingIn, loansIn);

                //add the new data (in our case checking) to the ArrayList
                importPerson.add(new Person(socialSecurityNumber, streetAddress, city, state, zipCode, firstName, lastName, userLevel, accounts));

                //debugging importPersons
                //System.out.println("count: " + (lineNum) + "\t" + importPerson.get(lineNum-1).toString());
            }

            //increment the line number
            lineNum++;
        }

        //close the bufferfile and return the ArrayList
        personsBR.close();
        return importPerson;
    }//end of person data import method

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
                //debugging importSavings
                //System.out.println("count: " + (lineNum) + "\t" + importSaving.get(lineNum-1).toString());
            }

            //increment the line number
            lineNum++;

        }//end of while loop

        //close the bufferfile and return the ArrayList
        savingsBR.close();
        return importSaving;

    }//end of saving data import method

    //current method to grab data from the checkings textfile in "memory"
    public static ArrayList<CheckingAccount> checkingsImportFile(ArrayList<Check> checksIn) throws IOException, ParseException {

        //creates a file referencing the text file in the memory folder
        File checkingsFileIn = new File("memory/checkings.txt");

        //creates a bufferedreader to read from a file
        BufferedReader checkingsBR = null;
        checkingsBR = new BufferedReader(new InputStreamReader(new FileInputStream(checkingsFileIn)));

        //buffer string to temporarily hold the line retrieved
        String line;

        //creates the ArrayList of data
        ArrayList<CheckingAccount> importChecking = new ArrayList<>();
        ArrayList<Check> checks = new ArrayList<>();

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
                Byte hasOverdraftProtection = Byte.parseByte(splitLine[3]);    // 1=true   0=false
                Byte connectedToATMCard = Byte.parseByte(splitLine[4]);    // 1=true   0=false
                int overdraftsThisMonth = Integer.parseInt(splitLine[5]);
                Date dateAccountOpened = new SimpleDateFormat("MM/dd/yyyy").parse(splitLine[6]);
                checks = searchChecks(cusID, checksIn);

                //add the new data (in our case checking) to the ArrayList
                importChecking.add(new CheckingAccount(cusID, balance, accountType, hasOverdraftProtection, connectedToATMCard,overdraftsThisMonth, dateAccountOpened, checks));

                //debugging importChecking
                //System.out.println("count: " + (lineNum) + "\t" + importChecking.get(lineNum-1).toString());
            }

            //increment the line number
            lineNum++;
        }

        //close the bufferfile and return the ArrayList
        checkingsBR.close();
        return importChecking;

    }//end of checking data import method

    //current method to grab data from the checks textfile in "memory"
    public static ArrayList<Check> checksImportFile() throws IOException, ParseException {

        //creates a file referencing the text file in the memory folder
        File checksFileIn = new File("memory/checks.txt");

        //creates a bufferedreader to read from a file
        BufferedReader checksBR = null;
        checksBR = new BufferedReader(new InputStreamReader(new FileInputStream(checksFileIn)));

        //buffer string to temporarily hold the line retrieved
        String line;

        //creates the ArrayList of data
        ArrayList<Check> importChecks = new ArrayList<>();

        //generic counter to know the line currently on
        int lineNum = 0;

        //while loop to go through the file
        while ((line = checksBR.readLine()) != null) {

            //if the file has a header, this if statement is to avoid that
            //remove "if" if final file has no header
            if(lineNum > 0) {

                //split the line into an array of strings
                String[] splitLine = line.split(",");

                //create temp variable to hold info from the split lines
                int cusID = Integer.parseInt(splitLine[0]);
                int checkID = Integer.parseInt(splitLine[1]);
                double checkAmt = Double.parseDouble(splitLine[2]);
                String payTo = splitLine[3];
                Date dateCheck = new SimpleDateFormat("MM/dd/yyyy").parse(splitLine[4]);
                String memo = splitLine[5];

                //add the new data (in our case checking) to the ArrayList
                importChecks.add(new Check(cusID, checkID, checkAmt, payTo, dateCheck, memo));

                //debugging importPersons
                //System.out.println("count: " + (lineNum) + "\t" + importChecks.get(lineNum-1).toString());
            }

            //increment the line number
            lineNum++;
        }

        //close the bufferfile and return the ArrayList
        checksBR.close();
        return importChecks;
    }//end of checks data import method

    //todo- need to finish when CC stuff comes in, see checking and check imports above for template
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

                //debugging importLoan
                //System.out.println("count: " + (lineNum) + "\t" + importLoans.get(lineNum-1).toString());
            }

            //increment the line number
            lineNum++;
        }

        //close the bufferfile and return the ArrayList
        loansBR.close();
        return importLoans;

    }//end of loans data import method

    //current method to grab data from the Credit Cards textfile in "memory" (to be created later)
    /*
    public static ArrayList<CreditCards> ccImportFile() throws IOException, ParseException {
        //stuff
    }
    */



















    //method to export persons data to a file
    public static void exportToFile(ArrayList<Person> persons) throws FileNotFoundException {
        //create a new PrintWriter to write to a file
        PrintWriter personWriter = new PrintWriter("memory/persons.txt");
        PrintWriter savingWriter = new PrintWriter("memory/savings.txt");
        PrintWriter checkingWriter = new PrintWriter("memory/checkings.txt");
        PrintWriter checkWriter = new PrintWriter("memory/checks.txt");
        PrintWriter loanWriter = new PrintWriter("memory/loans.txt");
        //PrintWriter CCWriter = new PrintWriter("memory/creditcards.txt");

        //printing the headers of the files
        personWriter.println("SocialSecurityNumber,Address,City,State,ZIP,FirstName,LastName,");
        savingWriter.println("CustomerID,AccountBalance,CurrInterestRate,DateAccOpened,DateCDDue,");
        checkingWriter.println("CustomerID,CheckingBalance,CheckingAccountType,hasBackup,Overdrafts,DateOpened,");
        checkWriter.println("CustomerID,CheckID,CheckAmt,PayTo,DateCheck,Memo,");
        loanWriter.println("CustomerID,Description,CurrentBalance,CurrentInterestRate,DatePaymentDue,DateNotifiedOfPayment,CurrentPaymentDue,DateSinceLastPaymentMade,MissedPaymentFlag,AccountType,");
        //CCWriter.println("")


        ArrayList<SavingAccount> savings = new ArrayList<>();
        ArrayList<CheckingAccount> checkings = new ArrayList<>();
        ArrayList<LoanAccount> loans = new ArrayList<>();

        //print the info for each customer
        for(Person person: persons) {
            personWriter.println(person.getId() + "," + person.getStreetAddress() + "," +
                    person.getCity() + "," + person.getState() + "," + person.getZipCode() + "," +
                    person.getfName() + "," + person.getlName() + ",");

            savings = person.getAccounts().get(0);
            checkings = person.getAccounts().get(1);
            loans = person.getAccounts().get(2);

            //todo- CC stuff here
            ArrayList<Check> checks = new ArrayList<>();
            //ArrayList<CreditCard> CCs = new ArrayList<>();

            for (SavingAccount saving: savings) {


                //check to see if it is a CD just a regular savings account
                if(saving.getDateCDDue() == null) {
                    savingWriter.println(saving.getCustomerID() + "," +
                            saving.getAccountBalance() + "," +
                            saving.getCurrentInterestRate() + "," +
                            new SimpleDateFormat("MM/dd/yyyy").format(saving.getDateAccountOpened()) + ",,");
                }
                else{
                    savingWriter.println(saving.getCustomerID() + "," +
                            saving.getAccountBalance() + "," +
                            saving.getCurrentInterestRate() + "," +
                            new SimpleDateFormat("MM/dd/yyy").format(saving.getDateAccountOpened()) + "," +
                            new SimpleDateFormat("MM/dd/yyy").format(saving.getDateCDDue()) + ",");
                }

            }
            for (CheckingAccount checking: checkings) {

                checkingWriter.println(checking.getCustomerID() + "," +
                        checking.getAccountBalance() + "," +
                        checking.getAccountType() + "," +
                        checking.getHasOverdraftProtection() + "," +
                        checking.getOverdraftsThisMonth() + "," +
                        new SimpleDateFormat("MM/dd/yyy").format(checking.getDateAccountOpened()) + ",");


                checks = checking.getChecks();
                for (Check check: checks) {
                    checkWriter.println(check.getCustomerID() + "," +
                            check.getCheckID() + "," +
                            check.getCheckAmt() + "," +
                            check.getPayTo() + "," +
                            new SimpleDateFormat("MM/dd/yyy").format(check.getDateCheck()) + "," +
                            new SimpleDateFormat("MM/dd/yyy").format(check.getDateCheck()) + ",");
                }
            }

            for (LoanAccount loan: loans) {
                loanWriter.println(loan.getCustomerID() + "," +
                        loan.getDescription() + "," +
                        loan.getAccountBalance() + "," +
                        loan.getCurrentInterestRate() + "," +
                        new SimpleDateFormat("MM/dd/yyy").format(loan.getDatePaymentDue()) + "," +
                        new SimpleDateFormat("MM/dd/yyy").format(loan.getDatePaymentNotified()) + "," +
                        loan.getCurrentPaymentDue() + "," +
                        new SimpleDateFormat("MM/dd/yyy").format(loan.getLastPaymentDate()) + "," +
                        loan.getMissedPaymentFlag() + "," +
                        loan.getAccountType() + ",");
            }
        }

        //close the PrintWriter objects
        personWriter.close();
        savingWriter.close();
        checkingWriter.close();
        checkWriter.close();
        loanWriter.close();
        //CCWriter.close();

    }//end of exportPersons

    //method to export savings data to a file
    public static void exportSavings(ArrayList<SavingAccount> savings) throws FileNotFoundException {
        //create a new PrintWriter to write to a file
        PrintWriter writer = new PrintWriter("memory/savings.txt");

        //printing the headers of the file
        writer.println("CustomerID,AccountBalance,CurrInterestRate,DateAccOpened,DateCDDue,");

        //print the info for each saving
        for(SavingAccount saving: savings) {

            //check to see if it is a CD just a regular savings account
            if(saving.getDateCDDue() == null) {
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

    //method to export checkings data to a file
    public static void exportCheckings(ArrayList<CheckingAccount> checkings) throws FileNotFoundException {
        //create a new PrintWriter to write to a file
        PrintWriter writer = new PrintWriter("memory/checkings.txt");

        //printing the headers of the file
        writer.println("CustomerID,CheckingBalance,CheckingAccountType,hasBackup,Overdrafts,DateOpened,");

        //print the info for each checking
        for(CheckingAccount checking: checkings) {
            writer.println(checking.getCustomerID() + "," + checking.getAccountBalance() + "," +
                    checking.getAccountType() + "," + checking.getHasOverdraftProtection() + "," +
                    checking.getOverdraftsThisMonth() + "," +
                    new SimpleDateFormat("MM/dd/yyy").format(checking.getDateAccountOpened()) + ",");
        }

        //close the PrintWriter object
        writer.close();

    }//end of exportCheckings

    //method to export checks data to a file
    public static void exportChecks(ArrayList<Check> checks) throws FileNotFoundException {
        //create a new PrintWriter to write to a file
        PrintWriter writer = new PrintWriter("memory/checks.txt");

        //printing the headers of the file
        writer.println("CustomerID,CheckID,CheckAmt,PayTo,DateCheck,Memo,");

        //print the info for each checking
        for(Check check: checks) {
            writer.println(check.getCustomerID() + "," + check.getCheckID() + "," +
                    check.getCheckAmt() + "," + check.getPayTo() + "," +
                    check.getDateCheck() + "," +
                    new SimpleDateFormat("MM/dd/yyy").format(check.getDateCheck()) + ",");
        }

        //close the PrintWriter object
        writer.close();

    }//end of exportCheckings

    public static void exportLoans(ArrayList<LoanAccount> loans) throws FileNotFoundException {
        //create a new PrintWriter to write to a file
        PrintWriter writer = new PrintWriter("memory/loans.txt");

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

    //todo- CC stuff to fix later
    //method to export Credit Card data to a file
    /*
    public static void exportCCs(ArrayList<CreditCards> creditCards) throws FileNotFoundException {

    }//end of exportCCs
    */

    //search all the accounts for a matching customerID
    public static ArrayList<ArrayList> searchAccounts(int custID, ArrayList<SavingAccount> savings, ArrayList<CheckingAccount> checkings, ArrayList<LoanAccount> loans){

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

    }//end of searchAccounts

    //search all checks for customer ID
    public static ArrayList<Check> searchChecks(int custID, ArrayList<Check> checks){

        //create a temporary arraylist for checks belonging to a customer
        ArrayList<Check> searchResults = new ArrayList<>();

        for(int i=0; i<checks.size(); i++){
            if(checks.get(i).getCustomerID()==custID){
                searchResults.add(checks.get(i));
            }
        }

        //will return an arraylist in the format of searchResult = (savingsArrayList, checkingArrayList, loanArrayList)
        //each of these individual array lists may have between 0 to many accounts in them
        return searchResults;

    }//end of searchChecks

    //search all CCs for customer ID
    /*
    public static ArrayList<CreditCards> searchCCs(int custID, ArrayList<CreditCards> CCs){

        //create a temporary arraylist for checks belonging to a customer
        ArrayList<CreditCards> searchResults = new ArrayList<>();

        for(int i=0; i<CCs.size()-1; i++){
            if(CCs.get(i).getCustomerID()==custID){
                searchResults.add(CCs.get(i));
            }
        }

        //will return an arraylist in the format of searchResult = (savingsArrayList, checkingArrayList, loanArrayList)
        //each of these individual array lists may have between 0 to many accounts in them
        return searchResults;

    }//end of searchChecks
    */















    //debugging the import method
    public static void debugImport(ArrayList<Person> people){
        System.out.println("Debugging import process");

        //temporary array lists
        ArrayList<SavingAccount> savings = new ArrayList<>();
        ArrayList<CheckingAccount> checkings = new ArrayList<>();
        ArrayList<LoanAccount> loans = new ArrayList<>();

        //looping through each person
        for(int i=0; i<people.size()-1; i++){
            System.out.println("Person: " + (i+1));
            System.out.println(people.get(i).toString());

            savings = people.get(i).getAccounts().get(0);
            checkings = people.get(i).getAccounts().get(1);
            loans = people.get(i).getAccounts().get(2);

            ArrayList<Check> checks = new ArrayList<>();
            //ArrayList<CreditCard> CCs = new ArrayList<>();

            //looping through each saving account
            for(int j=0; j<savings.size(); j++){
                System.out.println(savings.get(j).toString());
            }

            //looping through each checking account
            for(int j=0; j<checkings.size(); j++){
                System.out.println(checkings.get(j).toString());

                checks = checkings.get(j).getChecks();

                //looping through each check
                for(int k=0; k<checks.size(); k++){
                    System.out.println(checks.get(k).toString());
                }
            }

            //todo- add the CCs here after getting it
            //looping through each loan account
            for(int j=0; j<loans.size(); j++){
                System.out.println(loans.get(j).toString());

                /*
                looping through each Credit card
                for(int k=0; k<CCss.size(); k++){
                    System.out.println(CCs.get(k).toString());
                }
                */
            }

            System.out.println();

        }//end of for loop
    }//end of debugImport

}//end of main
