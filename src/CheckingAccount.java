class CheckingAccount extends Account{
    protected String accountType;
    protected Byte indicatedOverdraftProtection;    // 1=true   0=false
    protected int overdraftsThisMonth;
    protected String dateAccountOpened;

    //constructor for the Checking Account
    public CheckingAccount(int cusIDIn, double accBalIn, String accTypeIn, Byte OvProIn, int odThisMonth, String dateAccOpened){
        this.customerID = cusIDIn;
        this.accountBalance = accBalIn;
        this.accountType = accTypeIn;
        this.indicatedOverdraftProtection = OvProIn;
        this.overdraftsThisMonth = odThisMonth;
        this.dateAccountOpened = dateFormat.format(dateAccOpened);
    }

    //getters
    public String getAccountType() {return accountType;}
    public Byte getIndicatedOverdraftProtection() {return indicatedOverdraftProtection;}
    public int getOverdraftsThisMonth() {return overdraftsThisMonth;}
    public String getDateAccountOpened() {return dateAccountOpened;}

    //setters
    public void setAccountType(String accountType) {this.accountType = accountType;}
    public void setIndicatedOverdraftProtection(Byte indicatedOverdraftProtection) {this.indicatedOverdraftProtection = indicatedOverdraftProtection;}
    public void setOverdraftsThisMonth(int overdraftsThisMonth) {this.overdraftsThisMonth = overdraftsThisMonth;}
    public void setDateAccountOpened(String dateAccountOpened) {this.dateAccountOpened = dateAccountOpened;}

    //other methods to be decided





}//end of CheckingAccount