import java.util.ArrayList;
import java.util.Date;

class CheckingAccount extends Account{
    protected Boolean hasOverdraftProtection;    // 1=true   0=false
    protected Boolean connectedToATMCard;        // 1=true   0=false
    protected int overdraftsThisMonth;
    protected int withdrawsToday;
    protected ArrayList<Check> checks;

    //constructor for the Checking Account
    public CheckingAccount(int cusIdIn, double accBalIn, String accTypeIn, Byte OvProIn, Byte atm, int odThisMonth, Date dateAccOpened, ArrayList<Check> checksIn){
        super(cusIdIn,accBalIn, dateAccOpened, accTypeIn);
        this.hasOverdraftProtection = false;
        if(OvProIn == 1) {this.hasOverdraftProtection = true;}
        connectedToATMCard = false;
        if(atm == 1) {this.connectedToATMCard = true;}
        this.overdraftsThisMonth = odThisMonth;
        withdrawsToday = 0;
        this.checks = checksIn;
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
    public ArrayList<Check> getChecks() {return checks;}
    public void setChecks(ArrayList<Check> checks) {this.checks = checks;}

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

    //method for withdraw from checking
    public int withdraw(SavingAccount customerSaving, double withdrawlAmt){
        boolean customerWithdrawTooMuch = ((accountBalance-withdrawlAmt) < 0.0);
        boolean savingsNotEnough = (((customerSaving.getAccountBalance()+accountBalance) - withdrawlAmt) < 0.0);
        int errors = 0;
        double charge = 0.0;

        //todo- on interface, do not allow more than $500 withdrawl   UNLESS they are a part of management
        //todo- on interface, do not allow more than 2 withdraws      UNLESS they are a part of management


        //charge $0.50 for withdrawl from checking if not a gold account
        if(!accountType.equals("gold")) {charge += 0.5;}

        //begin the if-elses to determine amount left in account
        if(customerWithdrawTooMuch && !hasOverdraftProtection){

            //will lead to negative balance
            setAccountBalance(accountBalance - withdrawlAmt);

            //increment overdraftsThisMonth and apply $20 overdraft charge
            overdraftsThisMonth++;
            charge += 20.0;

            //change account type if fall below $1000 (just in case)
            if(accountType.equals("gold") && accountBalance < 1000.0){accountType = "regular";}

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
            if(accountType.equals("gold") && accountBalance < 1000.0){accountType = "regular";}

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
        if(!accountType.equals("gold")) {charge += 0.75;}

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
        if(accountType.equals("gold") && accountBalance < 1000.0){accountType = "regular";}

        //return which error was encountered:
        // -1 account overdraft
        // 0 default, successful transfer
        return errors;

    }//end of checking withdraw

    //method for deposit into checking
    public int deposit(double depositAmt){

        //charge $0.50 for deposit into checking if not a gold account
        if(!accountType.equals("gold")) {accountBalance = accountBalance - 0.5;}

        //some data validation
        if(depositAmt < 0.0) {
            //return -3 for error; cannot deposit negative money
            return -3;
        }

        //add the deposited amount to the current account balance
        accountBalance = accountBalance + depositAmt;

        //change account type if the person has > $1000
        if(!accountType.equals("gold") && accountBalance > 1000.0){accountType = "regular";}

        //return 0 for successful transaction
        return 0;

    }//end of checking deposit


    //method to stop a check
    public void stopCheck(int checkNum){
        // TODO
    }


}//end of CheckingAccount
