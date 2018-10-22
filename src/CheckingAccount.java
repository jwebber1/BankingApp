import java.util.Date;

class CheckingAccount extends Account{
    protected String accountType;
    protected Byte indicatedOverdraftProtection;    // 1=true   0=false
    protected int overdraftsThisMonth;
    protected Date dateAccountOpened;

    //constructor for the Checking Account
    public CheckingAccount(int cusIDIn, double accBalIn, String accTypeIn, Byte OvProIn, int odThisMonth, Date dateAccOpened){
        super(cusIDIn,accBalIn);
        this.accountType = accTypeIn;
        this.indicatedOverdraftProtection = OvProIn;
        this.overdraftsThisMonth = odThisMonth;
        this.dateAccountOpened = dateAccOpened;
    }

    //other methods to be decided

    @Override
    public String toString() {
        return "CheckingAccount{" + 
                "accountType='" + accountType + '\'' + 
                ", indicatedOverdraftProtection=" + indicatedOverdraftProtection + 
                ", overdraftsThisMonth=" + overdraftsThisMonth + 
                ", dateAccountOpened=" + dateAccountOpened + 
                ", customerID=" + customerID + 
                ", accountBalance=" + accountBalance + 
                '}';
    }
}//end of CheckingAccount