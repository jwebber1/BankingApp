import java.util.Date;

class CheckingAccount extends Account{
    protected Byte indicatedOverdraftProtection;    // 1=true   0=false
    protected int overdraftsThisMonth;

    //constructor for the Checking Account
    public CheckingAccount(int cusIDIn, double accBalIn, String accTypeIn, Byte OvProIn, int odThisMonth, Date dateAccOpened){
        super(cusIDIn,accBalIn, dateAccOpened, accTypeIn);
        this.indicatedOverdraftProtection = OvProIn;
        this.overdraftsThisMonth = odThisMonth;
    }

    public Byte getIndicatedOverdraftProtection() {return indicatedOverdraftProtection;}
    public void setIndicatedOverdraftProtection(Byte indicatedOverdraftProtection) {this.indicatedOverdraftProtection = indicatedOverdraftProtection;}
    public int getOverdraftsThisMonth() {return overdraftsThisMonth;}
    public void setOverdraftsThisMonth(int overdraftsThisMonth) {this.overdraftsThisMonth = overdraftsThisMonth;}

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

    //other methods to be decided



}//end of CheckingAccount
