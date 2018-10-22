import java.util.Date;

class CheckingAccount extends Account{
    protected String accountType;
    protected Byte indicatedOverdraftProtection;    // 1 = true   0 = false
    protected int overdraftsThisMonth;
    protected Date dateAccountOpened;

    //constructor for the Checking Account
    public CheckingAccount(int cusIDIn, double accBalIn, String accTypeIn, Byte ovProIn, int odThisMonth, Date dateAccOpened){
        this.customerID = cusIDIn;
        this.accountBalance = accBalIn;
        this.accountType = accTypeIn;
        this.indicatedOverdraftProtection = ovProIn;
        this.overdraftsThisMonth = odThisMonth;
        this.dateAccountOpened = dateAccOpened;
    }

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

    //other methods to be decided





}//end of CheckingAccount