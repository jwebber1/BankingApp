import java.util.Date;

/**
 * TODO:
 */

class SavingAccount extends Account{
    protected double currentInterestRate;
    protected Date dateAccountOpened;
    protected Date dateCDDue;

    //constructor for the SavingAccount
    public SavingAccount(int cusIDIn, double accBalIn, double currIntRateIn, Date dateAccOpenedIn){
        this.customerID = cusIDIn;
        this.accountBalance = accBalIn;
        this.currentInterestRate = currIntRateIn;
        this.dateAccountOpened = dateAccOpenedIn;
    }

    //overloading constructor if it is a CD
    public SavingAccount(int cusIDIn, double accBalIn, double currIntRateIn, Date dateAccOpenedIn, Date dateCDDueIn){
        this.customerID = cusIDIn;
        this.accountBalance = accBalIn;
        this.currentInterestRate = currIntRateIn;
        this.dateAccountOpened = dateAccOpenedIn;
        this.dateCDDue = dateCDDueIn;
    }

    //getters
    public double getCurrentInterestRate() {return currentInterestRate;}
    public Date getDateAccountOpened() {return dateAccountOpened;}
    public Date getDateCDDue() {return dateCDDue;}

    //setters
    public void setCurrentInterestRate(double currentInterestRate) {this.currentInterestRate = currentInterestRate;}
    public void setDateAccountOpened(Date dateAccountOpened) {this.dateAccountOpened = dateAccountOpened;}
    public void setDateCDDue(Date dateCDDue) {this.dateCDDue = dateCDDue;}

    //other methods to be decided





}//end of SavingAccount