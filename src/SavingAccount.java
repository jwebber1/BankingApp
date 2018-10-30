import java.util.Date;

class SavingAccount extends Account {
    private double currentInterestRate;
    private Date dateCDDue;

    //constructor for the SavingAccount
    private SavingAccount(int cusIDIn, double accBalIn, double currIntRateIn, Date dateAccOpenedIn) {
        super(cusIDIn, accBalIn, dateAccOpenedIn, "Saving");
        this.currentInterestRate = currIntRateIn;
        this.dateCDDue = null;
    }


       //overloading SavingAccount to CD
    //Moved to CD class
//    private SavingAccount(int cusIDIn, double accBalIn, double currIntRateIn, Date dateAccOpenedIn, Date dateCDDueIn) {
//        super(cusIDIn, accBalIn, dateAccOpenedIn, "CD");
//        this.customerID = cusIDIn;
//        this.accountBalance = accBalIn;
//        this.currentInterestRate = currIntRateIn;
//        this.dateCDDue = dateCDDueIn;
//    }

    private double getCurrentInterestRate() {
        return currentInterestRate;
    }

    public void setCurrentInterestRate(double currentInterestRate) {
        this.currentInterestRate = currentInterestRate;
    }

    private Date getDateCDDue() {
        return dateCDDue;
    }

    public void setDateCDDue(Date dateCDDue) {
        this.dateCDDue = dateCDDue;
    }

    @Override
    public String toString() {
        return "SavingAccount{" +
                "currentInterestRate=" + currentInterestRate +
                ", dateAccountOpened=" + dateAccountOpened +
                ", dateCDDue=" + dateCDDue +
                ", customerID=" + customerID +
                ", accountBalance=" + accountBalance +
                '}';
    }

    //other methods to be decided


}//end of SavingAccount
