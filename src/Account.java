import java.text.SimpleDateFormat;

class Account {
    protected int customerID;
    protected double accountBalance;

    //getters
    public int getCustomerID() {return customerID;}
    public double getAccountBalance() {return accountBalance;}

    //setters
    public void setCustomerID(int customerID) {this.customerID = customerID;}
    public void setAccountBalance(double accountBalance) {this.accountBalance = accountBalance;}

    //formatter to be used in subclasses with date String variables
    SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");

    //other methods to be decided

}//end of Account
