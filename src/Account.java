import java.util.Date;

public class Account {
    protected int customerID;
    protected double accountBalance;
    protected Date dateAccountOpened;
    protected String accountType;
    protected String mainAccountType; // I need this to display savings/checking/loans for the UI
    //SAVINGS:   Saving = "S"   CD = "CD"   CHECKINGS: "regular" or "gold"  LOANS:  long term = "LT"  short term = "ST"   credit card = "CC"

    //constructor
    public Account(int cusIDIn, double accBalIn, Date dateAccOpIn, String accTypeIn) {
        this.customerID = cusIDIn;
        this.accountBalance = accBalIn;
        this.dateAccountOpened = dateAccOpIn;
        this.accountType = accTypeIn;
    }

    //getters/setters
    public int getCustomerID() {return customerID;}
    public double getAccountBalance() {return accountBalance;}
    public void setAccountBalance(double accountBalance) {this.accountBalance = accountBalance;}
    public Date getDateAccountOpened() {return dateAccountOpened;}
    public String getAccountType() {return accountType;}
    public void setAccountType(String accountType) {this.accountType = accountType;}
}//end of Account
