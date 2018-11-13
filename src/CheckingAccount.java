import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

class CheckingAccount extends Account{
    private Boolean hasOverdraftProtection;
    private Boolean connectedToATMCard;
    private int overdraftsThisMonth;
    private int withdrawsToday;
    static ArrayList<CheckingAccount> checkingAccounts = new ArrayList<>();

    //constructor for the Checking Account
    public CheckingAccount(int cusIdIn, double accBalIn, Boolean OvProIn, Boolean atm, int odThisMonth, Date dateAccOpened){
        super(cusIdIn,accBalIn, dateAccOpened, ((accBalIn >= 1000.0) ? "gold" : "regular"));

        mainAccountType = "Checking - " + accountType.substring(0, 1).toUpperCase() + accountType.substring(1);
        this.hasOverdraftProtection = OvProIn;
        this.connectedToATMCard = atm;
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

    //current method to grab data from the checkings textfile in "memory"
    public static void importFile() throws IOException, ParseException {

        //creates a file referencing the text file in the memory folder
        File checkingsFileIn = new File("memory/checkings.txt");

        //creates a bufferedreader to read from a file
        BufferedReader checkingsBR = null;
        checkingsBR = new BufferedReader(new InputStreamReader(new FileInputStream(checkingsFileIn)));

        //buffer string to temporarily hold the line retrieved
        String line;
        
        //generic counter to know the line currently on
        int lineNum = 0;

        //while loop to go through the file
        while ((line = checkingsBR.readLine()) != null) {

            //file has a header, this if statement is to avoid that
            if(lineNum > 0) {

                //split the line into an array of strings
                String[] splitLine = line.split(",");

                //create temp variables to hold info from the split lines
                int cusID = Integer.parseInt(splitLine[0]);
                double balance = Double.parseDouble(splitLine[1]);
                boolean hasOverdraftProtection = Boolean.parseBoolean(splitLine[2]);  
                boolean connectedToATMCard =  Boolean.parseBoolean(splitLine[3]);  
                int overdraftsThisMonth = Integer.parseInt(splitLine[4]);
                Date dateAccountOpened = new SimpleDateFormat("MM/dd/yyyy").parse(splitLine[5]);

                //add the new data (in our case checking) to the ArrayList
                checkingAccounts.add(new CheckingAccount(cusID, balance, hasOverdraftProtection, connectedToATMCard,overdraftsThisMonth, dateAccountOpened));
            }

            //increment the line number
            lineNum++;
        }

        //close the bufferfile
        checkingsBR.close();
    }//end of checking data import method

    //export checking accounts to checkings.txt
    public static void exportFile() throws FileNotFoundException {
        //create a new PrintWriter to write to a file
        PrintWriter checkingWriter = new PrintWriter(new FileOutputStream("memory/checkings.txt",false));

        //printing the headers of the files
        checkingWriter.println("CustomerID,CheckingBalance,hasBackup,hasATM,Overdrafts,DateOpened,");

        //go through all the checking accounts
        for (CheckingAccount checking: checkingAccounts) {
            checkingWriter.println(checking.getCustomerID() + "," +
                    checking.getAccountBalance() + "," +
                    checking.getHasOverdraftProtection() + "," +
                    checking.getConnectedToATMCard() + "," +
                    checking.getOverdraftsThisMonth() + "," +
                    new SimpleDateFormat("MM/dd/yyy").format(checking.getDateAccountOpened()) + ",");
            checkingWriter.flush();
        }

        //close the PrintWriter objects
        checkingWriter.flush();
        checkingWriter.close();
    }//end of exportFile()

    //find all checking accounts given a customerID
    static CheckingAccount search(int custID){

        //initialize searchResults
        CheckingAccount searchResults = null;

        //loop through all checking accounts in global arraylist
        for(CheckingAccount account: checkingAccounts){
            if(account.getCustomerID() == custID){
                searchResults = account;
                return searchResults;
            }
        }

        //return found checking accounts OR null
        return searchResults;
    }//end of search

    //method for withdraw from checking
    public void withdraw(double withdrawlAmt){

        //set temporary boolean to see if the customer is withdrawing too much
        boolean customerWithdrawTooMuch = ((accountBalance-withdrawlAmt) < 0.0);

        //set a temporary variable for charge
        double charge = 0.0;

        //charge $0.50 for withdrawl from checking if not a gold account
        if(accountType.equals("regular")) {charge += 0.5;}

        //begin the if-elses to determine amount left in account
        if(customerWithdrawTooMuch && !hasOverdraftProtection){

            //will lead to negative balance
            setAccountBalance(accountBalance - withdrawlAmt);

            //increment overdraftsThisMonth and apply $20 overdraft charge
            overdraftsThisMonth++;
            charge += 20.0;
        }
        else if(customerWithdrawTooMuch && hasOverdraftProtection) {

            //get the customer's saving account
            SavingAccount customersSaving = SavingAccount.search(customerID);

            //set temporary variably to determine if there is enough in the savings account to withdraw from
            boolean savingsNotEnough = (((customersSaving.getAccountBalance()+accountBalance) - withdrawlAmt) < 0.0);
            
            if(savingsNotEnough){
                
                //get the new balance for checking after the overdraft
                double overdraftAmt = ((customersSaving.getAccountBalance()+accountBalance) - withdrawlAmt);

                //drain the Savings account to $0
                customersSaving.setAccountBalance(0.0);

                //set the checking to the new, overdrafted amount;
                accountBalance = overdraftAmt;

                //increment overdraftsThisMonth and apply $20 overdraft charge
                overdraftsThisMonth++;
                charge += 20.0;
            }
            else{
                
                //set the new Savings balance 
                customersSaving.setAccountBalance((customersSaving.getAccountBalance()+accountBalance) - withdrawlAmt);

                //set the checking balance to $0
                setAccountBalance(0.0);
            }

        }
        else{ setAccountBalance(accountBalance - withdrawlAmt); }

        //apply any charges accrued to the account
        accountBalance -= charge;

        //change account type if fall below $1000 (just in case)
        if(accountType.equals("gold") && accountBalance < 1000.0){accountType = "regular";}

        //increment amount of withdraws today
        withdrawsToday++;
    }//end of checking withdraw

    //method for deposit into checking
    public void deposit(double depositAmt){

        //charge $0.50 for deposit into checking if not a gold account
        if(accountType.equals("regular")) {accountBalance -= 0.5;}

        //add the deposited amount to the current account balance
        accountBalance += depositAmt;

        //change account type if the person has > $1000
        if(accountType.equals("regular") && accountBalance >= 1000.0){accountType = "gold";}

    }//end of checking deposit

    //method for transfer from checking to saving
    public void transferTo(double transferAmt){

        //set a temporary variable for charge
        double charge = 0.0;

        //grab the associated savings account
        SavingAccount customerSaving = SavingAccount.search(customerID);
        
        if(customerSaving != null && ((accountBalance - transferAmt) >= 0.0)){

            //charge $0.75 for transfer if not a gold account
            if(accountType.equals("regular")) {charge += 0.75;}

            //transfer the money from checking to saving
            customerSaving.deposit(transferAmt);

            //decrement the amount transferred out of checking balance
            accountBalance =- transferAmt;
        }

        //apply any charges accrued to the account
        accountBalance -= charge;

        //change account type if fall below $1000 (just in case)
        if(accountType.equals("gold") && accountBalance < 1000.0){accountType = "regular";}

    }//end of checking transferTo

    //transfer money from savings to checking
    public void transferFrom(double transferAmt) {

        //set a temporary variable for charge
        double charge = 0.0;

        //get the savings account with this customer
        SavingAccount customerSavings = SavingAccount.search(customerID);

        if(customerSavings != null && ((customerSavings.getAccountBalance()-transferAmt) >= 0.0)){

            //charge $0.75 for transfer from savings if not a gold account
            if(accountType.equals("regular")) {charge += 0.75;}

            //transfer the money from checking to saving
            customerSavings.withdraw(transferAmt);

            //increment the money in checking
            accountBalance += transferAmt;
        }

        //apply any charges accrued to the account
        accountBalance -= charge;

        //change account type if fall below $1000 (just in case)
        if(accountType.equals("regular") && accountBalance >= 1000.0){accountType = "gold";}

    }//end of transferFrom
    
}//end of CheckingAccount
