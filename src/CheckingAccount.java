import java.util.Date;

class CheckingAccount extends Account{
    protected Byte indicatedOverdraftProtection;    // 1=true   0=false
    protected int overdraftsThisMonth;

    //constructor for the Checking Account
    public CheckingAccount(int cusIDIn, double accBalIn, String accTypeIn, Byte OvProIn, int odThisMonth, Date dateAccOpened){
<<<<<<< HEAD
<<<<<<< HEAD
        this.customerID = cusIDIn;
        this.accountBalance = accBalIn;
=======
        super(cusIDIn,accBalIn);
>>>>>>> JonTextInBranch
        this.accountType = accTypeIn;
=======
        super(cusIDIn,accBalIn, dateAccOpened, accTypeIn);
>>>>>>> JonTextInBranch
        this.indicatedOverdraftProtection = OvProIn;
        this.overdraftsThisMonth = odThisMonth;
    }

<<<<<<< HEAD
<<<<<<< HEAD
    //getters
    public String getAccountType() {return accountType;}
    public Byte getIndicatedOverdraftProtection() {return indicatedOverdraftProtection;}
    public int getOverdraftsThisMonth() {return overdraftsThisMonth;}
    public Date getDateAccountOpened() {return dateAccountOpened;}

    //setters
    public void setAccountType(String accountType) {this.accountType = accountType;}
    public void setIndicatedOverdraftProtection(Byte indicatedOverdraftProtection) {this.indicatedOverdraftProtection = indicatedOverdraftProtection;}
    public void setOverdraftsThisMonth(int overdraftsThisMonth) {this.overdraftsThisMonth = overdraftsThisMonth;}
    public void setDateAccountOpened(Date dateAccountOpened) {this.dateAccountOpened = dateAccountOpened;}

=======
>>>>>>> JonTextInBranch
    //other methods to be decided
=======
    public Byte getIndicatedOverdraftProtection() {return indicatedOverdraftProtection;}
    public void setIndicatedOverdraftProtection(Byte indicatedOverdraftProtection) {this.indicatedOverdraftProtection = indicatedOverdraftProtection;}
    public int getOverdraftsThisMonth() {return overdraftsThisMonth;}
    public void setOverdraftsThisMonth(int overdraftsThisMonth) {this.overdraftsThisMonth = overdraftsThisMonth;}
>>>>>>> JonTextInBranch

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