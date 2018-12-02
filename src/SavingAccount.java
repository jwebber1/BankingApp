import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList;
import java.io.*;
import java.text.ParseException;

public class SavingAccount extends Account {
    private double currentInterestRate;
    static ArrayList<SavingAccount> savingAccounts = new ArrayList<>();
    private Date dateAccOpenedIn = new Date();


    //constructor for the SavingAccount
    public SavingAccount(int cusIDIn, double accBalIn, double currIntRateIn, Date dateAccOpenedIn) {
        super(cusIDIn, accBalIn, dateAccOpenedIn, "Saving");
        this.currentInterestRate = currIntRateIn;
        this.dateAccOpenedIn = dateAccOpenedIn;
    }

    //Getters and setters
    public double getCurrentInterestRate() {
        return currentInterestRate;
    }
    public void setCurrentInterestRate(double currentInterestRate) {
        this.currentInterestRate = currentInterestRate;
    }
    public Date getDateAccOpenedIn() { return dateAccOpenedIn; }
    public void setDateAccOpenedIn(Date dateAccOpenedIn) { this.dateAccOpenedIn = dateAccOpenedIn; }
    //Grab the current Savings account in the file in memory
    static void importFile() throws IOException, ParseException {
        //creates a file referencing the text file in the memory folder
        File savingsFileIn = new File("memory/savings.txt");
        //creates a bufferedreader to read from a file
        BufferedReader savingsBR;
        savingsBR = new BufferedReader(new InputStreamReader(new FileInputStream(savingsFileIn)));
        //buffer string to temporarily hold the line retrieved
        String line;
        //generic counter to know the line currently on
        int lineNum = 0;
        //while loop to go through the file
        while ((line = savingsBR.readLine()) != null) {

            //if the file has a header, this if statement is to avoid that
            //remove "if" if final file has no header
            if (lineNum > 0) {

                //split the line into an array of strings
                String[] splitLine = line.split(",");

                //create temp variable to hold info from the split lines
                int cusID = Integer.parseInt(splitLine[0]);
                double balance = Double.parseDouble(splitLine[1]);
                double currentInterest = Double.parseDouble(splitLine[2]);
                Date dateAccountOpened = new SimpleDateFormat("MM/dd/yyyy").parse(splitLine[3]);
                //add the new data to the ArrayList
                savingAccounts.add(new SavingAccount(cusID, balance, currentInterest, dateAccountOpened));
                //Debugging
                //System.out.println("count: " + (lineNum) + "\t" + savingAccounts.get(lineNum-1).toString());
            }
            //increment the line number
            lineNum++;
        }
        //close the bufferfile and return the ArrayList
        savingsBR.close();
    }//end of importFile

    //export saving accounts to savings.txt
    static void exportFile() throws FileNotFoundException {
        //create a new PrintWriter to write to a file
        PrintWriter savingWriter = new PrintWriter(new FileOutputStream("memory/savings.txt", false));
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");

        //printing the headers of the files
        savingWriter.println("CustomerID,AccountBalance,CurrentInterestRate,DateAccOpened,");
        for (SavingAccount saving : savingAccounts) {
            //format the line to put back into the file
            savingWriter.println(saving.getCustomerID() + "," +
                    saving.getAccountBalance() + "," +
                    saving.getCurrentInterestRate() + "," +
                    formatter.format(saving.getDateAccountOpened()) + ",");
            savingWriter.flush();
        }

        //close the PrintWriter objects
        savingWriter.flush();
        savingWriter.close();
    }//end of exportFile()

    public static SavingAccount search (int cusID){
        SavingAccount searchResult = null;
        for(SavingAccount savingAccount : savingAccounts){
            if (savingAccount.getCustomerID() == cusID){
                searchResult = savingAccount;
                return searchResult;//Exit when it finds the account
            }
        }
        return searchResult;
    }//end of search

    public void deposit(double depositAmount) {
        double currentBal = getAccountBalance();
        currentBal += depositAmount;
        setAccountBalance(currentBal);
    }//end of deposit

    public void withdraw(double withdrawAmount) {
        double currentBal = getAccountBalance();
        if (withdrawAmount > currentBal){
            //This will not allow the withdraw because the withdraw is larger than the current balance
        } else{
            setAccountBalance(currentBal - withdrawAmount);
        }
    }//end of withdraw

    public void transferTo(double amountToTransfer){
        //grab the customer checking account
        CheckingAccount customerChecking = CheckingAccount.search(customerID);
        if(customerChecking != null && amountToTransfer <= getAccountBalance()) {//make sure we cannot transfer more than the balance
            customerChecking.deposit(amountToTransfer);
            withdraw(amountToTransfer);
        }
    }

    public void transferFrom(double amountToTransfer) {
        //grab the customer checking account
        CheckingAccount customerChecking = CheckingAccount.search(customerID);
        //Make sure that we cannot take more than what is in the Checking account
        if(customerChecking != null && customerChecking.getAccountBalance() >= amountToTransfer){
            customerChecking.withdraw(amountToTransfer);
            deposit(amountToTransfer);
        }
    }
}//end of SavingAccount
