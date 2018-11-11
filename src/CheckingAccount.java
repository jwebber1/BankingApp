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
                boolean hasOverdraftProtection = Boolean.parseBoolean(splitLine[2]);    // 1=true   0=false
                boolean connectedToATMCard =  Boolean.parseBoolean(splitLine[3]);  // 1=true   0=false
                int overdraftsThisMonth = Integer.parseInt(splitLine[4]);
                Date dateAccountOpened = new SimpleDateFormat("MM/dd/yyyy").parse(splitLine[5]);

                //add the new data (in our case checking) to the ArrayList
                checkingAccounts.add(new CheckingAccount(cusID, balance, hasOverdraftProtection, connectedToATMCard,overdraftsThisMonth, dateAccountOpened));

                //debugging importChecking
                //System.out.println("count: " + (lineNum) + "\t" + checkingAccounts.get(lineNum-1).toString());
            }

            //increment the line number
            lineNum++;
        }

        //close the bufferfile and return the ArrayList
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
    static ArrayList<CheckingAccount> searchCheckingAccountsByCustomerID(int custID){

        //initialize searchResults to null
        ArrayList<CheckingAccount> searchResults = new ArrayList<>();

        //loop through all checking accounts in global arraylist
        for(CheckingAccount account: CheckingAccount.checkingAccounts){
            if(account.getCustomerID() == custID){
                searchResults.add(account);
            }
        }

        //return found checking accounts OR null
        return searchResults;
    }

    //method for withdraw from checking
    public void withdraw(SavingAccount customerSaving, double withdrawlAmt){
        boolean customerWithdrawTooMuch = ((accountBalance-withdrawlAmt) < 0.0);
        boolean savingsNotEnough = (((customerSaving.getAccountBalance()+accountBalance) - withdrawlAmt) < 0.0);
        double charge = 0.0;

        //todo- on interface, do not allow more than $500 withdrawl   UNLESS they are a part of management
        //todo- on interface, do not allow more than 2 withdraws      UNLESS they are a part of management


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

            //set the checking balance to $0
            setAccountBalance(0.0);

            //subtract the remaining balance not covered by the checking and set the new Savings balance (will be negative)
            customerSaving.setAccountBalance((customerSaving.getAccountBalance()+accountBalance) - withdrawlAmt);
            //todo- ^^^ will need to modify when Jacob finishes his withdraw method from SavingAcount

            //return -2 for insufficient funds even with a savings account
            if(savingsNotEnough){
                //increment overdraftsThisMonth and apply $20 overdraft charge
                overdraftsThisMonth++;
                charge += 20.0;
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

    //method for transfer from checking to saving
    public void transfer(SavingAccount customerSaving, double transferAmt){
        double charge = 0.0;

        //charge $0.50 for withdrawl from checking if not a gold account
        if(accountType.equals("regular")) {charge += 0.75;}

        //transfer the money from checking to saving
        setAccountBalance(accountBalance - transferAmt);
        customerSaving.setAccountBalance(customerSaving.getAccountBalance() + transferAmt);
        //todo- consider just doing a deposit method from SavingAccount here.

        //determine if the account is overdrafted
        if(accountBalance < 0.0){

            //increment overdraftsThisMonth and apply $20 overdraft charge
            overdraftsThisMonth++;
            charge += 20.0;
        }

        //apply any charges accrued to the account
        accountBalance -= charge;

        //change account type if fall below $1000 (just in case)
        if(accountType.equals("gold") && accountBalance < 1000.0){accountType = "regular";}

    }//end of checking transfer

    //method for deposit into checking
    public int deposit(double depositAmt){

        //charge $0.50 for deposit into checking if not a gold account
        if(accountType.equals("regular")) {accountBalance -= 0.5;}

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
}//end of CheckingAccount
