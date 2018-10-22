import java.util.Date;

class SavingAccount extends Account{
    protected double currentInterestRate;
    protected Date dateAccountOpened;
    protected Date dateCDDue;

    //constructor for the SavingAccount
    public SavingAccount(int cusIDIn, double accBalIn, double currIntRateIn, Date dateAccOpenedIn){
        super(cusIDIn, accBalIn);
        this.customerID = cusIDIn;
        this.accountBalance = accBalIn;
        this.currentInterestRate = currIntRateIn;
        this.dateAccountOpened = dateAccOpenedIn;
    }

    //overloading SavingAccount to CD
    public SavingAccount(int cusIDIn, double accBalIn, double currIntRateIn, Date dateAccOpenedIn, Date dateCDDueIn){
        super(cusIDIn, accBalIn);
        this.customerID = cusIDIn;
        this.accountBalance = accBalIn;
        this.currentInterestRate = currIntRateIn;
        this.dateAccountOpened = dateAccOpenedIn;
        this.dateCDDue = dateCDDueIn;
    }

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