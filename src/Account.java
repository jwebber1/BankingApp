import java.util.ArrayList;
import java.util.Date;

abstract class Account {
    protected int customerID;
    protected double accountBalance;
    protected Date dateAccountOpened;
    protected String accountType;
    //SAVINGS:   Saving = "S"   CD = "CD"
    //CHECKINGS: "regular" or "gold"
    //LOANS:     long term = "LT"  short term = "ST"   credit card = "CC"

    public Account(int cusIDIn, double accBalIn, Date dateAccOpIn, String accTypeIn) {
        this.customerID = cusIDIn;
        this.accountBalance = accBalIn;
        this.dateAccountOpened = dateAccOpIn;
        this.accountType = accTypeIn;
    }

    //getters and setters
    public int getCustomerID() {return customerID;}
    public void setCustomerID(int customerID) {this.customerID = customerID;}
    public double getAccountBalance() {return accountBalance;}
    public void setAccountBalance(double accountBalance) {this.accountBalance = accountBalance;}
    public Date getDateAccountOpened() {return dateAccountOpened;}
    public void setDateAccountOpened(Date dateAccountOpened) {this.dateAccountOpened = dateAccountOpened;}
    public String getAccountType() {return accountType;}
    public void setAccountType(String accountType) {this.accountType = accountType;}

    @Override
    public String toString() {
        return "Account{" +
                "customerID=" + customerID +
                ", accountBalance=" + accountBalance +
                ", dateAccountOpened=" + dateAccountOpened +
                ", accountType='" + accountType + '\'' +
                '}';
    }

    //other methods to be decided


    public int withdraw(ArrayList<ArrayList> cusAccounts, double withdrawlAmt){
        return 0;
    }

    public int deposit(ArrayList<ArrayList> cusAccounts, double depositAmt){
        return 0;
    }


}//end of Account
