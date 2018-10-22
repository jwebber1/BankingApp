abstract class Account {
    protected int customerID;
    protected double accountBalance;

    public Account(int cusIDIn, double accBalIn) {
        this.customerID = cusIDIn;
        this.accountBalance = accBalIn;
    }

    public int getCustomerID() {return customerID;}
    public void setCustomerID(int customerID) {this.customerID = customerID;}
    public double getAccountBalance() {return accountBalance;}
    public void setAccountBalance(double accountBalance) {this.accountBalance = accountBalance;}

    //other methods to be decided

}//end of Account
