<<<<<<< HEAD
import java.util.Date;

class Account {
    protected int customerID;
    protected double accountBalance;

    //getters
    public int getCustomerID() {return customerID;}
    public double getAccountBalance() {return accountBalance;}

    //setters
    public void setCustomerID(int customerID) {this.customerID = customerID;}
    public void setAccountBalance(double accountBalance) {this.accountBalance = accountBalance;}
=======
abstract class Account {
    protected int customerID;
    protected double accountBalance;

    public Account(int cusIDIn, double accBalIn) {
        this.customerID = cusIDIn;
        this.accountBalance = accBalIn;
    }
>>>>>>> JonTextInBranch

    //other methods to be decided

}//end of Account