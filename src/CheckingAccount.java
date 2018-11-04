import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

class CheckingAccount extends Account{
    protected Boolean hasOverdraftProtection;    // 1=true   0=false
    protected Boolean connectedToATMCard;        // 1=true   0=false
    protected int overdraftsThisMonth;
    protected int withdrawsToday;

    //constructor for the Checking Account
    public CheckingAccount(int cusIdIn, double accBalIn, Byte OvProIn, Byte atm, int odThisMonth, Date dateAccOpened){
        super(cusIdIn,accBalIn, dateAccOpened, ((accBalIn >1000.0) ? "gold" : "regular"));
        this.hasOverdraftProtection = (OvProIn==1) ? true : false;
        this.connectedToATMCard = (atm == 1) ? true : false;
        this.overdraftsThisMonth = odThisMonth;
        this.withdrawsToday = 0;
    }

    //getters and setters
    public Boolean getHasOverdraftProtection() {return hasOverdraftProtection;}
    public void setHasOverdraftProtection(Boolean hasOverdraftProtection) {this.hasOverdraftProtection = hasOverdraftProtection;}
    public Boolean getConnectedToATMCard() {return connectedToATMCard;}
    public void setConnectedToATMCard(Boolean connectedToATMCard) {this.connectedToATMCard = connectedToATMCard;}
    public int getOverdraftsThisMonth() {return overdraftsThisMonth;}
    public void setOverdraftsThisMonth(int overdraftsThisMonth) {this.overdraftsThisMonth = overdraftsThisMonth;}
    public int getWithdrawsToday() {return withdrawsToday;}
    public void setWithdrawsToday(int withdrawsToday) {this.overdraftsThisMonth = withdrawsToday;}

    @Override
    public String toString() {
        return "CheckingAccount{" + 
                "accountType='" + accountType + '\'' + 
                ", hasOverdraftProtection=" + hasOverdraftProtection + 
                ", overdraftsThisMonth=" + overdraftsThisMonth + 
                ", dateAccountOpened=" + dateAccountOpened + 
                ", customerID=" + customerID + 
                ", accountBalance=" + accountBalance + 
                '}';
    }

    //current method to grab data from the checkings textfile in "memory"
    public static ArrayList<CheckingAccount> importFile() throws IOException, ParseException {

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
                //String accountType = splitLine[2];
                Byte hasOverdraftProtection = Byte.parseByte(splitLine[2]);    // 1=true   0=false
                Byte connectedToATMCard = Byte.parseByte(splitLine[3]);    // 1=true   0=false
                int overdraftsThisMonth = Integer.parseInt(splitLine[4]);
                Date dateAccountOpened = new SimpleDateFormat("MM/dd/yyyy").parse(splitLine[5]);

                //add the new data (in our case checking) to the ArrayList
                importChecking.add(new CheckingAccount(cusID, balance, /*accountType,*/ hasOverdraftProtection, connectedToATMCard,overdraftsThisMonth, dateAccountOpened));

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

    //export checking accounts to checkings.txt
    public static void exportFile(ArrayList<CheckingAccount> checkings) throws FileNotFoundException {
        //create a new PrintWriter to write to a file
        PrintWriter checkingWriter = new PrintWriter(new FileOutputStream("memory/checkings.txt",false));

        //printing the headers of the files
        checkingWriter.println("CustomerID,CheckingBalance,CheckingAccountType,hasBackup,Overdrafts,DateOpened,");

        //go through all the checking accounts
        for (CheckingAccount checking: checkings) {

            int overDBit = checking.getHasOverdraftProtection() ? 1 : 0;
            int atmBit = checking.getConnectedToATMCard() ? 1 : 0;

            checkingWriter.println(checking.getCustomerID() + "," +
                    checking.getAccountBalance() + "," +
                    /*checking.getAccountType() + "," +*/
                    (checking.getHasOverdraftProtection() ? 1 : 0) + "," +
                    (checking.getConnectedToATMCard() ? 1 : 0) + "," +
                    checking.getOverdraftsThisMonth() + "," +
                    new SimpleDateFormat("MM/dd/yyy").format(checking.getDateAccountOpened()) + ",");
            checkingWriter.flush();
        }

        //close the PrintWriter objects
        checkingWriter.flush();
        checkingWriter.close();

    }//end of exportFile()

    //find all checking accounts given a customerID
    public static ArrayList<CheckingAccount> searchCheckingAccountsByCustomerID(int custID){

        //initialize searchResults to null
        ArrayList<CheckingAccount> searchResults = null;

        //loop through all checking accounts in global arraylist
        for(CheckingAccount account: Main.checkingAccounts){
            if(account.getCustomerID() == custID){
                searchResults.add(account);
            }
        }

        //return found checking accounts OR null
        return searchResults;
    }



        //method for withdraw from checking
    public int withdraw(SavingAccount customerSaving, double withdrawlAmt){
        boolean customerWithdrawTooMuch = ((accountBalance-withdrawlAmt) < 0.0);
        boolean savingsNotEnough = (((customerSaving.getAccountBalance()+accountBalance) - withdrawlAmt) < 0.0);
        int errors = 0;
        double charge = 0.0;

        //todo- on interface, do not allow more than $500 withdrawl   UNLESS they are a part of management
        //todo- on interface, do not allow more than 2 withdraws      UNLESS they are a part of management


        //charge $0.50 for withdrawl from checking if not a gold account
        if(accountType != "gold") {charge += 0.5;}

        //begin the if-elses to determine amount left in account
        if(customerWithdrawTooMuch && !hasOverdraftProtection){

            //will lead to negative balance
            setAccountBalance(accountBalance - withdrawlAmt);

            //increment overdraftsThisMonth and apply $20 overdraft charge
            overdraftsThisMonth++;
            charge += 20.0;

            //change account type if fall below $1000 (just in case)
            if(accountType == "gold" && accountBalance < 1000.0){accountType = "regular";}

            //increment amount of withdraws today
            withdrawsToday++;

            //return -1 for insufficient funds
            errors = -2;
        }
        else if(customerWithdrawTooMuch && hasOverdraftProtection) {

            //set the checking balance to $0
            setAccountBalance(0.0);

            //subtract the remaining balance not covered by the checking and set the new Savings balance (will be negative)
            customerSaving.setAccountBalance((customerSaving.getAccountBalance()+accountBalance) - withdrawlAmt);

            //change account type if fall below $1000 (just in case)
            if(accountType == "gold" && accountBalance < 1000.0){accountType = "regular";}

            //increment amount of withdraws today
            withdrawsToday++;

            //return -2 for insufficient funds even with a savings account
            if(savingsNotEnough){
                //increment overdraftsThisMonth and apply $20 overdraft charge
                overdraftsThisMonth++;
                charge += 20.0;
                errors = -1;
            }

            //otherwise, return -3 for successful withdrawl, but  savings account was used.
            errors = 1;
        }
        else{
            setAccountBalance(accountBalance - withdrawlAmt);
        }

        //apply any charges accrued to the account
        accountBalance -= charge;

        //change account type if fall below $1000 (just in case)
        if(accountType.equals("gold") && accountBalance < 1000.0){accountType = "regular";}

        //increment amount of withdraws today
        withdrawsToday++;

        //return which error was encountered:
        // -2 overdraft without backup
        // -1 overdraft with backup
        // 0 default, successful withdraw
        // +1 successful withdraw, savings account was used
        return errors;

    }//end of checking withdraw

    //method for transfer from checking to saving
    public int transfer(SavingAccount customerSaving, double transferAmt){
        int errors = 0;
        double charge = 0.0;

        //some data validation; return -1 for error; cannot transfer negative money
        if(transferAmt < 0.0) {return -1;}

        //charge $0.50 for withdrawl from checking if not a gold account
        if(accountType != "gold") {charge += 0.75;}

        //transfer the money from checking to saving
        setAccountBalance(accountBalance - transferAmt);
        customerSaving.setAccountBalance(customerSaving.getAccountBalance() + transferAmt);

        //determine if the account is overdrafted
        if(accountBalance < 0.0){

            //increment overdraftsThisMonth and apply $20 overdraft charge
            overdraftsThisMonth++;
            charge += 20.0;

            //return -2 for insufficient funds
            errors = -1;
        }

        //apply any charges accrued to the account
        accountBalance -= charge;

        //change account type if fall below $1000 (just in case)
        if(accountType == "gold" && accountBalance < 1000.0){accountType = "regular";}

        //return which error was encountered:
        // -1 account overdraft
        // 0 default, successful transfer
        return errors;

    }//end of checking withdraw

    //method for deposit into checking
    public int deposit(double depositAmt){

        //charge $0.50 for deposit into checking if not a gold account
        if(accountType != "gold") {accountBalance = accountBalance - 0.5;}

        //some data validation
        if(depositAmt < 0.0) {
            //return -3 for error; cannot deposit negative money
            return -3;
        }

        //add the deposited amount to the current account balance
        accountBalance = accountBalance + depositAmt;

        //change account type if the person has > $1000
        if(accountType != "gold" && accountBalance > 1000.0){accountType = "regular";}

        //return 0 for successful transa ction
        return 0;

    }//end of checking deposit

    public static void debugImport(){
        System.out.println("Debugging Checking Account import process");

        int count = 1;
        for(CheckingAccount account: Main.checkingAccounts){
            System.out.println("Checking Account " + count + ":\n" +
                    "accountType='" + account.getAccountType() + '\'' + "\n" +
                    "hasOverdraftProtection=" + account.getHasOverdraftProtection() + "\n" +
                    "overdraftsThisMonth=" + account.getOverdraftsThisMonth() + "\n" +
                    "dateAccountOpened=" + account.getDateAccountOpened() + "\n" +
                    "customerID=" + account.getCustomerID() + "\n" +
                    "accountBalance=" + account.getAccountBalance());

            count++;
        }
    }

}//end of CheckingAccount
