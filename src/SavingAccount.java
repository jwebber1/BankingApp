import java.util.Date;

class SavingAccount extends Account{
    protected double currentInterestRate;
    protected Date dateAccountOpened;
    protected Date dateCDDue;

    //constructor for the SavingAccount
    public SavingAccount(int cusIDIn, double accBalIn, double currIntRateIn, Date dateAccOpenedIn){
<<<<<<< HEAD
=======
        super(cusIDIn, accBalIn);
>>>>>>> JonTextInBranch
        this.customerID = cusIDIn;
        this.accountBalance = accBalIn;
        this.currentInterestRate = currIntRateIn;
        this.dateAccountOpened = dateAccOpenedIn;
    }

<<<<<<< HEAD
    //overloading constructor if it is a CD
    public SavingAccount(int cusIDIn, double accBalIn, double currIntRateIn, Date dateAccOpenedIn, Date dateCDDueIn){
=======
    //overloading SavingAccount to CD
    public SavingAccount(int cusIDIn, double accBalIn, double currIntRateIn, Date dateAccOpenedIn, Date dateCDDueIn){
        super(cusIDIn, accBalIn);
>>>>>>> JonTextInBranch
        this.customerID = cusIDIn;
        this.accountBalance = accBalIn;
        this.currentInterestRate = currIntRateIn;
        this.dateAccountOpened = dateAccOpenedIn;
        this.dateCDDue = dateCDDueIn;
    }

<<<<<<< HEAD
    //getters
    public double getCurrentInterestRate() {return currentInterestRate;}
    public Date getDateAccountOpened() {return dateAccountOpened;}
    public Date getDateCDDue() {return dateCDDue;}

    //setters
    public void setCurrentInterestRate(double currentInterestRate) {this.currentInterestRate = currentInterestRate;}
    public void setDateAccountOpened(Date dateAccountOpened) {this.dateAccountOpened = dateAccountOpened;}
    public void setDateCDDue(Date dateCDDue) {this.dateCDDue = dateCDDue;}

=======
>>>>>>> JonTextInBranch
    //other methods to be decided

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
}//end of SavingAccount