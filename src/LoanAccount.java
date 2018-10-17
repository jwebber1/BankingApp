class LoanAccount extends Account{
    protected String loanType;      //long term = "LT"  short term = "ST"   credit card = "CC"
    protected double currentInterestRate;
    protected String datePaymentDue;
    protected double currentPaymentDue;
    protected String datePaymentNotified;
    protected String lastPaymentDate;
    protected Byte missedPaymentFlag;   // 1=true   0=false

    public LoanAccount(int cusIDIn, double accBalIn, String loanTypeIn, double currIntRate, String datePayDue, double currPayDue, String datePayNotifIn, String lastPayDateIn, Byte missPayFlagIn){
        this.customerID = cusIDIn;
        this.accountBalance = accBalIn;
        this.loanType = loanTypeIn;
        this.currentInterestRate = currIntRate;
        this.datePaymentDue = dateFormat.format(datePayDue);
        this.currentPaymentDue = currPayDue;
        this.datePaymentNotified = dateFormat.format(datePayNotifIn);
        this.lastPaymentDate = dateFormat.format(lastPayDateIn);
        this.missedPaymentFlag = missPayFlagIn;
    }

    //getters
    public String getLoanType() {return loanType;}
    public double getCurrentInterestRate() {return currentInterestRate;}
    public String getDatePaymentDue() {return datePaymentDue;}
    public double getCurrentPaymentDue() {return currentPaymentDue;}
    public String getDatePaymentNotified() {return datePaymentNotified;}
    public String getLastPaymentDate() {return lastPaymentDate;}
    public Byte   getMissedPaymentFlag() {return missedPaymentFlag;}
    public void setLoanType(String loanType) {this.loanType = loanType;}

    //setters
    public void setCurrentInterestRate(double currentInterestRate) {this.currentInterestRate = currentInterestRate;}
    public void setDatePaymentDue(String datePaymentDue) {this.datePaymentDue = datePaymentDue;}
    public void setCurrentPaymentDue(double currentPaymentDue) {this.currentPaymentDue = currentPaymentDue;}
    public void setDatePaymentNotified(String datePaymentNotified) {this.datePaymentNotified = datePaymentNotified;}
    public void setLastPaymentDate(String lastPaymentDate) {this.lastPaymentDate = lastPaymentDate;}
    public void setMissedPaymentFlag(Byte missedPaymentFlag) {this.missedPaymentFlag = missedPaymentFlag;}

    //other methods to be decided





}//end of LoanAccount